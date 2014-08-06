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
package org.apache.creadur.whisker.out.velocity;

import org.apache.commons.lang.StringUtils;
import org.apache.creadur.whisker.app.Configuration;
import org.apache.creadur.whisker.model.ByOrganisation;
import org.apache.creadur.whisker.model.Descriptor;
import org.apache.creadur.whisker.model.Resource;
import org.apache.creadur.whisker.model.WithLicense;

/**
 * Factors out rendering logic from template.
 */
public class RenderingHelper {

    /**
     * The work being rendered, not null
     */
    private final Descriptor work;

    /**
     * Configuration for the rendering.
     */
    private final Configuration configuration;

    /**
     * Constructs a helper for the given work and configuration.
     * @param work not null
     * @param configuration not null
     */
    public RenderingHelper(final Descriptor work, final Configuration configuration) {
        super();
        this.work = work;
        this.configuration = configuration;
    }

    /**
     * Should resources with the given license by
     * the given organisation be rendered?
     * @param organisation not null
     * @param license not null
     * @return true when resources should be rendered,
     * false otherwise
     */
    public boolean renderResources(final ByOrganisation organisation,
            final WithLicense license) {
        return isNot(
                primary(organisation)) ||
                isNot(primary(license)) ||
                license.hasCopyrightNotice();
    }

    /**
     * Is this license the primary license for the work?
     * @param license not null
     * @return true when this is the primary license
     * for the work, false otherwise
     */
    private boolean primary(final WithLicense license) {
        return work.isPrimary(license.getLicense());
    }

    /**
     * Is this the primary organisation?
     * @param organisation true when this is the primary license,
     * false otherwise
     * @return
     */
    private boolean primary(final ByOrganisation organisation) {
        return work.isPrimary(organisation);
    }

    /**
     * Negates claim.
     * @param claim to be negated
     * @return true when claim is false,
     * false when true
     */
    public boolean isNot(boolean claim) {
        return !claim;
    }

    /**
     * Renders the resource source URL
     * based on configuration setting suitable
     * for the license file.
     * @param resource not null
     * @return not null, possible empty string
     */
    public String sourceUrl(final Resource resource) {
        final String result;
        final String source = resource.getSource();
        if (StringUtils.isBlank(source) || isNot(configuration.includeSourceURLsInLicense())) {
            result = "";
        } else {
            result = " from " + source;
        }
        return result;
    }
}