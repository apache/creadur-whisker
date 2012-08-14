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
 * Describes a software license.
 */
public class License implements Comparable<License> {

    /**
     * Is information about source distribution required 
     * by this license? 
     */
    private final boolean isSourceRequired;
    /** Template for license wording. */
    private final String baseText;
    /** Parameters expected by the template. */
    private final Collection<String> expectedParameters;
    /** Identifies this license. */
    private final String id;
    /** Canonical locator for this license. */
    private final String url;
    /** Names this license */
    private final String name;
    
    /**
     * Constructs meta-data for a family of licenses.
     * @param isSourceRequired true if this license requires
     * information about source distribution to be included
     * within the distribution
     * @param baseText a template for the legal text, not null
     * @param expectedParameters not null
     * @param id not null
     * @param url not null
     * @param name not null
     */
    public License(final boolean isSourceRequired, final String baseText,
            final Collection<String> expectedParameters, final String id,
            final String url, final String name) {
        super();
        this.isSourceRequired = isSourceRequired;
        this.baseText = baseText;
        this.expectedParameters = Collections
                .unmodifiableCollection(expectedParameters);
        this.id = id;
        this.url = url;
        this.name = name;
    }

    /**
     * Is source information inclusion required by this
     * license?
     * @return true when information about the source
     * should be included, false otherwise
     */
    public boolean isSourceRequired() {
        return this.isSourceRequired;
    }

    /**
     * Gets legal text expressing this license.
     * @return not null
     * @throws LicenseTemplateException when the text cannot
     * be created from the template
     */
    public String getText() throws LicenseTemplateException {
        return getText(null);
    }

    /**
     * Gets legal text expressing this license,
     * 
     * @param parameters possibly null
     * @return not null
     * @throws LicenseTemplateException when the text cannot
     * be created from the template
     */
    public String getText(final Map<String, String> parameters)
            throws LicenseTemplateException {
        return substituteInto(validate(parameters), this.baseText);
    }
    
    /**
     * Gets parameters required by the template
     * to generate a instance of this license family.
     * @return not null, possibly empty
     */
    public Collection<String> getExpectedParameters() {
        return this.expectedParameters;
    }

    /**
     * Validates that these given parameters
     * are suitable for the template expressing the legalise.
     * @param parameters possibly null
     * @return parameters, not null
     * @throws LicenseTemplateException when the parameter
     * do not fulfill the expectations of the template
     */
    @SuppressWarnings("unchecked")
    private Map<String, String> validate(final Map<String, String> parameters)
            throws LicenseTemplateException {
        if (parameters == null) {
            return validate(Collections.EMPTY_MAP);
        }

        if (this.expectedParameters.isEmpty() && parameters != null
                && !parameters.isEmpty()) {
            throw LicenseTemplateException.notLicenseTemplate(parameters,
                    getName());
        }

        if (!parametersMatch(parameters, this.expectedParameters)) {
            throw LicenseTemplateException.parameterMismatch(
                    this.expectedParameters, parameters.keySet(), getName());
        }

        return parameters;
    }

    /**
     * Do the presented parameters fulfill expectations? 
     * @param parameters possibly null
     * @param expectedParameters possibly null
     * @return true when expected and presented parameters
     * match, false otherwise
     */
    private boolean parametersMatch(final Map<String, String> parameters,
            final Collection<String> expectedParameters) {
        final Set<String> keySet = parameters.keySet();
        return (keySet.containsAll(expectedParameters) && expectedParameters
                .containsAll(keySet));
    }

    /**
     * Translates a parameter name to 
     * the variable style used by the template
     * @param parameterName not null
     * @return variable in template format, not null
     */
    private String variable(final String parameterName) {
        return "${" + parameterName + "}";
    }

    /**
     * Substitutes parameter values into the variable
     * in the template legalise, parameterising 
     * an instance of the license family.
     * @param parameters not null
     * @param text template text
     * @return not null
     */
    private String substituteInto(final Map<String, String> parameters,
            final String text) {
        String result = text;
        for (final Map.Entry<String, String> entry : parameters.entrySet()) {
            result = result.replace(variable(entry.getKey()), entry.getValue());
        }
        return result;
    }

    /**
     * Stores the license by its id.
     * @param map not null
     * @return the license stored
     */
    public License storeIn(final Map<String, License> map) {
        map.put(getId(), this);
        return this;
    }

    /**
     * Gets the unique identifier for this license.
     * @return not null
     */
    public String getId() {
        return this.id;
    }

    /**
     * Gets a locator for this license.
     * @return not null
     */
    public String getURL() {
        return this.url;
    }

    /**
     * Gets a name for this license suitable for 
     * display.
     * @return not null
     */
    public String getName() {
        return this.name;
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
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final License other = (License) obj;
        return getId().equals(other);
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final License other) {
        final int nameDifference = getName().compareTo(other.getName());
        return nameDifference == 0 ? getId().compareTo(other.getId())
                : nameDifference;
    }

    @Override
    public String toString() {
        return "License [id=" + this.id + ", name=" + this.name + "]";
    }

}
