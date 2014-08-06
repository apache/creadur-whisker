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
package org.apache.creadur.whisker.app.analysis;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;


import org.apache.commons.lang3.tuple.Pair;
import org.apache.creadur.whisker.model.Resource;
import org.apache.creadur.whisker.model.ResourceNamesCollator;
import org.apache.creadur.whisker.model.Descriptor;
import org.apache.creadur.whisker.scan.Directory;

/**
 * Analyses licenses.
 */
public class LicenseAnalyst {

    /**
     * Maps issues.
     * @return not null
     */
    private static Map<ResourceDefinitionError,
                        Collection<ResourceDescription>> buildIssueMap() {
        final Map<ResourceDefinitionError,
                    Collection<ResourceDescription>> results =
                    new HashMap<ResourceDefinitionError,
                        Collection<ResourceDescription>>();
        for (final ResourceDefinitionError error:
                ResourceDefinitionError.values()) {
            initIssues(results, error);
        }
        return results;
    }

    /**
     * Builds an initial map.
     * @param results not null
     * @param error not null
     */
    private static void initIssues(
            final Map<
                ResourceDefinitionError,
                Collection<ResourceDescription>> results,
            ResourceDefinitionError error) {
        results.put(error, new TreeSet<ResourceDescription>());
    }

    /** Directories analysed. */
    private final Collection<Directory> directories;
    /** Maps resource errors to resources. */
    private final Map<ResourceDefinitionError,
                    Collection<ResourceDescription>> issues;

    /**
     * Constructs empty analyst.
     */
    public LicenseAnalyst() {
        this(null);
    }

    /**
     * Analyse the given directories.
     * @param directories not null
     */
    public LicenseAnalyst(final Collection<Directory> directories) {
        super();
        this.directories = directories;
        issues = buildIssueMap();
    }

    /**
     * Discover discrepancies between meta-data and source directories.
     * @param work not null
     * @return this, not null
     */
    public LicenseAnalyst analyse(final Descriptor work) {
        if (directories == null) {
            final ResourceNamesCollator collator =
                    new ResourceNamesCollator();
            work.traverse(collator);
            analyseDuplicates(collator);

            final ResourceSourceAuditor sourceAuditor =
                    new ResourceSourceAuditor();
            work.traverse(sourceAuditor);
            analyse(sourceAuditor);
        } else {
            for (final Directory directory: directories) {
                final ResourceNamesCollator collator =
                        new ResourceNamesCollator();
                work.traverseDirectory(collator, directory.getName());
                analyseLicenses(directory, collator);
                analyseDuplicates(collator);

                final ResourceSourceAuditor sourceAuditor = new
                        ResourceSourceAuditor();
                work.traverseDirectory(sourceAuditor, directory.getName());
                analyse(sourceAuditor);
            }
        }
        return this;
    }

    /**
     * Analyse the directories with this auditor.
     * @param sourceAuditor not null
     */
    private void analyse(final ResourceSourceAuditor sourceAuditor) {
        addIssues(sourceAuditor.getResourcesMissingSource(),
                ResourceDefinitionError.MISSING_SOURCE);
    }

    /**
     * Were any errors found?
     * @return true when the meta-data is valid,
     * false otherwise
     */
    public boolean isValid() {
        for (final ResourceDefinitionError error:
                ResourceDefinitionError.values()) {
            if (!getIssues(error).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks the descriptor against the source directories.
     * @param work not null
     * @return valid meta-data
     * @throws ResourceDefinitionException when issues are found
     */
    public Descriptor validate(final Descriptor work)
            throws ResourceDefinitionException {
        analyse(work);
        if (isValid()) {
            return work;
        } else {
            throw new ResourceDefinitionException(issues);
        }

    }

    /**
     * Discovers duplicates.
     * @param collator not null
     */
    private void analyseDuplicates(
            final ResourceNamesCollator collator) {
        addIssues(collator.getDuplicates(),
                ResourceDefinitionError.DUPLICATE);
    }

    /**
     * Adds the issues to the store.
     * @param resources not null
     * @param error not null
     */
    private void addIssues(
            final Collection<Pair<
                org.apache.creadur.whisker.model.WithinDirectory,
                Resource>> resources,
            final ResourceDefinitionError error) {
        for (Pair<
                org.apache.creadur.whisker.model.WithinDirectory,
                Resource> duplicate: resources) {
            getIssues(error).add(
                    new ResourceDescription(
                            duplicate.getLeft().getName(),
                            duplicate.getRight().getName()));
        }
    }

    /**
     * Checks for too many or few licenses.
     * @param directory not null
     * @param collator not null
     */
    private void analyseLicenses(final Directory directory,
            final ResourceNamesCollator collator) {
        analyseExtraLicenses(directory, collator);
        analyseMissingLicenses(directory, collator);
    }


    /**
     * Checks for to many licenses.
     * @param directory not null
     * @param collator not null
     */
    private void analyseExtraLicenses(final Directory directory,
            final ResourceNamesCollator collator) {
        final Collection<String> actualResources = directory.getContents();
        for (final String resourceLicense: collator.getNames()) {
            if (!actualResources.contains(resourceLicense)) {
                getExtraLicenses().add(
                        new ResourceDescription(
                                directory.getName(),
                                resourceLicense));
            }
        }
    }

    /**
     * Checks for too few licenses.
     * @param directory not null
     * @param collator not null
     */
    private void analyseMissingLicenses(final Directory directory,
            final ResourceNamesCollator collator) {
        final Collection<String> licensedResources = collator.getNames();
        for (final String actualResource: directory.getContents()) {
            if (!licensedResources.contains(actualResource)) {
                getMissingLicenses().add(
                        new ResourceDescription(
                                directory.getName(),
                                actualResource));
            }
        }
    }

    /**
     * Gets resources whose sources are missing.
     * @return not null, possibly empty
     */
    public Collection<ResourceDescription> getResourcesMissingSources() {
        return getIssues(ResourceDefinitionError.MISSING_SOURCE);
    }

    /**
     * Gets surplus licenses.
     * @return not null, possibly empty
     */
    public Collection<ResourceDescription> getExtraLicenses() {
        return getIssues(ResourceDefinitionError.EXTRA_LICENSE);
    }

    /**
     * Gets missing license.
     * @return not null, possibly empty
     */
    public Collection<ResourceDescription> getMissingLicenses() {
        return getIssues(ResourceDefinitionError.MISSING_LICENSE);
    }

    /**
     * Gets duplicate resources.
     * @return the duplicates
     */
    public Collection<ResourceDescription> getDuplicates() {
        return getIssues(ResourceDefinitionError.DUPLICATE);
    }

    /**
     * Gets issues by type.
     * @param ofType not null
     * @return issues of given type, not null, possibly empty
     */
    public Collection<ResourceDescription> getIssues(
            ResourceDefinitionError ofType) {
        return issues.get(ofType);
    }

    /**
     * Describes suitably for logging.
     * @return something suitable for logging
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "LicenseAnalyst [directories=" + directories + "]";
    }
}