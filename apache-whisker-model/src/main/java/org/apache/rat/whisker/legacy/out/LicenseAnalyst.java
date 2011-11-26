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
package org.apache.rat.whisker.legacy.out;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;


import org.apache.commons.lang3.tuple.Pair;
import org.apache.rat.whisker.legacy.scan.Directory;

/**
 * 
 */
public class LicenseAnalyst {
    
    /**
     * @return
     */
    private static Map<ResourceDefinitionError, Collection<ResourceDescription>> buildIssueMap() {
        final Map<ResourceDefinitionError, Collection<ResourceDescription>> results = new HashMap<ResourceDefinitionError, Collection<ResourceDescription>>();
        for (final ResourceDefinitionError error: ResourceDefinitionError.values()) {
            initIssues(results, error);
        }
        return results;
    }

    /**
     * @param results
     * @param error
     */
    private static void initIssues(
            final Map<ResourceDefinitionError, Collection<ResourceDescription>> results,
            ResourceDefinitionError error) {
        results.put(error, new TreeSet<ResourceDescription>());
    }

    
    private final Collection<Directory> directories;
    private final Map<ResourceDefinitionError, Collection<ResourceDescription>> issues;
    
    public LicenseAnalyst() {
        this(null);
    }
    
    /**
     * @param work
     * @param directories
     */
    public LicenseAnalyst(Collection<Directory> directories) {
        super();
        this.directories = directories;
        issues = buildIssueMap();
    }

    public LicenseAnalyst analyse(final Work work) {
        if (directories == null) {
            final ResourceNamesCollator collator = new ResourceNamesCollator();
            work.traverse(collator);
            analyseDuplicates(collator);
            
            final ResourceSourceAuditor sourceAuditor = new ResourceSourceAuditor();
            work.traverse(sourceAuditor);
            analyse(sourceAuditor);
        } else {
            for (final Directory directory: directories) {
                final ResourceNamesCollator collator = new ResourceNamesCollator();
                work.traverseDirectory(collator, directory.getName());
                analyseLicenses(directory, collator);
                analyseDuplicates(collator);
                
                final ResourceSourceAuditor sourceAuditor = new ResourceSourceAuditor();
                work.traverseDirectory(sourceAuditor, directory.getName());
                analyse(sourceAuditor);
            }
        }
        return this;
    }

    /**
     * @param sourceAuditor
     */
    private void analyse(ResourceSourceAuditor sourceAuditor) {
        addIssues(sourceAuditor.getResourcesMissingSource(), ResourceDefinitionError.MISSING_SOURCE);
    }

