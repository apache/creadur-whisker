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
 * High level description of licensing qualities.
 */
public class Descriptor {

    /** Principle license for main work. */
    private final License primaryLicense;
    /** Optional additional primary copyright notice*/
    private final String primaryCopyrightNotice;
    /** Individual or group with main responsible for main work. */
    private final String primaryOrganisationId;
    /** A NOTICE for the main work, for inclusion alongside the LICENSE. */
    private final String primaryNotice;
    /** License meta-data, indexed by id. */
    private final Map<String, License> licenses;
    /** Organisation meta-data, indexed by id */
    private final Map<String, Organisation> organisations;
    /** Notice meta-data, indexed by id. */
    private final Map<String, String> notices;
    /** Directories expected to be contained within the release. */
    private final Collection<WithinDirectory> contents;


    /**
     * Constructs a description of the expected licensing qualities of a
     * distribution, with no additional primary copyright notice.
     *
     * @param primaryLicense
     *            not null
     * @param primaryOrganisationId
     *            not null
     * @param primaryNotice
     *            possibly null
     * @param licenses
     *            not null, possibly empty
     * @param notices
     *            not null, possibly empty
     * @param organisations
     *            not null, possibly empty
     * @param contents
     *            not null, possibly empty
     */
    public Descriptor(final License primaryLicense,
            final String primaryOrganisationId,
            final String primaryNotice,
            final Map<String, License> licenses,
            final Map<String, String> notices,
            final Map<String, Organisation> organisations,
            final Collection<WithinDirectory> contents) {
        this(   primaryLicense,
                null,
                primaryOrganisationId,
                primaryNotice,
                licenses,
                notices,
                organisations,
                contents);
    }

    /**
     * Constructs a description of the expected licensing qualities of a
     * distribution, with a primary additional copyright notice.
     *
     * @param primaryLicense
     *            not null
     * @param primaryCopyrightNotice
     *            optional primary copyright notice, possibly null
     *
     * @param primaryOrganisationId
     *            not null
     * @param primaryNotice
     *            possibly null
     * @param licenses
     *            not null, possibly empty
     * @param notices
     *            not null, possibly empty
     * @param organisations
     *            not null, possibly empty
     * @param contents
     *            not null, possibly empty
     */
    public Descriptor(final License primaryLicense,
            final String primaryCopyrightNotice,
            final String primaryOrganisationId,
            final String primaryNotice,
            final Map<String, License> licenses,
            final Map<String, String> notices,
            final Map<String, Organisation> organisations,
            final Collection<WithinDirectory> contents) {
        super();
        this.primaryLicense = primaryLicense;
        this.primaryCopyrightNotice = primaryCopyrightNotice;
        this.primaryOrganisationId = primaryOrganisationId;
        this.primaryNotice = primaryNotice;
        this.licenses = licenses;
        this.notices = notices;
        this.organisations = organisations;
        this.contents = contents;
    }

    /**
     * Gets an additional copyright notice needed
     * for some primary licenses.
     * @return optional primary copyright notice,
     * possibly null
     */
    public String getPrimaryCopyrightNotice() {
        return primaryCopyrightNotice;
    }

    /**
     * Is there a primary copyright notice?
     * @return true if a primary copyright notice
     * has been set, false otherwise
     */
    public boolean isPrimaryCopyrightNotice() {
        return primaryCopyrightNotice != null;
    }

    /**
     * Gets the principle NOTICE for the main work.
     *
     * @return the primaryNotice
     */
    public String getPrimaryNotice() {
        return this.primaryNotice;
    }

    /**
     * Collates NOTICE meta-data for resources.
     *
     * @return not null, possibly empty
     */
    public Map<String, Collection<Resource>> getResourceNotices() {
        final NoticeCollator collator = new NoticeCollator();
        traverse(collator);
        return collator.resourceNotices(this.notices);
    }

    /**
     * Gets the organisations described.
     * @return organisations indexed by id, not null
     */
    public Map<String, Organisation> getOrganisations() {
        return organisations;
    }

