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
package org.apache.creadur.whisker.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * 
 */
public class License implements Comparable<License> {

    private final boolean isSourceRequired;
    private final String baseText;
    private final Collection<String> expectedParameters;
    private final String id;
    private final String url;
    private final String name;
    
    public License(boolean isSourceRequired, String baseText,
            Collection<String> expectedParameters, String id, String url,
            String name) {
        super();
        this.isSourceRequired = isSourceRequired;
        this.baseText = baseText;
        this.expectedParameters = Collections.unmodifiableCollection(expectedParameters);
        this.id = id;
        this.url = url;
        this.name = name;
    }

    public boolean isSourceRequired() {
        return isSourceRequired;
    }
    
    public String getText() throws LicenseTemplateException {
        return getText(null);
    }
    
    public String getText(final Map<String, String> parameters) throws LicenseTemplateException {
        return substituteInto(validate(parameters), baseText);
    }
    
    public Collection<String> getExpectedParameters() {
        return expectedParameters;
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
    public License storeIn(final Map<String, License> map) {
        map.put(getId(), this);
        return this;
    }

    /** 
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * @return
     */
    public String getURL() {
        return url;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
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
        final int nameDifference = getName().compareTo(other.getName());
        return nameDifference == 0 ? getId().compareTo(other.getId()) : nameDifference;
    }

    @Override
    public String toString() {
        return "License [id=" + id + ", name=" + name + "]";
    }
    
}
