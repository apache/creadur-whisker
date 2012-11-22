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
package org.apache.creadur.whisker.out.velocity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.creadur.whisker.model.ByOrganisation;
import org.apache.creadur.whisker.model.Descriptor;
import org.apache.creadur.whisker.model.License;
import org.apache.creadur.whisker.model.Organisation;
import org.apache.creadur.whisker.model.Resource;
import org.apache.creadur.whisker.model.WithLicense;
import org.apache.creadur.whisker.model.WithinDirectory;

public class DescriptorBuilderForTesting {

    private static final String A_PRIMARY_COPYRIGHT_NOTICE = "Copyright (c) this is primary";
    String primaryLicenseText = "This is the primary license text";
    Organisation thirdPartyOrg = new Organisation("third-party", "thirdparty.org", "http://thirdparty.org");
    License primaryLicense = new License(false, primaryLicenseText, Collections.<String> emptyList(), "example.org", "http://example.org", "Example License");
    License secondaryLicense = new License(false, "This is the secondary license text", Collections.<String> emptyList(), "example.org:secondary", "http://example.org/secondary", "Example Secondary License");

    String primaryOrgName = "example.org";
    String primaryNotice = "The primary notice.";
    Collection<WithinDirectory> contents = new ArrayList<WithinDirectory>();
    Map<String, License> licenses = new HashMap<String, License>();
    Map<String, String> notices = new HashMap<String, String>();
    Map<String, Organisation> organisations = new HashMap<String, Organisation>();
    String secondaryCopyright = null;
    String resourceName;
    String primaryCopyrightNotice = null;
    public String sourceUrl = "";

    public DescriptorBuilderForTesting() {
        resourceName = "resource";
        primaryLicense.storeIn(licenses);
    }

    public DescriptorBuilderForTesting withPrimaryLicenseAndOrgInDirectory(String directoryName) {
        addDirectory(getPrimaryLicense(), primaryOrganisation(), directoryName);
        return this;
    }


    public DescriptorBuilderForTesting withPrimaryLicenseAndThirdPartyOrgInDirectory(String directoryName) {
        addDirectory(getPrimaryLicense(), getThirdPartyOrg(), directoryName);
        return this;
    }

    public Organisation primaryOrganisation() {
        return new Organisation(getPrimaryOrgName(), "primary.org", "http://example.org");
    }

    public Descriptor build() {
        return new Descriptor(primaryLicense, primaryCopyrightNotice, primaryOrgName,
                primaryNotice, licenses, notices, organisations, contents);
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getSecondaryCopyright() {
        return secondaryCopyright;
    }

    public String getPrimaryOrgName() {
        return primaryOrgName;
    }

    public Organisation getThirdPartyOrg() {
        return thirdPartyOrg;
    }

    public License getPrimaryLicense() {
        return primaryLicense;
    }

    public String getPrimaryLicenseText() {
        return primaryLicenseText;
    }

    public void addDirectory(License license, final Organisation org,
            final String directoryName) {
        final WithinDirectory withinDirectory = buildDirectory(license, org,
                directoryName);
        contents.add(withinDirectory);
    }

    private Collection<Resource> buildResources() {
        String noticeId = "notice:id";
        notices.put(noticeId, "Some notice text");
        Collection<Resource> resources = new ArrayList<Resource>();
        resources.add(new Resource(resourceName, noticeId, sourceUrl));
        return resources;
    }

    private WithinDirectory buildDirectory(License license,
            final Organisation org, final String directoryName) {
        Collection<ByOrganisation> byOrgs = new ArrayList<ByOrganisation>();
        Collection<Resource> resources = buildResources();
        byOrgs.add(new ByOrganisation(org, resources));

        Collection<WithLicense> withLicenses = new ArrayList<WithLicense>();
        Map<String, String> params = Collections.emptyMap();
        withLicenses.add(new WithLicense(license, secondaryCopyright, params, byOrgs));

        Collection<ByOrganisation> publicDomain = Collections.emptyList();

        final WithinDirectory withinDirectory = new WithinDirectory(directoryName, withLicenses, publicDomain);
        return withinDirectory;
    }

    public DescriptorBuilderForTesting withPrimaryCopyrightNotice() {
        return withPrimaryCopyrightNotice(A_PRIMARY_COPYRIGHT_NOTICE);
    }


    public DescriptorBuilderForTesting withPrimaryCopyrightNotice(String primaryCopyrightNotice) {
        this.primaryCopyrightNotice = primaryCopyrightNotice;
        return this;
    }

    public DescriptorBuilderForTesting withSecondaryLicensePrimaryOrganisationInDirectory(
            String directoryName) {
        addDirectory(secondaryLicense, primaryOrganisation(), directoryName);
        return this;
    }

    public DescriptorBuilderForTesting withSecondaryCopyrightNotice() {
        return withSecondaryCopyrightNotice("Copyright (c) this is secondary");
    }

    public DescriptorBuilderForTesting withSecondaryCopyrightNotice(final String secondaryCopyright) {
        this.secondaryCopyright = secondaryCopyright;
        return this;
    }

    public DescriptorBuilderForTesting withSourceURL() {
        sourceUrl = "http://example.org/bogus";
        return this;
    }

}
