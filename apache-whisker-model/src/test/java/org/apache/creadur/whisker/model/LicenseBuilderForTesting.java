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

    private static final String DEFAULT_NAME = "Example License";
    private static final String DEFAULT_URL = "http://example.org";
    private static final String DEFAULT_ID = "example.org";
    private static final String DEFAULT_LICENSE_TEXT = "This is the license text";

    boolean isSourceRequired = false;
    String baseText = DEFAULT_LICENSE_TEXT;
    Collection<String> expectedParameters = Collections.<String> emptyList();
    String id = DEFAULT_ID;
    String url = DEFAULT_URL;
    String name = DEFAULT_NAME;

    public License build() {
        return new License(isSourceRequired, baseText, expectedParameters, id, url, name);
    }
}
