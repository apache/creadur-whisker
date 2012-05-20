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
package org.apache.creadur.whisker.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * 
 */
public class WithLicense implements Comparable<WithLicense> {

    private final License license;
    private final Collection<ByOrganisation> organisations;
    private final String copyrightNotice;
    private final Map<String, String> parameters;

    /**
     * @param license
     * @param element
     */
    public WithLicense(final License license, final String copyrightNotice,
            final Map<String, String> parameters,
            final Collection<ByOrganisation> organisations) {
        super();
        this.license = license;
        this.copyrightNotice = copyrightNotice;
        this.parameters = Collections.unmodifiableMap(parameters);
        this.organisations = Collections.unmodifiableCollection(organisations);
    }

    public String getCopyrightNotice() {
        return this.copyrightNotice;
    }

    public String getName() {
        return this.license.getName();
    }

    public String getURL() {
        return this.license.getURL();
    }

    public License getLicense() {
        return this.license;
    }

    public String getText() throws LicenseTemplateException {
        return this.license.getText(this.parameters);
    }

    public Collection<ByOrganisation> getOrganisations() {
        return this.organisations;
    }

    public Map<String, String> getParameters() {
        return this.parameters;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final WithLicense other) {
        return this.license.compareTo(other.getLicense());
    }

    /**
     * @param visitor
     */
    public void accept(final Visitor visitor) {
        if (visitor != null && visitor.traverseWithLicense()) {
            visitor.visit(this);
            for (final ByOrganisation organisation : getOrganisations()) {
                organisation.accept(visitor);
            }
        }
    }

    /**
     * @return
     */
    public boolean isSourceRequired() {
        return this.license.isSourceRequired();
    }

}
