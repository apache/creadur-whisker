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

import java.util.Map;

import junit.framework.TestCase;

import org.apache.rat.whisker.model.Organisation;
import org.jdom.Document;
import org.jdom.Element;

/**
 * 
 */
public class JDomBuilderWorkTest extends TestCase {

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

    public void testMapOrganisationsIsEmptyWhenDocumentHasNoOrganisations() throws Exception {
        final Map<String, Organisation> results = 
            subject.mapOrganisations(new Document().setRootElement(new Element("manifest")));
        assertNotNull("Expected builder to build something", results);
        assertTrue("Expected builder to build empty map when document has no documents", results.isEmpty());
    }
    
    public void testMapOrganisationsExpectedToBeImmutable() throws Exception {
        final Map<String, Organisation> results = 
            subject.mapOrganisations(new Document().setRootElement(new Element("manifest")));
        assertNotNull("Expected builder to build something", results);
        try {
            results.put("whatever", new Organisation("", "", ""));
            fail("Expected map to be immutable");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
    
    public void testMapOrganisationsFindOrganisationDefinedInDocument() throws Exception {
        for (int i=1;i<256;i++) {
            checkMapOrganisationsWith(i);
        };
    }

    /**
     * @param numberOfOrganisations
     */
    private void checkMapOrganisationsWith(final int numberOfOrganisations) {
        final String baseId = "ID";
        final String baseName = "Name";
        final Document in = new Document();
        final Element organisations = new Element("organisations");
        in.setRootElement(new Element("manifest").addContent(organisations));
        for (int i=0;i<numberOfOrganisations;i++) {
            organisations.addContent(
                    new Element("organisation").setAttribute("id", combine(baseId, i)).setAttribute("name", combine(baseName, i)));
        }
        final Map<String, Organisation> results = subject.mapOrganisations(in);
        assertEquals("One organisation in map for each in the document", numberOfOrganisations, results.size());
        for (int i=0;i<numberOfOrganisations;i++) {
            final Organisation next = results.get(combine(baseId, i));
            assertNotNull("Expected organisation to be stored", next);
            assertEquals("Expected correct organisation to be stored", next.getName(), combine(baseName, i));
        }
    }

    /**
     * @param baseId
     * @param i
     * @return
     */
    private String combine(String base, int i) {
        return base + i;
    }
}
