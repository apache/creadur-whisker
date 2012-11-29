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
 * Builds configurations fluently.
 */
public class ConfigurationBuilder {

    /**
     * Creates a new builder.
     * @return not null
     */
    public static ConfigurationBuilder aConfiguration() {
        return new ConfigurationBuilder();
    }

    /**
     * Include source URLs in the License document?
     */
    private boolean includeSourceUrlsInLicense;

    /**
     * Create via factory method.
     */
    private ConfigurationBuilder() {
        includeSourceUrlsInLicense = true;
    }

    /**
     * Builds a configuration.
     * @return not null
     */
    public Configuration build() {
        return new Configuration(includeSourceUrlsInLicense);
    }

    /**
     * Set source URLs in license to true
     * when the configuration is built.
     * @return this, not null
     */
    public ConfigurationBuilder withSourceURLsInLicense() {
        includeSourceUrlsInLicense = true;
        return this;
    }

    /**
     * Set source URLs in license to false
     * when the configuration is built.
     * @return this, not null
     */
    public ConfigurationBuilder noSourceURLsInLicense() {
        includeSourceUrlsInLicense = false;
        return this;
    }

    /**
     * Adjusts the configuration.
     * @param licenseConfiguration not null
     * @return this builder, not null
     */
    public ConfigurationBuilder with(final LicenseConfiguration licenseConfiguration) {
        includeSourceUrlsInLicense = licenseConfiguration.includeSourceUrlsInLicense();
        return this;
    }

}
