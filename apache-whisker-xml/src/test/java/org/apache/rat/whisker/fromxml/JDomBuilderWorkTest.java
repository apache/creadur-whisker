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
package org.apache.rat.whisker.fromxml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.rat.whisker.model.License;
import org.apache.rat.whisker.model.Organisation;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;

/**
 * 
 */
public class JDomBuilderWorkTest extends TestCase {

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


    public void testMapNoticesIsEmptyWhenDocumentHasNoNotices() throws Exception {
        final Map<String, String> results = subject.mapNotices(new Document().setRootElement(new Element("manifest")));
        assertNotNull("Builder should build something", results);
        assertTrue("Should be empty when no licenses present", results.isEmpty());
    }
    
    public void testMapNoticesExpectedToBeImmutable() throws Exception {
        final Map<String, String> results = 
            subject.mapNotices(new Document().setRootElement(new Element("manifest")));
        assertNotNull("Expected builder to build something", results);
        try {
            results.put("whatever", "next");
            fail("Expected map to be immutable");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    
    public void testMapNoticesFindsNoticesDefinedInDocument() throws Exception {
        for (int i=0; i<256; i++) {
            checkMapNoticesWith(i);
        }
    }

    /**
     * @param numberOfLicenses
     */
    private void checkMapNoticesWith(final int numberOfLicenses) {
        final String baseId = "ID";
        final String baseText = "Rhubarb Rhubarb";
        final Document in = new Document();
        final Element licenses = new Element("notices");
        in.setRootElement(new Element("manifest").addContent(licenses));
        for (int i=0;i<numberOfLicenses;i++) {
            licenses.addContent(
                    new Element("notice").setAttribute("id", combine(baseId, i)).addContent(new CDATA(combine(baseText, i))));
        }
        final Map<String, String> results = subject.mapNotices(in);
        assertEquals("One license in map for each in the document", numberOfLicenses, results.size());
        for (int i=0;i<numberOfLicenses;i++) {
            final String next = results.get(combine(baseId, i));
            assertNotNull("Expected organisation to be stored", next);
            assertEquals("Expected correct organisation to be stored", next, combine(baseText, i));
        }
    }

    
    public void testMapLicensesIsEmptyWhenDocumentHasNoLicenses() throws Exception {
        final Map<String, License> results = subject.mapLicenses(new Document().setRootElement(new Element("manifest")));
        assertNotNull("Builder should build something", results);
        assertTrue("Should be empty when no licenses present", results.isEmpty());
    }
    
    public void testMapLicensesExpectedToBeImmutable() throws Exception {
        final Map<String, License> results = 
            subject.mapLicenses(new Document().setRootElement(new Element("manifest")));
        assertNotNull("Expected builder to build something", results);
        try {
            results.put("whatever", new License(true, "", new ArrayList<String>(), "", "", ""));
            fail("Expected map to be immutable");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
    
    public void testMapLicensesFindsLicensesDefinedInDocument() throws Exception {
        for (int i=0; i<256; i++) {
            checkMapLicensesWith(i);
        }
    }

    /**
     * @param numberOfLicenses
     */
    private void checkMapLicensesWith(final int numberOfLicenses) {
        final String baseId = "ID";
        final String baseName = "Name";
        final Document in = new Document();
        final Element licenses = new Element("licenses");
        in.setRootElement(new Element("manifest").addContent(licenses));
        for (int i=0;i<numberOfLicenses;i++) {
            licenses.addContent(
                    new Element("license").setAttribute("id", combine(baseId, i)).setAttribute("name", combine(baseName, i)));
        }
        final Map<String, License> results = subject.mapLicenses(in);
        assertEquals("One license in map for each in the document", numberOfLicenses, results.size());
        for (int i=0;i<numberOfLicenses;i++) {
            final License next = results.get(combine(baseId, i));
            assertNotNull("Expected organisation to be stored", next);
            assertEquals("Expected correct organisation to be stored", next.getName(), combine(baseName, i));
        }
    }

    
    public void testMapOrganisationsIsEmptyWhenDocumentHasNoOrganisations() throws Exception {
        final Map<String, Organisation> results = 
            subject.mapOrganisations(new Document().setRootElement(new Element("manifest")));
        assertNotNull("Expected builder to build something", results);
        assertTrue("Expected builder to build empty map when document has no documents", results.isEmpty());
    }
    
    public void testMapOrganisationsExpectedToBeImmutable() throws Exception {
        final Map<String, Organisation> results = 
            subject.mapOrganisations(new Document().setRootElement(new Element("manifest")));
        assertNotNull("Expected builder to build something", results);
        try {
            results.put("whatever", new Organisation("", "", ""));
            fail("Expected map to be immutable");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
    
    public void testMapOrganisationsFindOrganisationDefinedInDocument() throws Exception {
        for (int i=1;i<256;i++) {
            checkMapOrganisationsWith(i);
        };
    }

    /**
     * @param numberOfOrganisations
     */
    private void checkMapOrganisationsWith(final int numberOfOrganisations) {
        final String baseId = "ID";
        final String baseName = "Name";
        final Document in = new Document();
        final Element organisations = new Element("organisations");
        in.setRootElement(new Element("manifest").addContent(organisations));
        for (int i=0;i<numberOfOrganisations;i++) {
            organisations.addContent(
                    new Element("organisation").setAttribute("id", combine(baseId, i)).setAttribute("name", combine(baseName, i)));
        }
        final Map<String, Organisation> results = subject.mapOrganisations(in);
        assertEquals("One organisation in map for each in the document", numberOfOrganisations, results.size());
        for (int i=0;i<numberOfOrganisations;i++) {
            final Organisation next = results.get(combine(baseId, i));
            assertNotNull("Expected organisation to be stored", next);
            assertEquals("Expected correct organisation to be stored", next.getName(), combine(baseName, i));
        }
    }
    
    /**
     * @param baseId
     * @param i
     * @return
     */
    private String combine(String base, int i) {
        return base + i;
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
    
    public void testPrimaryLicense() throws Exception {
        final String id = "The primary ID";
        final Map<String, License> licenses = new HashMap<String, License> ();
        @SuppressWarnings("unchecked")
        final License expected = new License(false, "", Collections.EMPTY_LIST, id, "url", "name");
        expected.storeIn(licenses);
        addLicenseTo(licenses, "noise");
        final License result = subject.primaryLicense(new Document().setRootElement(new Element("manifest").
                addContent(new Element("primary-license").setAttribute("id", id))), licenses);
        assertNotNull("Builder should find primary license", result);
        assertEquals("Builder should find primary licenser", expected, result);
    }
    
    public void testThrowsMissingIDExceptionWhenPrimaryLicenseMissing() throws Exception {
        final String id = "The primary ID";
        final Map<String, License> licenses = new HashMap<String, License> ();
        addLicenseTo(licenses, "noise");
        try {
            subject.primaryLicense(new Document().setRootElement(new Element("manifest").
                addContent(new Element("primary-license").setAttribute("id", id))), licenses);
            fail();
        } catch (MissingIDException e) {
            // expected
        }
    }
}
