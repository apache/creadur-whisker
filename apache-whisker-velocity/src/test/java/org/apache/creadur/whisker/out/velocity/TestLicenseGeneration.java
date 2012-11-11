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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.creadur.whisker.app.Result;
import org.apache.creadur.whisker.model.ByOrganisation;
import org.apache.creadur.whisker.model.Descriptor;
import org.apache.creadur.whisker.model.License;
import org.apache.creadur.whisker.model.Organisation;
import org.apache.creadur.whisker.model.Resource;
import org.apache.creadur.whisker.model.WithLicense;
import org.apache.creadur.whisker.model.WithinDirectory;

import junit.framework.TestCase;

public class TestLicenseGeneration extends TestCase {

    StringResultWriterFactory writerFactory;
    VelocityEngine subject;
    DescriptorBuilderForTesting builder;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        writerFactory = new StringResultWriterFactory();
        subject = new VelocityEngine(new EmptyLog());
        builder = new DescriptorBuilderForTesting();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }


    public void testThatWhenThereAreNoThirdPartyContentsFooterIsNotShown() throws Exception {
        Descriptor work = builder.build();

        subject.generate(work, writerFactory);

        assertTrue("Check that work is suitable for this test", work.isPrimaryOnly());
        assertEquals("Only one request for LICENSE writer", 1, writerFactory.requestsFor(Result.LICENSE));
        assertEquals("When no third party contents, expect that only the license text is output",
                builder.getPrimaryLicenseText(),
                writerFactory.firstOutputFor(Result.LICENSE).trim());
    }

    public void testThatFooterIsShownWhenThereAreThirdPartyContents() throws Exception {
        Descriptor work = builder.withPrimaryLicenseAndThirdPartyOrgInDirectory("lib").build();

        assertFalse("Check that work is suitable for this test", work.isPrimaryOnly());

        subject.generate(work, writerFactory);

        assertEquals("Only one request for LICENSE writer", 1, writerFactory.requestsFor(Result.LICENSE));
        assertTrue("Expect information when third party contents present: " + writerFactory.firstOutputFor(Result.LICENSE),
                StringUtils.contains(writerFactory.firstOutputFor(Result.LICENSE),
                        "This distribution contains third party resources."));
    }

    public void testSecondaryCopyrightNoticeForPrimaryLicense() throws Exception {
        Descriptor work = builder.withPrimaryCopyrightNotice().withPrimaryLicenseAndOrgInDirectory("lib").build();

        subject.generate(work, writerFactory);

        assertEquals("Only one request for LICENSE writer", 1, writerFactory.requestsFor(Result.LICENSE));
        assertTrue("Expect secondary copyright to be presented: " + writerFactory.firstOutputFor(Result.LICENSE),
                StringUtils.contains(writerFactory.firstOutputFor(Result.LICENSE),
                        builder.getSecondaryCopyright()));
        assertTrue("Expect resource to be indicated: " + writerFactory.firstOutputFor(Result.LICENSE),
                StringUtils.contains(writerFactory.firstOutputFor(Result.LICENSE),
                        builder.getResourceName()));

    }

}
