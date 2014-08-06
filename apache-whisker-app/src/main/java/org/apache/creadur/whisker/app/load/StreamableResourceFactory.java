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

import java.io.File;

import org.apache.creadur.whisker.app.StreamableResource;

/**
 * Conveniently builds {@link StreamableResource} implementations.
 */
public final class StreamableResourceFactory {

    /**
     * Builds instance that streams, on demand,
     * from resource on the class path.
     * @param name full name, including path, of the resource not null
     * @return an instance that streams, on demand, the given resource,
     * not null
     */
    public StreamableResource streamFromClassPathResource(final String name) {
        return new StreamableClassPathResource(name);
    }

    /**
     * Builds instance that streams, on demand,
     * from resource stored in the file system.
     * @param fileName full name, including path, of the resource not null
     * @return an instance that streams, on demand, the given resource,
     * not null
     */
    public StreamableResource streamFromFileResource(final String fileName) {
        return new StreamableFileNameResource(fileName);
    }

    /**
     * Builds instance that streams, on demand,
     * from resource stored in the file system.
     * @param file a file storing the resource not null
     * @return an instance that streams, on demand, the given resource,
     * not null
     */
    public StreamableResource streamFromFileResource(final File file) {
        return new StreamableFileResource(file);
    }

    /**
     * When the resource is found on the classpath,
     * builds an instance that streams, on demand,
     * from the classpath. Otherwise, builds an
     * instances that streams from the file system.
     * @param resourceName source stream name.
     * @return not null
     */
    public StreamableResource streamFromResource(final String resourceName) {
        final StreamableClassPathResource streamFromClasspath =
                new StreamableClassPathResource(resourceName);
        if (streamFromClasspath.exists()) {
            return streamFromClasspath;
        }
        return streamFromFileResource(resourceName);
    }
}
