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
import java.util.Collections;

public class LicenseBuilderForTesting {

    private static final String DEFAULT_PRIMARY_TEXT = "License text for the primary license.";
    public static final String DEFAULT_PRIMARY_URL = "http://primary.example.org";
    public static final String DEFAULT_PRIMARY_NAME = "Primary License";
    public static final String DEFAULT_PRIMARY_ID = "primary.example.org";
    public static final String DEFAULT_NAME = "Example License";
    public static final String DEFAULT_URL = "http://example.org";
    public static final String DEFAULT_ID = "example.org";
    public static final String DEFAULT_LICENSE_TEXT = "This is the license text";

    public static License defaultPrimaryLicense() {
        return new LicenseBuilderForTesting()
            .withId(DEFAULT_PRIMARY_ID)
            .withName(DEFAULT_PRIMARY_NAME)
            .withUrl(DEFAULT_PRIMARY_URL)
            .withText(DEFAULT_PRIMARY_TEXT)
            .build();
    }

    boolean isSourceRequired = false;
    String baseText = DEFAULT_LICENSE_TEXT;
    Collection<String> expectedParameters = Collections.<String> emptyList();
    String id = DEFAULT_ID;
    String url = DEFAULT_URL;
    String name = DEFAULT_NAME;

    public License build() {
        return new License(isSourceRequired, baseText, expectedParameters, id, url, name);
    }

    public LicenseBuilderForTesting withId(String id) {
        this.id = id;
        return this;
    }

    public LicenseBuilderForTesting withUrl(String url) {
        this.url = url;
        return this;
    }

    public LicenseBuilderForTesting withName(String name) {
        this.name = name;
        return this;
    }

    public LicenseBuilderForTesting withText(String baseText) {
        this.baseText = baseText;
        return this;
    }
}
