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
package org.apache.creadur.whisker.out.velocity;

import static org.apache.creadur.whisker.app.ConfigurationBuilder.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.creadur.whisker.app.Result;
import org.apache.creadur.whisker.model.Descriptor;
import org.apache.creadur.whisker.model.License;
import org.apache.creadur.whisker.model.Organisation;
import org.apache.creadur.whisker.model.WithinDirectory;

import junit.framework.TestCase;

public class TestNoticeGeneration extends TestCase {

    StringResultWriterFactory writerFactory;
    VelocityEngine subject;
    License primaryLicense = new License(false, "This is the license text", Collections.<String> emptyList(), "example.org", "http://example.org", "Example License");
    String primaryOrg = "example.org";
    String primaryNotice = "The primary notice.";
    Collection<WithinDirectory> contents = new ArrayList<WithinDirectory>();
    Map<String, License> licenses = new HashMap<String, License>();
    Map<String, String> notices = new HashMap<String, String>();
    Map<String, Organisation> organisations = new HashMap<String, Organisation>();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        writerFactory = new StringResultWriterFactory();
        subject = new VelocityEngine(new EmptyLog());
        primaryLicense.storeIn(licenses);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testThatWhenThereAreNoThirdPartyNoticesHeaderIsNotShown() throws Exception {
        Descriptor work =
                new Descriptor(primaryLicense, primaryOrg,  primaryNotice,
                        licenses, notices, organisations, contents);

        subject.generate(work, writerFactory, aConfiguration().build());

        assertEquals("Only one request for NOTICE writer", 1, writerFactory.requestsFor(Result.NOTICE));
        assertEquals("When no third party notices, expect that only the primary notice is output", primaryNotice, writerFactory.firstOutputFor(Result.NOTICE).trim());
    }

    public void testThatNoticeOutputIsSkippedWhenThereAreNoNotices() throws Exception {
        Descriptor work =
                new Descriptor(primaryLicense, primaryOrg,  "",
                        licenses, notices, organisations, contents);

        subject.generate(work, writerFactory, aConfiguration().build());

        assertEquals("No requests for NOTICE writer", 0, writerFactory.requestsFor(Result.NOTICE));
    }

}
