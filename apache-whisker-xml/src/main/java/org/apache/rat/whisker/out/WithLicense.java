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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.rat.whisker.fromxml.JDomBuilder;
import org.apache.rat.whisker.model.ByOrganisation;
import org.apache.rat.whisker.model.Organisation;
import org.jdom.Element;

/**
 * 
 */
public class WithLicense implements Comparable<WithLicense> {
    
    /**
     * @param element
     * @param licenses
     * @return
     */
    private static License license(Element element,
            Map<String, License> licenses) {
        return licenses.get(element.getAttributeValue("id"));
    }
    
    private final License license;
    private final Element element;
    private final Collection<ByOrganisation> organisations;
    
    public WithLicense(final Element element, final Map<String, License> licenses,
            Map<String, Organisation> organisations) {
        this(license(element, licenses), element, new JDomBuilder().collectByOrganisations(element, organisations));
    }
        /**
     * @param license
     * @param element
     */
    public WithLicense(License license, Element element, 
            final Collection<ByOrganisation> organisations) {
        super();
        this.license = license;
        this.element = element;
        this.organisations = organisations;
    }

    public String getCopyrightNotice() {
        final String result;
        final Element copyrightNoticeElement = element.getChild("copyright-notice");
        if (copyrightNoticeElement == null) {
            result = null;
        } else {
            result = copyrightNoticeElement.getTextTrim();
        }
        return result;
    }
    
    public String getName() {
        return license.getName();
    }
    
    public String getURL() {
        return license.getURL();
    }
    
    public License getLicense() {
        return license;
    }
    
    public String getText() throws LicenseTemplateException {
        return license.getText(parameters());
    }
    
    /**
     * @return
     * @throws LicenseTemplateException 
     */
    @SuppressWarnings("unchecked")
    private Map<String, String> parameters() throws LicenseTemplateException {
        final Map<String, String> results = new HashMap<String, String>();
        final Element licenseParametersElement = element.getChild("license-parameters");
        if (licenseParametersElement != null) {
            for (Element parameterElement: (List<Element>) licenseParametersElement.getChildren("parameter")) {
                final String name = parameterElement.getChild("name").getTextTrim();
                if (results.containsKey(name)) {
                    throw LicenseTemplateException.duplicateParameterName(name);
                }
                results.put(name, 
                        parameterElement.getChild("value").getTextTrim());
            }   
        }
        return results;
    }

    public Collection<ByOrganisation> getOrganisations() {
        return organisations;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "WithLicense [element=" + element + "]";
    }

    /**
     * TODO: incompatible with equals?
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(WithLicense other) {
        return getName().compareTo(other.getName());
    }
    
    /**
     * @return
     */
    public boolean isSourceRequired() {
        return license.isSourceRequired();
    }
    
}
