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

import com.wapitia.common.test.junit.WapitiaTestMain;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * JUnit test suite servicing the {@code com.eis.budget} package and associated
 * code. The {@link Suite.SuiteClasses @SuiteClasses} annotation lists
 * the JUnit {@link org.junit.Test @Test}  classes in the suite.
 *
 * @author Corey Morgan
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    TestFlatSpliter.class,
    })
public class StreamsTestSuite {

    /**
     * Runs the tests in this test suite by invoking
     * {@link JUnitCore#runClasses}
     * and then prints the results to standard out.
     *
     * <p>Note that various JUnit test harnesses such as the Eclipse plugin
     * don't call this {@code main} but rather instantiate and run this
     * test suite from their own  {@code main}.
     *
     * @param args not used here
     */
    public static void main(String[] args) {
        WapitiaTestMain.runSuite(StreamsTestSuite.class);
    }

}
