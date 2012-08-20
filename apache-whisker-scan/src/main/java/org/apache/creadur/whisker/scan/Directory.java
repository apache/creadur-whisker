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
package org.apache.creadur.whisker.scan;

import java.util.Set;
import java.util.TreeSet;

/**
 * Describes a directory.
 */
public class Directory implements Comparable<Directory> {
    /** Names this directory. */
    private String name;
    /** Names resources contained. */
    private Set<String> contents = new TreeSet<String>();


    /**
     * Gets the directory name.
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * Sets the directory name.
     * @param name the name to set
     * @return this, not null
     */
    public Directory setName(final String name) {
        this.name = name;
        return this;
    }

    /**
     * Gets the directory contents.
     * @return the contents
     */
    public Set<String> getContents() {
        return contents;
    }

    /**
     * Sets the directory contents.
     * @param contents the contents to set
     */
    public void setContents(final Set<String> contents) {
        this.contents = contents;
    }

    /**
     * @return the hash code
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /**
     * Equal if and only if names are equal.
     * @param obj possibly null
     * @return true when equal
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Directory other = (Directory) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    /**
     * Suitable for logging.
     * @return not null
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Directory [name=" + name + "]";
    }

    /**
     * Registers a contained resource.
     * @param name not null
     */
    public void addResource(final String name) {
        contents.add(name);
    }

    /**
     * Natural comparison based on name.
     * @param other another directory
     * @return comparison
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final Directory other) {
        if (other == null) {
            return -1;
        }
        return this.name.compareTo(other.name);
    }
}
