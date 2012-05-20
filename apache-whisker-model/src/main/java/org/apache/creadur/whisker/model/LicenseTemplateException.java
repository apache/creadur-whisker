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

    /**
     * Builds an instance.
     * @param parameters not null
     * @param name not null
     * @return not null 
     */
    public static LicenseTemplateException notLicenseTemplate(
            final Map<String, String> parameters, final String name) {
        return new LicenseTemplateException("This is not a templated license",
                parameters, name);
    }

    /**
     * Constructs an instance.
     * @param message not null
     * @param parameters parameters passed
     * @param name license name
     */
    public LicenseTemplateException(final String message,
            final Map<String, String> parameters, final String name) {
        // TODO: improve reporting
        super(message);
    }

    /**
     * Constructs an instance.
     * @param message not null
     * @param expectedParameters not null
     * @param actualParameters not null
     */
    public LicenseTemplateException(final String message,
            final Collection<String> expectedParameters,
            final Collection<String> actualParameters) {
        // TODO improve reporting
        super(message);
    }

    /**
     * Constructs an instance
     * @param message not null
     * @param name not null
     */
    public LicenseTemplateException(final String message, final String name) {
        // TODO improve reporting
        super(message);
    }

    /**
     * Builds an exception indicating that parameter passed
     * do not fulfill expectations.
     * @param expectedParameters not null
     * @param actualParameters not null
     * @return not null
     */
    public static LicenseTemplateException parameterMismatch(
            final Collection<String> expectedParameters,
            final Collection<String> actualParameters) {
        return new LicenseTemplateException("Parameter mismatch.",
                expectedParameters, actualParameters);
    }

    /**
     * Builds an exception indicating that duplicate parameter
     * names have been passed.
     * @param name names the parameter duplicated, not null
     * @return not null
     */
    public static LicenseTemplateException duplicateParameterName(
            final String name) {
        return new LicenseTemplateException("Duplicate parameter name.", name);
    }

}