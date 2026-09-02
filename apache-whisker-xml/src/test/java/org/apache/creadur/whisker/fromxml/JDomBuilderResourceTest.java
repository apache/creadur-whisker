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

import org.apache.creadur.whisker.model.Resource;

import org.jdom2.Element;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 
 */
class JDomBuilderResourceTest {

    private JDomBuilder subject;

    @BeforeEach
    void setUp() throws Exception {
        subject = new JDomBuilder();
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    void buildResourceWithNameNoticeAndSourceTrimsSpacesBeforeValue() throws Exception {
        checkResourceBuild(" name", "name", 
                "  notice", "notice",
                "   source", "source");        
    }


    @Test
    void buildResourceWithNameNoticeAndSourceTrimsSpaces() throws Exception {        
        checkResourceBuild(" name   ", "name", 
                "  notice    ", "notice", "  " +
                "   source   ", "source");   
        
    }


    @Test
    void buildResourceWithNameNoticeAndSourceTrimsSpacesAfterValue() throws Exception {
        checkResourceBuild("name   ", "name", 
                "notice    ", "notice", "  " +
                "source   ", "source");  
    }


    @Test
    void buildResourceWithNameNoticeAndSourceTrimsWithSpacesInValue() throws Exception {
        checkResourceBuild(" n  ame   ", "n  ame", 
                "  not ic e    ", "not ic e", "  " +
                "sour  ce   ", "sour  ce");  
    }

    @Test
    void buildResourceWithNameNoticeAndSourceSpacesInVaule() throws Exception {
        checkResourceBuildWithoutSurroundingSpace("a name", "a notice", "some source");
    }

    @Test
    void buildResourceWithNameNoticeAndSourceLowers() throws Exception {
        checkResourceBuildWithoutSurroundingSpace("name", "notice", "source");
    }

    @Test
    void buildResourceWithNameNoticeAndSourceCaps() throws Exception {
        checkResourceBuildWithoutSurroundingSpace("NOTICE", "NOTICE", "SOURCE");
    }

    @Test
    void throwsIllegalArgumentWhenElementIsNotResource() throws Exception {
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
        assertNotNull(result, "Expected builder to build");
        assertEquals(expectedNameValue, result.getName(), "Name value set from xml attribute");
        assertEquals(expectedNoticeValue, result.getNoticeId(), "Notice value set from xml attribute");
        assertEquals(expectedSourceValue, result.getSource(), "Source value set from xml attribute");
    }
}
