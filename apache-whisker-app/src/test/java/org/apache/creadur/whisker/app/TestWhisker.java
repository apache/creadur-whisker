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
package org.apache.creadur.whisker.app;

import junit.framework.TestCase;

public class TestWhisker extends TestCase {

    Whisker subject;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        subject = new Whisker();
    }

    public void testSetNullLicenseConfiguration() {
        assertEquals(
                subject.setLicenseConfiguration(null).getLicenseConfiguration(),
                LicenseConfiguration.DEFAULT_LICENSE_CONFIGURATION);
    }

    public void testMinimalLicenseConfigurationConfiguresNoSourceUrls() {
        assertFalse(
                subject
                    .setLicenseConfiguration(LicenseConfiguration.MINIMAL)
                    .configuration().includeSourceURLsInLicense());
    }

    public void testIncludeSourceUrlsLicenseConfigurationConfiguresNoSourceUrls() {
        assertTrue(
                subject
                    .setLicenseConfiguration(LicenseConfiguration.INCLUDE_SOURCE_URLS)
                    .configuration().includeSourceURLsInLicense());
    }

}
