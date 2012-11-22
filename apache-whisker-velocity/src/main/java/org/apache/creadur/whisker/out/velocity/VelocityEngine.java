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

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.creadur.whisker.app.AbstractEngine;
import org.apache.creadur.whisker.app.Configuration;
import org.apache.creadur.whisker.app.ResultWriterFactory;
import org.apache.creadur.whisker.app.analysis.LicenseAnalyst;
import org.apache.creadur.whisker.model.Descriptor;
import org.apache.creadur.whisker.scan.Directory;

/**
 * Uses Apache Velocity to implement {@link AbstractEngine}.
 *
 * @see <a href='http://velocity.apache.org'>Apache Velocity</a>
 */
public class VelocityEngine extends AbstractEngine {
    /** Not null. */
    private final Log log;

    /**
     * Constructs an engine running on Apache Velocity.
     * @param log not null
     */
    public VelocityEngine(final Log log) {
        super();
        this.log = log;
    }

    /**
     * Generates a template, and writes result using given factory.
     * @param withBase not null
     * @param writerFactory not null
     * @param configuration not null
     * @return this engine, not null
     * @throws Exception when generation fails
     * @see AbstractEngine#skeleton(Collection, ResultWriterFactory, Configuration)
     */
    @Override
    public final AbstractEngine skeleton(
            final Collection<Directory> withBase,
            final ResultWriterFactory writerFactory,
            final Configuration configuration)
                throws Exception {
        reporter(writerFactory).generateTemplate(withBase);
        return this;
    }

    /**
     * Creates a reporter for the given factory.
     * @param writerFactory not null
     * @return a reporter, not null
     */
    private VelocityReports reporter(final ResultWriterFactory writerFactory) {
        return new VelocityReports(writerFactory, log);
    }

    /**
     * Generates a validation report, and writes result using given factory.
     * @param analyst not null
     * @param writerFactory not null
     * @param configuration not null
     * @return this, not null
     * @throws Exception when validation fails
     * @see AbstractEngine#validate(LicenseAnalyst, ResultWriterFactory, Configuration)
     */
    @Override
    public final AbstractEngine validate(
            final LicenseAnalyst analyst,
            final ResultWriterFactory writerFactory,
            final Configuration configuration) throws Exception {
        reporter(writerFactory).validate(analyst);
        return this;
    }

    /**
     * Generates a directories report, and writes result using given factory.
     * @param directories not null
     * @param writerFactory not null
     * @param configuration not null
     * @return this, not null
     * @throws Exception when reporting fails
     * @see AbstractEngine#report(java.util.Collection, ResultWriterFactory, Configuration)
     */
    @Override
    public final AbstractEngine report(
            final Collection<Directory> directories,
            final ResultWriterFactory writerFactory,
            final Configuration configuration) throws Exception {
        reporter(writerFactory).report(directories);
        return this;
    }

    /**
     * Generates documents, and writes results using given factory.
     * @param work not null
     * @param writerFactory not null
     * @param configuration not null
     * @return this, not null
     * @throws Exception when generation fails.
     * @see AbstractEngine#generate(Descriptor, ResultWriterFactory, Configuration)
     */
    @Override
    public final AbstractEngine generate(
            final Descriptor work,
            final ResultWriterFactory writerFactory,
            final Configuration configuration) throws Exception {
        reporter(writerFactory).generate(work, configuration);
        return this;
    }
}
