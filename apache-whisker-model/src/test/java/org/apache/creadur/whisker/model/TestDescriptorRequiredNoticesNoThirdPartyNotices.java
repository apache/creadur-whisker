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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestDescriptorRequiredNoticesNoThirdPartyNotices {

    License primaryLicense = new License(false, "This is the license text", Collections.<String> emptyList(), "example.org", "http://example.org", "Example License");
    String primaryOrg = "example.org";
    String primaryNotice = "The primary notice.";
    Collection<WithinDirectory> contents = new ArrayList<WithinDirectory>();
    Map<String, License> licenses = new HashMap<String, License>();
    Map<String, String> notices = new HashMap<String, String>();
    Map<String, Organisation> organisations = new HashMap<String, Organisation>();
    
    Descriptor subject;

    @BeforeEach
    void setUp() throws Exception {
        primaryLicense.storeIn(licenses);
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    void noticeRequiredWhenPrimaryNoticeExists() throws Exception {
        subject = 
                new Descriptor(primaryLicense, primaryOrg,  primaryNotice, 
                        licenses, notices, organisations, contents);
        assertTrue(subject.isNoticeRequired(), "When primary notices exists, even if there are not other notices display is required");        
    }

    @Test
    void noticeNotRequiredWhenPrimaryNoticeIsNullAndNoNotices() throws Exception {
        subject = 
                new Descriptor(primaryLicense, primaryOrg,  null, 
                        licenses, notices, organisations, contents);
        assertFalse(subject.isNoticeRequired(), 
                "When no other notices exist and no primary notice, display is not required");        
    }

    @Test
    void noticeNotRequiredWhenPrimaryNoticeIsEmptyAndNoNotices() throws Exception {
        subject = 
                new Descriptor(primaryLicense, primaryOrg,  "", 
                        licenses, notices, organisations, contents);
        assertFalse(subject.isNoticeRequired(), 
                "When no other notices exist and no primary notice, display is not required");        
    }

    @Test
    void noticeNotRequiredWhenPrimaryNoticeIsWhitespaceAndNoNotices() throws Exception {
        subject = 
                new Descriptor(primaryLicense, primaryOrg,  "   ", 
                        licenses, notices, organisations, contents);
        assertFalse(subject.isNoticeRequired(), 
                "When no other notices exist and no primary notice, display is not required");        
    }
}