    public boolean isValid() {
        for (final ResourceDefinitionError error: ResourceDefinitionError.values()) {
            if (!getIssues(error).isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    public Work validate(final Work work) throws ResourceDefinitionException {
        analyse(work);
        if (isValid()) {
            return work;
        } else {
            throw new ResourceDefinitionException(issues);
        }
            
    }
    
    /**
     * @param collator
     */
    private void analyseDuplicates(final ResourceNamesCollator collator) {
        addIssues(collator.getDuplicates(), ResourceDefinitionError.DUPLICATE);
    }

    /**
     * @param resources
     * @param error
     */
    private void addIssues(
            Collection<Pair<org.apache.rat.whisker.legacy.out.WithinDirectory, Resource>> resources,
            ResourceDefinitionError error) {
        for (Pair<org.apache.rat.whisker.legacy.out.WithinDirectory, Resource> duplicate: resources) {
            getIssues(error).add(new ResourceDescription(duplicate.getLeft().getName(), duplicate.getRight().getName()));
        }
    }

    private void analyseLicenses(final Directory directory,
            final ResourceNamesCollator collator) {
        analyseExtraLicenses(directory, collator);
        analyseMissingLicenses(directory, collator);
    }

    
    /**
     * @param directory
     * @param collator
     */
    private void analyseExtraLicenses(final Directory directory,
            final ResourceNamesCollator collator) {
        final Collection<String> actualResources = directory.getContents();
        for (final String resourceLicense: collator.getNames()) {
            if (actualResources.contains(resourceLicense)) {
                // Fine
            } else {
                getExtraLicenses().add(new ResourceDescription(directory.getName(), resourceLicense));
            }
        }
    }
    
    /**
     * @param directory
     * @param collator
     */
    private void analyseMissingLicenses(final Directory directory,
            final ResourceNamesCollator collator) {
        final Collection<String> licensedResources = collator.getNames();
        for (final String actualResource: directory.getContents()) {
            if (licensedResources.contains(actualResource)) {
                // Fine
            } else {
                getMissingLicenses().add(new ResourceDescription(directory.getName(), actualResource));
            }
        }
    }


    public Collection<ResourceDescription> getResourcesMissingSources() {
        return getIssues(ResourceDefinitionError.MISSING_SOURCE);
    }

    
    
    public Collection<ResourceDescription> getExtraLicenses() {
        return getIssues(ResourceDefinitionError.EXTRA_LICENSE);
    }
    
    public Collection<ResourceDescription> getMissingLicenses() {
        return getIssues(ResourceDefinitionError.MISSING_LICENSE);
    }
    
    /**
     * @return the duplicates
     */
    public Collection<ResourceDescription> getDuplicates() {
        return getIssues(ResourceDefinitionError.DUPLICATE);
    }

    public Collection<ResourceDescription> getIssues(ResourceDefinitionError ofType) {
        return issues.get(ofType);
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "LicenseAnalyst [directories=" + directories + "]";
    }
    
    public static class ResourceDescription implements Comparable<ResourceDescription> {
        private final String directory;
        private final String resource;
        
        /**
         * @param directoryName
         * @param resourceName
         */
        public ResourceDescription(String directoryName, String resourceName) {
            super();
            this.directory = directoryName;
            this.resource = resourceName;
        }

        /**
         * @return the directoryName
         */
        public String getDirectory() {
            return directory;
        }

        /**
         * @return the resourceName
         */
        public String getResource() {
            return resource;
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((directory == null) ? 0 : directory.hashCode());
            result = prime * result
                    + ((resource == null) ? 0 : resource.hashCode());
            return result;
        }

        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            ResourceDescription other = (ResourceDescription) obj;
            if (directory == null) {
                if (other.directory != null)
                    return false;
            } else if (!directory.equals(other.directory))
                return false;
            if (resource == null) {
                if (other.resource != null)
                    return false;
            } else if (!resource.equals(other.resource))
                return false;
            return true;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "ResourceMissingLicense [directoryName=" + directory
                    + ", resourceName=" + resource + "]";
        }

        /**
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
        public int compareTo(ResourceDescription other) {
            final int result;
            final int compareOnDirectoryName = this.getDirectory().compareTo(other.getDirectory());
            if (compareOnDirectoryName == 0) {
                result = this.getResource().compareTo(other.getResource());
            } else {
                result = compareOnDirectoryName;
            }
            return result;
        }
        
        
    }
    
    public enum ResourceDefinitionError {
        MISSING_LICENSE("Missing license(s)"),
        EXTRA_LICENSE("Extra license(s)"),
        DUPLICATE("Duplicate resource(s)"),
        MISSING_SOURCE("Missing link to source ");
        
        private final String description;
        
        private ResourceDefinitionError (final String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
       
    public static final class ResourceDefinitionException extends Exception {

        private final static String message(final Map<ResourceDefinitionError, Collection<ResourceDescription>> issues) {
            final StringBuilder builder = new StringBuilder("Resources definitions are incorrect. ");
            for (ResourceDefinitionError error: issues.keySet()) {
                if (!issues.get(error).isEmpty()) {
                    builder.append(error.getDescription()).append(": ");
                    boolean firstTime = true;
                    for (final ResourceDescription description: issues.get(error)) {
                        if (firstTime) {
                            firstTime = false;
                        } else {
                            builder.append("; ");
                        }
                        builder.append(description.getResource()).append(" in ").append(description.getDirectory());
                    }
                    builder.append(". ");
                }
            }
            return builder.toString();
        }

        private static final long serialVersionUID = -455455829914484243L;
        
        private final Map<ResourceDefinitionError, Collection<ResourceDescription>> issues;
        
        /**
         * @param duplicates
         */
        public ResourceDefinitionException(Map<ResourceDefinitionError, Collection<ResourceDescription>> issues) {
            super(message(issues));
            this.issues = Collections.unmodifiableMap(issues);
        }

        /**
         * @return the issues
         */
        public Map<ResourceDefinitionError, Collection<ResourceDescription>> getIssues() {
            return issues;
        }
    }
}
