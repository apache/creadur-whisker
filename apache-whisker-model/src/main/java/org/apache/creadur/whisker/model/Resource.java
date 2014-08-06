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

/**
 * A resource expected in a software distribution.
 */
public class Resource implements Comparable<Resource>, ContentElement {

    /** Names this resource. */
    private final String name;
    /** Optional link to a notice for this resource. */
    private final String noticeId;
    /**
     * Optional describes how source may be obtained
     * for this resource.
     */
    private final String source;

    /**
     * Constructs a resource in a software distribution.
     * @param name not null
     * @param noticeId identifies the notice for this resource,
     * null when there is no NOTICE
     * @param source describes how source may be obtained,
     * null when this is not needed
     */
    public Resource(final String name, final String noticeId,
            final String source) {
        super();
        this.name = name;
        this.noticeId = noticeId;
        this.source = source;
    }

    /**
     * Gets the name for this resource
     * expected in a software distribution.
     * @return not null
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets an identifier for the optional NOTICE.
     * @return an identifier for the NOTICE,
     * or null when the resource has no NOTICE
     */
    public String getNoticeId() {
        return this.noticeId;
    }

    /**
     * Based on name.
     * @see java.lang.Object#hashCode()
     * @return hash code for the name
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }

    /**
     * Based on name.
     * @see java.lang.Object#equals(java.lang.Object)
     * @param obj possibly null
     * @return equality based on name
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
        final Resource other = (Resource) obj;
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    /**
     * Gets a description suitable for logging.
     * @see java.lang.Object#toString()
     * @return a description suitable for logging
     */
    @Override
    public String toString() {
        return "Resource [name=" + this.name + "]";
    }

    /**
     * Comparison happens based on name.
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     * @param other resource to compare to.
     * @return result of comparison based on name
     */
    public int compareTo(final Resource other) {
        return getName().compareTo(other.getName());
    }

    /**
     * Accepts a visitor.
     * @param visitor possibly null
     */
    public void accept(final Visitor visitor) {
        if (visitor != null && visitor.traverseResource()) {
            visitor.visit(this);
        }
    }

    /**
     * Gets a locator for the source.
     * @return a source locator, possibly null
     */
    public String getSource() {
        return this.source;
    }

    /**
     * Is this resource linked to source?
     * @return true when this resource has linked source,
     * false otherwise
     */
    public boolean hasSource() {
        return getSource() != null && !"".equals(getSource());
    }
}
