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

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Collection;

import org.apache.creadur.whisker.app.analysis.LicenseAnalyst;
import org.apache.rat.whisker.fromxml.JDomBuilder;
import org.apache.rat.whisker.model.Descriptor;
import org.apache.creadur.whisker.scan.Directory;
import org.apache.creadur.whisker.scan.FromFileSystem;
import org.jdom.JDOMException;


/**
 * 
 */
public class Whisker {
    
    private Act act;
    private String source;
    private StreamableResource licenseDescriptor;
    private ResultWriterFactory writerFactory;
    
    private AbstractEngine engine;

    /**
     * Gets the factory that builds product {@link Writer}s.
     * @return factory
     */
    public ResultWriterFactory getWriterFactory() {
        return writerFactory;
    }

    /**
     * Sets the factory that builds product {@link Writer}s.
     * @param writerFactory not null
     */
    public Whisker setWriterFactory(ResultWriterFactory writerFactory) {
        this.writerFactory = writerFactory;
        return this;
    }

    /**
     * Gets the reporting engine.
     * @return not null
     */
    public AbstractEngine getEngine() {
        return engine;
    }

    /**
     * Sets the reporting engine.
     * @param engine not null
     * @return this, not null
     */
    public Whisker setEngine(AbstractEngine engine) {
        this.engine = engine;
        return this;
    }

    /**
     * @return the base
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the base to set
     */
    public Whisker setSource(String source) {
        this.source = source;
        return this;
    }

    /**
     * @return the licenseDescriptor
     */
    public StreamableResource getLicenseDescriptor() {
        return licenseDescriptor;
    }

    /**
     * @param licenseDescriptor the licenseDescriptor to set
     */
    public Whisker setLicenseDescriptor(StreamableResource licenseDescriptor) {
        this.licenseDescriptor = licenseDescriptor;
        return this;
    }

    /**
     * @return the act
     */
    public Act getAct() {
        return act;
    }

    /**
     * @param act the act to set
     */
    public Whisker setAct(Act act) {
        this.act = act;
        return this;
    }
    
    public Whisker act() throws Exception {
        switch (act) {
            case REPORT:
                return report();
            case AUDIT:
                return validate();
            case TEMPLATE:
                return template();
            case GENERATE:
            default:
                return generate();
        }
    }
    
    
    
    /**
     * @return
     * @throws Exception 
     */
    private Whisker template() throws Exception {
        engine.generateTemplate(directories(), getWriterFactory());
        return this;
    }

    /**
     * @return
     */
    private Whisker report() throws Exception {
        engine.report(directories(), getWriterFactory());
        return this;
    }
    
    /**
     * @return
     * @throws Exception 
     */
    private Whisker generate() throws Exception {
        engine.generate(new LicenseAnalyst().validate(load(getLicenseDescriptor())), getWriterFactory());
        return this;
    }

    /**
     * @return
     * @throws Exception 
     */
    private Whisker validate() throws Exception {
        engine.validate(new LicenseAnalyst(directories()).analyse(load(getLicenseDescriptor())), getWriterFactory());
        return this;
    }

    /**
     * @return
     * @throws IOException
     */
    private Collection<Directory> directories() throws IOException {
        return new FromFileSystem().withBase(getSource());
    }

    @Override
    public String toString() {
        return "Whisker [act=" + act + ", base=" + source
                + ", licenseDescriptor=" + licenseDescriptor + "]";
    }
    

    /**
     * @param resource
     * @return
     * @throws IOException 
     */
    private InputStream resourceAsStream(final StreamableResource resource) throws IOException {
        return resource.open();
    }
    
    
    /**
     * @param resource
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    private Descriptor load(final StreamableResource resource) throws Exception {
        final InputStream resourceAsStream = resourceAsStream(resource);
        if (resourceAsStream == null) {
            throw new IllegalArgumentException("Cannot load " + resource);
        }
        return new JDomBuilder().build(resourceAsStream);
    }
}
