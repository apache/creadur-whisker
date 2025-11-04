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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.creadur.whisker.model.ByOrganisation;
import org.apache.creadur.whisker.model.License;
import org.apache.creadur.whisker.model.Organisation;
import org.apache.creadur.whisker.model.WithinDirectory;

import org.jdom2.Element;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 
 */
class JDomBuilderWithDirectoryTest {

    private JDomBuilder subject;

    @BeforeEach
    void setUp() throws Exception {
        subject = new JDomBuilder();
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    void buildWithinDirectorySetsLicenses() throws Exception {
       for(int i=0;i<101;i++) {
           checkWithinLicenses(i);
       }
    }

    private void checkWithinLicenses(final int numberOfOrgs) {
        final Map<String, License> licenses = new HashMap<String, License>();
        final Map<String, Organisation> organisations = new HashMap<String, Organisation> ();
        final Element element = withLicense(numberOfOrgs, licenses);
        final WithinDirectory result = subject.withinDirectory(
                element.setAttribute("dir", "a name"), licenses, organisations);
        assertNotNull(result, "Builder should build");
        assertNotNull(result.getLicenses(), "Builder should build licenses");
        assertEquals(numberOfOrgs, result.getLicenses().size(), "Builder should set licenses");
    }

    @SuppressWarnings("unchecked")
    private Element withLicense(int numberOfLicenses,
            final Map<String, License> licenses) {
        final Element element = new Element("within");
        for (int i=0;i<numberOfLicenses;i++) {
            final String idValue = "id" + i;
            element.addContent(new Element("with-license").setAttribute("id", idValue));
            new License(false,"", Collections.EMPTY_LIST, idValue, "url" + i, "name" + i).storeIn(licenses);
        }
        return element;
    }

    @Test
    void buildWithinDirectorySetsPublicDomain() throws Exception {
        for (int i=0;i<101;i++) {
            checkWithinPublicDomain(i);
        }
    }
    
    private void checkWithinPublicDomain(final int numberOfOrgs) {
        final Map<String, License> licenses = new HashMap<String, License>();
        final Map<String, Organisation> organisations = new HashMap<String, Organisation> ();
        final Element element = withPublicDomain(numberOfOrgs, organisations);
        final WithinDirectory result = subject.withinDirectory(
                element.setAttribute("dir", "a name"), licenses, organisations);
        assertNotNull(result, "Builder should build");
        assertNotNull(result.getPublicDomain(), "Builder should build public domain");
        assertEquals(numberOfOrgs, result.getPublicDomain().size(), "Builder should set public domain");
    }

    @Test
    void buildWithinDirectorySetsDirectoryName() throws Exception {
        final Map<String, License> licenses = new HashMap<String, License>();
        final Map<String, Organisation> organisations = new HashMap<String, Organisation> ();
        final String expected = "a name";
        final WithinDirectory result = subject.withinDirectory(
                new Element("within").setAttribute("dir", expected), licenses, organisations);
        assertNotNull(result, "Builder should build");
        assertEquals(expected, result.getName(), "Builder should set name from dir");
    }

    @Test
    void collectPublicDomainOrgs() throws Exception {
        for (int i=0;i<256;i++) {
            checkPublicDomainOrg(i);
        }
    }

    private void checkPublicDomainOrg(int numberOfOrgs) {
        final Map<String, Organisation> organisations = new HashMap<String, Organisation> ();
        final Element element = withPublicDomain(numberOfOrgs, organisations);
        final Collection<ByOrganisation> results = subject.publicDomain(
                organisations,
                element);
        assertNotNull(results, "Expected builder to build");
        assertEquals(numberOfOrgs, results.size(), "Expected one organisation per child of public domain");
    }

    private Element withPublicDomain(int numberOfOrgs,
            final Map<String, Organisation> organisations) {
        final Element publicDomain = new Element("public-domain");
        for (int i=0;i<numberOfOrgs;i++) {
            final String idValue = "id" + i;
            publicDomain.addContent(new Element("by-organisation").setAttribute("id", idValue));
            new Organisation(idValue, "name" + i, "url" + i).storeIn(organisations);
        }
        final Element element = new Element("within").addContent(publicDomain);
        return element;
    }
}
