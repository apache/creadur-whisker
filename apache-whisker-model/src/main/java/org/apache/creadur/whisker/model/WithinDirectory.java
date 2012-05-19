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
 * 
 */
public class WithinDirectory implements Comparable<WithinDirectory> {


    private final Collection<WithLicense> licenses;
    private final Collection<ByOrganisation> publicDomain;
    private final String name;
     
    /**
     * @param element
     */
    public WithinDirectory(final String name, final Collection<WithLicense> licenses, 
            Collection<ByOrganisation> publicDomain) {
        super();
        this.name = name;
        this.licenses = licenses;
        this.publicDomain = publicDomain;
    }
    
    /**
     * @return the publicDomain
     */
    public Collection<ByOrganisation> getPublicDomain() {
        return publicDomain;
    }

    public String getName() {
        return name;
    }
    
    public Collection<WithLicense> getLicenses() {
        return licenses;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return getName().hashCode();
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
        final WithinDirectory other = (WithinDirectory) obj;
        return getName().equals(other.getName());
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(WithinDirectory other) {
        return getName().compareTo(other.getName());
    }


    /**
     * @param directoryName
     * @return
     */
    public boolean isNamed(String directoryName) {
        return getName().equals(directoryName);
    }


    /**
     * @param visitor
     */
    public void accept(Visitor visitor) {
        if (visitor != null) {      
            visitor.visit(this);
            if (visitor.traversePublicDomain()) {
                for (final ByOrganisation organisation: getPublicDomain()) {
                    organisation.accept(visitor);
                }        
            }
    
            for (final WithLicense license: getLicenses()) {
                license.accept(visitor);
            }
        }
    }
    
    
}
