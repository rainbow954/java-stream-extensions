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

package com.wapitia.stream.test;

import static java.util.stream.IntStream.range;

import com.wapitia.stream.Streams;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestFlatSpliter {

    static class Transaction {
        @Override
        public String toString() {
            return date.toString();
        }

        public Transaction(LocalDate date) {
            this.date = date;
        }

        LocalDate date;

        static class Comp implements Comparator<Transaction> {

            @Override
            public int compare(Transaction o1, Transaction o2) {
                return o1.date.compareTo(o2.date);
            }
        }

    }

    @Test
    public void test1() {

        List<Stream<Transaction>> chx = range(0, 10)
            .mapToObj(
                i -> {
                    LocalDate date =
                           LocalDate.of(2016, Month.JANUARY, 1).plusDays(i);
                    return new Transaction(date);
                })
            .map(
                x -> {
                    return Stream.iterate(x,
                        (Transaction xp) ->
                            new Transaction(xp.date.plusWeeks(1)));
                })
            .collect(Collectors.toList());

        Stream<Transaction> results =
            Streams.streamFlatten(chx, new Transaction.Comp());
        results.limit(40).forEach(System.out::println);
    }


}
