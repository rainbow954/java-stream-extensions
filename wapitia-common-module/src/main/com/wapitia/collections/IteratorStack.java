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

package com.wapitia.collections;

import java.util.Iterator;
import java.util.Stack;


/**
 * Wraps an {@link Iterator} allowing peeking and push back of the items.
 * This is a stack/iterator hybrid that will 'pop' and 'peek' items usually from
 * the backing Iterator, but will first interrogate this stack in case any
 * items have been pushed onto it.
 *
 * <p><b>Note</b> This implements the the purest form of the stack interface,
 *   with {@link #push(Object)},{@link #pop()}, {@link #peek()}, and
 *   {@link #empty()}, but hasn't implemented the indexed or terminating
 *   methods that {@link Stack} has to offer.
 *
 * @author Corey Morgan
 *
 * @param <T> items to be looked at
 */
public class IteratorStack<T> {

    private final Stack<T> stack;
    private final Iterator<T> iterator;

    /**
     * Blends an iterator with an empty stack to produce a buffered iterator.
     *
     * @param iterator possibly endless iterator of fresh items
     */
    public IteratorStack(final Iterator<T> iterator) {
        this(new Stack<>(), iterator);
    }

    /**
     * Blends an iterator with a stack to produce a buffered iterator.
     * The stack items take precedence over the iterator items.
     *
     * @param stack existing starter stack
     * @param iterator possibly endless iterator of fresh items
     */
    public IteratorStack(final Stack<T> stack, final Iterator<T> iterator) {
        this.stack = stack;
        this.iterator = iterator;
    }

    /**
     * Push an item onto the stack, return that item.
     * @param item item to push must be the same type as the iterator items.
     * @return item
     */
    public T push(final T item) {
        stack.push(item);
        return item;
    }

    /**
     * Pop the item from the stack, or from the backing iterator
     * if the stack is empty.
     * @return item from the stack or the iterator or kaboom
     */
    public synchronized T pop() {
        final T result;
        if (stack.isEmpty()) {
            result = iterator.next();
        } else {
            result = stack.pop();
        }
        return result;
    }

    /**
     * Look at the next or top item in our iterator/stack beast, if available.
     * If not available, do not call {@code peek}.
     *
     * <p><b>Note</b> since {@code iterator}s don't have a "{@code peek}"
     * the next item from the iterator will be fetched and pushed on the
     * stack when the stack is empty.
     *
     * @return the top or next item from these collections
     */
    public synchronized T peek() {
        // peeking is allowed neither in iterators nor streams,
        // hence the need for this class

        if (stack.empty()) {
            stack.push(iterator.next());
        }
        return stack.peek();
    }

    /**
     * Are both the stack and iterator empty?
     *
     * @return true if there are no more elements in the stack or iterator.
     */
    public boolean empty() {
        final boolean result = stack.empty() && !iterator.hasNext();
        return result;
    }
}