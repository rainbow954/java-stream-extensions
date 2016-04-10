/*
 * Copyright 2016 wapitia.com
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * Neither the name of wapitia.com or the names of contributors may be used to
 * endorse or promote products derived from this software without specific
 * prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED.
 * WAPITIA.COM ("WAPITIA") AND ITS LICENSORS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL WAPITIA OR
 * ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR
 * DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE
 * DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF
 * WAPITIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 */

package com.wapitia.stream;

import com.wapitia.collections.IteratorStack;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterators;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@code FlatSpliter<T>} does a flat map treatment on a list of
 * similar-typed input
 * {@link Stream}s, providing a new single stream of all of the elements from
 * all the  inputs.
 * A {@link Comparator} aids in prioritizing the output traffic.
 * Streams in the list should be SORTED for the {@code Comparator} to function
 * correctly.
 * The generated stream continues until all streams are exhausted.
 *
 * @author Corey Morgan
 *
 * @param <T> Shared type of elements in the input and output stream
 */
class FlatSpliter<T> extends Spliterators.AbstractSpliterator<T> {

    /**
     * Takes a {@link List} of similar-typed {@link Stream}s and provides
     * a flattened output of those items.
     * The {@code Comparator} is used to prioritize which top item among the
     * active streams should go next. The "least" item will be on deck.
     *
     * <p>Incomparable streams will yield predictable but hard to
     * anticipate results.
     *
     * <p>This wraps the incoming streams into {@link IteratorStack}s in order
     * to "peek" at the top elements of these streams for comparison purposes
     * without yet consuming them.
     *
     * @param streamList list of similar streams to traverse. Not null.
     * @param comp      compares two stream items, the "least" of which will
     *                  be processed first. Not null.
     */
    public FlatSpliter(
        final List<Stream<T>> streamList,
        final Comparator<? super T> comp)
    {
        super(Long.MAX_VALUE, 0);
        Objects.requireNonNull(streamList);
        Objects.requireNonNull(comp);
        this.istackList = streamList.stream()
                .map(Stream::iterator)
                .map(IteratorStack<T>::new)
                .collect(Collectors.toList());

        this.comp = comp;
    }

    private final List<IteratorStack<T>> istackList;

    // compares two stream items, return negative if first should go first,
    // positive otherwise
    final Comparator<? super T> comp;

    final Comparator<? super T> comp() {
        return this.comp;
    }

    /**
     * Compare the top elements of two stacks, according to the
     * {@link Comparator} of this {@link FlatSpliter#FlatSpliter FlatSpliter}
     * object.
     *
     * <p>When stack items {@code t1} and {@code t2} are compared,
     * this returns {@code t1} iff {@code t1 <= t2}.
     * If  {@code t1} is empty, this returns  {@code t2}  and vice versa.
     * Will return an {@link IteratorStack#empty() empty}
     * stack when both stacks are exhausted.
     * @see IteratorStack#empty()
     * @see Comparator
     * @see IteratorStack#peek()
     */
    BinaryOperator<IteratorStack<T>> stackHavingBestTop = (ts1,ts2) -> {
        final IteratorStack<T> result;
        if (ts1.empty()) {
            result = ts2;
        } else if (ts2.empty()) {
            result = ts1;
        } else {
            final int cmp = comp().compare(ts1.peek(), ts2.peek());
            result = (cmp <= 0) ? ts1 : ts2;
        }
        return result;
    };


    /**
     * Advance until all streams are exhausted.
     *
     * @return {@code true} while more input elements to be consumed,
     *         {@code false} when all streams are exhausted
     */
    @Override
    public boolean tryAdvance(final Consumer<? super T> action) {
        boolean itsOver = false;
        boolean advanced = false;
        while (!advanced && !itsOver) {
            Optional<IteratorStack<T>> stackOpt =
                    istackList.stream().parallel().reduce(stackHavingBestTop);

            if (stackOpt.isPresent()) {
                Objects.requireNonNull(action);
                T thing = stackOpt.get().pop();
                action.accept(thing);
                advanced = true;
            } else {
                // when there are no stacks returned, we've reached
                // the end of all data
                itsOver = true;
            }
        }
        return advanced;
    }

}
