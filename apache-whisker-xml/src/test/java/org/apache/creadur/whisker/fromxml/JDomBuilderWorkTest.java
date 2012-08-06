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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.creadur.whisker.model.License;
import org.apache.creadur.whisker.model.Organisation;
import org.apache.creadur.whisker.model.WithinDirectory;
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

    public void testNoPrimaryCopyright() throws Exception {
        final String primaryCopyrightNotice = subject.primaryCopyrightNotice(new Document().setRootElement(new Element("manifest").
                addContent(new Element("primary-license").setAttribute("id", "The primary ID"))));
        assertNull("Builder should only set primary copyright when present", primaryCopyrightNotice);
    }

    public void testPrimaryCopyright() throws Exception {
        final String copyrightNoticeSet = "Some Copyright Claim";
        final String result = subject.primaryCopyrightNotice(new Document().setRootElement(new Element("manifest").
                addContent(
                        new Element("primary-license").setAttribute("id", "The primary ID")
                        .addContent(
                                new Element("copyright-notice").addContent(copyrightNoticeSet)))));
        assertEquals("Builder should set primary copyright notice", result, copyrightNoticeSet);
    }

    public void testBuildPrimaryCopyright() throws Exception {
        final String copyrightNoticeSet = "Some Copyright Claim";
        final String result = subject.build(new Document().setRootElement(
                new Element("manifest")
                    .addContent(
                            new Element("licenses").addContent(
                                    new Element("license").setAttribute("id", "The primary ID")))
                    .addContent(
                        new Element("primary-license").setAttribute("id", "The primary ID")
                        .addContent(
                                new Element("copyright-notice").addContent(copyrightNoticeSet))))
                                ).getPrimaryCopyrightNotice();
        assertEquals("Builder should set primary copyright notice", result, copyrightNoticeSet);
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
    
    public void testPrimaryNoticeFindsNoticeText() throws Exception {
        final String noticeText = "Some sort of notice";
        final String result = subject.primaryNotice(new Document().setRootElement(
                new Element("manifest").addContent(
                        new Element("primary-notice").addContent(new CDATA(noticeText)))));
        assertEquals("Expected builder to find the text of the primary notice element", noticeText, result);
    }
    
    public void testPrimaryNoticeIsNullWhenThereIsNoNoticeText() throws Exception {
        final String result = subject.primaryNotice(new Document().setRootElement(
                new Element("manifest")));
        assertNull("When there is no primary notice, expect null", result);
    }

    public void testPrimaryNoticeSubstitutesYearInNoticeText() throws Exception {
        final String noticeBaseText = "Some sort of notice";
        final String result = subject.primaryNotice(new Document().setRootElement(
                new Element("manifest").addContent(
                        new Element("primary-notice").addContent(new CDATA(noticeBaseText + "${year}")))));
        assertEquals("Expected builder to find the text of the primary notice element", 
                noticeBaseText + Calendar.getInstance().get(Calendar.YEAR), result);
    }

    public void testFindPrimaryOrganisationIdReturnsNullWhenOrganisationUnset() throws Exception {
        final String result = subject.primaryOrganisationId(new Document().setRootElement(new Element("manifest")));
        assertNull("Primary organisation is optional, and null should be returned when unset", result);
    }
    
    public void testFindPrimaryOrganisationIdWhenSet() throws Exception {
        final String idValue = "An ID value";
        final String result = subject.primaryOrganisationId(
                new Document().setRootElement(
                        new Element("manifest").addContent(
                                new Element("primary-organisation").setAttribute("id", idValue))));
        assertEquals("When set, builder should find value", idValue, result);
    }
    
    public void testCollectContentsReturneEmptyWhenDocumentHasNoContents() throws Exception {
        final Collection<WithinDirectory> results = subject.collectContents(
                new Document().setRootElement(new Element("manifest")), new HashMap<String, License>(),
                new HashMap<String, Organisation>());
        assertNotNull("Builder should build something", results);
        assertTrue("Collection should be empty when there are no contents", results.isEmpty());
    }
    
    
    public void testCollectDirectoriesDefinedInDocument() throws Exception {
        for (int i=1;i<256;i++) {
            checkCollectDirectoriesWith(i);
        };
    }

    /**
     * @param numberOfDirectories
     */
    private void checkCollectDirectoriesWith(final int numberOfDirectories) {
        final String baseDir = "/dir/path";
        final Document in = new Document();
        final Element rootElement = new Element("manifest");
        in.setRootElement(rootElement);
        for (int i=0;i<numberOfDirectories;i++) {
            rootElement.addContent(
                    new Element("within").setAttribute("dir", combine(baseDir, i)));
        }
        final Collection<WithinDirectory> results = subject.collectContents(in, new HashMap<String, License>(),
                new HashMap<String, Organisation>());
        assertEquals("One organisation in map for each in the document", numberOfDirectories, results.size());
        final Collection<String> dirNames = new HashSet<String>();
        for (final WithinDirectory within:results) {
            dirNames.add(within.getName());
        }
        for (int i=0;i<numberOfDirectories;i++) {
            assertTrue("", dirNames.contains(combine(baseDir,i)));
        }
    }
    
    public void testCollectDirectoriesThrowsDuplicateElementExceptionWhenDirAttributeDuplicated() throws Exception {
        final String dir = "duplicate/path";
        try {
            subject.collectContents(
                    new Document().setRootElement(
                            new Element("manifest").addContent(
                                    new Element("within").setAttribute("dir", dir)).addContent(
                                            new Element("within").setAttribute("dir", dir))), 
                    new HashMap<String, License>(),
                    new HashMap<String, Organisation>());
            fail("Collect should throw a DuplicateElementException when dir names are not unique");
        } catch (DuplicateElementException e) {
            // expected
        }
    }
}
