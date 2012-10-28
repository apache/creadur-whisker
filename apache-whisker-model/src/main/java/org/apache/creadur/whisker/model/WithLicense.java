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
 * Groups resources sharing a license and claimed copyright.
 */
public class WithLicense implements Comparable<WithLicense>, ContentElement {

    /** License shared by contained resources, not null. */
    private final License license;
    /** Resources grouped by responsible organisation. */
    private final Collection<ByOrganisation> organisations;
    /** Copyright claim shared by contained resources. */
    private final String copyrightNotice;
    /** Parameters to specialise the license family template. */
    private final Map<String, String> parameters;

    /**
     * Groups resources sharing a license and copyright claim.
     * @param license License shared by contained resources,
     * not null
     * @param copyrightNotice copyright claim
     * shared by contained resources, possibly null
     * @param parameters parameters to specialise
     * the license family template, not null
     * @param organisations resources grouped by
     * responsible organisation, not null
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

    /**
     * Gets the copyright claim shared
     * by the resources contained.
     * @return possibly null
     */
    public String getCopyrightNotice() {
        return this.copyrightNotice;
    }


    /**
     * Indicates whether a copyright notice
     * accumpianies this license.
     * @return true when {@link #getCopyrightNotice()} is not null,
     * false when {@link #getCopyrightNotice()} is null
     */
    public boolean hasCopyrightNotice() {
        return getCopyrightNotice() != null;
    }

    /**
     * Gets the presentation name for the license
     * shared by the resources contained.
     * @return not null
     */
    public String getName() {
        return this.license.getName();
    }

    /**
     * Gets a locator for the license
     * shared by the resources contained.
     * @return not null
     */
    public String getURL() {
        return this.license.getURL();
    }

    /**
     * Gets license meta-data shared by the resources
     * contained.
     * @return not null
     */
    public License getLicense() {
        return this.license;
    }

    /**
     * Gets the license legalise shared by the resources
     * contained. Computed by applying the parameters
     * to the license template.
     * @return not null
     * @throws LicenseTemplateException when the license
     * text cannot be generated from the template
     */
    public String getText() throws LicenseTemplateException {
        return this.license.getText(this.parameters);
    }

    /**
     * Gets resources grouped by responsible organisation.
     * @return not null
     */
    public Collection<ByOrganisation> getOrganisations() {
        return this.organisations;
    }

    /**
     * Gets the parameters substituted into the license
     * template when generating the license legalise.
     * @return not null
     */
    public Map<String, String> getParameters() {
        return this.parameters;
    }

    /**
     * Based on license.
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     * @param other possibly null
     * @return license comparison
     */
    public int compareTo(final WithLicense other) {
        return this.license.compareTo(other.getLicense());
    }

    /**
     * Accepts a visit.
     * @param visitor possibly null
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
     * Should information about the source distribution of
     * contained resources be included?
     * @return true when license asks that information
     * about the source distribution is included,
     * false otherwise
     */
    public boolean isSourceRequired() {
        return this.license.isSourceRequired();
    }
}
