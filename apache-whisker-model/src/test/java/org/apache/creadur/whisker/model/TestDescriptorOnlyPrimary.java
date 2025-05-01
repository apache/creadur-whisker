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

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestDescriptorOnlyPrimary {

    DescriptorBuilderForTesting builder;
    Descriptor subject;

    @BeforeEach
    void setUp() throws Exception {
        builder = new DescriptorBuilderForTesting();
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    void isOnlyPrimaryWithSubsidaryCopyrightNotice() throws Exception {
        builder.withSubsidaryCopyrightNotice().withThirdParty().withDirectory(".");
        subject = builder.build();
        assertFalse(subject.isOnlyPrimary(builder.contents.iterator().next()),
                "Work is not only primary when subsidary copyright notices exist.");
    }
}
