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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;

import org.apache.rat.whisker.fromxml.JDomBuilder;
import org.apache.rat.whisker.model.Work;
import org.apache.rat.whisker.out.velocity.Engine;
import org.apache.rat.whisker.out.velocity.LicenseAnalyst;
import org.apache.rat.whisker.scan.Directory;
import org.apache.rat.whisker.scan.FromFileSystem;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


/**
 * 
 */
public class Whisker {
    
    private Act act;
    private String source;
    private String licenseDescriptor;
    
    private ResourceLoader loader = new FileLoader();

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
        doTemplateGeneration();
        return this;
    }

    /**
     * @return
     */
    private Whisker report() throws Exception {
        doReport();
        return this;
    }
    
    /**
     * @return
     * @throws Exception 
     */
    private Whisker generate() throws Exception {
        doGenerate();
        return this;
    }

    /**
     * @return
     * @throws Exception 
     */
    private Whisker validate() throws Exception {
        doValidate();
        return this;
    }

    /**
     * @return
     * @throws IOException
     */
    protected Collection<Directory> directories() throws IOException {
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
    public InputStream resourceAsStream(final String resource) {
        return loader.load(resource);
    }
    
    
    protected void doGenerate() throws Exception {
        new Engine().generate(new LicenseAnalyst().validate(load(getLicenseDescriptor())));
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
        return new JDomBuilder().build(new SAXBuilder().build(resourceAsStream));
    }

    protected void doValidate() throws Exception {
        new Engine().validate(new LicenseAnalyst(directories()).analyse(load(getLicenseDescriptor())));
    }

    protected void doReport() throws Exception {
        new Engine().report(directories());
    }

    protected void doTemplateGeneration() throws Exception {
        new Engine().generateTemplate(directories());
    }


    public interface ResourceLoader {
        InputStream load(final String resource);
    }
    
    public static class ClassPathLoader implements ResourceLoader {

        @Override
        public InputStream load(String resource) {
            return getClass().getClassLoader().getResourceAsStream(resource);
        }
    }
    
    public static class FileLoader implements ResourceLoader {

        @Override
        public InputStream load(String resource) {
            try {
                return new FileInputStream(new File(resource));
            } catch (FileNotFoundException e) {
                return null;
            }
        }
        
    }
}
