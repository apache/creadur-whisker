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
package org.apache.creadur.whisker.app.analysis;

/**
 * Enumerates modes of definitional error.
 */
public enum ResourceDefinitionError {
    /** Expected license definition is missing. */
    MISSING_LICENSE("Missing license(s)"),
    /** Duplicate licenses */
    EXTRA_LICENSE("Extra license(s)"),
    /** Duplicate resources.  */
    DUPLICATE("Duplicate resource(s)"),
    /** Links to source is missing. */
    MISSING_SOURCE("Missing link to source ");

    /** Describes this error suitable for display. */
    private final String description;

    /**
     * Constructs an error with the given description.
     * @param description not null
     */
    private ResourceDefinitionError(final String description) {
        this.description = description;
    }

    /**
     * Gets a description suitable for display.
     * @return not null
     */
    public final String getDescription() {
        return description;
    }
}
