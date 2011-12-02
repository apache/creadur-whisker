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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.rat.whisker.model.ByOrganisation;
import org.apache.rat.whisker.model.License;
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
     * @throws MissingIDException when the linked organisation is not found in the given map
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
    
    /**
     * Builds a by-organisation model from xml.
     * @param element not null
     * @param organisation not null
     * @return not null
     */
    public ByOrganisation byOrganisation(final Element element, final Organisation organisation) {
        return new ByOrganisation(organisation, collectResources(element));
    }

    /**
     * Builds a by-organisation model from xml.
     * @param byOrganisation not null
     * @param organisationsById not null 
     * @return not null
     * @throws MissingIDException when the linked organisation is not found in the given map
     */
    public ByOrganisation byOrganisation(final Element byOrganisation, 
            final Map<String, Organisation> organisationsById) throws MissingIDException  {
        return byOrganisation(byOrganisation, organisation(byOrganisation, organisationsById));
    }

    /**
     * Collects by-organisation children.
     * @param parent not null
     * @param map not null
     * @return unmodifiable set sort by natural order, not null
     */
    @SuppressWarnings("unchecked")
    public SortedSet<ByOrganisation> collectByOrganisations(final Element parent, 
            final Map<String, Organisation> map) {
        final SortedSet<ByOrganisation> results = new TreeSet<ByOrganisation>();
        if (parent != null) {
            for (final Element byOrgElement: (List<Element>) parent.getChildren("by-organisation")) {
                results.add(byOrganisation(byOrgElement, map));
            }
        }
        return Collections.unmodifiableSortedSet(results);
    }

    /**
     * Builds a license model from xml.
     * @param element not null
     * @return not null
     */
    public License license(Element element) {
        final Element text = element.getChild("text");
        return new License("yes".equalsIgnoreCase(element.getAttributeValue("requires-source")), 
                text == null ? "" : text.getText(), 
                expectedParameters(element), 
                element.getAttributeValue("id"), 
                element.getAttributeValue("url"),
                element.getAttributeValue("name"));
    }
    
    @SuppressWarnings("unchecked")
    private Collection<String> expectedParameters(final Element element) {
        final Collection<String> results = new HashSet<String>();
        final Element templateElement = element.getChild("template");
        if (templateElement != null) {
            for (Element parameterNameElement: (List<Element>) templateElement.getChildren("parameter-name")) {
                results.add(parameterNameElement.getTextTrim());
            }
        }
        return results;
    }

    /**
     * Finds the license with an id matching that referenced by the element.
     * @param element not null
     * @param licenses not null
     * @return not null
     */
    public License license(final Element element, final Map<String, License> licenses) {
        final String id = element.getAttributeValue("id");
        if (licenses.containsKey(id)) {
            return licenses.get(id);
        } else {
            throw new MissingIDException("license", element.getName(), id);
        }
    }
}
