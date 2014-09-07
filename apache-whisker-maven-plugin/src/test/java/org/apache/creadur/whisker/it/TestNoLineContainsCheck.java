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

import junit.framework.TestCase;

public class TestNoLineContainsCheck extends TestCase {

    private static final String CHECKED_FOR_VALUE = "some value";

    NoLineContainsCheck subject;
    Results results;

    public void setUp() {
        subject = new NoLineContainsCheck(CHECKED_FOR_VALUE);
        results = new Results();
    }

    public void testNullLine() {
        subject.check(null);
        assertTrue(subject.hasPassed());
        subject.report(results);
        assertFalse(results.hasFailed());
        assertNull(results.collate());
    }

    public void testDifferentLine() {
        subject.check("something different");
        assertTrue(subject.hasPassed());
        subject.report(results);
        assertFalse(results.hasFailed());
        assertNull(results.collate());
    }

    public void testSameLine() {
        subject.check(CHECKED_FOR_VALUE);
        assertFalse(subject.hasPassed());
        subject.report(results);
        assertTrue(results.hasFailed());
        assertTrue(results.collate().contains(NoLineContainsCheck.WARNING + CHECKED_FOR_VALUE));
    }

    public void testLineContainingText() {
        subject.check("prefix " + CHECKED_FOR_VALUE + " suffix");
        assertFalse(subject.hasPassed());
        subject.report(results);
        assertTrue(results.hasFailed());
        assertTrue(results.collate().contains(NoLineContainsCheck.WARNING + CHECKED_FOR_VALUE));
    }

}
