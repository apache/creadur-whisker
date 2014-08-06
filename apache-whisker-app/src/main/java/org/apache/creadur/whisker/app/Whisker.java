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

import static org.apache.creadur.whisker.app.LicenseConfiguration.*;
import static org.apache.creadur.whisker.app.ConfigurationBuilder.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.apache.creadur.whisker.app.analysis.LicenseAnalyst;
import org.apache.creadur.whisker.fromxml.JDomBuilder;
import org.apache.creadur.whisker.model.Descriptor;
import org.apache.creadur.whisker.scan.Directory;
import org.apache.creadur.whisker.scan.FromFileSystem;


/**
 * High level application, configured as a bean.
 */
public class Whisker {

    /** Operation to be performed. */
    private Act act;
    /** The source on which the operation is to be performed. */
    private String source;
    /** The meta-data. */
    private StreamableResource licenseDescriptor;
    /** Pluggable report writer. */
    private ResultWriterFactory writerFactory;
    /** Pluggable templating. */
    private AbstractEngine engine;
    /** Configuration options for license rendering */
    private LicenseConfiguration licenseConfiguration = DEFAULT_LICENSE_CONFIGURATION;

    /**
     * Gets the configuration options for license rendering.
     * @return not null
     */
    public LicenseConfiguration getLicenseConfiguration() {
        return licenseConfiguration;
    }

    /**
     * Sets the configuration options for license rendering.
     * @param licenseConfiguration not null
     * @return this, not null
     */
    public Whisker setLicenseConfiguration(final LicenseConfiguration licenseConfiguration) {
        this.licenseConfiguration = notNull(licenseConfiguration);
        return this;
    }

    /**
     * Gets the factory that builds product {@link java.io.Writer}s.
     * @return factory
     */
    public final ResultWriterFactory getWriterFactory() {
        return writerFactory;
    }

    /**
     * Sets the factory that builds product {@link java.io.Writer}s.
     * @param writerFactory not null
     * @return this, not null
     */
    public final Whisker setWriterFactory(final ResultWriterFactory writerFactory) {
        this.writerFactory = writerFactory;
        return this;
    }

    /**
     * Gets the reporting engine.
     * @return not null
     */
    public final AbstractEngine getEngine() {
        return engine;
    }

    /**
     * Sets the reporting engine.
     * @param engine not null
     * @return this, not null
     */
    public final Whisker setEngine(final AbstractEngine engine) {
        this.engine = engine;
        return this;
    }

    /**
     * Gets the source on which the operation will be performed.
     * @return the base, not null
     */
    public final String getSource() {
        return source;
    }

    /**
     * Sets the source
     * @param source the base to set
     * @return this, not null
     */
    public final Whisker setSource(final String source) {
        this.source = source;
        return this;
    }

    /**
     * @return the licenseDescriptor
     */
    public final StreamableResource getLicenseDescriptor() {
        return licenseDescriptor;
    }

    /**
     * Sets meta-data describing the source licensing.
     * @param licenseDescriptor the licenseDescriptor to set
     * @return this, not null
     */
    public final Whisker setLicenseDescriptor(StreamableResource licenseDescriptor) {
        this.licenseDescriptor = licenseDescriptor;
        return this;
    }

    /**
     * Gets the operation to be performed.
     * @return the act
     */
    public final Act getAct() {
        return act;
    }

    /**
     * Sets the operation to be performed.
     * @param act the act to set
     * @return this, not null
     */
    public final Whisker setAct(Act act) {
        this.act = act;
        return this;
    }

    /**
     * Performs the operation set.
     * @return this, not null
     * @throws Exception when the operation fails
     */
    public Whisker act() throws Exception {
        switch (act) {
            case REPORT:
                return report();
            case AUDIT:
                return validate();
            case SKELETON:
                return skeleton();
            case GENERATE:
            default:
                return generate();
        }
    }

    /**
     * Creates a template to help create meta-data.
     * @throws Exception when the report creation fails
     * @return this, not null
     */
    private Whisker skeleton() throws Exception {
        engine.skeleton(directories(), getWriterFactory(), configuration());
        return this;
    }

    /**
     * Builds a populated configuration.
     * @return not null
     */
    public Configuration configuration() {
        return aConfiguration().with(licenseConfiguration).build();
    }

    /**
     * Writes a report on the directories in the source.
     * @return this, not null
     * @throws Exception when the report creation fails
     */
    private Whisker report() throws Exception {
        engine.report(directories(), getWriterFactory(), configuration());
        return this;
    }

    /**
     * Generates legal documentation.
     * @return this, not null
     * @throws Exception when the generation fails
     */
    private Whisker generate() throws Exception {
        engine.generate(new LicenseAnalyst().validate(load(getLicenseDescriptor())), getWriterFactory(), configuration());
        return this;
    }

    /**
     * Writes a validation report.
     * @return this, not null
     * @throws Exception when the validation fails
     */
    private Whisker validate() throws Exception {
        engine.validate(new LicenseAnalyst(directories()).analyse(load(getLicenseDescriptor())), getWriterFactory(), configuration());
        return this;
    }

    /**
     * Describes the directories within the source.
     * @return not null
     * @throws IOException when reading the source fails
     */
    private Collection<Directory> directories() throws IOException {
        return new FromFileSystem().withBase(getSource());
    }

    /**
     * Describes the state suitable for logging.
     * @return something suitable for logging
     */
    @Override
    public String toString() {
        return "Whisker [act=" + act + ", base=" + source
                + ", licenseDescriptor=" + licenseDescriptor + "]";
    }


    /**
     * Opens a resource as a stream.
     * @param resource not null
     * @return not null
     * @throws IOException
     */
    private InputStream resourceAsStream(final StreamableResource resource) throws IOException {
        return resource.open();
    }


    /**
     * Reads meta-data from the given source.
     * @param resource not null
     * @return not null
     * @throws Exception when meta-data cannot be loaded
     */
    private Descriptor load(final StreamableResource resource) throws Exception {
        final InputStream resourceAsStream = resourceAsStream(resource);
        if (resourceAsStream == null) {
            throw new IllegalArgumentException("Cannot load " + resource);
        }
        return new JDomBuilder().build(resourceAsStream);
    }
}
