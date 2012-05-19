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

public class ResourceDescription implements Comparable<ResourceDescription> {
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