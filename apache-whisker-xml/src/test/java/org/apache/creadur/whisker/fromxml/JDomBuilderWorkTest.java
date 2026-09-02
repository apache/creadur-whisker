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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.creadur.whisker.model.License;
import org.apache.creadur.whisker.model.Organisation;
import org.apache.creadur.whisker.model.WithinDirectory;
import org.jdom2.CDATA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 
 */
class JDomBuilderWorkTest {

    private JDomBuilder subject;

    @BeforeEach
    void setUp() throws Exception {
        subject = new JDomBuilder();
    }

    @AfterEach
    void tearDown() throws Exception {
    }


    @Test
    void mapNoticesIsEmptyWhenDocumentHasNoNotices() throws Exception {
        final Map<String, String> results = subject.mapNotices(new Document().setRootElement(new Element("manifest")));
        assertNotNull(results, "Builder should build something");
        assertTrue(results.isEmpty(), "Should be empty when no licenses present");
    }

    @Test
    void mapNoticesExpectedToBeImmutable() throws Exception {
        final Map<String, String> results = 
            subject.mapNotices(new Document().setRootElement(new Element("manifest")));
        assertNotNull(results, "Expected builder to build something");
        try {
            results.put("whatever", "next");
            fail("Expected map to be immutable");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }


    @Test
    void mapNoticesFindsNoticesDefinedInDocument() throws Exception {
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
        assertEquals(numberOfLicenses, results.size(), "One license in map for each in the document");
        for (int i=0;i<numberOfLicenses;i++) {
            final String next = results.get(combine(baseId, i));
            assertNotNull(next, "Expected organisation to be stored");
            assertEquals(next, combine(baseText, i), "Expected correct organisation to be stored");
        }
    }


    @Test
    void mapLicensesIsEmptyWhenDocumentHasNoLicenses() throws Exception {
        final Map<String, License> results = subject.mapLicenses(new Document().setRootElement(new Element("manifest")));
        assertNotNull(results, "Builder should build something");
        assertTrue(results.isEmpty(), "Should be empty when no licenses present");
    }

    @Test
    void mapLicensesExpectedToBeImmutable() throws Exception {
        final Map<String, License> results = 
            subject.mapLicenses(new Document().setRootElement(new Element("manifest")));
        assertNotNull(results, "Expected builder to build something");
        try {
            results.put("whatever", new License(true, "", new ArrayList<String>(), "", "", ""));
            fail("Expected map to be immutable");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    void mapLicensesFindsLicensesDefinedInDocument() throws Exception {
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
        assertEquals(numberOfLicenses, results.size(), "One license in map for each in the document");
        for (int i=0;i<numberOfLicenses;i++) {
            final License next = results.get(combine(baseId, i));
            assertNotNull(next, "Expected organisation to be stored");
            assertEquals(next.getName(), combine(baseName, i), "Expected correct organisation to be stored");
        }
    }


    @Test
    void mapOrganisationsIsEmptyWhenDocumentHasNoOrganisations() throws Exception {
        final Map<String, Organisation> results = 
            subject.mapOrganisations(new Document().setRootElement(new Element("manifest")));
        assertNotNull(results, "Expected builder to build something");
        assertTrue(results.isEmpty(), "Expected builder to build empty map when document has no documents");
    }

    @Test
    void mapOrganisationsExpectedToBeImmutable() throws Exception {
        final Map<String, Organisation> results = 
            subject.mapOrganisations(new Document().setRootElement(new Element("manifest")));
        assertNotNull(results, "Expected builder to build something");
        try {
            results.put("whatever", new Organisation("", "", ""));
            fail("Expected map to be immutable");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    void mapOrganisationsFindOrganisationDefinedInDocument() throws Exception {
        for (int i=1;i<256;i++) {
            checkMapOrganisationsWith(i);
        }
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
        assertEquals(numberOfOrganisations, results.size(), "One organisation in map for each in the document");
        for (int i=0;i<numberOfOrganisations;i++) {
            final Organisation next = results.get(combine(baseId, i));
            assertNotNull(next, "Expected organisation to be stored");
            assertEquals(next.getName(), combine(baseName, i), "Expected correct organisation to be stored");
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

    @Test
    void primaryLicense() throws Exception {
        final String id = "The primary ID";
        final Map<String, License> licenses = new HashMap<String, License> ();
        @SuppressWarnings("unchecked")
        final License expected = new License(false, "", Collections.EMPTY_LIST, id, "url", "name");
        expected.storeIn(licenses);
        addLicenseTo(licenses, "noise");
        final License result = subject.primaryLicense(new Document().setRootElement(new Element("manifest").
                addContent(new Element("primary-license").setAttribute("id", id))), licenses);
        assertNotNull(result, "Builder should find primary license");
        assertEquals(expected, result, "Builder should find primary licenser");
    }

    @Test
    void noPrimaryCopyright() throws Exception {
        final String primaryCopyrightNotice = subject.primaryCopyrightNotice(new Document().setRootElement(new Element("manifest").
                addContent(new Element("primary-license").setAttribute("id", "The primary ID"))));
        assertNull(primaryCopyrightNotice, "Builder should only set primary copyright when present");
    }

    @Test
    void primaryCopyright() throws Exception {
        final String copyrightNoticeSet = "Some Copyright Claim";
        final String result = subject.primaryCopyrightNotice(new Document().setRootElement(new Element("manifest").
                addContent(
                        new Element("primary-license").setAttribute("id", "The primary ID")
                        .addContent(
                                new Element("copyright-notice").addContent(copyrightNoticeSet)))));
        assertEquals(copyrightNoticeSet, result, "Builder should set primary copyright notice");
    }

    @Test
    void buildPrimaryCopyright() throws Exception {
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
        assertEquals(copyrightNoticeSet, result, "Builder should set primary copyright notice");
    }

    @Test
    void throwsMissingIDExceptionWhenPrimaryLicenseMissing() throws Exception {
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

    @Test
    void primaryNoticeFindsNoticeText() throws Exception {
        final String noticeText = "Some sort of notice";
        final String result = subject.primaryNotice(new Document().setRootElement(
                new Element("manifest").addContent(
                        new Element("primary-notice").addContent(new CDATA(noticeText)))));
        assertEquals(noticeText, result, "Expected builder to find the text of the primary notice element");
    }

    @Test
    void primaryNoticeIsNullWhenThereIsNoNoticeText() throws Exception {
        final String result = subject.primaryNotice(new Document().setRootElement(
                new Element("manifest")));
        assertNull(result, "When there is no primary notice, expect null");
    }

    @Test
    void primaryNoticeSubstitutesYearInNoticeText() throws Exception {
        final String noticeBaseText = "Some sort of notice";
        final String result = subject.primaryNotice(new Document().setRootElement(
                new Element("manifest").addContent(
                        new Element("primary-notice").addContent(new CDATA(noticeBaseText + "${year}")))));
        assertEquals(noticeBaseText + Calendar.getInstance().get(Calendar.YEAR), result, "Expected builder to find the text of the primary notice element");
    }

    @Test
    void findPrimaryOrganisationIdReturnsNullWhenOrganisationUnset() throws Exception {
        final String result = subject.primaryOrganisationId(new Document().setRootElement(new Element("manifest")));
        assertNull(result, "Primary organisation is optional, and null should be returned when unset");
    }

    @Test
    void findPrimaryOrganisationIdWhenSet() throws Exception {
        final String idValue = "An ID value";
        final String result = subject.primaryOrganisationId(
                new Document().setRootElement(
                        new Element("manifest").addContent(
                                new Element("primary-organisation").setAttribute("id", idValue))));
        assertEquals(idValue, result, "When set, builder should find value");
    }

    @Test
    void collectContentsReturneEmptyWhenDocumentHasNoContents() throws Exception {
        final Collection<WithinDirectory> results = subject.collectContents(
                new Document().setRootElement(new Element("manifest")), new HashMap<String, License>(),
                new HashMap<String, Organisation>());
        assertNotNull(results, "Builder should build something");
        assertTrue(results.isEmpty(), "Collection should be empty when there are no contents");
    }


    @Test
    void collectDirectoriesDefinedInDocument() throws Exception {
        for (int i=1;i<256;i++) {
            checkCollectDirectoriesWith(i);
        }
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
        assertEquals(numberOfDirectories, results.size(), "One organisation in map for each in the document");
        final Collection<String> dirNames = new HashSet<String>();
        for (final WithinDirectory within:results) {
            dirNames.add(within.getName());
        }
        for (int i=0;i<numberOfDirectories;i++) {
            assertTrue(dirNames.contains(combine(baseDir,i)), "");
        }
    }

    @Test
    void collectDirectoriesThrowsDuplicateElementExceptionWhenDirAttributeDuplicated() throws Exception {
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
