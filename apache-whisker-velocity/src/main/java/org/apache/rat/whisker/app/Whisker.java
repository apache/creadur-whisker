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
package org.apache.rat.whisker.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.apache.rat.whisker.fromxml.JDomBuilder;
import org.apache.rat.whisker.model.Work;
import org.apache.rat.whisker.scan.Directory;
import org.apache.rat.whisker.scan.FromFileSystem;
import org.jdom.JDOMException;


/**
 * 
 */
public class Whisker {
    
    private Act act;
    private String source;
    private String licenseDescriptor;
    
    private ResourceLoader loader = new FileLoader();
    
    private AbstractEngine engine;

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
    public String getLicenseDescriptor() {
        return licenseDescriptor;
    }

    /**
     * @param licenseDescriptor the licenseDescriptor to set
     */
    public Whisker setLicenseDescriptor(String licenseDescriptor) {
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
        engine.generateTemplate(directories());
        return this;
    }

    /**
     * @return
     */
    private Whisker report() throws Exception {
        engine.report(directories());
        return this;
    }
    
    /**
     * @return
     * @throws Exception 
     */
    private Whisker generate() throws Exception {
        engine.generate(new LicenseAnalyst().validate(load(getLicenseDescriptor())));
        return this;
    }

    /**
     * @return
     * @throws Exception 
     */
    private Whisker validate() throws Exception {
        engine.validate(new LicenseAnalyst(directories()).analyse(load(getLicenseDescriptor())));
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
     */
    private InputStream resourceAsStream(final String resource) {
        return loader.load(resource);
    }
    
    
    /**
     * @param resource
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    private Work load(final String resource) throws JDOMException, IOException {
        final InputStream resourceAsStream = resourceAsStream(resource);
        if (resourceAsStream == null) {
            throw new IllegalArgumentException("Cannot load " + resource);
        }
        return new JDomBuilder().build(resourceAsStream);
    }
}