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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.creadur.whisker.model.ByOrganisation;
import org.apache.creadur.whisker.model.License;
import org.apache.creadur.whisker.model.Organisation;
import org.apache.creadur.whisker.model.Resource;
import org.apache.creadur.whisker.model.WithLicense;
import org.apache.creadur.whisker.model.WithinDirectory;
import org.apache.creadur.whisker.model.Descriptor;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Builds a model from xml using JDOM.
 */
public class JDomBuilder {

	private static final String COPYRIGHT_NOTICE_NAME = "copyright-notice";
	/**
     * 
     */
	private static final String LICENSE_ELEMENT_NAME = "license";
	/**
     * 
     */
	private static final String PRIMARY_LICENSE_NAME = "primary-license";
	/** Names the element representing an organisation */
	private static final String ORGANISATION_ELEMENT_NAME = "organisation";
	/** Names the element representing a resource */
	private static final String RESOURCE_ELEMENT_NAME = "resource";

	/**
	 * Builds a resource.
	 * 
	 * @param element
	 *            not null
	 * @return built resource, not null
	 * @throws UnexpectedElementException
	 *             when element is not named 'resource'
	 */
	public Resource resource(Element element) throws UnexpectedElementException {
		if (RESOURCE_ELEMENT_NAME.equals(element.getName())) {
			return new Resource(StringUtils.trim(element
					.getAttributeValue("name")), StringUtils.trim(element
					.getAttributeValue("notice")), StringUtils.trim(element
					.getAttributeValue("source")));
		} else {
			throw unexpectedElementException(element, RESOURCE_ELEMENT_NAME);
		}
	}

	/**
	 * Builds a suitable exception when the element name is unexpected.
	 * 
	 * @param element
	 *            , not null
	 * @param expectedElement
	 *            , not null
	 * @return a suitable exception, not null
	 */
	private UnexpectedElementException unexpectedElementException(
			Element element, final String expectedElement) {
		return new UnexpectedElementException(expectedElement,
				element.getName());
	}

	/**
	 * Builds an organisation model from xml.
	 * 
	 * @param element
	 *            , not null
	 * @return {@link Organisation} not null
	 * @throws UnexpectedElementException
	 *             when element is not named 'organisation'
	 */
	public Organisation organisation(Element element)
			throws UnexpectedElementException {
		if (ORGANISATION_ELEMENT_NAME.equals(element.getName())) {
			return new Organisation(element.getAttributeValue("id"),
					element.getAttributeValue("name"),
					element.getAttributeValue("url"));
		} else {
			throw unexpectedElementException(element, ORGANISATION_ELEMENT_NAME);
		}
	}

	/**
	 * @param element
	 *            JDOM element to collect.
	 * @return collected resources.
	 */
	@SuppressWarnings("unchecked")
	public Collection<Resource> collectResources(Element element) {
		final Collection<Resource> resources = new TreeSet<Resource>();
		for (Element resourceElement : (List<Element>) element
				.getChildren("resource")) {
			resources.add(new JDomBuilder().resource(resourceElement));
		}
		return Collections.unmodifiableCollection(resources);
	}

	/**
	 * @return Finds the organisation linked by ID from the given element.
	 * @param element
	 *            modelled ByOrganisation, not null
	 * @param organisationsById
	 *            organisations identified, not null
	 * @throws MissingIDException
	 *             when the linked organisation is not found in the given map
	 */
	public Organisation organisation(final Element element,
			final Map<String, Organisation> organisationsById)
			throws MissingIDException {
		final String id = element.getAttributeValue("id");
		if (organisationsById.containsKey(id)) {
			return organisationsById.get(id);
		} else {
			throw new MissingIDException(ORGANISATION_ELEMENT_NAME,
					element.getName(), id);
		}
	}

	/**
	 * Builds a by-organisation model from xml.
	 * 
	 * @param element
	 *            not null
	 * @param organisation
	 *            not null
	 * @return not null
	 */
	public ByOrganisation byOrganisation(final Element element,
			final Organisation organisation) {
		return new ByOrganisation(organisation, collectResources(element));
	}

