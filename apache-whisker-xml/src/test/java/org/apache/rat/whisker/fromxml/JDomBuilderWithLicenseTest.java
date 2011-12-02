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
package org.apache.rat.whisker.fromxml;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.rat.whisker.model.License;
import org.jdom.Element;

import junit.framework.TestCase;

/**
 * 
 */
public class JDomBuilderWithLicenseTest extends TestCase {

    private JDomBuilder subject;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        subject = new JDomBuilder();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }


    public void testLicenseFromListThrowsMissingIDWhenEmpty() throws Exception {        
        final Map<String, License> licenses = new HashMap<String, License>();
        try {
            subject.license(new Element("with-license").setAttribute("id", "id"), licenses);
            fail("Throw an exception when the ID is missing");
        } catch (MissingIDException e) {
            // expected
        }
    }

    @SuppressWarnings("unchecked")
    public void testLicenseFromListThrowsMissingIDWhenIDsAreMismatched() throws Exception {        
        final Map<String, License> licenses = new HashMap<String, License>();
        new License(false, "", Collections.EMPTY_LIST, "noise", "noise url", "name").storeIn(licenses);
        try {
            subject.license(new Element("with-license").setAttribute("id", "id"), licenses);
            fail("Throw an exception when the ID is missing");
        } catch (MissingIDException e) {
            // expected
        }
    }
    
    @SuppressWarnings("unchecked")
    public void testLicenseFromListFindsLicense() throws Exception {
        final String id = "id";
        
        final Map<String, License> licenses = new HashMap<String, License>();
        final License expected = new License(false, "", Collections.EMPTY_LIST, id, "url", "name");
        expected.storeIn(licenses);
        new License(false, "", Collections.EMPTY_LIST, "noise", "noise url", "name").storeIn(licenses);
        
        final License output = subject.license(new Element("with-license").setAttribute("id", id), licenses);
        assertNotNull("Expected builder to build", output);
        assertEquals("Expected license to be found", expected, output);
    }
}
