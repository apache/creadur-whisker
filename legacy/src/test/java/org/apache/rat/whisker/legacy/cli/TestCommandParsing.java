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
package org.apache.rat.whisker.legacy.cli;

import org.apache.commons.cli.ParseException;
import org.apache.rat.whisker.legacy.app.Whisker;

import junit.framework.TestCase;

/**
 *
 */
public class TestCommandParsing extends TestCase {
    
    private Main subject;

    @Override
    protected void setUp() throws Exception {
        subject = new Main(new Whisker());
    }

    
    public void testSetLicenseDescriptorShortByCLI() throws Exception {
        exerciseShortLicenseDescriptionWithPath("/some/path");
        exerciseShortLicenseDescriptionWithPath("another/path");
        exerciseShortLicenseDescriptionWithPath("short");
        exerciseShortLicenseDescriptionWithPath("");
        exerciseShortLicenseDescriptionWithPath("http://url.style/path");
    }
    
    public void testSetLicenseDescriptorLongByCLI() throws Exception {
        exerciseLongLicenseDescriptionWithPath("/some/path");
        exerciseLongLicenseDescriptionWithPath("another/path");
        exerciseLongLicenseDescriptionWithPath("short");
        exerciseLongLicenseDescriptionWithPath("");
        exerciseLongLicenseDescriptionWithPath("http://url.style/path");
    }

    /**
     * @param aPath
     */
    private void exerciseShortLicenseDescriptionWithPath(String aPath) throws Exception {
        exerciseLicenseDescriptor(aPath, "-" + Main.LICENSE_DESCRIPTOR_SHORT_OPT);
    }
    
    /**
     * @param aPath
     */
    private void exerciseLongLicenseDescriptionWithPath(String aPath) throws Exception {
        exerciseLicenseDescriptor(aPath, "--" + Main.LICENSE_DESCRIPTOR_LONG_OPT);
    }


    /**
     * @param aPath
     * @param arg
     * @throws ParseException
     */
    private void exerciseLicenseDescriptor(String aPath, String arg)
            throws ParseException {
        assertEquals("Long license descriptor arg should set property on Whisker", aPath, subject.configure(args(arg, aPath)).getLicenseDescriptor());
    }
    
    private String[] args(String ...strings) {
        return strings;
    }
}
