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
package org.apache.rat.whisker.out;

import java.util.Map;

import org.jdom.Element;

/**
 * 
 */
public class Organisation implements Comparable<Organisation> {

    private final Element element;

    /**
     * @param element
     */
    public Organisation(Element element) {
        super();
        this.element = element;
    }

    /**
     * @param organisationsById
     */
    public Organisation storeIn(Map<String, Organisation> organisationsById) {
        organisationsById.put(element.getAttributeValue("id"), this);
        return this;
    }
    
    public String getName() {
        return element.getAttributeValue("name");
    }

    public String getURL() {
        return element.getAttributeValue("url");
    }

    /**
     * @return
     */
    public String getId() {
        return element.getAttributeValue("id");
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((element == null) ? 0 : element.hashCode());
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
        Organisation other = (Organisation) obj;
        return getId().equals(other.getId());
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Organisation other) {
        return getName().compareTo(other.getName());
    }
    
    
}
