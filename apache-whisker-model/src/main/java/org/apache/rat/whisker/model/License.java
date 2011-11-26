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
package org.apache.rat.whisker.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Element;

/**
 * 
 */
public class License implements Comparable<License> {

    private final Element element;

    /**
     * @param element
     */
    public License(Element element) {
        super();
        this.element = element;
    }
    
    public boolean isSourceRequired() {
        return "yes".equalsIgnoreCase(this.element.getAttributeValue("requires-source"));
    }
    
    public String getText() throws LicenseTemplateException {
        return getText(null);
    }
    
    public String getText(final Map<String, String> parameters) throws LicenseTemplateException {
        return substituteInto(validate(parameters), element.getChild("text").getText());
    }

    /**
     * @param parameters
     * @return
     * @throws LicenseTemplateException 
     */
    @SuppressWarnings("unchecked")
    private Map<String, String> validate(final Map<String, String> parameters) throws LicenseTemplateException {
        if (parameters == null) {
            return validate(Collections.EMPTY_MAP);
        }
        
        final Collection<String> expectedParameters = expectedParameters();
        if (expectedParameters.isEmpty() && parameters != null && !parameters.isEmpty()) {
            throw LicenseTemplateException.notLicenseTemplate(parameters, getName());
        }
        
        if (!parametersMatch(parameters, expectedParameters)) {
            throw LicenseTemplateException.parameterMismatch(expectedParameters, parameters.keySet());
        }
        
        return parameters;
    }

    /**
     * @param parameters
     * @param expectedParameters
     * @return
     */
    private boolean parametersMatch(final Map<String, String> parameters,
            final Collection<String> expectedParameters) {
        final Set<String> keySet = parameters.keySet();
        return (keySet.containsAll(expectedParameters) && expectedParameters.containsAll(keySet));
    }

    @SuppressWarnings("unchecked")
    private Collection<String> expectedParameters() {
        final Collection<String> results = new HashSet<String>();
        final Element templateElement = element.getChild("template");
        if (templateElement != null) {
            for (Element parameterNameElement: (List<Element>) templateElement.getChildren("parameter-name")) {
                results.add(parameterNameElement.getTextTrim());
            }
        }
        return results;
    }
    
    private String variable(final String parameterName) {
        return "${" + parameterName + "}";
    }
    
    /**
     * @param parameters
     * @param trim
     * @return
     */
    private String substituteInto(final Map<String, String> parameters, final String text) {
        String result = text;
        for (Map.Entry<String, String> entry: parameters.entrySet()) {
            result = result.replace(variable(entry.getKey()), entry.getValue());
        }
        return result;
    }

    /**
     * @param results
     */
    public void storeIn(final Map<String, License> map) {
        map.put(getId(), this);
    }

    /** 
     * @return
     */
    public String getId() {
        return element.getAttributeValue("id");
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "License [element=" + element + "]";
    }

    /**
     * @return
     */
    public String getURL() {
        return element.getAttributeValue("url");
    }

    /**
     * @return
     */
    public String getName() {
        return element.getAttributeValue("name");
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final License other = (License) obj;
        return getId().equals(other);
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(License other) {
        return getName().compareTo(other.getName());
    }
    
    
}
