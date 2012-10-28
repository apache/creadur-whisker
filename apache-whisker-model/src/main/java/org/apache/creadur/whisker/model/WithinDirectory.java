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
 * Links resources expected within a directory in the distribution
 * to licensing meta-data.
 */
public class WithinDirectory implements Comparable<WithinDirectory>, ContentElement {

    /** Resources grouped by license applicable. */
    private final Collection<WithLicense> licenses;
    /** Public domain resources, grouped by responsible organisation. */
    private final Collection<ByOrganisation> publicDomain;
    /** The directory name. */
    private final String name;

    /**
     * Constructs a description of a directory
     * @param name directory name, not null
     * @param licenses resources contained,
     * grouped by license applicable, not null
     * @param publicDomain resources in the public domain,
     * grouped by responsible organisation
     */
    public WithinDirectory(final String name,
            final Collection<WithLicense> licenses,
            final Collection<ByOrganisation> publicDomain) {
        super();
        this.name = name;
        this.licenses = licenses;
        this.publicDomain = publicDomain;
    }

    /**
     * Gets resources in the public domain,
     * grouped by the organisation responsible.
     * @return not null
     */
    public Collection<ByOrganisation> getPublicDomain() {
        return this.publicDomain;
    }

    /**
     * Gets the name of the directory described.
     * @return not null
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets resources contained,
     * grouped by license applicable.
     * @return not null
     */
    public Collection<WithLicense> getLicenses() {
        return this.licenses;
    }

    /**
     * Based on name.
     * @see java.lang.Object#hashCode()
     * @return hash code
     */
    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    /**
     * Based on directory name.
     * @see java.lang.Object#equals(java.lang.Object)
     * @param obj possibly null
     * @return true when directory names are equal,
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
        final WithinDirectory other = (WithinDirectory) obj;
        return getName().equals(other.getName());
    }

    /**
     * Based on name.
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     * @return based on name
     */
    public int compareTo(final WithinDirectory other) {
        return getName().compareTo(other.getName());
    }

    /**
     * Does the directory described have the given name?
     * @param directoryName not null
     * @return true when the name match that of this directory,
     * false otherwise
     */
    public boolean isNamed(final String directoryName) {
        return getName().equals(directoryName);
    }

    /**
     * Accepts a visitor.
     * @param visitor possibly null
     */
    public void accept(final Visitor visitor) {
        if (visitor != null) {
            visitor.visit(this);
            if (visitor.traversePublicDomain()) {
                for (final ByOrganisation organisation : getPublicDomain()) {
                    organisation.accept(visitor);
                }
            }

            for (final ContentElement license : getLicenses()) {
                license.accept(visitor);
            }
        }
    }
}
