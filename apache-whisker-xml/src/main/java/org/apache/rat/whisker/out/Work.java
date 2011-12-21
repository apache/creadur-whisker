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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.rat.whisker.fromxml.JDomBuilder;
import org.apache.rat.whisker.model.ByOrganisation;
import org.apache.rat.whisker.model.License;
import org.apache.rat.whisker.model.Organisation;
import org.apache.rat.whisker.model.WithinDirectory;
import org.jdom.Document;
import org.jdom.Element;

/**
 * 
 */
public class Work {

    
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
        this.licenses = new JDomBuilder().mapLicenses(document);
        this.notices = new JDomBuilder().mapNotices(document);
        this.primaryLicense = new JDomBuilder().primaryLicense(document, this.licenses);
        this.primaryNotice = new JDomBuilder().primaryNotice(document);
        this.organisations = new JDomBuilder().mapOrganisations(document);
        this.primaryOrganisationId = new JDomBuilder().primaryOrganisationId(document);
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
        return new JDomBuilder().withinDirectory(element, this.licenses, this.organisations);
    }

}
