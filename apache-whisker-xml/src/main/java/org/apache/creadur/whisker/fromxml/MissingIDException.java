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
 * Indicates that the element linked by ID is missing.
 */
public class MissingIDException extends InvalidXmlException {

    private static final long serialVersionUID = 2226669867694728783L;
    private final String linkedElement;
    private final String linkingElement;
    private final String id;
    
    /**
     * Constructs an instance.
     * @param linkedElement not null
     * @param linkingElement not null
     * @param id not null
     */
    public MissingIDException(final String linkedElement, final String linkingElement,
            final String id) {
        super("Missing ID '" + id + "' for element '" + linkedElement + "' linked from element '" + linkingElement  + "'");
        this.linkedElement = linkedElement;
        this.linkingElement = linkingElement;
        this.id = id;
    }

    /**
     * Gets the name of the missing element linked by ID.
     * @return not null
     */
    public String getLinkedElement() {
        return linkedElement;
    }

    /**
     * Gets the name of the present element linked to a missing element 
     * by an ID.
     * @return not null
     */
    public String getLinkingElement() {
        return linkingElement;
    }

    /**
     * Gets the ID whose linked element is missing.
     * @return not null
     */
    public String getId() {
        return id;
    }
}