    /**
     * Collates NOTICE meta-data not linked to any resource.
     *
     * @return not null, possibly empty
     */
    public Set<String> getOtherNotices() {
        final NoticeCollator collator = new NoticeCollator();
        traverse(collator);
        return collator.notices(this.notices);
    }

    /**
     * Gets the license with the given id.
     *
     * @param id
     *            not null
     * @return the license with the given id, or null
     */
    public License license(final String id) {
        return this.licenses.get(id);
    }

    /**
     * Gets the principle license under which the work is licensed.
     *
     * @return the principle license, not null
     */
    public License getPrimaryLicense() {
        return this.primaryLicense;
    }

    /**
     * Gets the contents expected in the distribution.
     *
     * @return not null, possibly null
     */
    public Collection<WithinDirectory> getContents() {
        return this.contents;
    }

    /**
     * Is the given license the principle license for the main work?
     *
     * @param license
     *            not null
     * @return true when the given license is the primary license, not null
     */
    public boolean isPrimary(final License license) {
        return this.primaryLicense.equals(license);
    }

    /**
     * Is the given individual or group the principle organisation with
     * responsibility for the main work.
     *
     * @param byOrganisation
     *            not null
     * @return true when the given organisation is primary
     */
    public boolean isPrimary(final ByOrganisation byOrganisation) {
        return byOrganisation.getId().equals(this.primaryOrganisationId);
    }

    /**
     * Is this collection of resources expected to contain only material
     * licensed under the primary license by the
     * primary organisation with the primary copyright notice?
     *
     * @param contentElement
     *            not null
     * @return true when the contents are all licensed under the primary license
     *         by the primary organisation
     */
    public boolean isOnlyPrimary(final ContentElement contentElement) {
        final NoCopyrightNoticeVerifier verifier = new NoCopyrightNoticeVerifier();
        final LicenseAndOrganisationCollator collator = new LicenseAndOrganisationCollator();
        contentElement.accept(collator);
        contentElement.accept(verifier);
        return collator.isOnlyLicense(getPrimaryLicense())
                && collator.isOnlyOrganisation(this.primaryOrganisationId)
                && !verifier.isCopyrightNoticePresent();
    }

    /**
     * Traverses the content directories.
     *
     * @param visitor
     *            possibly null
     */
    public void traverse(final Visitor visitor) {
        for (final WithinDirectory directory : getContents()) {
            directory.accept(visitor);
        }
    }

    /**
     * Traverses the given directory.
     *
     * @param visitor
     *            possibly null
     * @param directoryName
     *            not null
     */
    public void traverseDirectory(final Visitor visitor,
            final String directoryName) {
        for (final WithinDirectory directory : getContents()) {
            if (directory.isNamed(directoryName)) {
                directory.accept(visitor);
            }
        }
    }

    /**
     * Is a NOTICE document required?
     *
     * @return true when a NOTICE is required, false otherwise
     */
    public boolean isNoticeRequired() {
        return primaryNoticeExists() || resourceNoticesExist();
    }

    /**
     * Does any resource have a required notice?
     *
     * @return true when at least one required third party notice exists, false
     *         otherwise
     */
    public boolean resourceNoticesExist() {
        return !getResourceNotices().isEmpty();
    }

    /**
     * Does the work described have a primary notice?
     *
     * @return true unless the primary notice is null or whitespace
     */
    public boolean primaryNoticeExists() {
        return (this.primaryNotice != null)
                && !"".equals(this.primaryNotice.trim());
    }

    /**
     * Is this the work of the primary organisation only?
     *
     * @return true when no third party resources are contained, false when
     *         third party resources exist. In particular, true when
     *         contents are empty.
     */
    public boolean isPrimaryOnly() {
        final boolean result;
        if (contents.size() > 0) {
            final LicenseAndOrganisationCollator collator = new LicenseAndOrganisationCollator();
            for (final WithinDirectory directory : contents) {
                directory.accept(collator);
            }
            result = collator.isOnlyOrganisation(primaryOrganisationId);
        } else {
            result = true;
        }
        return result;
    }
}
