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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.apache.creadur.whisker.model.LicenseBuilderForTesting.*;
import static org.apache.creadur.whisker.model.OrganisationBuilderForTesting.*;

public class DescriptorBuilderForTesting {

    public static final String DEFAULT_PRIMARY_COPYRIGHT_NOTICE = "Copyright (c) Primary";
    public static final String DEFAULT_SUBSIDARY_COPYRIGHT_NOTICE = "Copyright (c) Secondary";

    License primaryLicense = defaultPrimaryLicense();
    String primaryCopyrightNotice = DEFAULT_PRIMARY_COPYRIGHT_NOTICE;
    Organisation primaryOrg = defaultPrimaryOrganisation();
    String primaryNotice = "The primary notice.";
    Collection<WithinDirectory> contents = new ArrayList<WithinDirectory>();
    Map<String, License> licenses = new HashMap<String, License>();
    Map<String, String> notices = new HashMap<String, String>();
    Map<String, Organisation> organisations = new HashMap<String, Organisation>();

    String subsidaryCopyrightNotice;

    public Descriptor build() {
        primaryLicense.storeIn(licenses);
        primaryOrg.storeIn(organisations);

        return new Descriptor(primaryLicense,
            primaryCopyrightNotice,
            primaryOrg.getId(),
            primaryNotice,
            licenses,
            notices,
            organisations,
            contents);
    }

    public DescriptorBuilderForTesting withSubsidaryCopyrightNotice() {
        return withSubsidaryCopyrightNotice(DEFAULT_SUBSIDARY_COPYRIGHT_NOTICE);
    }

    public DescriptorBuilderForTesting withSubsidaryCopyrightNotice(String subsidaryCopyrightNotice) {
        this.subsidaryCopyrightNotice = subsidaryCopyrightNotice;
        return this;
    }

    public DescriptorBuilderForTesting withThirdParty(
            OrganisationBuilderForTesting builder) {
        builder.build().storeIn(organisations);
        return this;
    }

    public DescriptorBuilderForTesting withThirdParty() {
        return withThirdParty(new OrganisationBuilderForTesting());
    }

    public DescriptorBuilderForTesting withDirectory(final String directoryName) {
        return withDirectory(primaryLicense, primaryOrg, directoryName);
    }

    public DescriptorBuilderForTesting withDirectory(License license, final Organisation org,
            final String directoryName) {
        final WithinDirectory withinDirectory = buildDirectory(license, org,
                directoryName);
        contents.add(withinDirectory);
        return this;
    }

    public DescriptorBuilderForTesting withThirdPartyDirectory(String directoryName) {
        return withDirectory(
                primaryLicense,
                new OrganisationBuilderForTesting().build(),
                directoryName);
    }

    private WithinDirectory buildDirectory(License license,
            final Organisation org, final String directoryName) {
        Collection<ByOrganisation> byOrgs = new ArrayList<ByOrganisation>();
        Collection<Resource> resources = buildResources();
        byOrgs.add(new ByOrganisation(org, resources));

        Collection<WithLicense> withLicenses = new ArrayList<WithLicense>();
        Map<String, String> params = Collections.emptyMap();
        withLicenses.add(new WithLicense(license, subsidaryCopyrightNotice, params, byOrgs));

        Collection<ByOrganisation> publicDomain = Collections.emptyList();

        final WithinDirectory withinDirectory = new WithinDirectory(directoryName, withLicenses, publicDomain);
        return withinDirectory;
    }

    private Collection<Resource> buildResources() {
        String noticeId = "notice:id";
        notices.put(noticeId, "Some notice text");
        Collection<Resource> resources = new ArrayList<Resource>();
        String source = "";
        String name = "resource";
        resources.add(new Resource(name, noticeId, source));
        return resources;
    }
}
