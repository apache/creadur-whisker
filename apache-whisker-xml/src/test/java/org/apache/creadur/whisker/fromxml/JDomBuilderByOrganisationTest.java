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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import junit.framework.TestCase;

import org.apache.creadur.whisker.model.ByOrganisation;
import org.apache.creadur.whisker.model.Organisation;
import org.apache.creadur.whisker.model.Resource;
import org.jdom.Element;

/**
 * 
 */
public class JDomBuilderByOrganisationTest extends TestCase {
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

    public void testBuildResourcesFromNoResources() throws Exception {
        final Collection<Resource> results = subject.collectResources(new Element("by-organisation"));
        assertNotNull("Expected empty collection to be returned", results);
        assertEquals("Expected collection to be empty", 0, results.size());
    }
    

    public void testCollectResourcesNumbered1() throws Exception {
        checkCollectResourcesNumbered(1);
    }

    public void testCollectResourcesNumbered2() throws Exception {
        checkCollectResourcesNumbered(2);
    }
    
    public void testCollectResourcesNumbered3() throws Exception {
        checkCollectResourcesNumbered(3);
    }
    
    public void testCollectResourcesNumbered4() throws Exception {
        checkCollectResourcesNumbered(4);
    }

    public void testOrganisationByIdThrowsIllegalArgumentWhenOrganisationsEmpty() throws Exception {
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

    public void testOrganisationByIdThrowsIllegalArgumentWhenOrganisationsMissing() throws Exception {
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

    public void testOrganisationByIdFindsOrganisationsPresent() throws Exception {
        final String idValue = "some id";
        final HashMap<String, Organisation> map = new HashMap<String, Organisation>();
        final Organisation expected = new Organisation(idValue, "name", "url");
        expected.storeIn(map);
        assertEquals("Expected organisation with matching ID to be found", expected,
                subject.organisation(
                        new Element("by-organisation").setAttribute("id", idValue), 
                        Collections.unmodifiableMap(map)));
    }

    public void testByOrganisationBuildsFromOrganisationAndElementWith3ChildResources() throws Exception {
        checkOrganisationAndElementBuild(3);
    }


    public void testByOrganisationBuildsFromOrganisationAndElementWith5ChildResources() throws Exception {
        checkOrganisationAndElementBuild(5);
    }

    public void testByOrganisationBuildsFromOrganisationAndElementWith10ChildResources() throws Exception {
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
        assertNotNull("Expected builder to build", result);
        assertNotNull("Expected resources to be built", result.getResources());
        assertNotNull("Expected organisation to be set", result.getOrganisation());
        assertEquals("Expected number of resources built matches children", numberOfChildResources, result.getResources().size());
        assertEquals("Expected organisation to be set to input", input, result.getOrganisation());
    }

    public void testByOrganisationBuildsFromMapAndElementWith1ChildResources() throws Exception {
        checkElementAndMapBuild(1);
    }

    
    public void testByOrganisationBuildsFromMapAndElementWith5ChildResources() throws Exception {
        checkElementAndMapBuild(5);
    }
    
    public void testByOrganisationBuildsFromMapAndElementWith10ChildResources() throws Exception {
        checkElementAndMapBuild(10);
    }

    public void testByOrganisationCollectiveOneChild()throws Exception {
        checkCollectByOrganisations(1);
    }

    public void testByOrganisationCollective2Children()throws Exception {
        checkCollectByOrganisations(2);
    }

    public void testByOrganisationCollective5Child()throws Exception {
        checkCollectByOrganisations(5);
    }

    public void testByOrganisationCollective3Child()throws Exception {
        checkCollectByOrganisations(3);
    }
    
    public void testByOrganisationCollective7Child()throws Exception {
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
        assertNotNull("Builder should build something", results);
        assertEquals("Expected an by-organisation in collection per child", numberOfChildren, results.size());
    }
    
    public void testByOrganisationEmptyCollective()throws Exception {
        final Element parent = new Element("with-license");
        final Organisation input = new Organisation("id", "name", "url");
        final HashMap<String, Organisation> map = new HashMap<String, Organisation>();
        input.storeIn(map);
        final Collection<ByOrganisation> results = subject.collectByOrganisations(parent, map);
        assertNotNull("Builder should build something", results);
        assertTrue("No children so expected an empty collection", results.isEmpty());
    }

    
    public void testByOrganisationCollectiveUnmodifiable()throws Exception {
        final Element parent = new Element("with-license");
        final Organisation input = new Organisation("id", "name", "url");
        final HashMap<String, Organisation> map = new HashMap<String, Organisation>();
        input.storeIn(map);
        final Collection<ByOrganisation> results = subject.collectByOrganisations(parent, map);
        assertNotNull("Builder should build something", results);
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
        assertNotNull("Expected builder to build", result);
        assertNotNull("Expected resources to be built", result.getResources());
        assertNotNull("Expected organisation to be set", result.getOrganisation());
        assertEquals("Expected number of resources built matches children", numberOfChildResources, result.getResources().size());
        assertEquals("Expected organisation to be set to input", input, result.getOrganisation());
    }
    
    
    
    /**
     * @param numberOfResources
     */
    private void checkCollectResourcesNumbered(final int numberOfResources) {
        final Collection<Resource> results = subject.collectResources(withChildResources(numberOfResources));
        assertNotNull("Expected empty collection to be returned", results);
        assertEquals("Expected collection to be empty", numberOfResources, results.size());
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
