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
 * Enumerates
 */
public enum LicenseConfiguration {
    /** Include source URLs in the LICENSE (as well as the NOTICE) */
    INCLUDE_SOURCE_URLS,
    /** The essentials only. */
    MINIMAL;

    /**
     * Default to include source URLs.
     */
    public static LicenseConfiguration DEFAULT_LICENSE_CONFIGURATION = INCLUDE_SOURCE_URLS;

    /**
     * Replaces null with DEFAULT_LICENSE_CONFIGURATION.
     * @param configuration possibly null
     * @return not null
     */
    public static LicenseConfiguration notNull(final LicenseConfiguration configuration) {
        final LicenseConfiguration result;
        if (configuration == null) {
            result = DEFAULT_LICENSE_CONFIGURATION;
        } else {
            result = configuration;
        }
        return result;
    }

    /**
     * Should source URLs be included in the LICENSE?
     * @return true when source URLs should be included,
     * false otherwise
     */
    public boolean includeSourceUrlsInLicense() {
        return this == INCLUDE_SOURCE_URLS;
    }
}
