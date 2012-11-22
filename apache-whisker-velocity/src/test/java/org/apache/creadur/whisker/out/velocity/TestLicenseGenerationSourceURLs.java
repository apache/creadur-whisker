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
import junit.framework.TestCase;

import org.apache.commons.lang3.StringUtils;
import org.apache.creadur.whisker.app.Result;
import org.apache.creadur.whisker.model.Descriptor;

public class TestLicenseGenerationSourceURLs extends TestCase {

    StringResultWriterFactory writerFactory;
    VelocityEngine subject;
    DescriptorBuilderForTesting builder;
    Descriptor work;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        writerFactory = new StringResultWriterFactory();
        subject = new VelocityEngine(new EmptyLog());
        builder = new DescriptorBuilderForTesting().withSourceURL();
        work = builder.withPrimaryLicenseAndThirdPartyOrgInDirectory(".").build();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testNoSourceUrlsConfiguration() throws Exception {
        subject.generate(work, writerFactory, aConfiguration().noSourceURLsInLicense().build());
        assertFalse(failureMessage(), outputContainsSourceUrl());

    }

    private boolean outputContainsSourceUrl() {
        return StringUtils.contains(writerFactory.firstOutputFor(Result.LICENSE),
                builder.sourceUrl);
    }

    private String failureMessage() {
        return "Expect information when third party contents present: " + writerFactory.firstOutputFor(Result.LICENSE);
    }

    public void testWithSourceUrlsConfiguration() throws Exception {
        subject.generate(work, writerFactory, aConfiguration().withSourceURLsInLicense().build());

        assertTrue(failureMessage(), outputContainsSourceUrl());

    }
}
