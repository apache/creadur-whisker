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

import org.apache.creadur.whisker.model.License;

import org.jdom2.CDATA;
import org.jdom2.Element;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 
 */
class JDomBuilderLicenseTest {

    private JDomBuilder subject;

    @BeforeEach
    void setUp() throws Exception {
        subject = new JDomBuilder();
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    void licenseWithNameUrl() throws Exception {
        checkSetNameUrlId("a name", "some url", "some id");
        checkSetNameUrlId("name", "url", "id");
        checkSetNameUrlId("NAME", "URL", "ID");
        checkSetNameUrlId("a name", "  some url  ", "  some id  ");        
    }

    /**
     * @param nameValue
     * @param urlValue
     * @param idValue
     */
    private void checkSetNameUrlId(final String nameValue,
            final String urlValue, final String idValue) {
        final License result = subject.license(new Element("license")
                    .setAttribute("name", nameValue)
                    .setAttribute("url", urlValue)
                    .setAttribute("id", idValue));
        assertNotNull(result, "Builder should build");
        assertEquals(idValue, result.getId(), "Set id");
        assertEquals(urlValue, result.getURL(), "Set url");
        assertEquals(nameValue, result.getName(), "Set name");
    }

    @Test
    void licenseRequiresSourceTrue() throws Exception {
        assertTrue(licenseWithSource("yes").isSourceRequired(), "Expected required source to be set");
    }

    @Test
    void licenseRequiresSourceTrueAllCaps() throws Exception {
        assertTrue(licenseWithSource("YES").isSourceRequired(), 
                "Expected required source to be set even when yes is in all caps case. This is against the schema.");
    }

    @Test
    void licenseRequiresSourceTrueMixedCase() throws Exception {
        assertTrue(licenseWithSource("Yes").isSourceRequired(), 
                "Expected required source to be set even when yes is in mixed case. This is against the schema.");
    }

    @Test
    void licenseRequiresSourceFalse() throws Exception {
        assertFalse(licenseWithSource("no").isSourceRequired(), "Expected required source to be set");
    }

    @Test
    void licenseRequiresSourceDefault() throws Exception {
        final License result = subject.license(new Element("license")
            .setAttribute("name", "name Value")
            .setAttribute("url", "urlValue")
            .setAttribute("id", "idValue"));
        assertNotNull(result, "Builder should build");
        assertFalse(result.isSourceRequired(), "Expected to default to false");
    }
    
    /**
     * @param requireSourceValue
     * @return
     */
    private License licenseWithSource(final String requireSourceValue) {
        final License result = subject.license(new Element("license")
            .setAttribute("name", "name Value")
            .setAttribute("url", "urlValue")
            .setAttribute("id", "idValue")
            .setAttribute("requires-source", requireSourceValue));
        assertNotNull(result, "Builder should build");
        return result;
    }

    @Test
    void licenseNoParameters() throws Exception {
        final License result = subject.license(new Element("license")
            .setAttribute("name", "name Value")
            .setAttribute("url", "urlValue")
            .setAttribute("id", "idValue")
            .setContent(new Element("template")));
        assertNotNull(result, "Builder should build");
        assertNotNull(result.getExpectedParameters(), "Builder should always set parameters even when empty");
        assertTrue(result.getExpectedParameters().isEmpty(), "No parameters in template so collection should be empty");
    }

    @Test
    void licenseOneParameter() throws Exception {
        final String parameterName = "whatever";
        final License result = subject.license(new Element("license")
            .setAttribute("name", "name Value")
            .setAttribute("url", "urlValue")
            .setAttribute("id", "idValue")
            .setContent(new Element("template").setContent(new Element("parameter-name").setContent(new CDATA(parameterName)))));
        assertNotNull(result, "Builder should build");
        assertNotNull(result.getExpectedParameters(), "Builder should always set parameters even when empty");
        assertEquals(1, result.getExpectedParameters().size(), "One parameters in template");
        assertEquals(parameterName, result.getExpectedParameters().iterator().next(), "Parameter name should be set from xml");
    }

    @Test
    void licenseTwoParameters() throws Exception {
        final String parameterName = "whatever";
        final License result = subject.license(new Element("license")
            .setAttribute("name", "name Value")
            .setAttribute("url", "urlValue")
            .setAttribute("id", "idValue")
            .setContent(new Element("template")
                .addContent(new Element("parameter-name").setContent(new CDATA(parameterName)))
                .addContent(new Element("parameter-name").setContent(new CDATA(parameterName + "2")))
                ));
        assertNotNull(result, "Builder should build");
        assertNotNull(result.getExpectedParameters(), "Builder should always set parameters even when empty");
        assertEquals(2, result.getExpectedParameters().size(), "One parameters in template");
    }


    @Test
    void licenseBaseText() throws Exception {
        final String text = "Some text";
        final License result = subject.license(new Element("license")
            .setAttribute("name", "name Value")
            .setAttribute("url", "urlValue")
            .setAttribute("id", "idValue")
            .setContent(new Element("text").setContent(new CDATA(text))));
        assertNotNull(result, "Builder should build");
        assertEquals(text, result.getText(), "Expected base text to be set");
    }

}
