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

import static org.apache.creadur.whisker.app.ConfigurationBuilder.*;

import org.apache.creadur.whisker.model.Descriptor;
import org.apache.creadur.whisker.model.Resource;

import junit.framework.TestCase;

public class TestRenderingHelper extends TestCase {

    private static final String A_SOURCE_URL = "http://example.org/sample";

    RenderingHelper subject;

    Resource resourceWithSourceUrl;
    Resource resourceNoSourceUrl;

    Descriptor work;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        resourceWithSourceUrl = new Resource("a name", "an:id", A_SOURCE_URL);
        resourceNoSourceUrl = new Resource("a name", "an:id", null);
    }

    public void testSourceWithSourceUrlsConfiguration() {
        subject = new RenderingHelper(work,
                aConfiguration().withSourceURLsInLicense().build());
        assertEquals(subject.sourceUrl(resourceWithSourceUrl), " from " + A_SOURCE_URL);
    }

    public void testSourceNoSourceUrlsConfiguration() {
        subject = new RenderingHelper(work,
                aConfiguration().noSourceURLsInLicense().build());
        assertEquals(subject.sourceUrl(resourceWithSourceUrl), "");
    }

    public void testNoSourceWithSourceUrlsConfiguration() {
        subject = new RenderingHelper(work,
                aConfiguration().withSourceURLsInLicense().build());
        assertEquals(subject.sourceUrl(resourceNoSourceUrl), "");
    }

    public void testNoSourceNoSourceUrlsConfiguration() {
        subject = new RenderingHelper(work,
                aConfiguration().noSourceURLsInLicense().build());
        assertEquals(subject.sourceUrl(resourceNoSourceUrl), "");
    }

}
