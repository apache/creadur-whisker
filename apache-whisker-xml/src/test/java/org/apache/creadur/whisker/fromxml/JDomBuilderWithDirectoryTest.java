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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.creadur.whisker.model.ByOrganisation;
import org.apache.creadur.whisker.model.License;
import org.apache.creadur.whisker.model.Organisation;
import org.apache.creadur.whisker.model.WithinDirectory;
import org.jdom.Element;

import junit.framework.TestCase;

/**
 * 
 */
public class JDomBuilderWithDirectoryTest extends TestCase {

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
        
    public void testBuildWithinDirectorySetsLicenses() throws Exception {
       for(int i=0;i<101;i++) {
           checkWithinLicenses(i);
       }
    }

    /**
     * @param numberOfOrgs
     */
    private void checkWithinLicenses(final int numberOfOrgs) {
        final Map<String, License> licenses = new HashMap<String, License>();
        final Map<String, Organisation> organisations = new HashMap<String, Organisation> ();
        final Element element = withLicense(numberOfOrgs, licenses);
        final WithinDirectory result = subject.withinDirectory(
                element.setAttribute("dir", "a name"), licenses, organisations);
        assertNotNull("Builder should build", result);
        assertNotNull("Builder should build licenses", result.getLicenses());
        assertEquals("Builder should set licenses", numberOfOrgs, result.getLicenses().size());
    }

    /**
     * @param numberOfOrgs
     * @param organisations
     * @return
     */
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
    
    public void testBuildWithinDirectorySetsPublicDomain() throws Exception {
        for (int i=0;i<101;i++) {
            checkWithinPublicDomain(i);
        }
    }
    
    /**
     * @param numberOfOrgs
     */
    private void checkWithinPublicDomain(final int numberOfOrgs) {
        final Map<String, License> licenses = new HashMap<String, License>();
        final Map<String, Organisation> organisations = new HashMap<String, Organisation> ();
        final Element element = withPublicDomain(numberOfOrgs, organisations);
        final WithinDirectory result = subject.withinDirectory(
                element.setAttribute("dir", "a name"), licenses, organisations);
        assertNotNull("Builder should build", result);
        assertNotNull("Builder should build public domain", result.getPublicDomain());
        assertEquals("Builder should set public domain", numberOfOrgs, result.getPublicDomain().size());
    }
    
    public void testBuildWithinDirectorySetsDirectoryName() throws Exception {
        final Map<String, License> licenses = new HashMap<String, License>();
        final Map<String, Organisation> organisations = new HashMap<String, Organisation> ();
        final String expected = "a name";
        final WithinDirectory result = subject.withinDirectory(
                new Element("within").setAttribute("dir", expected), licenses, organisations);
        assertNotNull("Builder should build", result);
        assertEquals("Builder should set name from dir", expected, result.getName());
    }
    
    public void testCollectPublicDomainOrgs() throws Exception {
        for (int i=0;i<256;i++) {
            checkPublicDomainOrg(i);
        }
    }

    /**
     * @param numberOfOrgs
     */
    private void checkPublicDomainOrg(int numberOfOrgs) {
        final Map<String, Organisation> organisations = new HashMap<String, Organisation> ();
        final Element element = withPublicDomain(numberOfOrgs, organisations);
        final Collection<ByOrganisation> results = subject.publicDomain(
                organisations,
                element);
        assertNotNull("Expected builder to build", results);
        assertEquals("Expected one organisation per child of public domain",numberOfOrgs, results.size());
    }

    /**
     * @param numberOfOrgs
     * @param organisations
     * @return
     */
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
