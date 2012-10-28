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
package org.apache.creadur.whisker.app.load;

import java.io.IOException;
import java.io.InputStream;

import org.apache.creadur.whisker.app.StreamableResource;

/**
 * Streams, on demand, the contents of a resource located on the class path.
 */
public final class StreamableClassPathResource extends StreamableResource {

    /**
     * The full name <strong>including path</strong>
     * of a resource on the class path.
     */
    private final String name;

    /**
     * Constructs an instance that streams the given class path resource
     * on demand.
     * @param name full name <strong>including path</strong> of
     * a resource on the class path,
     * not null
     */
    public StreamableClassPathResource(final String name) {
        super();
        this.name = name;
    }


    /**
     * Gets the location on the class path of the resource to be streamed.
     * @return not null
     */
    public String getName() {
        return name;
    }

    /**
     * Is this resource found on the classpath?
     * @return true when the resource is found on the classpath,
     * null otherwise
     */
    public boolean exists()  {
        try {
            return open() != null;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Opens a resource on the classpath.
     * @return not null
     * @see StreamableResource#open()
     * @throws IOException when resource cannot be open
     */
    @Override
    public InputStream open() throws IOException {
        return getClass().getClassLoader().getResourceAsStream(name);
    }

    /**
     * Suitable for logging.
     * @return a description
     */
    @Override
    public String toString() {
        return "StreamableClassPathResource [name=" + name + "]";
    }
}