	/**
	 * Builds a by-organisation model from xml.
	 * 
	 * @param byOrganisation
	 *            not null
	 * @param organisationsById
	 *            not null
	 * @return not null
	 * @throws MissingIDException
	 *             when the linked organisation is not found in the given map
	 */
	public ByOrganisation byOrganisation(final Element byOrganisation,
			final Map<String, Organisation> organisationsById)
			throws MissingIDException {
		return byOrganisation(byOrganisation,
				organisation(byOrganisation, organisationsById));
	}

	/**
	 * Collects by-organisation children.
	 * 
	 * @param parent
	 *            not null
	 * @param map
	 *            not null
	 * @return unmodifiable set sort by natural order, not null
	 */
	@SuppressWarnings("unchecked")
	public SortedSet<ByOrganisation> collectByOrganisations(
			final Element parent, final Map<String, Organisation> map) {
		final SortedSet<ByOrganisation> results = new TreeSet<ByOrganisation>();
		if (parent != null) {
			for (final Element byOrgElement : (List<Element>) parent
					.getChildren("by-organisation")) {
				results.add(byOrganisation(byOrgElement, map));
			}
		}
		return Collections.unmodifiableSortedSet(results);
	}

	/**
	 * Builds a license model from xml.
	 * 
	 * @param element
	 *            not null
	 * @return not null
	 */
	public License license(Element element) {
		final Element text = element.getChild("text");
		return new License("yes".equalsIgnoreCase(element
				.getAttributeValue("requires-source")), text == null ? ""
				: text.getText(), expectedParameters(element),
				element.getAttributeValue("id"),
				element.getAttributeValue("url"),
				element.getAttributeValue("name"));
	}

	@SuppressWarnings("unchecked")
	private Collection<String> expectedParameters(final Element element) {
		final Collection<String> results = new HashSet<String>();
		final Element templateElement = element.getChild("template");
		if (templateElement != null) {
			for (Element parameterNameElement : (List<Element>) templateElement
					.getChildren("parameter-name")) {
				results.add(parameterNameElement.getTextTrim());
			}
		}
		return results;
	}

	/**
	 * Finds the license with an id matching that referenced by the element.
	 * 
	 * @param element
	 *            not null
	 * @param licenses
	 *            not null
	 * @return not null
	 * @throws MissingIDException
	 *             when referenced license isn't found in the collection
	 */
	public License license(final Element element,
			final Map<String, License> licenses) throws MissingIDException {
		final String id = element.getAttributeValue("id");
		if (licenses.containsKey(id)) {
			return licenses.get(id);
		} else {
			throw new MissingIDException(LICENSE_ELEMENT_NAME,
					element.getName(), id);
		}
	}

	/**
	 * @return Builds a with-license model from xml.
	 * @param element
	 *            not null
	 * @param licenses
	 *            not null
	 * @param organisations
	 *            not null
	 * 
	 * @throws MissingIDException
	 *             when referenced license isn't found in the collection
	 */
	public WithLicense withLicense(Element element,
			Map<String, License> licenses,
			Map<String, Organisation> organisations) throws MissingIDException {
		return new WithLicense(license(element, licenses),
				copyrightNotice(element), parameters(element),
				collectByOrganisations(element, organisations));
	}

	/**
	 * Extracts copyright notice content from with-license.
	 * 
	 * @param element
	 *            not null
	 * @return not null
	 */
	private String copyrightNotice(final Element element) {
		final String result;
		final Element copyrightNoticeElement = element
				.getChild(COPYRIGHT_NOTICE_NAME);
		if (copyrightNoticeElement == null) {
			result = null;
		} else {
			result = copyrightNoticeElement.getTextTrim();
		}
		return result;
	}

