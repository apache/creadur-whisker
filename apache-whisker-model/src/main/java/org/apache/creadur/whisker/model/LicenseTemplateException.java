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

import java.util.Collection;
import java.util.Map;

/**
 * Indicates that generating an instance of a license
 * from the template (for the license family) has failed.
 */
public class LicenseTemplateException extends Exception {

    /** Exception are serializable, so provide a SUID. */
    private static final long serialVersionUID = -4085365930968672572L;
    /** Names the erroneous license */
    private final String licenseName;

    /**
     * Builds an exception indicating that parameter passed
     * do not fulfill expectations.
     * @param expectedParameters not null
     * @param actualParameters not null
     * @param licenseName not null
     * @return not null
     */
    public static LicenseTemplateException parameterMismatch(
            final Collection<String> expectedParameters,
            final Collection<String> actualParameters,
            final String licenseName) {
        final StringBuilder message = new StringBuilder("Parameter mismatch for license '");
        message.append(licenseName);
        message.append('.');
        explainDiffs(expectedParameters, actualParameters, message, " Missing parameters: ");
        explainDiffs(actualParameters, expectedParameters, message, " Unexpected parameters: ");
        return new LicenseTemplateException(message.toString(), licenseName);
    }

    /**
     * Explains the differences between the sample and the base.
     * @param sample not null
     * @param base not null
     * @param message not null
     * @param preamble not null
     */
    private static void explainDiffs(final Collection<String> sample,
            final Collection<String> base, final StringBuilder message,
            final String preamble) {
        boolean first = true;
        for (final String expected:sample) {
            if (!base.contains(expected)) {
                if (first) {
                    first = false;
                    message.append(preamble);
                } else {
                    message.append(", ");
                }
                message.append(expected);
            }
        }
        if (!first) {
            message.append('.');
        }
    }

    
    /**
     * Builds an instance.
     * @param parameters not null
     * @param licenseName not null
     * @return not null 
     */
    public static LicenseTemplateException notLicenseTemplate(
            final Map<String, String> parameters, final String licenseName) {
        final StringBuilder message = new StringBuilder("'");
        message.append(licenseName);
        message.append("' is not a templated license but parameters were set (");
        boolean first = true;
        for (final String name:parameters.keySet()) {
            if (first) {
                first = false;
            } else {
                message.append(", ");
            }
            message.append(name);
        }
        message.append(")");
        return new LicenseTemplateException(message.toString(), licenseName);
    }

    /**
     * Constructs an instance.
     * @param message not null
     * @param parameters parameters passed
     * @param licenseName license name, not null
     */
    private LicenseTemplateException(final String message, final String licenseName) {
        super(message);
        this.licenseName = licenseName;
    }

    /**
     * Gets the name of the erroneous license.
     * @return license name, not null
     */
    public String getLicenseName() {
        return licenseName;
    }

    
}