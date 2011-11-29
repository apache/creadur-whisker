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

import org.apache.commons.lang.StringUtils;
import org.jdom.Element;

/**
 * 
 */
public class Resource implements Comparable<Resource>{

    private final String name;
    private final String noticeId;
    private final String source;

    public Resource(final Element element) {
        this(StringUtils.trim(element.getAttributeValue("name")), 
                StringUtils.trim(element.getAttributeValue("notice")),
                        StringUtils.trim(element.getAttributeValue("source")));
    }
    
    /**
     * @param name
     */
    public Resource(String name, final String noticeId, final String source) {
        super();
        this.name = name;
        this.noticeId = noticeId;
        this.source = source;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the noticeId
     */
    public String getNoticeId() {
        return noticeId;
    }

    /**
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
        Resource other = (Resource) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Resource [name=" + name + "]";
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Resource other) {
        return getName().compareTo(other.getName());
    }

    /**
     * @param visitor
     */
    public void accept(Visitor visitor) {
        if (visitor != null && visitor.traverseResource()) {
            visitor.visit(this);
        }
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @return
     */
    public boolean hasSource() {
        return getSource() != null && !"".equals(getSource());
    }
}
