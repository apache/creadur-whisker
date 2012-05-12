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

import org.apache.creadur.whisker.model.License;
import org.jdom.CDATA;
import org.jdom.Element;

import junit.framework.TestCase;

/**
 * 
 */
public class JDomBuilderLicenseTest extends TestCase {

    private JDomBuilder subject;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        subject = new JDomBuilder();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testLicenseWithNameUrl() throws Exception {
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
        assertNotNull("Builder should build", result);
        assertEquals("Set id", idValue, result.getId());
        assertEquals("Set url", urlValue, result.getURL());
        assertEquals("Set name", nameValue, result.getName());
    }
    
    public void testLicenseRequiresSourceTrue() throws Exception {
        assertTrue("Expected required source to be set", licenseWithSource("yes").isSourceRequired());
    }
    
    public void testLicenseRequiresSourceTrueAllCaps() throws Exception {
        assertTrue("Expected required source to be set even when yes is in all caps case. This is against the schema.", 
                licenseWithSource("YES").isSourceRequired());
    }
    
    public void testLicenseRequiresSourceTrueMixedCase() throws Exception {
        assertTrue("Expected required source to be set even when yes is in mixed case. This is against the schema.", 
                licenseWithSource("Yes").isSourceRequired());
    }

    public void testLicenseRequiresSourceFalse() throws Exception {
        assertFalse("Expected required source to be set", licenseWithSource("no").isSourceRequired());
    }
    
    public void testLicenseRequiresSourceDefault() throws Exception {
        final License result = subject.license(new Element("license")
            .setAttribute("name", "name Value")
            .setAttribute("url", "urlValue")
            .setAttribute("id", "idValue"));
        assertNotNull("Builder should build", result);
        assertFalse("Expected to default to false", result.isSourceRequired());
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
        assertNotNull("Builder should build", result);
        return result;
    }
    
    public void testLicenseNoParameters() throws Exception {
        final License result = subject.license(new Element("license")
            .setAttribute("name", "name Value")
            .setAttribute("url", "urlValue")
            .setAttribute("id", "idValue")
            .setContent(new Element("template")));
        assertNotNull("Builder should build", result);
        assertNotNull("Builder should always set parameters even when empty", result.getExpectedParameters());
        assertTrue("No parameters in template so collection should be empty", result.getExpectedParameters().isEmpty());
    }

    public void testLicenseOneParameter() throws Exception {
        final String parameterName = "whatever";
        final License result = subject.license(new Element("license")
            .setAttribute("name", "name Value")
            .setAttribute("url", "urlValue")
            .setAttribute("id", "idValue")
            .setContent(new Element("template").setContent(new Element("parameter-name").setContent(new CDATA(parameterName)))));
        assertNotNull("Builder should build", result);
        assertNotNull("Builder should always set parameters even when empty", result.getExpectedParameters());
        assertEquals("One parameters in template", 1, result.getExpectedParameters().size());
        assertEquals("Parameter name should be set from xml", parameterName, result.getExpectedParameters().iterator().next());
    }

    public void testLicenseTwoParameters() throws Exception {
        final String parameterName = "whatever";
        final License result = subject.license(new Element("license")
            .setAttribute("name", "name Value")
            .setAttribute("url", "urlValue")
            .setAttribute("id", "idValue")
            .setContent(new Element("template")
                .addContent(new Element("parameter-name").setContent(new CDATA(parameterName)))
                .addContent(new Element("parameter-name").setContent(new CDATA(parameterName + "2")))
                ));
        assertNotNull("Builder should build", result);
        assertNotNull("Builder should always set parameters even when empty", result.getExpectedParameters());
        assertEquals("One parameters in template", 2, result.getExpectedParameters().size());
    }

    
    public void testLicenseBaseText() throws Exception {
        final String text = "Some text";
        final License result = subject.license(new Element("license")
            .setAttribute("name", "name Value")
            .setAttribute("url", "urlValue")
            .setAttribute("id", "idValue")
            .setContent(new Element("text").setContent(new CDATA(text))));
        assertNotNull("Builder should build", result);
        assertEquals("Expected base text to be set", text, result.getText());
    }

}
