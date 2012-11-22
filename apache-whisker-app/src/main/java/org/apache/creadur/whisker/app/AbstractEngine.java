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
package org.apache.creadur.whisker.app;

import java.util.Collection;

import org.apache.creadur.whisker.app.analysis.LicenseAnalyst;
import org.apache.creadur.whisker.model.Descriptor;
import org.apache.creadur.whisker.scan.Directory;

/**
 * A pluggable template.
 */
public abstract class AbstractEngine {

    /**
     * Writes templates to help create meta-data.
     * @param withBase from this base, not null
     * @param writerFactory not null
     * @param configuration not null
     * @return this
     * @throws Exception when creation fails
     */
    public abstract AbstractEngine skeleton(
            Collection<Directory> withBase,
            ResultWriterFactory writerFactory, Configuration configuration) throws Exception;

    /**
     * Reports validations.
     * @param analyst not null
     * @param writerFactory not null
     * @param configuration not null
     * @return not null
     * @throws Exception when report creation fails
     */
    public abstract AbstractEngine validate(LicenseAnalyst analyst,
            ResultWriterFactory writerFactory, Configuration configuration) throws Exception;

    /**
     * Writes a report describing the directories present.
     * @param directories not null
     * @param writerFactory not null
     * @param configuration not null
     * @return this
     * @throws Exception when report creation fails
     */
    public abstract AbstractEngine report(
            final Collection<Directory> directories,
            ResultWriterFactory writerFactory, Configuration configuration) throws Exception;

    /**
     * Writes legal documents.
     * @param work not null
     * @param writerFactory not null
     * @param configuration not null
     * @return this
     * @throws Exception when report creation fails
     */
    public abstract AbstractEngine generate(final Descriptor work,
            ResultWriterFactory writerFactory, Configuration configuration) throws Exception;

}
