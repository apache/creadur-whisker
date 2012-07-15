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

import org.apache.commons.lang3.StringUtils;
import org.apache.creadur.whisker.app.Result;
import org.apache.creadur.whisker.model.ByOrganisation;
import org.apache.creadur.whisker.model.Descriptor;
import org.apache.creadur.whisker.model.License;
import org.apache.creadur.whisker.model.Organisation;
import org.apache.creadur.whisker.model.Resource;
import org.apache.creadur.whisker.model.WithLicense;
import org.apache.creadur.whisker.model.WithinDirectory;

import junit.framework.TestCase;

public class TestLicenseGeneration extends TestCase {
    
    StringResultWriterFactory writerFactory;
    VelocityEngine subject;
    String primaryLicenseText = "This is the primary license text";
    Organisation thirdPartyOrg = new Organisation("third-party", "thirdparty.org", "http://thirdparty.org");
    License primaryLicense = new License(false, primaryLicenseText, Collections.<String> emptyList(), "example.org", "http://example.org", "Example License");
    String primaryOrg = "example.org";
    String primaryNotice = "The primary notice.";
    Collection<WithinDirectory> contents = new ArrayList<WithinDirectory>();
    Map<String, License> licenses = new HashMap<String, License>();
    Map<String, String> notices = new HashMap<String, String>();
    Map<String, Organisation> organisations = new HashMap<String, Organisation>();
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        writerFactory = new StringResultWriterFactory();
        subject = new VelocityEngine(new EmptyLog());
        primaryLicense.storeIn(licenses);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private void addDirectory(License license, final Organisation org,
            final String directoryName) {
        final WithinDirectory withinDirectory = buildDirectory(license, org,
                directoryName);
        contents.add(withinDirectory);
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

    private WithinDirectory buildDirectory(License license,
            final Organisation org, final String directoryName) {
        Collection<ByOrganisation> byOrgs = new ArrayList<ByOrganisation>();
        Collection<Resource> resources = buildResources();
        byOrgs.add(new ByOrganisation(org, resources));
        
        Collection<WithLicense> withLicenses = new ArrayList<WithLicense>();
        String copyright = "Copyright Blah";
        Map<String, String> params = Collections.emptyMap();
        withLicenses.add(new WithLicense(license, copyright, params, byOrgs));
        
        Collection<ByOrganisation> publicDomain = Collections.emptyList();
        
        final WithinDirectory withinDirectory = new WithinDirectory(directoryName, withLicenses, publicDomain);
        return withinDirectory;
    }

    public void testThatWhenThereAreNoThirdPartyContentsFooterIsNotShown() throws Exception {
        Descriptor work = 
                new Descriptor(primaryLicense, primaryOrg,  primaryNotice, 
                        licenses, notices, organisations, contents);
        
        subject.generate(work, writerFactory);

        assertTrue("Check that work is suitable for this test", work.isPrimaryOnly());
        assertEquals("Only one request for LICENSE writer", 1, writerFactory.requestsFor(Result.LICENSE));
        assertEquals("When no third party contents, expect that only the license text is output", 
                primaryLicenseText, 
                writerFactory.firstOutputFor(Result.LICENSE).trim());
    }
    
    public void testThatFooterIsShownWhenThereAreThirdPartyContents() throws Exception {
        addDirectory(primaryLicense, thirdPartyOrg, "lib");
        
        Descriptor work = 
                new Descriptor(primaryLicense, primaryOrg,  primaryNotice, 
                        licenses, notices, organisations, contents);
        
        assertFalse("Check that work is suitable for this test", work.isPrimaryOnly());
        
        subject.generate(work, writerFactory);

        assertEquals("Only one request for LICENSE writer", 1, writerFactory.requestsFor(Result.LICENSE));
        assertTrue("Expect information when third party contents present: " + writerFactory.firstOutputFor(Result.LICENSE), 
                StringUtils.contains(writerFactory.firstOutputFor(Result.LICENSE), 
                        "This distribution contains third party resources."));
    }
}
