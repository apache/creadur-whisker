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
package org.apache.rat.whisker.app.out;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.rat.whisker.app.Result;
import org.apache.rat.whisker.app.ResultWriterFactory;

/**
 * Writes results as files within a directory.
 */
public class WriteResultsIntoDirectoryFactory implements ResultWriterFactory  {

    private final File directory;
    private final String encoding;

    public WriteResultsIntoDirectoryFactory(final File directory, final String encoding) {
        super();
        this.directory = directory;
        this.encoding = encoding;
    }

    /**
     * @see org.apache.rat.whisker.app.ResultWriterFactory#writerFor(org.apache.rat.whisker.app.Result)
     */
    @Override
    public Writer writerFor(Result result) throws IOException {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(result.within(directory)), encoding));
    }
}
