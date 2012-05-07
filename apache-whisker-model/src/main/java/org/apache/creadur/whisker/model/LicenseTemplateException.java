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
 * 
 */
public class LicenseTemplateException extends Exception {

    private static final long serialVersionUID = -4085365930968672572L;

    /**
     * @param parameters
     * @return
     */
    public static LicenseTemplateException notLicenseTemplate(Map<String, String> parameters, String name) {
        return new LicenseTemplateException("This is not a templated license", parameters, name);
    }

    
    /**
     * @param string
     * @param parameters
     */
    public LicenseTemplateException(String message,
            Map<String, String> parameters, String name) {
        // TODO: improve reporting
        super(message);
    }


    /**
     * @param message
     * @param expectedParameters
     * @param actualParameters
     */
    public LicenseTemplateException(String message,
            Collection<String> expectedParameters,
            Collection<String> actualParameters) {
        // TODO improve reporting
        super(message);
    }


    /**
     * @param string
     * @param name
     */
    public LicenseTemplateException(String message, String name) {
        // TODO improve reporting
        super(message);
    }


    /**
     * @param expectedParameters
     * @param keySet
     * @return
     */
    public static LicenseTemplateException parameterMismatch(
            Collection<String> expectedParameters, Collection<String> actualParameters) {
        return new LicenseTemplateException("Parameter mismatch.", expectedParameters, actualParameters);
    }


    /**
     * @param name
     * @return
     */
    public static LicenseTemplateException duplicateParameterName(String name) {
        return new LicenseTemplateException("Duplicate parameter name.", name);
    }


}
