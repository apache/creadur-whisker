/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.creadur.whisker.it;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import junit.framework.TestCase;

public class TestAllCheck extends TestCase  {

    Collection<Check> checks;
    Results results;
    AllCheck subject;


    protected void setUp() throws Exception {
        super.setUp();
        results = new Results();
        checks = new ArrayList<Check>();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testNoChecksPass() {
        subject = new AllCheck(Collections.<Check> emptyList());
        subject.check("a line");
        verifyPass();
    }

    public void testOneCheckFail() {
        final String value = "some value";
        checks.add(new AnyLineContainsCheck(value));

        subject = new AllCheck(checks);
        subject.check("a line");
        verifyFail();
    }

    private void verifyFail() {
        assertFalse(subject.hasPassed());
        subject.report(results);
        assertTrue(results.hasFailed());
    }

    public void testOneCheckPass() {
        final String value = "some value";
        checks.add(new AnyLineContainsCheck(value));

        subject = new AllCheck(checks);
        subject.check(value);
        verifyPass();
    }

    private void verifyPass() {
        assertTrue(subject.hasPassed());
        subject.report(results);
        assertFalse(results.hasFailed());
    }

    public void testTwoChecksPass() {
        final String value = "some value";
        final String anotherValue = "another value";
        checks.add(new AnyLineContainsCheck(value));
        checks.add(new AnyLineContainsCheck(anotherValue));
        subject = new AllCheck(checks);
        subject.check(value);
        subject.check(anotherValue);
        verifyPass();
    }

    public void testTwoChecksFail() {
        final String value = "some value";
        final String anotherValue = "another value";
        checks.add(new AnyLineContainsCheck(value));
        checks.add(new AnyLineContainsCheck(anotherValue));
        subject = new AllCheck(checks);
        subject.check("not");
        verifyFail();
    }

    public void testTwoChecksWhenOneMatchFailsAllFail() {
        final String value = "some value";
        final String anotherValue = "another value";
        checks.add(new AnyLineContainsCheck(value));
        checks.add(new AnyLineContainsCheck(anotherValue));
        subject = new AllCheck(checks);
        subject.check("not");
        subject.check(value);
        verifyFail();
    }

}
