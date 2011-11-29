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
package org.apache.rat.whisker.out;

import java.util.Set;
import java.util.TreeSet;

/**
 * 
 */
public class LicenseAndOrganisationCollator extends Visitor {

    private final Set<License> licenses = new TreeSet<License>();
    private final Set<Organisation> organisations = new TreeSet<Organisation>();
    
    /**
     * @return the licenses
     */
    public Set<License> getLicenses() {
        return licenses;
    }
    /**
     * @return the organisation
     */
    public Set<Organisation> getOrganisation() {
        return organisations;
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
    public void visit(WithLicense license) {
        licenses.add(license.getLicense());
    }
    
    public boolean isOnlyLicense(final License license) {
        return (licenses.size() == 1) && licenses.contains(license);
    }
    
    /**
     * @see org.apache.rat.whisker.legacy.out.Visitor#visit(org.apache.rat.whisker.legacy.out.ByOrganisation)
     */
    @Override
    public void visit(ByOrganisation byOrganisation) {
        organisations.add(byOrganisation.getOrganisation());
    }
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "LicenseAndOrganisationCollator [licenses=" + licenses
                + ", organisations=" + organisations + "]";
    }
    /**
     * @param primaryOrganisationId
     * @return
     */
    public boolean isOnlyOrganisation(String primaryOrganisationId) {
        return organisations.size() == 1 && organisations.iterator().next().getId().equals(primaryOrganisationId);
    }
}
