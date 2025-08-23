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
package org.apache.creadur.whisker.fromxml;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.apache.creadur.whisker.model.Organisation;

import org.jdom2.Element;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 
 */
class JDomBuilderOrganisationTest {
    private JDomBuilder subject;

    @BeforeEach
    void setUp() throws Exception {
        subject = new JDomBuilder();
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    void organisationSetsIdNameUrl() throws Exception {
        final Organisation result = subject.organisation(
                new Element("organisation")
                .setAttribute("name", "a name")
                .setAttribute("url", "an url")
                .setAttribute("id", "an id"));
        assertNotNull(result, "Builder should build an organisation");
        
    }

    @Test
    void throwIllegalArgumentWhenResourceIsNotOrganisation() throws Exception {
        try {
            subject.organisation(
                new Element("bogus")
                    .setAttribute("name", "name")
                    .setAttribute("url", "url")
                    .setAttribute("id", "id"));
            fail("Expected IllegalArgument throw when elements is not named 'organisation'");  
        } catch (UnexpectedElementException e) {
            //expected
        } catch (Throwable t) {
            fail("Expected IllegalArgument throw when elements is not named 'organisation' but " + t + " was instead");
        }
    }
}
