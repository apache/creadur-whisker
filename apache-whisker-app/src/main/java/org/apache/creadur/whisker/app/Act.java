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
package org.apache.creadur.whisker.app;

/**
 * An operation to be performed by Whisker.
 */
public enum Act {

    /** Generate legal documentation. */
    GENERATE(false),
    /** Validate contents against meta-data. */
    AUDIT(true),
    /** Writes report on directories. */
    REPORT(true),
    /** Outlines the directory structure. */
    SKELETON(true);

    /** Is the source required to perform this operation? */
    private final boolean isSourceRequired;

    /**
     * Constructs an instance.
     * @param isSourceRequired true when source is require,
     * false otherwise
     */
    private Act(final boolean isSourceRequired) {
        this.isSourceRequired = isSourceRequired;
    }

    /**
     * Is source required for this operation?
     * @return true when the source is required,
     * false otherwise
     */
    public boolean isSourceRequired() {
        return isSourceRequired;
    }
}
