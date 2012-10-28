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
package org.apache.creadur.whisker.model;

import java.util.Collection;

/**
 * Relates the responsible group or individual to one
 * or more resources.
 */
public class ByOrganisation implements Comparable<ByOrganisation>, ContentElement {

    /** The responsible group or individual. */
    private final Organisation organisation;
    /** The resources for which the group or individual is responsible. */
    private final Collection<Resource> resources;

    /**
     * Links an individual or group
     * to the resources for which they have responsibility.
     * @param resources
     *            not null
     * @param organisation
     *            not null
     */
    public ByOrganisation(final Organisation organisation,
            final Collection<Resource> resources) {
        super();
        this.organisation = organisation;
        this.resources = resources;
    }

    /**
     * Gets the name of the individual or group responsible.
     * @return not null
     */
    public String getName() {
        return this.organisation.getName();
    }

    /**
     * Gets the primary URL for the individual or group responsible.
     * @return not null
     */
    public String getURL() {
        return this.organisation.getURL();
    }

    /**
     * Gets the primary identifier for the individual or group responsible.
     * @return not null
     */
    public String getId() {
        return this.organisation.getId();
    }

    /**
     * Gets the resource for which the linked individual or group is responsible.
     * @return not null, possibly empty
     */
    public Collection<Resource> getResources() {
        return this.resources;
    }

    /**
     * Gets the organisation representing the individual or group responible
     * for the linked resources.
     * @return the organisation , not ull
     */
    public Organisation getOrganisation() {
        return this.organisation;
    }

    /**
     * Based on organisation.
     * @see java.lang.Object#hashCode()
     * @return hash based on organisation
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((this.organisation == null) ? 0 : this.organisation
                        .hashCode());
        return result;
    }

    /**
     * Equal iff organisations are equal.
     * @see java.lang.Object#equals(java.lang.Object)
     * @param obj possibly null
     * @return true when organisations are equal,
     * false otherwise
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
        final ByOrganisation other = (ByOrganisation) obj;
        if (this.organisation == null) {
            if (other.organisation != null) {
                return false;
            }
        } else if (!this.organisation.equals(other.organisation)) {
            return false;
        }
        return true;
    }

    /**
     * Delegates to organisation.
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     * @param other possibly null
     * @return {@link Organisation#compareTo(Organisation)}
     */
    public int compareTo(final ByOrganisation other) {
        return this.organisation.compareTo(other.getOrganisation());
    }

    /**
     * Accepts a visitor.
     * @param visitor possibly null
     */
    public void accept(final Visitor visitor) {
        if (visitor != null && visitor.traverseByOrganisation()) {
            visitor.visit(this);
            for (final Resource resource : getResources()) {
                resource.accept(visitor);
            }
        }
    }

    /**
     * Describes object suitably for logging.
     * @return something suitable for logging
     */
    @Override
    public String toString() {
        return "ByOrganisation [organisation=" + this.organisation
                + ", resources=" + this.resources + "]";
    }
}
