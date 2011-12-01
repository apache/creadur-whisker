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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.rat.whisker.model.Organisation;
import org.apache.rat.whisker.model.Resource;
import org.jdom.Element;

/**
 * Builds a model from xml using JDOM.
 */
public class JDomBuilder {
    
    /** Names the element representing an organisation */
    private static final String ORGANISATION_ELEMENT_NAME = "organisation";
    /** Names the element representing a resource */
    private static final String RESOURCE_ELEMENT_NAME = "resource";

    /**
     * Builds a resource.
     * @param element not null
     * @return built resource, not null
     * @throws UnexpectedElementException when element is not named 'resource'
     */
    public Resource resource(Element element) throws UnexpectedElementException {
        if (RESOURCE_ELEMENT_NAME.equals(element.getName())) {
            return new Resource(StringUtils.trim(element.getAttributeValue("name")), 
                    StringUtils.trim(element.getAttributeValue("notice")),
                    StringUtils.trim(element.getAttributeValue("source")));
        } else {
            throw unexpectedElementException(element, RESOURCE_ELEMENT_NAME);
        }
    }

    /**
     * Builds a suitable exception when the element name is unexpected.
     * @param element, not null
     * @param expectedElement, not null
     * @return a suitable exception, not null
     */
    private UnexpectedElementException unexpectedElementException(Element element,
            final String expectedElement) {
        return new UnexpectedElementException(expectedElement, element.getName());
    }

    /**
     * Builds an organisation model from xml.
     * @param element, not null
     * @return {@link Organisation} not null
     * @throws UnexpectedElementException when element is not named 'organisation'
     */
    public Organisation organisation(Element element) throws UnexpectedElementException {
        if (ORGANISATION_ELEMENT_NAME.equals(element.getName())) {
            return new Organisation(
                    element.getAttributeValue("id"), 
                    element.getAttributeValue("name"), 
                    element.getAttributeValue("url"));
        } else {
            throw unexpectedElementException(element, ORGANISATION_ELEMENT_NAME);
        }
    }

    /**
     * @param element
     * @return
     */
    @SuppressWarnings("unchecked")
    public Collection<Resource> collectResources(Element element) {
        final Collection<Resource> resources = new TreeSet<Resource>();
        for (Element resourceElement: (List<Element>) element.getChildren("resource")) {
            resources.add(new JDomBuilder().resource(resourceElement));
        }
        return Collections.unmodifiableCollection(resources);
    }

    /**
     * Finds the organisation linked by ID from the given element.
     * @param element modelled ByOrganisation, not null
     * @param organisationsById organisations identified, not null
     * @throws MissingIDException when the linked organisation is not found in the given collection
     */
    public Organisation organisation(final Element element,
            final Map<String, Organisation> organisationsById) throws MissingIDException {
        final String id = element.getAttributeValue("id");
        if (organisationsById.containsKey(id)) {
            return organisationsById.get(id);
        } else {
            throw new MissingIDException(ORGANISATION_ELEMENT_NAME, element.getName(), id);
        }
    }
}
