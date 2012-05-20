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
import java.util.Map;
import java.util.Set;

/**
 * 
 */
public class Descriptor {

    private final License primaryLicense;
    private final String primaryOrganisationId;
    private final String primaryNotice;
    private final Map<String, License> licenses;
    private final Map<String, String> notices;
    private final Collection<WithinDirectory> contents;

    public Descriptor(final License primaryLicense,
            final String primaryOrganisationId, final String primaryNotice,
            final Map<String, License> licenses,
            final Map<String, String> notices,
            final Map<String, Organisation> organisations,
            final Collection<WithinDirectory> contents) {
        super();
        this.primaryLicense = primaryLicense;
        this.primaryOrganisationId = primaryOrganisationId;
        this.primaryNotice = primaryNotice;
        this.licenses = licenses;
        this.notices = notices;
        this.contents = contents;
    }

    /**
     * @return the primaryNotice
     */
    public String getPrimaryNotice() {
        return this.primaryNotice;
    }

    public Map<String, Collection<Resource>> getResourceNotices() {
        final NoticeCollator collator = new NoticeCollator();
        traverse(collator);
        return collator.resourceNotices(this.notices);
    }

    public Set<String> getOtherNotices() {
        final NoticeCollator collator = new NoticeCollator();
        traverse(collator);
        return collator.notices(this.notices);
    }

    public License license(final String id) {
        return this.licenses.get(id);
    }

    public License getPrimaryLicense() {
        return this.primaryLicense;
    }

    public Collection<WithinDirectory> getContents() {
        return this.contents;
    }

    public boolean isPrimary(final License license) {
        return this.primaryLicense.equals(license);
    }

    public boolean isPrimary(final ByOrganisation byOrganisation) {
        return byOrganisation.getId().equals(this.primaryOrganisationId);
    }

    public boolean isOnlyPrimary(final WithinDirectory directory) {
        final LicenseAndOrganisationCollator collator = new LicenseAndOrganisationCollator();
        directory.accept(collator);
        return collator.isOnlyLicense(getPrimaryLicense())
                && collator.isOnlyOrganisation(this.primaryOrganisationId);
    }

    public boolean isOnlyPrimary(final WithLicense license) {
        final LicenseAndOrganisationCollator collator = new LicenseAndOrganisationCollator();
        license.accept(collator);
        return collator.isOnlyLicense(getPrimaryLicense())
                && collator.isOnlyOrganisation(this.primaryOrganisationId);
    }

    public void traverse(final Visitor visitor) {
        for (final WithinDirectory directory : getContents()) {
            directory.accept(visitor);
        }
    }

    public void traverseDirectory(final Visitor visitor,
            final String directoryName) {
        for (final WithinDirectory directory : getContents()) {
            if (directory.isNamed(directoryName)) {
                directory.accept(visitor);
            }
        }
    }

}
