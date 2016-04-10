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

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Deprecated   // Use jdk9 java.util.stream.WhileIter when it comes on line.
class WhileSpliter<T> extends Spliterators.AbstractSpliterator<T> {

    protected final Predicate<T> continueWhile;
    protected final Iterator<T> streamIter;
    protected boolean done = false;

    public WhileSpliter(Stream<T> stream, Predicate<T> continueWhile) {
        super(Long.MAX_VALUE, 0);
        Objects.requireNonNull(continueWhile);
        Objects.requireNonNull(stream);
        this.continueWhile = continueWhile;
        this.streamIter = stream.iterator();
    }

    @Override
    public boolean tryAdvance(final Consumer<? super T> consum) {
        Objects.requireNonNull(consum);

        boolean advanced = false;
        while (!done && !advanced && streamIter.hasNext()) {
            final T item = streamIter.next();
            if (continueWhile.test(item)) {
                consum.accept(item);
                advanced = true;
            } else {
                this.done = true;
            }
        }
        return advanced;
    }

}