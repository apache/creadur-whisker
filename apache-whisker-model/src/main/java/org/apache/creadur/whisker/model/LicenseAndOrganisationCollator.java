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
 * Collates 
 */
public class LicenseAndOrganisationCollator extends Visitor {

    private final Set<License> licenses = new TreeSet<License>();
    private final Set<Organisation> organisations = new TreeSet<Organisation>();

    /**
     * @return the licenses
     */
    public Set<License> getLicenses() {
        return this.licenses;
    }

    /**
     * @return the organisation
     */
    public Set<Organisation> getOrganisation() {
        return this.organisations;
    }

    /**
     * @see org.apache.rat.whisker.legacy.out.Visitor#traverseResource()
     */
    @Override
    public boolean traverseResource() {
        return false;
    }

    /**
     * @see org.apache.rat.whisker.legacy.out.Visitor#visit(org.apache.rat.whisker.legacy.out.WithLicense)
     */
    @Override
    public void visit(final WithLicense license) {
        this.licenses.add(license.getLicense());
    }

    public boolean isOnlyLicense(final License license) {
        return (this.licenses.size() == 1) && this.licenses.contains(license);
    }

    /**
     * @see org.apache.rat.whisker.legacy.out.Visitor#visit(org.apache.rat.whisker.legacy.out.ByOrganisation)
     */
    @Override
    public void visit(final ByOrganisation byOrganisation) {
        this.organisations.add(byOrganisation.getOrganisation());
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "LicenseAndOrganisationCollator [licenses=" + this.licenses
                + ", organisations=" + this.organisations + "]";
    }

    /**
     * @param primaryOrganisationId
     * @return
     */
    public boolean isOnlyOrganisation(final String primaryOrganisationId) {
        return this.organisations.size() == 1
                && this.organisations.iterator().next().getId()
                        .equals(primaryOrganisationId);
    }
}
