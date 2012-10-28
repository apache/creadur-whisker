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

public class OrganisationBuilderForTesting {

    public static final String DEFAULT_ORG_URL = "http://thirdparty.org";
    public static final String DEFAULT_ORG_NAME = "thirdparty.org";
    public static final String DEFAULT_ORG_ID = "third-party";
    public static final String DEFAULT_PRIMARY_ORG_URL = "http://primary.org";
    public static final String DEFAULT_PRIMARY_ORG_NAME = "primary organisation";
    public static final String DEFAULT_PRIMARY_ORG_ID = "primary.org";

    public static Organisation defaultPrimaryOrganisation() {
        return new OrganisationBuilderForTesting()
            .withId(DEFAULT_PRIMARY_ORG_ID)
            .withName(DEFAULT_PRIMARY_ORG_NAME)
            .withUrl(DEFAULT_PRIMARY_ORG_URL)
            .build();
    }

    String id = DEFAULT_ORG_ID;
    String name = DEFAULT_ORG_NAME;
    String url = DEFAULT_ORG_URL;

    public Organisation build() {
        return new Organisation(id, name,url);
    }

    public OrganisationBuilderForTesting withId(String id) {
        this.id = id;
        return this;
    }

    public OrganisationBuilderForTesting withName(String name) {
        this.name = name;
        return this;
    }

    public OrganisationBuilderForTesting withUrl(String url) {
        this.url = url;
        return this;
    }
}
