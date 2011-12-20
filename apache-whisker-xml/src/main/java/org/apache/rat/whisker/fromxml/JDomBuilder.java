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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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
import org.apache.rat.whisker.model.WithLicense;
import org.apache.rat.whisker.model.WithinDirectory;
import org.jdom.Document;
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
     * @throws MissingIDException when referenced license isn't found in the collection
     */
    public License license(final Element element, final Map<String, License> licenses) throws MissingIDException {
        final String id = element.getAttributeValue("id");
        if (licenses.containsKey(id)) {
            return licenses.get(id);
        } else {
            throw new MissingIDException("license", element.getName(), id);
        }
    }

    /**
     * Builds a with-license model from xml.
     * @param element not null
     * @param licenses not null
     * @param organisations not null
     * @return
     * @throws MissingIDException when referenced license isn't found in the collection
     */
    public WithLicense withLicense(Element element,
            Map<String, License> licenses,
            Map<String, Organisation> organisations) throws MissingIDException  {
        return new WithLicense(license(element, licenses), copyrightNotice(element), 
                parameters(element), collectByOrganisations(element, organisations));
    }
    
    /**
     * Extracts copyright notice content from with-license.
     * @param element not null
     * @return not null
     */
    private String copyrightNotice(final Element element) {
        final String result;
        final Element copyrightNoticeElement = element.getChild("copyright-notice");
        if (copyrightNoticeElement == null) {
            result = null;
        } else {
            result = copyrightNoticeElement.getTextTrim();
        }
        return result;
    }

    /**
     * Builds a list of parameter values by name.
     * @param element not null
     * @return parameter values indexed by value, not null
     * @throws DuplicateElementException when two parameters shared the same name
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> parameters(Element element) throws DuplicateElementException {
        final Map<String, String> results = new HashMap<String, String>();
        final Element licenseParametersElement = element.getChild("license-parameters");
        if (licenseParametersElement != null) {
            for (Element parameterElement: (List<Element>) licenseParametersElement.getChildren("parameter")) {
                final String name = parameterElement.getChild("name").getTextTrim();
                if (results.containsKey(name)) {
                    throw new DuplicateElementException("Duplicate parameter '" + name + "'");
                }
                results.put(name, 
                        parameterElement.getChild("value").getTextTrim());
            }   
        }
        return results;
    }

    /**
     * Collects child with-licenses.
     * @param licenses not null
     * @param organisations not null
     * @param parent not null
     * @return not null, possibly empty
     */
    @SuppressWarnings("unchecked")
    public Collection<WithLicense> withLicenses(Map<String, License> licenses,
            Map<String, Organisation> organisations, Element parent) {
        final List<WithLicense> results = new ArrayList<WithLicense>();
        for (Element withLicenseElement: (List<Element>) parent.getChildren("with-license")) {
            results.add(new JDomBuilder().withLicense(withLicenseElement, licenses, organisations));
        }
        Collections.sort(results);
        return results;
    }

    /**
     * Collects child organisations of public domain.
     * @param organisations not null
     * @param parent not null
     * @return not null, possibly null
     */
    public Collection<ByOrganisation> publicDomain(
            final Map<String, Organisation> organisations, final Element parent) {
        return new JDomBuilder().collectByOrganisations(parent.getChild("public-domain"), organisations);
    }

    /**
     * Builds a within directory model from XML.
     * @param element not null
     * @param licenses not null
     * @param organisations not null
     * @return not null
     */
    public WithinDirectory withinDirectory(Element element,
            Map<String, License> licenses,
            Map<String, Organisation> organisations) {
        return new WithinDirectory(element.getAttributeValue("dir"), 
                withLicenses(licenses, organisations, element), publicDomain(organisations, element));
    }

    /**
     * Collection organisation definitions within document.
     * @param document, not null
     * @return organisations indexed by id, not null possibly empty
     */
    public Map<String, Organisation> mapOrganisations(Document document) {
        final Map<String, Organisation> organisationsById = new HashMap<String, Organisation>();
        
        final Element childOrganisations = document.getRootElement().getChild("organisations");
        if (childOrganisations != null) {
            @SuppressWarnings("unchecked")
            final List<Element> organisations = (List<Element>) childOrganisations.getChildren("organisation");
            for (final Element element: organisations) {
                new JDomBuilder().organisation(element).storeIn(organisationsById);
            }
        }
        return Collections.unmodifiableMap(organisationsById);
    }

}
