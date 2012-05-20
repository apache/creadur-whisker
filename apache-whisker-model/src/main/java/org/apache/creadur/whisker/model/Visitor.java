/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
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
 * Visits element in the model.
 */
public abstract class Visitor {

    /**
     * Tunes traversal, allowing public domain resources
     * to be ignored.
     * @return true when public domain resources should
     * be traversed, false otherwise
     */
    public boolean traversePublicDomain() {
        return true;
    }

    /**
     * Tunes traversal, allowing a shallow traversal
     * without {@link WithLicense} elements.
     *
     * @return true when {@link WithLicense} elements
     * should be stepped over, false when they should
     * be included
     */
    public boolean traverseWithLicense() {
        return true;
    }

    /**
     * Tunes traversal, allowing a moderate traversal
     * without {@link ByOrganisation} elements.
     * @return true when {@link ByOrganisation} elements
     * should be stepped over, false when they should
     * be included
     */
    public boolean traverseByOrganisation() {
        return true;
    }

    /**
     * Tunes traversal, allowing {@link Resource}
     * elements to be ignored.
     * @return true when {@link Resource} elements
     * should be stepped over, false when they should
     * be included
     */
    public boolean traverseResource() {
        return true;
    }

    /**
     * Visits {@link WithinDirectory}.
     * @param directory not null
     */
    public void visit(final WithinDirectory directory) {
    };

    /**
     * Visits {@link WithLicense}.
     * @param license not null
     */
    public void visit(final WithLicense license) {
    };

    /**
     * Visits {@link ByOrganisation}.
     * @param byOrganisation not null
     */
    public void visit(final ByOrganisation byOrganisation) {
    };

    /**
     * Visits {@link Resource}.
     * @param resource not null
     */
    public void visit(final Resource resource) {
    };
}