	/**
	 * Builds a list of parameter values by name.
	 * 
	 * @param element
	 *            not null
	 * @return parameter values indexed by value, not null
	 * @throws DuplicateElementException
	 *             when two parameters shared the same name
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> parameters(Element element)
			throws DuplicateElementException {
		final Map<String, String> results = new HashMap<String, String>();
		final Element licenseParametersElement = element
				.getChild("license-parameters");
		if (licenseParametersElement != null) {
			for (Element parameterElement : (List<Element>) licenseParametersElement
					.getChildren("parameter")) {
				final String name = parameterElement.getChild("name")
						.getTextTrim();
				if (results.containsKey(name)) {
					throw new DuplicateElementException("Duplicate parameter '"
							+ name + "'");
				}
				results.put(name, parameterElement.getChild("value")
						.getTextTrim());
			}
		}
		return results;
	}

	/**
	 * Collects child with-licenses.
	 * 
	 * @param licenses
	 *            not null
	 * @param organisations
	 *            not null
	 * @param parent
	 *            not null
	 * @return not null, possibly empty
	 */
	@SuppressWarnings("unchecked")
	public Collection<WithLicense> withLicenses(Map<String, License> licenses,
			Map<String, Organisation> organisations, Element parent) {
		final List<WithLicense> results = new ArrayList<WithLicense>();
		for (Element withLicenseElement : (List<Element>) parent
				.getChildren("with-license")) {
			results.add(new JDomBuilder().withLicense(withLicenseElement,
					licenses, organisations));
		}
		Collections.sort(results);
		return results;
	}

	/**
	 * Collects child organisations of public domain.
	 * 
	 * @param organisations
	 *            not null
	 * @param parent
	 *            not null
	 * @return not null, possibly null
	 */
	public Collection<ByOrganisation> publicDomain(
			final Map<String, Organisation> organisations, final Element parent) {
		return new JDomBuilder().collectByOrganisations(
				parent.getChild("public-domain"), organisations);
	}

	/**
	 * Builds a within directory model from XML.
	 * 
	 * @param element
	 *            not null
	 * @param licenses
	 *            not null
	 * @param organisations
	 *            not null
	 * @return not null
	 */
	public WithinDirectory withinDirectory(Element element,
			Map<String, License> licenses,
			Map<String, Organisation> organisations) {
		return new WithinDirectory(element.getAttributeValue("dir"),
				withLicenses(licenses, organisations, element), publicDomain(
						organisations, element));
	}

	/**
	 * Collects organisation definitions within document.
	 * 
	 * @param document
	 *            , not null
	 * @return organisations indexed by id, not null possibly empty
	 */
	public Map<String, Organisation> mapOrganisations(Document document) {
		final Map<String, Organisation> organisationsById = new HashMap<String, Organisation>();

		final Element childOrganisations = document.getRootElement().getChild(
				"organisations");
		if (childOrganisations != null) {
			@SuppressWarnings("unchecked")
			final List<Element> organisations = (List<Element>) childOrganisations
					.getChildren("organisation");
			for (final Element element : organisations) {
				new JDomBuilder().organisation(element).storeIn(
						organisationsById);
			}
		}
		return Collections.unmodifiableMap(organisationsById);
	}

	/**
	 * Collects license definitions within document.
	 * 
	 * @param document
	 *            , not null
	 * @return licenses, indexed by id, not null, possibly empty
	 */
	public Map<String, License> mapLicenses(Document document) {
		final Map<String, License> results = new HashMap<String, License>();
		final Element licensesChild = document.getRootElement().getChild(
				"licenses");
		if (licensesChild != null) {
			@SuppressWarnings("unchecked")
			final List<Element> children = (List<Element>) licensesChild
					.getChildren();
			for (final Element element : children) {
				new JDomBuilder().license(element).storeIn(results);
			}
		}
		return Collections.unmodifiableMap(results);

	}

	/**
	 * Finds the primary license for the given document from the given licenses.
	 * 
	 * @param document
	 *            not null
	 * @param licenses
	 *            not null
	 * @return not null
	 */
	public License primaryLicense(Document document,
			Map<String, License> licenses) {
		final String idAttributeValue = getPrimaryLicenseElement(document)
				.getAttributeValue("id");
		final License results = licenses.get(idAttributeValue);
		if (results == null) {
			throw new MissingIDException(LICENSE_ELEMENT_NAME,
					PRIMARY_LICENSE_NAME, idAttributeValue);
		}
		return results;
	}

	/**
	 * Gets the element representing the primary license.
	 * 
	 * @param document
	 *            not null
	 * @return not null
	 */
	private Element getPrimaryLicenseElement(final Document document) {
		return document.getRootElement().getChild(PRIMARY_LICENSE_NAME);
	}

