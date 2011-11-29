/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
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
package org.apache.rat.whisker.out;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.jdom.Document;
import org.jdom.Element;

/**
 * 
 */
public class Work {


    /**
     * @param document
     * @return
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Organisation> mapOrganisations(Document document) {
        final Map<String, Organisation> organisationsById = new HashMap<String, Organisation>();
        
        for (final Element element: 
            (List<Element>) document.getRootElement().getChild("organisations").getChildren("organisation")) {
            new Organisation(element).storeIn(organisationsById);
        }
        return Collections.unmodifiableMap(organisationsById);
    }


    /**
     * @param document
     * @return
     */
    @SuppressWarnings("unchecked")
    private static Map<String, License> mapLicenses(Document document) {
        Map<String, License> results = new HashMap<String, License>();
        for (final Element element: (List<Element>) document.getRootElement().getChild("licenses").getChildren()) {
            new License(element).storeIn(results);
        }
        return Collections.unmodifiableMap(results);
    }
    
    
    /**
     * @param document
     * @return
     */
    @SuppressWarnings("unchecked")
    private static Map<String, String> mapNotices(Document document) {
        Map<String, String> results = new HashMap<String, String>();
        for (final Element element: (List<Element>) document.getRootElement().getChild("notices").getChildren()) {
            results.put(element.getAttributeValue("id"), element.getTextTrim());
        }
        return results;
    }

    
    /**
     * @param document
     * @return
     */
    private static License primaryLicense(final Document document, final Map<String, License> licenses) {
        return licenses.get(document.getRootElement().getChild("primary-license").getAttributeValue("id"));
    }
    
    private static String primaryNotice(final Document document) {
        final String result;
        final Element primaryNoticeElement = document.getRootElement().getChild("primary-notice");
        if (primaryNoticeElement == null) {
            result = null;
        } else {
            result = primaryNoticeElement.getText()
                .replace("${year}", Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
        }
        return result;
    }


    /**
     * @param document
     * @return
     */
    private static String primaryOrganisationId(Document document) {
        final String result;
        final Element primaryOrganisationElement = document.getRootElement().getChild("primary-organisation");
        if (primaryOrganisationElement == null) {
            result = null;
        } else {
            result = primaryOrganisationElement.getAttributeValue("id");
        }
        return result;
    }
    
    private final Document document;
    private final License primaryLicense;
    private final String primaryOrganisationId;
    private final String primaryNotice;
    private final Map<String, License> licenses;
    private final Map<String, String> notices;
    private final Map<String, Organisation> organisations;
    
    /**
     * @param document
     */
    public Work(Document document) {
        super();
        this.document = document;
        this.licenses = mapLicenses(document);
        this.notices = mapNotices(document);
        this.primaryLicense = primaryLicense(document, this.licenses);
        this.primaryNotice = primaryNotice(document);
        this.organisations = mapOrganisations(document);
        this.primaryOrganisationId = primaryOrganisationId(document);
    }


    /**
     * @return the primaryNotice
     */
    public String getPrimaryNotice() {
        return primaryNotice;
    }

    public License license(final String id) {
        return licenses.get(id);
    }
    
    public License getPrimaryLicense() {
        return primaryLicense;
    }
    
    @SuppressWarnings("unchecked")
    public Collection<WithinDirectory> getContents() {
        final Collection<WithinDirectory> results = new TreeSet<WithinDirectory>();
        for (Element element: (List<Element>)document.getRootElement().getChildren("within")) {
            if (results.add(directory(element))) {
                // OK System.out.println("Ok");
            } else {
                throw new IllegalArgumentException("Duplicate directory " + element.getAttribute("dir"));
            }
        }
        return results;
    }
    
    public boolean isPrimary( final License license) {
        return this.primaryLicense.equals(license);
    }
    
    public boolean isPrimary( final ByOrganisation byOrganisation ) {
        return byOrganisation.getId().equals(primaryOrganisationId);
    }
    
    private WithinDirectory directory(final Element element) {
        return new WithinDirectory(element, this.licenses, this.organisations);
    }

}
