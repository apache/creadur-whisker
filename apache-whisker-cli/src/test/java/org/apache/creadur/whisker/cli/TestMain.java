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
package org.apache.creadur.whisker.cli;

import org.apache.creadur.whisker.app.Whisker;

import junit.framework.TestCase;

public class TestMain extends TestCase {

    Main subject;

    @Override
    protected void setUp() throws Exception {
       super.setUp();
       subject = new Main(new Whisker());
    }

    public void testIsHelpHelp() throws Exception {
        assertTrue(subject.printHelp(args("--help")));
    }

    public void testIsGenerateHelp() throws Exception {
        assertFalse(subject.printHelp(args("--generate")));
    }

    public void testIsAuditHelp() throws Exception {
        assertFalse(subject.printHelp(args("--audit")));
    }

    public void testIsSkeletonHelp() throws Exception {
        assertFalse(subject.printHelp(args("--skeleton")));
    }

    private String[] args(String ...strings) {
        return strings;
    }
}
