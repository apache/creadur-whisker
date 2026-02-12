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

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import org.apache.creadur.whisker.model.ByOrganisation;
import org.apache.creadur.whisker.model.Organisation;
import org.apache.creadur.whisker.model.Resource;
import org.jdom2.Element;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 
 */
class JDomBuilderByOrganisationTest {
    private JDomBuilder subject;

    @BeforeEach
    void setUp() throws Exception {
        subject = new JDomBuilder();
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    void buildResourcesFromNoResources() throws Exception {
        final Collection<Resource> results = subject.collectResources(new Element("by-organisation"));
        assertNotNull(results, "Expected empty collection to be returned");
        assertEquals(0, results.size(), "Expected collection to be empty");
    }


    @Test
    void collectResourcesNumbered1() throws Exception {
        checkCollectResourcesNumbered(1);
    }

    @Test
    void collectResourcesNumbered2() throws Exception {
        checkCollectResourcesNumbered(2);
    }

    @Test
    void collectResourcesNumbered3() throws Exception {
        checkCollectResourcesNumbered(3);
    }

    @Test
    void collectResourcesNumbered4() throws Exception {
        checkCollectResourcesNumbered(4);
    }

    @Test
    void organisationByIdThrowsIllegalArgumentWhenOrganisationsEmpty() throws Exception {
        try {
            subject.organisation(
                    new Element("by-organisation").setAttribute("id", "some id"), 
                    Collections.unmodifiableMap(new HashMap<String, Organisation>()));
            fail("Expected MissingIDException to be thrown when organisation isn't found in map");
        } catch (MissingIDException e) {
            // expected
        } catch (Throwable t) {
            fail("Expected MissingIDException to be thrown when organisation isn't found in map but instead " 
                    + t + " was thrown");
        }
    }

    @Test
    void organisationByIdThrowsIllegalArgumentWhenOrganisationsMissing() throws Exception {
        try {
            final HashMap<String, Organisation> map = new HashMap<String, Organisation>();
            new Organisation("BOGUS", "name", "url").storeIn(map);
            subject.organisation(
                    new Element("by-organisation").setAttribute("id", "some id"), 
                    Collections.unmodifiableMap(map));
            fail("Expected MissingIDException to be thrown when organisation isn't found in map");
        } catch (MissingIDException e) {
            // expected
        } catch (Throwable t) {
            fail("Expected MissingIDException to be thrown when organisation isn't found in map but instead " 
                    + t + " was thrown");
        }
    }

    @Test
    void organisationByIdFindsOrganisationsPresent() throws Exception {
        final String idValue = "some id";
        final HashMap<String, Organisation> map = new HashMap<String, Organisation>();
        final Organisation expected = new Organisation(idValue, "name", "url");
        expected.storeIn(map);
        assertEquals(expected,
                subject.organisation(
                        new Element("by-organisation").setAttribute("id", idValue), 
                        Collections.unmodifiableMap(map)),
                "Expected organisation with matching ID to be found");
    }

    @Test
    void byOrganisationBuildsFromOrganisationAndElementWith3ChildResources() throws Exception {
        checkOrganisationAndElementBuild(3);
    }


    @Test
    void byOrganisationBuildsFromOrganisationAndElementWith5ChildResources() throws Exception {
        checkOrganisationAndElementBuild(5);
    }

    @Test
    void byOrganisationBuildsFromOrganisationAndElementWith10ChildResources() throws Exception {
        checkOrganisationAndElementBuild(10);
    }
    
    
    /**
     * @param numberOfChildResources
     */
    private void checkOrganisationAndElementBuild(
            final int numberOfChildResources) {
        final Element byOrganisation = withChildResources(numberOfChildResources);
        final String idValue = "some id";
        final Organisation input = new Organisation(idValue, "name", "url");
        final ByOrganisation result = subject.byOrganisation(byOrganisation, input);
        assertNotNull(result, "Expected builder to build");
        assertNotNull(result.getResources(), "Expected resources to be built");
        assertNotNull(result.getOrganisation(), "Expected organisation to be set");
        assertEquals(numberOfChildResources, result.getResources().size(), "Expected number of resources built matches children");
        assertEquals(input, result.getOrganisation(), "Expected organisation to be set to input");
    }

    @Test
    void byOrganisationBuildsFromMapAndElementWith1ChildResources() throws Exception {
        checkElementAndMapBuild(1);
    }


    @Test
    void byOrganisationBuildsFromMapAndElementWith5ChildResources() throws Exception {
        checkElementAndMapBuild(5);
    }

    @Test
    void byOrganisationBuildsFromMapAndElementWith10ChildResources() throws Exception {
        checkElementAndMapBuild(10);
    }

    @Test
    void byOrganisationCollectiveOneChild() throws Exception {
        checkCollectByOrganisations(1);
    }

    @Test
    void byOrganisationCollective2Children() throws Exception {
        checkCollectByOrganisations(2);
    }

    @Test
    void byOrganisationCollective5Child() throws Exception {
        checkCollectByOrganisations(5);
    }

    @Test
    void byOrganisationCollective3Child() throws Exception {
        checkCollectByOrganisations(3);
    }

    @Test
    void byOrganisationCollective7Child() throws Exception {
        checkCollectByOrganisations(7);
    }

    
    /**
     * @param numberOfChildren
     */
    private void checkCollectByOrganisations(final int numberOfChildren) {
        final Element parent = new Element("with-license");
        final String idValue = "some id";
        final HashMap<String, Organisation> map = new HashMap<String, Organisation>();
        for (int i=0;i<numberOfChildren;i++) {
            final String nextIdValue = idValue + i;
            parent.addContent(withChildResources(10, nextIdValue));
            new Organisation(nextIdValue, "name" + i, "url").storeIn(map);
        }
        final Collection<ByOrganisation> results = subject.collectByOrganisations(parent, map);
        assertNotNull(results, "Builder should build something");
        assertEquals(numberOfChildren, results.size(), "Expected an by-organisation in collection per child");
    }

    @Test
    void byOrganisationEmptyCollective() throws Exception {
        final Element parent = new Element("with-license");
        final Organisation input = new Organisation("id", "name", "url");
        final HashMap<String, Organisation> map = new HashMap<String, Organisation>();
        input.storeIn(map);
        final Collection<ByOrganisation> results = subject.collectByOrganisations(parent, map);
        assertNotNull(results, "Builder should build something");
        assertTrue(results.isEmpty(), "No children so expected an empty collection");
    }


    @Test
    void byOrganisationCollectiveUnmodifiable() throws Exception {
        final Element parent = new Element("with-license");
        final Organisation input = new Organisation("id", "name", "url");
        final HashMap<String, Organisation> map = new HashMap<String, Organisation>();
        input.storeIn(map);
        final Collection<ByOrganisation> results = subject.collectByOrganisations(parent, map);
        assertNotNull(results, "Builder should build something");
        try {
            results.clear();
            fail("Expected collection to be unmodifiable");
        } catch (UnsupportedOperationException e) {
            //expected
        }
    }

    
    /**
     * @param numberOfChildResources
     */
    private void checkElementAndMapBuild(final int numberOfChildResources) {
        final Element byOrganisation = withChildResources(numberOfChildResources);
        final String idValue = "some id";
        final Organisation input = new Organisation(idValue, "name", "url");
        final HashMap<String, Organisation> map = new HashMap<String, Organisation>();
        input.storeIn(map);
        final ByOrganisation result = subject.byOrganisation(byOrganisation, map);
        assertNotNull(result, "Expected builder to build");
        assertNotNull(result.getResources(), "Expected resources to be built");
        assertNotNull(result.getOrganisation(), "Expected organisation to be set");
        assertEquals(numberOfChildResources, result.getResources().size(), "Expected number of resources built matches children");
        assertEquals(input, result.getOrganisation(), "Expected organisation to be set to input");
    }
    
    
    
    /**
     * @param numberOfResources
     */
    private void checkCollectResourcesNumbered(final int numberOfResources) {
        final Collection<Resource> results = subject.collectResources(withChildResources(numberOfResources));
        assertNotNull(results, "Expected empty collection to be returned");
        assertEquals(numberOfResources, results.size(), "Expected collection to be empty");
    }

    /**
     * @return
     */
    private Element withChildResources(int children) {
        return withChildResources(children, "some id");
    }

    /**
     * @param children
     * @param idValue
     * @return
     */
    private Element withChildResources(int children, final String idValue) {
        final Element result = new Element("by-organisation").setAttribute("id", idValue);
        for (int i=0;i<children;i++) {
                result.addContent(new Element("resource").setAttribute("name", "name" + i))
                .addContent(new Element("whatever"));
        }
        return result;
    }
}
