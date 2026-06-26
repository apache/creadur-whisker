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
import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.creadur.whisker.app.Result;
import org.apache.creadur.whisker.model.Descriptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestLicenseGeneration {

    StringResultWriterFactory writerFactory;
    LoggingVelocityEngine subject;
    DescriptorBuilderForTesting builder;

    @BeforeEach
    void setUp() throws Exception {
        writerFactory = new StringResultWriterFactory();
        subject = new LoggingVelocityEngine();
        builder = new DescriptorBuilderForTesting();
    }

    @AfterEach
    void tearDown() throws Exception {
    }


    @Test
    void thatWhenThereAreNoThirdPartyContentsFooterIsNotShown() throws Exception {
        Descriptor work = builder.build();

        subject.generate(work, writerFactory, aConfiguration().build());

        assertTrue(work.isPrimaryOnly(), "Check that work is suitable for this test");
        assertEquals(1, writerFactory.requestsFor(Result.LICENSE), "Only one request for LICENSE writer");
        assertEquals(builder.getPrimaryLicenseText(),
                writerFactory.firstOutputFor(Result.LICENSE).trim(),
                "When no third party contents, expect that only the license text is output");
    }

    @Test
    void thatFooterIsShownWhenThereAreThirdPartyContents() throws Exception {
        Descriptor work = builder.withPrimaryLicenseAndThirdPartyOrgInDirectory("lib").build();

        assertFalse(work.isPrimaryOnly(), "Check that work is suitable for this test");

        subject.generate(work, writerFactory, aConfiguration().build());

        assertEquals(1, writerFactory.requestsFor(Result.LICENSE), "Only one request for LICENSE writer");
        assertTrue(StringUtils.contains(writerFactory.firstOutputFor(Result.LICENSE),
                        "This distribution contains third party resources."),
                "Expect information when third party contents present: " + writerFactory.firstOutputFor(Result.LICENSE));
    }

    @Test
    void secondaryCopyrightNoticeForPrimaryLicense() throws Exception {
        subject.generate(
                builder
                .withSecondaryCopyrightNotice()
                .withPrimaryCopyrightNotice()
                .withPrimaryLicenseAndOrgInDirectory("lib").build(), writerFactory, aConfiguration().build());

        assertEquals(1, writerFactory.requestsFor(Result.LICENSE), "Only one request for LICENSE writer");
        assertTrue(StringUtils.contains(writerFactory.firstOutputFor(Result.LICENSE),
                        builder.getSecondaryCopyright()),
                "Expect secondary copyright to be presented: " + writerFactory.firstOutputFor(Result.LICENSE));
        verifyThatResourceNameIsWritten();

    }

    private void verifyThatResourceNameIsWritten() {
        assertTrue(StringUtils.contains(writerFactory.firstOutputFor(Result.LICENSE),
                        builder.getResourceName()),
                "Expect resource to be indicated: " + writerFactory.firstOutputFor(Result.LICENSE));
    }

    private void verifyThatResourceNameIsNotWritten() {
        assertFalse(StringUtils.contains(writerFactory.firstOutputFor(Result.LICENSE),
                        builder.getResourceName()),
                "Expect resource to be indicated: " + writerFactory.firstOutputFor(Result.LICENSE));
    }

    @Test
    void primaryOrganisationSecondaryLicense() throws Exception {
        subject.generate(
                builder.withSecondaryLicensePrimaryOrganisationInDirectory("lib").build(),
                writerFactory, aConfiguration().build());
        assertEquals(1, writerFactory.requestsFor(Result.LICENSE), "Only one request for LICENSE writer");

        verifyThatResourceNameIsWritten();

    }

    @Test
    void ignorePrimaryOrganisationPrimaryLicense() throws Exception {
        subject.generate(
                builder.withPrimaryLicenseAndOrgInDirectory("lib").build(),
                writerFactory, aConfiguration().build());
        assertEquals(1, writerFactory.requestsFor(Result.LICENSE), "Only one request for LICENSE writer");
        verifyThatResourceNameIsNotWritten();

    }

    @Test
    void ignorePrimaryOrganisationPrimaryLicensePrimaryCopyrightNotice() throws Exception {
        subject.generate(
                builder
                    .withPrimaryCopyrightNotice()
                    .withPrimaryLicenseAndOrgInDirectory("lib")
                    .build(),
                writerFactory, aConfiguration().build());
        assertEquals(1, writerFactory.requestsFor(Result.LICENSE), "Only one request for LICENSE writer");
        verifyThatResourceNameIsNotWritten();

    }

}
