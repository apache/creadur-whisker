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
package org.apache.creadur.whisker.app.out;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.creadur.whisker.app.Result;
import org.apache.creadur.whisker.app.ResultWriterFactory;

/**
 * Writes results as files within a directory.
 */
public final class WriteResultsIntoDirectoryFactory
                       implements ResultWriterFactory  {

    /** Output directory. */
    private final File directory;
    /** Output encoding. */
    private final String encoding;

    /**
     * Constructs a factory that write reports
     * into the given directory with the given
     * encoding.
     * @param directory not null
     * @param encoding not null
     */
    public WriteResultsIntoDirectoryFactory(
            final File directory, final String encoding) {
        super();
        this.directory = directory;
        this.encoding = encoding;
    }

    /**
     * Creates a suitable write for the given report.
     * @param result not null
     * @return not null
     * @see ResultWriterFactory#writerFor(Result)
     * @throws IOException when the report cannot be written
     */
    public Writer writerFor(final Result result)
            throws IOException {
        return new BufferedWriter(
                new FileWriterWithEncoding(
                        result.within(directory), encoding));
    }
}
