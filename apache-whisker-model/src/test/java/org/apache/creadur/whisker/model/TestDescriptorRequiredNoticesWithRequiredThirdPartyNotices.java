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

import junit.framework.TestCase;

public class TestDescriptorRequiredNoticesWithRequiredThirdPartyNotices extends TestCase {

    License primaryLicense = new License(false, "This is the license text", Collections.<String> emptyList(), "example.org", "http://example.org", "Example License");
    String primaryOrg = "example.org";
    String primaryNotice = "The primary notice.";
    Collection<WithinDirectory> contents = new ArrayList<WithinDirectory>();
    Map<String, License> licenses = new HashMap<String, License>();
    Map<String, String> notices = new HashMap<String, String>();
    Map<String, Organisation> organisations = new HashMap<String, Organisation>();
    
    Descriptor subject;
    
    protected void setUp() throws Exception {
        super.setUp();
        primaryLicense.storeIn(licenses);
        String noticeId = "notice:id";
        notices.put(noticeId, "Some notice text");
        Collection<WithLicense> withLicenses = new ArrayList<WithLicense>();
        License license = primaryLicense;
        Collection<ByOrganisation> orgs = new ArrayList<ByOrganisation>();
        Collection<Resource> resources = new ArrayList<Resource>();
        String source = "";
        String name = "resource";
        resources.add(new Resource(name , noticeId, source));
        String orgUrl = "org:url";
        String orgId = "org:id";
        String orgName = "example.org";
        Organisation org = new Organisation(orgId, orgName, orgUrl);
        orgs.add(new ByOrganisation(org, resources));
        String copyright = "Copyright Blah";
        Map<String, String> params = Collections.emptyMap();
        withLicenses.add(new WithLicense(license, copyright, params, orgs));
        Collection<ByOrganisation> publicDomain = Collections.emptyList();
        contents.add(new WithinDirectory(".", withLicenses, publicDomain));
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testNoticeRequiredWhenPrimaryNoticeExists() throws Exception {
        subject = 
                new Descriptor(primaryLicense, primaryOrg,  primaryNotice, 
                        licenses, notices, organisations, contents);
        assertTrue("When primary notices exists and third party notices needed then display is required", 
                subject.isNoticeRequired());        
    }

    public void testNoticeRequiredWhenPrimaryNoticeIsNullAndThirdPartyNotices() throws Exception {
        subject = 
                new Descriptor(primaryLicense, primaryOrg,  null, 
                        licenses, notices, organisations, contents);
        assertTrue("When no other notices exist and third party notices needed then display is required", 
                subject.isNoticeRequired());        
    }

    public void testNoticeRequiredWhenPrimaryNoticeIsEmptyAndThirdPartyNotices() throws Exception {
        subject = 
                new Descriptor(primaryLicense, primaryOrg,  "", 
                        licenses, notices, organisations, contents);
        assertTrue("When no other notices exist and third party notices needed then display is required", 
                subject.isNoticeRequired());        
    }

    public void testNoticeRequiredWhenPrimaryNoticeIsWhitespaceAndThirdPartyNotices() throws Exception {
        subject = 
                new Descriptor(primaryLicense, primaryOrg,  "   ", 
                        licenses, notices, organisations, contents);
        assertTrue("When no other notices exist and third party notices needed then display is required", 
                subject.isNoticeRequired());        
    }
}
