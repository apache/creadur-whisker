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

/**
 * Describes a resource in a software distribution.
 */
public class ResourceDescription implements Comparable<ResourceDescription> {
    /** Containing directory. */
    private final String directory;
    /** Resource name. */
    private final String resource;

    /**
     * Constructs a description of the given resource.
     * @param directoryName not null
     * @param resourceName not null
     */
    public ResourceDescription(
            final String directoryName, final String resourceName) {
        super();
        this.directory = directoryName;
        this.resource = resourceName;
    }

    /**
     * Gets the name of the containing directory.
     * @return the directoryName, not null
     */
    public final String getDirectory() {
        return directory;
    }

    /**
     * Gets the resource name.
     * @return the resourceName, not null
     */
    public final String getResource() {
        return resource;
    }

    /**
     * Hash code compatible with equals.
     * @return a suitable hash code
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
     * Equal means identical name and directory.
     * @param obj object to be compared against
     * @return true when equal
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ResourceDescription other = (ResourceDescription) obj;
        if (directory == null) {
            if (other.directory != null) {
                return false;
            }
        } else if (!directory.equals(other.directory)) {
            return false;
        }
        if (resource == null) {
            if (other.resource != null) {
                return false;
            }
        } else if (!resource.equals(other.resource)) {
            return false;
        }
        return true;
    }

    /**
     * Suitable for logging.
     * @return not null
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ResourceMissingLicense [directoryName=" + directory
                + ", resourceName=" + resource + "]";
    }

    /**
     * Natural comparison is directory name, then resource name.
     * @param other possibly null
     * @return numeric comparison
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final ResourceDescription other) {
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