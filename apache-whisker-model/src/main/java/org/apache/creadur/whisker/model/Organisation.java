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

import java.util.Map;

/**
 * 
 */
public class Organisation implements Comparable<Organisation> {

    private final String id;
    private final String name;
    private final String url;

    public Organisation(final String id, final String name, final String url) {
        super();
        this.id = id;
        this.name = name;
        this.url = url;
    }

    /**
     * @param organisationsById
     */
    public Organisation storeIn(
            final Map<String, Organisation> organisationsById) {
        organisationsById.put(this.id, this);
        return this;
    }

    public String getName() {
        return this.name;
    }

    public String getURL() {
        return this.url;
    }

    /**
     * @return
     */
    public String getId() {
        return this.id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }

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
        final Organisation other = (Organisation) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final Organisation other) {
        final int nameDifference = getName().compareTo(other.getName());
        return nameDifference == 0 ? getId().compareTo(other.getId())
                : nameDifference;
    }

    @Override
    public String toString() {
        return "Organisation [id=" + this.id + ", name=" + this.name + ", url="
                + this.url + "]";
    }
}
