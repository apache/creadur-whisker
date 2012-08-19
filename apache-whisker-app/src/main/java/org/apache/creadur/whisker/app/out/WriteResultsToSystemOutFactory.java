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
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.creadur.whisker.app.Result;
import org.apache.creadur.whisker.app.ResultWriterFactory;

/**
 * Factory builds writers that output to <code>System.out</code>.
 */
public final class WriteResultsToSystemOutFactory
                        implements ResultWriterFactory {

    /** Encoding used. */
    private final String encoding;

    /**
     * Constructs a factory with given encoding.
     * @param encoding not null
     */
    public WriteResultsToSystemOutFactory(final String encoding) {
        super();
        this.encoding = encoding;
    }

    /**
     * Constructs a factory with default
     * (<code>UTF-8</code>) encoding.
     */
    public WriteResultsToSystemOutFactory() {
        this("UTF-8");
    }

    /**
     * Writes given result to <code>System.our</code>.
     * @param result not null
     * @return not null
     * @throws IOException when result cannot be written
     * @see ResultWriterFactory#writerFor(Result)
     */
    public Writer writerFor(final Result result)
            throws IOException {
        return new BufferedWriter(
                new OutputStreamWriter(System.out, encoding));
    }
}
