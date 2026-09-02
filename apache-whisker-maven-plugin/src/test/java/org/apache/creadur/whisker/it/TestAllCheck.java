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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestAllCheck {

    Collection<Check> checks;
    Results results;
    AllCheck subject;


    @BeforeEach
    void setUp() throws Exception {
        results = new Results();
        checks = new ArrayList<Check>();
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    void noChecksPass() {
        subject = new AllCheck(Collections.<Check> emptyList());
        subject.check("a line");
        verifyPass();
    }

    @Test
    void oneCheckFail() {
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

    @Test
    void oneCheckPass() {
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

    @Test
    void twoChecksPass() {
        final String value = "some value";
        final String anotherValue = "another value";
        checks.add(new AnyLineContainsCheck(value));
        checks.add(new AnyLineContainsCheck(anotherValue));
        subject = new AllCheck(checks);
        subject.check(value);
        subject.check(anotherValue);
        verifyPass();
    }

    @Test
    void twoChecksFail() {
        final String value = "some value";
        final String anotherValue = "another value";
        checks.add(new AnyLineContainsCheck(value));
        checks.add(new AnyLineContainsCheck(anotherValue));
        subject = new AllCheck(checks);
        subject.check("not");
        verifyFail();
    }

    @Test
    void twoChecksWhenOneMatchFailsAllFail() {
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