	/**
	 * Gets the additional primary copyright notice from the document.
	 * 
	 * @param document
	 *            not null
	 * @return optional primary copyright notice, possibly null
	 */
	public String primaryCopyrightNotice(final Document document) {
		final String result;
		final Element copyrightElement = getPrimaryLicenseElement(document)
				.getChild(COPYRIGHT_NOTICE_NAME);
		if (copyrightElement == null) {
			result = null;
		} else {
			result = copyrightElement.getTextTrim();
		}
		return result;
	}

	/**
	 * Collects notices in the given documents.
	 * 
	 * @param document
	 *            , not null
	 * @return notices indexed by id, immutable, not null, possibly empty
	 */
	public Map<String, String> mapNotices(Document document) {
		final Map<String, String> results = new HashMap<String, String>();
		final Element noticesElement = document.getRootElement().getChild(
				"notices");
		if (noticesElement != null) {
			@SuppressWarnings("unchecked")
			final List<Element> children = (List<Element>) noticesElement
					.getChildren();
			for (final Element element : children) {
				results.put(element.getAttributeValue("id"),
						element.getTextTrim());
			}
		}
		return Collections.unmodifiableMap(results);

	}

	/**
	 * Retrieves the text of the primary notice.
	 * 
	 * @param document
	 *            , not null
	 * @return the text of the primary notice, or null when there is no primary
	 *         notice
	 */
	public String primaryNotice(Document document) {
		final String result;
		final Element primaryNoticeElement = document.getRootElement()
				.getChild("primary-notice");
		if (primaryNoticeElement == null) {
			result = null;
		} else {
			result = primaryNoticeElement.getText()
					.replace(
							"${year}",
							Integer.toString(Calendar.getInstance().get(
									Calendar.YEAR)));
		}
		return result;
	}

	/**
	 * Retrieves the ID of the primary organisation.
	 * 
	 * @param document
	 *            , not null
	 * @return the id of the primary organisation when set, otherwise null
	 */
	public String primaryOrganisationId(final Document document) {
		final String result;
		final Element primaryOrganisationElement = document.getRootElement()
				.getChild("primary-organisation");
		if (primaryOrganisationElement == null) {
			result = null;
		} else {
			result = primaryOrganisationElement.getAttributeValue("id");
		}
		return result;
	}

	private WithinDirectory directory(final Element element,
			final Map<String, License> licenses,
			final Map<String, Organisation> organisations) {
		return new JDomBuilder().withinDirectory(element, licenses,
				organisations);
	}

	/**
	 * Collects contents of the document.
	 * 
	 * @param document
	 *            not null
	 * @param licenses
	 *            not null
	 * @param organisations
	 *            not null
	 * @return not null, possibly empty
	 * @throws DuplicateElementException
	 *             when directory names are not unique
	 */
	public Collection<WithinDirectory> collectContents(final Document document,
			final Map<String, License> licenses,
			final Map<String, Organisation> organisations)
			throws DuplicateElementException {
		final Collection<WithinDirectory> results = new TreeSet<WithinDirectory>();
		@SuppressWarnings("unchecked")
		final List<Element> children = document.getRootElement().getChildren(
				"within");
		for (Element element : children) {
			boolean addedSuccessfully = results.add(directory(element,
					licenses, organisations));

			if (!addedSuccessfully) {
				throw new DuplicateElementException("Duplicate parameter '"
						+ element.getAttribute("dir").getValue() + "'");
			}
		}
		return results;
	}

	/**
	 * Builds work from the given document.
	 * 
	 * @param document
	 *            not null
	 * @return not null
	 */
	public Descriptor build(final Document document) {
		final Map<String, Organisation> organisations = mapOrganisations(document);
		final Map<String, License> licenses = mapLicenses(document);
		final Map<String, String> notices = mapNotices(document);
		final License primaryLicense = primaryLicense(document, licenses);
		final String primaryCopyrightNotice = primaryCopyrightNotice(document);
		final String primaryNotice = primaryNotice(document);
		final String primaryOrganisationId = primaryOrganisationId(document);
		final Collection<WithinDirectory> contents = collectContents(document,
				licenses, organisations);
		return new Descriptor(primaryLicense, primaryCopyrightNotice,
				primaryOrganisationId, primaryNotice, licenses, notices,
				organisations, contents);
	}

	public Descriptor build(final InputStream xmlStream) throws JDOMException,
			IOException {
		return build(new SAXBuilder().build(xmlStream));
	}
}
