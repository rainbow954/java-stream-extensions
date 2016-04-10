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

import java.util.Comparator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Utility class of Stream producers and intermediate methods as an extension
 * of the Stream framework.
 *
 * <p>In every case unless otherwise noted, passing any null parameter to
 * these helper methods will likely result in a {@link NullPointerException}.
 *
 * @author Corey Morgan
 *
 */
public class Streams {

    /**
     * Pass through a stream while a condition is met.
     * When the condition is no longer met, the stream is terminated.
     *
     * <p><b>Usage:</b>
     * <br> &nbsp;&nbsp;&nbsp;&nbsp;
     * {@code Stream<Integer> doubles = Stream.iterate(1, i -> 2*i);}
     * <br> &nbsp;&nbsp;&nbsp;&nbsp;
     * {@code Stream<Integer> doublesUnder100 =
     *                        streamWhile(i -> i < 100, doubles)}
     * @param stream incoming stream
     * @param whileCond produce elements of the stream while this is met
     *
     * @param <T> type of the items in the stream
     * @return the same elements as the incoming stream, truncated
     *             when and if the `whileCond` predicate returns false.
     * @see Stream#limit
     */
    @Deprecated   // use jdk9 Stream.takeWhile(Predicate<T>) when it's ready
    public static <T> Stream<T> takeWhile(
        final Stream<T> stream,
        final Predicate<T> whileCond)
    {
        final Spliterator<T> spliter = new WhileSpliter<T>(stream, whileCond);
        final Stream<T> result = StreamSupport.stream(spliter, false);
        //        Stream<T> result = null;
        //        stream.peek(t -> {}).allMatch(whileCond);
        return result;
    }

    /**
     * A constant stream of a single value.
     *
     * @param <T>  elements of stream
     * @param value Anything, really
     * @return A stream of this constant value
     */
    public static <T> Stream<T> streamConst(final T value) {
        final Stream<T> result = Stream.iterate(value, titem -> value);
        return result;
    }

    /**
     * A {@link Stream} pulling from a {@link List} of similar streams, with
     * a {@link Comparator} comparing the top elements among all streams in
     *  the list. The least element is next in queue.
     *
     * @param <T>     Shared stream item type, comparable
     * @param streams List of similar streams
     * @param comp    Comparator comparing the top elements among all streams
     * @return        A new stream as a blend of all streams in the list.
     */
    public static <T> Stream<T> streamFlatten(
        final List<Stream<T>> streams,
        final Comparator<? super T> comp)
    {
        final Spliterator<T> spltr = new FlatSpliter<>(streams, comp);
        final Stream<T> result = StreamSupport.stream(spltr, false);
        return result;
    }

}
