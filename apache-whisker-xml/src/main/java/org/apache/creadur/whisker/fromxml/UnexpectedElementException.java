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

/**
 * Indicates that an unexpected element occurred in the xml.
 */
public class UnexpectedElementException extends InvalidXmlException {

    /**  */
    private static final long serialVersionUID = -4801232871203301470L;

    private final String expectedElement;
    private final String actualElement;
    
    /**
     * Constructs an instance.
     * @param expectedElement names the element expected not null
     * @param actualElement names the element that occured not null
     */
    public UnexpectedElementException(final String expectedElement, final String actualElement) {
        super("Expected '"  + expectedElement + "' element but was " + actualElement);
        this.expectedElement = expectedElement;
        this.actualElement = actualElement;
    }

    /**
     * Gets the name of the element that was expected.
     * @return not null
     */
    public String getExpectedElement() {
        return expectedElement;
    }

    /**
     * Gets the name of the element that occured.
     * @return not null
     */
    public String getActualElement() {
        return actualElement;
    }
}
