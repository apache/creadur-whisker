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

import org.apache.creadur.whisker.model.Resource;
import org.jdom.Element;

import junit.framework.TestCase;

/**
 * 
 */
public class JDomBuilderResourceTest extends TestCase {

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

    public void testBuildResourceWithNameNoticeAndSourceTrimsSpacesBeforeValue() throws Exception {
        checkResourceBuild(" name", "name", 
                "  notice", "notice",
                "   source", "source");        
    }


    public void testBuildResourceWithNameNoticeAndSourceTrimsSpaces() throws Exception {        
        checkResourceBuild(" name   ", "name", 
                "  notice    ", "notice", "  " +
                "   source   ", "source");   
        
    }


    public void testBuildResourceWithNameNoticeAndSourceTrimsSpacesAfterValue() throws Exception {
        checkResourceBuild("name   ", "name", 
                "notice    ", "notice", "  " +
                "source   ", "source");  
    }


    public void testBuildResourceWithNameNoticeAndSourceTrimsWithSpacesInValue() throws Exception {
        checkResourceBuild(" n  ame   ", "n  ame", 
                "  not ic e    ", "not ic e", "  " +
                "sour  ce   ", "sour  ce");  
    }

    public void testBuildResourceWithNameNoticeAndSourceSpacesInVaule() throws Exception {
        checkResourceBuildWithoutSurroundingSpace("a name", "a notice", "some source");
    }
    
    public void testBuildResourceWithNameNoticeAndSourceLowers() throws Exception {
        checkResourceBuildWithoutSurroundingSpace("name", "notice", "source");
    }

    public void testBuildResourceWithNameNoticeAndSourceCaps() throws Exception {
        checkResourceBuildWithoutSurroundingSpace("NOTICE", "NOTICE", "SOURCE");
    }

    public void testThrowsIllegalArgumentWhenElementIsNotResource() throws Exception {
        try {
            subject.resource(
                new Element("bogus")
                    .setAttribute("name", "name")
                    .setAttribute("notice", "notice")
                    .setAttribute("source", "source"));
            fail("Expected IllegalArgument throw when elements is not named 'resource'");  
        } catch (UnexpectedElementException e) {
            //expected
        } catch (Throwable t) {
            fail("Expected IllegalArgument throw when elements is not named 'resource' but " + t + " was instead");
        }
    }
    
    /**
     * @param nameValue
     * @param noticeValue
     * @param sourceValue
     */
    private void checkResourceBuildWithoutSurroundingSpace(
            final String nameValue, final String noticeValue,
            final String sourceValue) {
        final String expectedNameValue = nameValue;
        final String expectedNoticeValue = noticeValue;
        final String expectedSourceValue = sourceValue;
        
        checkResourceBuild(nameValue, expectedNameValue, noticeValue,
                expectedNoticeValue, sourceValue, expectedSourceValue);
    }

    /**
     * @param nameValue
     * @param expectedNameValue
     * @param noticeValue
     * @param expectedNoticeValue
     * @param sourceValue
     * @param expectedSourceValue
     */
    private void checkResourceBuild(final String nameValue,
            final String expectedNameValue, final String noticeValue,
            final String expectedNoticeValue, final String sourceValue,
            final String expectedSourceValue) {
        final Resource result = subject.resource(
                new Element("resource")
                    .setAttribute("name", nameValue)
                    .setAttribute("notice", noticeValue)
                    .setAttribute("source", sourceValue));
        assertNotNull("Expected builder to build", result);
        assertEquals("Name value set from xml attribute", expectedNameValue, result.getName());
        assertEquals("Notice value set from xml attribute", expectedNoticeValue, result.getNoticeId());
        assertEquals("Source value set from xml attribute", expectedSourceValue, result.getSource());
    }
}
