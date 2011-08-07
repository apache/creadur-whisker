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
package org.apache.rat.whisker.legacy.app;

import java.io.IOException;
import java.util.Collection;

import org.apache.rat.whisker.legacy.scan.Directory;
import org.apache.rat.whisker.legacy.scan.FromFileSystem;


/**
 * 
 */
public class Whisker {

    
    
    private Act act = Act.TEMPLATE;
    private String base = "app";
    private String licenseDescriptor = "org/apache/rat/whisker/samples/james/james.xml";

    /**
     * @return the base
     */
    public String getBase() {
        return base;
    }

    /**
     * @param base the base to set
     */
    public Whisker setBase(String base) {
        this.base = base;
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

    protected void doTemplateGeneration() throws Exception {
        throw new UnsupportedOperationException("Template generation is unsupported.");
    }
    
    protected void doGenerate() throws Exception {
        throw new UnsupportedOperationException("Generation is unsupported.");
    }
        
    protected void doValidate() throws Exception {
        throw new UnsupportedOperationException("Validation is unsupported.");
    }

    protected void doReport() throws Exception {
        throw new UnsupportedOperationException("Reporting is unsupported.");        
    }

    /**
     * @return
     * @throws IOException
     */
    protected Collection<Directory> fileSystem() throws IOException {
        return new FromFileSystem().withBase(getBase());
    }

    @Override
    public String toString() {
        return "Whisker [act=" + act + ", base=" + base
                + ", licenseDescriptor=" + licenseDescriptor + "]";
    }
}
