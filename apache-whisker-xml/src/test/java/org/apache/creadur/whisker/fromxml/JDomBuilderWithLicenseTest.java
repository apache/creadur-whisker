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
package org.apache.creadur.whisker.fromxml;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.creadur.whisker.model.License;
import org.apache.creadur.whisker.model.Organisation;
import org.apache.creadur.whisker.model.WithLicense;
import org.jdom2.CDATA;
import org.jdom2.Element;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 
 */
class JDomBuilderWithLicenseTest {

    private JDomBuilder subject;

    @BeforeEach
    void setUp() throws Exception {
        subject = new JDomBuilder();
    }

    @AfterEach
    void tearDown() throws Exception {
    }


    @Test
    void licenseFromListThrowsMissingIDWhenEmpty() throws Exception {        
        final Map<String, License> licenses = new HashMap<String, License>();
        try {
            subject.license(new Element("with-license").setAttribute("id", "id"), licenses);
            fail("Throw an exception when the ID is missing");
        } catch (MissingIDException e) {
            // expected
        }
    }

    @Test
    void licenseFromListThrowsMissingIDWhenIDsAreMismatched() throws Exception {        
        final Map<String, License> licenses = new HashMap<String, License>();
        addLicenseTo(licenses, "noise");
        try {
            subject.license(new Element("with-license").setAttribute("id", "id"), licenses);
            fail("Throw an exception when the ID is missing");
        } catch (MissingIDException e) {
            // expected
        }
    }

    @Test
    void licenseFromListFindsLicense() throws Exception {
        final String id = "id";
        
        final Map<String, License> licenses = new HashMap<String, License>();
        @SuppressWarnings("unchecked")
        final License expected = new License(false, "", Collections.EMPTY_LIST, id, "url", "name");
        expected.storeIn(licenses);
        addLicenseTo(licenses, "noise");
        
        final License output = subject.license(new Element("with-license").setAttribute("id", id), licenses);
        assertNotNull(output, "Expected builder to build");
        assertEquals(expected, output, "Expected license to be found");
    }

    /**
     * @param licenses
     * @param id
     * @return 
     */
    @SuppressWarnings("unchecked")
    private License addLicenseTo(final Map<String, License> licenses,
            final String id) {
        return new License(false, "", Collections.EMPTY_LIST, id, "noise url", "name").storeIn(licenses);
    }

    @Test
    void buildLicenseFromElementWithCopyrightNotice() throws Exception {
        checkSetCopyrightNotice("Some Copyright Text", "Some Copyright Text");
    }

    @Test
    void buildLicenseFromElementWithCopyrightNoticeTrimSpaces() throws Exception {
        checkSetCopyrightNotice("  Some Copyright Text  ", "Some Copyright Text");
    }
    
    /**
     * @param copyrightNotice
     * @param expectCopyrightNotice
     */
    private void checkSetCopyrightNotice(final String copyrightNotice,
            final String expectCopyrightNotice) {
        final String id = "an ID";
        final Map<String, License> licenses = new HashMap<String, License>();
        final Map<String, Organisation> organisations = new HashMap<String, Organisation>();
        addLicenseTo(licenses, id);
        final WithLicense output = subject.withLicense(
                new Element("with-license")
                    .setAttribute("id", id)
                    .addContent(new Element("copyright-notice").setContent(new CDATA(copyrightNotice)))
                , licenses, organisations);
        assertNotNull(output, "Expected builder to build");
        assertEquals(expectCopyrightNotice, output.getCopyrightNotice(), "Builder should set copyright notice from xml");
    }

    @Test
    void buildLicenseFromElementNoCopyrightNoticeNoParameters() throws Exception {
        checkWithElementJustId("some id");
        checkWithElementJustId("id");
        checkWithElementJustId("  some id  ");
        checkWithElementJustId("");
    }
    
    /**
     * @param id
     */
    private void checkWithElementJustId(final String id) {
        final Map<String, License> licenses = new HashMap<String, License>();
        final Map<String, Organisation> organisations = new HashMap<String, Organisation>();
        final License expected = addLicenseTo(licenses, id);
        final WithLicense output = subject.withLicense(new Element("with-license").setAttribute("id", id), licenses, organisations);
        assertNotNull(output, "Expected builder to build");
        assertEquals(expected, output.getLicense(), "Expected builder to find license and set it");
    }


    @Test
    void buildLicenseWithParametersIsEmptyWhenNoParameters() throws Exception {
        final Map<String, String> results = subject.parameters(
                new Element("with-license")
                .addContent(new Element("license-parameters")));
        assertNotNull(results, "Expected builder to build parameters");
        assertTrue(results.isEmpty(), "When there are no parameters, map should be empty");
    }


    @Test
    void buildLicenseWithOneParameter() throws Exception {
        checkBuildLicenseWithParameters(1);
    }


    @Test
    void buildLicenseWithTwoParameters() throws Exception {
        checkBuildLicenseWithParameters(2);
    }

