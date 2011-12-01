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
package org.apache.rat.whisker.model;

import java.util.Collection;

/**
 * 
 */
public class ByOrganisation implements Comparable<ByOrganisation> {
    
    private final Organisation organisation;
    private final Collection<Resource> resources;
    
    
    /**
     * @param resources not null
     * @param organisation not null
     */
    public ByOrganisation(final Organisation organisation, final Collection<Resource> resources) {
        super();
        this.organisation = organisation;
        this.resources = resources;
    }

    public String getName() {
        return organisation.getName();
    }
    
    public String getURL() {
        return organisation.getURL();
    }
    
    public String getId() {
        return organisation.getId();
    }
   
    public Collection<Resource> getResources() {
        return resources;
    }
    
    /**
     * @return the organisation
     */
    public Organisation getOrganisation() {
        return organisation;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((organisation == null) ? 0 : organisation.hashCode());
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
        ByOrganisation other = (ByOrganisation) obj;
        if (organisation == null) {
            if (other.organisation != null)
                return false;
        } else if (!organisation.equals(other.organisation))
            return false;
        return true;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(ByOrganisation other) {
        return organisation.compareTo(other.getOrganisation());
    }

    /**
     * @param visitor
     */
    public void accept(Visitor visitor) {
        if (visitor != null && visitor.traverseByOrganisation()) {
            visitor.visit(this);
            for (final Resource resource: getResources()) {
                resource.accept(visitor);
            }
        }
    }

    @Override
    public String toString() {
        return "ByOrganisation [organisation=" + organisation + ", resources="
                + resources + "]";
    }
}
