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

import junit.framework.TestCase;

import org.apache.commons.lang3.StringUtils;
import org.apache.creadur.whisker.app.Result;
import org.apache.creadur.whisker.model.Descriptor;

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

        subject.generate(work, writerFactory, aConfiguration().build());

        assertTrue("Check that work is suitable for this test", work.isPrimaryOnly());
        assertEquals("Only one request for LICENSE writer", 1, writerFactory.requestsFor(Result.LICENSE));
        assertEquals("When no third party contents, expect that only the license text is output",
                builder.getPrimaryLicenseText(),
                writerFactory.firstOutputFor(Result.LICENSE).trim());
    }

    public void testThatFooterIsShownWhenThereAreThirdPartyContents() throws Exception {
        Descriptor work = builder.withPrimaryLicenseAndThirdPartyOrgInDirectory("lib").build();

        assertFalse("Check that work is suitable for this test", work.isPrimaryOnly());

        subject.generate(work, writerFactory, aConfiguration().build());

        assertEquals("Only one request for LICENSE writer", 1, writerFactory.requestsFor(Result.LICENSE));
        assertTrue("Expect information when third party contents present: " + writerFactory.firstOutputFor(Result.LICENSE),
                StringUtils.contains(writerFactory.firstOutputFor(Result.LICENSE),
                        "This distribution contains third party resources."));
    }

    public void testSecondaryCopyrightNoticeForPrimaryLicense() throws Exception {
        subject.generate(
                builder
                .withSecondaryCopyrightNotice()
                .withPrimaryCopyrightNotice()
                .withPrimaryLicenseAndOrgInDirectory("lib").build(), writerFactory, aConfiguration().build());

        assertEquals("Only one request for LICENSE writer", 1, writerFactory.requestsFor(Result.LICENSE));
        assertTrue("Expect secondary copyright to be presented: " + writerFactory.firstOutputFor(Result.LICENSE),
                StringUtils.contains(writerFactory.firstOutputFor(Result.LICENSE),
                        builder.getSecondaryCopyright()));
        verifyThatResourceNameIsWritten();

    }

    private void verifyThatResourceNameIsWritten() {
        assertTrue("Expect resource to be indicated: " + writerFactory.firstOutputFor(Result.LICENSE),
                StringUtils.contains(writerFactory.firstOutputFor(Result.LICENSE),
                        builder.getResourceName()));
    }

    private void verifyThatResourceNameIsNotWritten() {
        assertFalse("Expect resource to be indicated: " + writerFactory.firstOutputFor(Result.LICENSE),
                StringUtils.contains(writerFactory.firstOutputFor(Result.LICENSE),
                        builder.getResourceName()));
    }

    public void testPrimaryOrganisationSecondaryLicense() throws Exception {
        subject.generate(
                builder.withSecondaryLicensePrimaryOrganisationInDirectory("lib").build(),
                writerFactory, aConfiguration().build());
        assertEquals("Only one request for LICENSE writer", 1, writerFactory.requestsFor(Result.LICENSE));

        verifyThatResourceNameIsWritten();

    }

    public void testIgnorePrimaryOrganisationPrimaryLicense() throws Exception {
        subject.generate(
                builder.withPrimaryLicenseAndOrgInDirectory("lib").build(),
                writerFactory, aConfiguration().build());
        assertEquals("Only one request for LICENSE writer", 1, writerFactory.requestsFor(Result.LICENSE));
        verifyThatResourceNameIsNotWritten();

    }

    public void testIgnorePrimaryOrganisationPrimaryLicensePrimaryCopyrightNotice() throws Exception {
        subject.generate(
                builder
                    .withPrimaryCopyrightNotice()
                    .withPrimaryLicenseAndOrgInDirectory("lib")
                    .build(),
                writerFactory, aConfiguration().build());
        assertEquals("Only one request for LICENSE writer", 1, writerFactory.requestsFor(Result.LICENSE));
        verifyThatResourceNameIsNotWritten();

    }

}