    @Test
    void buildLicenseWith3Parameters() throws Exception {
        checkBuildLicenseWithParameters(3);
    }

    @Test
    void buildLicenseWith4Parameters() throws Exception {
        checkBuildLicenseWithParameters(4);
    }

    @Test
    void buildLicenseWith7Parameters() throws Exception {
        checkBuildLicenseWithParameters(7);
    }

    @Test
    void buildLicenseWith11Parameters() throws Exception {
        checkBuildLicenseWithParameters(11);
    }

    @Test
    void buildLicenseWith101Parameters() throws Exception {
        checkBuildLicenseWithParameters(101);
    }
    
    /**
     * @param numberOfParameters
     */
    private void checkBuildLicenseWithParameters(final int numberOfParameters) {
        final Element licenseParametersElement = new Element("license-parameters");
        for (int i=0;i<numberOfParameters; i++) {
            licenseParametersElement
            .addContent(new Element("parameter")
                .addContent(new Element("name").addContent(new CDATA(name(i))))
                .addContent(new Element("value").addContent(new CDATA(value(i)))));
        }
        final Element input = new Element("with-license")
            .addContent(licenseParametersElement);
        final Map<String, String> results = 
            subject.parameters(input);
        assertNotNull(results, "Expected builder to build parameters");
        assertEquals(numberOfParameters, results.size(), "Expected builder to add one name, value pair per parameter");
        for (int i=0;i<numberOfParameters;i++) {
            assertEquals(results.get(name(i)), value(i), "Value indexed by name");
        }
    }

    @Test
    void withLicenseBuildWithParameters() throws Exception {
        for (int i=0;i<128;i++) {
            checkBuildWithLicenseWithParameters(i);
        }
    }

    
    /**
     * @param numberOfParameters
     */
    private void checkBuildWithLicenseWithParameters(final int numberOfParameters) {
        final String id = "some id";
        final Map<String, License> licenses = new HashMap<String, License>();
        final Map<String, Organisation> organisations = new HashMap<String, Organisation>();
        addLicenseTo(licenses, id);
        final Element licenseParametersElement = new Element("license-parameters");
        for (int i=0;i<numberOfParameters; i++) {
            licenseParametersElement
            .addContent(new Element("parameter")
                .addContent(new Element("name").addContent(new CDATA(name(i))))
                .addContent(new Element("value").addContent(new CDATA(value(i)))));
        }
        final Element input = new Element("with-license").setAttribute("id", id)
            .addContent(licenseParametersElement);
        final Map<String, String> results = 
            subject.withLicense(input, licenses, organisations).getParameters();
        assertNotNull(results, "Expected builder to build parameters");
        assertEquals(numberOfParameters, results.size(), "Expected builder to add one name, value pair per parameter");
        for (int i=0;i<numberOfParameters;i++) {
            assertEquals(results.get(name(i)), value(i), "Value indexed by name");
        }
    }

    
    /**
     * @param i
     * @return
     */
    private String name(int i) {
        return "name" + i;
    }

    /**
     * @param i
     * @return
     */
    private String value(int i) {
        return "value" + i;
    }

    @Test
    void buildLicenseWithParametersThrowsExceptionWhenParameterIsDuplicated() throws Exception {
        try {
            subject.parameters(
                    new Element("with-license")
                        .addContent(new Element("license-parameters")
                            .addContent(new Element("parameter")
                                .addContent(new Element("name").addContent(new CDATA("A parameter name")))
                                .addContent(new Element("value").addContent(new CDATA("A parameter value"))))
                            .addContent(new Element("parameter")
                                .addContent(new Element("name").addContent(new CDATA("A parameter name")))
                                .addContent(new Element("value").addContent(new CDATA("A parameter value"))))));
            fail("When element contains duplicate definitions of the same parameter, the build should throw an exception");
        } catch (DuplicateElementException e) {
            // expected
        }
    }

    @Test
    void buildCollectWithLicenses() throws Exception {
        for (int i=0; i<256; i++) {
            checkCollectWithLicenses(i);
        }
    }

    /**
     * @param numberOfWithLicenses
     */
    private void checkCollectWithLicenses(final int numberOfWithLicenses) {
        final String id = "an ID";
        final Map<String, License> licenses = new HashMap<String, License>();
        final Map<String, Organisation> organisations = new HashMap<String, Organisation>();
        final Element parent = new Element("within");
        
        for(int i=0;i<numberOfWithLicenses;i++) {
            addWithLicense(id + i, licenses, parent);            
        }
        
        final Collection<WithLicense> results = subject.withLicenses(licenses, organisations, parent);
        assertNotNull(results, "Builder should build");
        assertEquals(numberOfWithLicenses, results.size(), "Builder should build one with-license for each child");
    }

    /**
     * @param id
     * @param licenses
     * @param parent
     */
    private void addWithLicense(final String id,
            final Map<String, License> licenses, final Element parent) {
        addLicenseTo(licenses, id);
        parent.addContent(new Element("with-license").setAttribute("id", id));
    }
}
