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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.creadur.whisker.app.StreamableResource;
/**
 * Streams, on demand, the contents of a file identified by a full file name,
 * including path.
 */
public final class StreamableFileNameResource extends StreamableResource {
    /**
     * The full file name, including path,
     * of the resource to be streamed.
     */
    private final String fileName;

    /**
     * Constructs an instance that streams
     * the resource identified by name on demand.
     * @param fileName full file name, including path,
     * of the resource to be streamed
     * on demand, not null
     */
    public StreamableFileNameResource(final String fileName) {
        super();
        this.fileName = fileName;
    }

    /**
     * Gets the file name of the resource to be streamed.
     * @return full file name, not null
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Opens the file as an input stream.
     * @return not null
     * @see StreamableResource#open()
     * @throws IOException when resource cannot be opened
     */
    @Override
    public InputStream open() throws IOException {
        return new BufferedInputStream(new FileInputStream(new File(fileName)));
    }

    /**
     * Suitable for logging.
     * @return a description
     */
    @Override
    public String toString() {
        return "StreamableFileNameResource [fileName=" + fileName + "]";
    }
}
