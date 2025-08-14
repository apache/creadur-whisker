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

import static org.apache.creadur.whisker.app.ConfigurationBuilder.aConfiguration;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.apache.creadur.whisker.app.Result;
import org.apache.creadur.whisker.model.Descriptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestLicenseGenerationSourceURLs {

    StringResultWriterFactory writerFactory;
    LoggingVelocityEngine subject;
    DescriptorBuilderForTesting builder;
    Descriptor work;

    @BeforeEach
    void setUp() throws Exception {
        writerFactory = new StringResultWriterFactory();
        subject = new LoggingVelocityEngine();
        builder = new DescriptorBuilderForTesting().withSourceURL();
        work = builder.withPrimaryLicenseAndThirdPartyOrgInDirectory(".").build();
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    void noSourceUrlsConfiguration() throws Exception {
        subject.generate(work, writerFactory, aConfiguration().noSourceURLsInLicense().build());
        assertFalse(outputContainsSourceUrl(), failureMessage());

    }

    private boolean outputContainsSourceUrl() {
        return StringUtils.contains(writerFactory.firstOutputFor(Result.LICENSE),
                builder.sourceUrl);
    }

    private String failureMessage() {
        return "Expect information when third party contents present: " + writerFactory.firstOutputFor(Result.LICENSE);
    }

    @Test
    void withSourceUrlsConfiguration() throws Exception {
        subject.generate(work, writerFactory, aConfiguration().withSourceURLsInLicense().build());

        assertTrue(outputContainsSourceUrl(), failureMessage());

    }
}
