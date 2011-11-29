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

import java.util.Collection;

import org.apache.rat.whisker.model.Resource;
import org.jdom.Element;

import junit.framework.TestCase;

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
        final Element result = new Element("by-organisation");
        for (int i=0;i<children;i++) {
                result.addContent(new Element("resource").setAttribute("name", "name" + i))
                .addContent(new Element("whatever"));
        }
        return result;
    }
    
}
