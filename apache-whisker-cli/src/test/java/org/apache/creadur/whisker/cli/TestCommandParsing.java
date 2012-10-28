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

import junit.framework.TestCase;

import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.ParseException;
import org.apache.creadur.whisker.app.Act;
import org.apache.creadur.whisker.app.StreamableResource;
import org.apache.creadur.whisker.app.Whisker;
import org.apache.creadur.whisker.app.load.StreamableClassPathResource;
import org.apache.creadur.whisker.app.load.StreamableFileNameResource;

public class TestCommandParsing extends TestCase {

    private static final String LONG_OPT = "--";
    private static final String SHORT_OPT = "-";
    private Main subject;

    @Override
    protected void setUp() throws Exception {
        subject = new Main(new Whisker());
    }

    public void testGenerateAndAuditAreMutuallyExclusive() throws Exception {
        try {
            subject.configure(
                args(longOpt(CommandLineOption.LICENSE_DESCRIPTION.getLongName()), "PATH",
                        longOpt(CommandLineOption.ACT_TO_AUDIT.getLongName()),
                        longOpt(CommandLineOption.ACT_TO_GENERATE.getLongName())));

            fail("Expected audit and generate to together to throw exception");
        } catch (AlreadySelectedException e) {
            // expected
        }
    }

    public void testGenerateAndSkeletonAreMutuallyExclusive() throws Exception {
        try {
            subject.configure(
                args(longOpt(CommandLineOption.LICENSE_DESCRIPTION.getLongName()), "PATH",
                        longOpt(CommandLineOption.ACT_TO_SKELETON.getLongName()),
                        longOpt(CommandLineOption.ACT_TO_GENERATE.getLongName())));

            fail("Expected audit and generate to together to throw exception");
        } catch (AlreadySelectedException e) {
            // expected
        }
    }

    public void testSkeletonAndAuditAreMutuallyExclusive() throws Exception {
        try {
            subject.configure(
                args(longOpt(CommandLineOption.LICENSE_DESCRIPTION.getLongName()), "PATH",
                        longOpt(CommandLineOption.ACT_TO_AUDIT.getLongName()),
                        longOpt(CommandLineOption.ACT_TO_SKELETON.getLongName())));

            fail("Expected audit and generate to together to throw exception");
        } catch (AlreadySelectedException e) {
            // expected
        }
    }


    public void testSetGenerateAct() throws Exception {
        checkSetActForOption(Act.GENERATE, CommandLineOption.ACT_TO_GENERATE);
    }

    public void testSetAuditAct() throws Exception {
        checkSetActForOption(Act.AUDIT, CommandLineOption.ACT_TO_AUDIT);
    }

    public void testSetSkeletonAct() throws Exception {
        checkSetActForOption(Act.SKELETON, CommandLineOption.ACT_TO_SKELETON);
    }


    /**
     * @param act
     * @param option
     * @throws ParseException
     */
    private void checkSetActForOption(Act act, CommandLineOption option)
            throws ParseException {
        assertEquals(act + " arg should set property on Whisker", act, subject.configure(
                args(longOpt(CommandLineOption.LICENSE_DESCRIPTION.getLongName()), "PATH",
                        shortOpt(CommandLineOption.SOURCE.getShortName()), "path", longOpt(option.getLongName()))).getAct());
        assertEquals(act + "Audit arg should set property on Whisker", act, subject.configure(
                args(longOpt(CommandLineOption.LICENSE_DESCRIPTION.getLongName()), "PATH",
                        shortOpt(CommandLineOption.SOURCE.getShortName()), "path", shortOpt(option.getShortName()))).getAct());
    }

    public void testSetSourceByCli() throws Exception {
        checkSourceWithPath("/some/path");
        checkSourceWithPath("/");
        checkSourceWithPath("relative");
    }

    public void testAuditRequiresSource() throws Exception {
        try {
            subject.configure(args(
                    longOpt(CommandLineOption.ACT_TO_AUDIT.getLongName()),
                    shortOpt(CommandLineOption.LICENSE_DESCRIPTION.getShortName()), "some/path"));
            fail("Audit requires source");
        } catch (ParseException e) {
            // Expected
        }
    }

    /**
     * @param aPath
     */
    private void checkSourceWithPath(String aPath) throws Exception {
        checkSource(aPath, shortOpt(CommandLineOption.SOURCE.getShortName()));
        checkSource(aPath, longOpt(CommandLineOption.SOURCE.getLongName()));
    }


    private void checkSource(String aPath, String arg) throws ParseException {
        assertEquals("Source arg should set property on Whisker", aPath,
                subject.configure(args(arg, aPath,
                        longOpt(CommandLineOption.ACT_TO_AUDIT.getLongName()),
                        shortOpt(CommandLineOption.LICENSE_DESCRIPTION.getShortName()), "Whatever/bin")).getSource());
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
        exerciseLicenseDescriptor(aPath, shortOpt(CommandLineOption.LICENSE_DESCRIPTION.getShortName()));
    }


    /**
     * @param licenseDescriptorShortOpt
     * @return
     */
    private String shortOpt(char licenseDescriptorShortOpt) {
        return SHORT_OPT + licenseDescriptorShortOpt;
    }

    /**
     * @param aPath
     */
    private void exerciseLongLicenseDescriptionWithPath(String aPath) throws Exception {
        exerciseLicenseDescriptor(aPath, longOpt(CommandLineOption.LICENSE_DESCRIPTION.getLongName()));
    }


    /**
     * @param licenseDescriptorLongOpt
     * @return
     */
    private String longOpt(String licenseDescriptorLongOpt) {
        return LONG_OPT + licenseDescriptorLongOpt;
    }


    /**
     * @param aPath
     * @param arg
     * @throws ParseException
     */
    private void exerciseLicenseDescriptor(String aPath, String arg)
            throws ParseException {
        final StreamableResource streamableResource = subject.configure(
                        args(arg, aPath, longOpt(CommandLineOption.ACT_TO_GENERATE.getLongName())))
                            .getLicenseDescriptor();

        assertEquals("License descriptor arg should set property on Whisker", aPath,
                name(streamableResource));
    }

    private String name(final StreamableResource streamableResource) {
        if (streamableResource instanceof StreamableClassPathResource) {
            return ((StreamableClassPathResource)streamableResource).getName();
        } else {
            return ((StreamableFileNameResource)streamableResource).getFileName();
        }
    }

    private String[] args(String ...strings) {
        return strings;
    }
}
