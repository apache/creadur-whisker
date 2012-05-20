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

import java.util.Set;
import java.util.TreeSet;

/**
 * Collates licenses and organisations.
 */
public class LicenseAndOrganisationCollator extends Visitor {
    /** The licenses currently collected. */
    private final Set<License> licenses = new TreeSet<License>();
    /** The organisations currently collected. */
    private final Set<Organisation> organisations = new TreeSet<Organisation>();

    /**
     * Gets the licenses collected.
     * @return not null
     */
    public Set<License> getLicenses() {
        return this.licenses;
    }

    /**
     * Gets the organisations collected.
     * @return not null
     */
    public Set<Organisation> getOrganisation() {
        return this.organisations;
    }

    /**
     * Don't traverse resources.
     * @see Visitor#traverseResource()
     * @return false
     */
    @Override
    public boolean traverseResource() {
        return false;
    }

    /**
     * Visits {@link WithLicense}.
     * @see Visitor#visit(WithLicense)
     * @param license not null
     */
    @Override
    public void visit(final WithLicense license) {
        this.licenses.add(license.getLicense());
    }

    /**
     * Was this the only license collected?
     * @param license not null
     * @return true when the collection contains just this license,
     * false when no licenses or any other licenses were collected
     */
    public boolean isOnlyLicense(final License license) {
        return (this.licenses.size() == 1) && this.licenses.contains(license);
    }

    /**
     * Visits {@link ByOrganisation}.
     * @see Visitor#visit(ByOrganisation)
     * @param byOrganisation not null
     */
    @Override
    public void visit(final ByOrganisation byOrganisation) {
        this.organisations.add(byOrganisation.getOrganisation());
    }

    /**
     * Something useful for logging.
     * @see java.lang.Object#toString()
     * @return something for logging
     */
    @Override
    public String toString() {
        return "LicenseAndOrganisationCollator [licenses=" + this.licenses
                + ", organisations=" + this.organisations + "]";
    }

    /**
     * Is there only one organisation collected with the given id?
     * @param primaryOrganisationId not null
     * @return true when only one organisation has been collected
     * and it has the given id
     */
    public boolean isOnlyOrganisation(final String primaryOrganisationId) {
        return this.organisations.size() == 1
                && this.organisations.iterator().next().getId()
                        .equals(primaryOrganisationId);
    }
}
