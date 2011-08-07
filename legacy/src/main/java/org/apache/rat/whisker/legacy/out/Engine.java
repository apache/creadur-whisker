/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
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
package org.apache.rat.whisker.legacy.out;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;

import org.apache.rat.whisker.legacy.out.LicenseAnalyst.ResourceDefinitionException;
import org.apache.rat.whisker.legacy.scan.Directory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;
import org.jdom.JDOMException;

/**
 * Wraps velocity engine.
 */
public class Engine implements LogChute {
   
    private static final String[] TEMPLATES = {"LICENSE", "NOTICE"};
    private static final String[] DIRECTORIES_REPORT_TEMPLATE = {"DIRECTORIES"};
    private static final String[] MISSING_LICENSE_REPORT_TEMPLATE = {"MISSING-LICENSE"};
    private static final String[] XML_TEMPLATE = {"XML-TEMPLATE"};

    
    private final VelocityEngine engine;
             
    public Engine() {
        engine = new VelocityEngine();
        engine.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM, this);
        engine.setProperty(VelocityEngine.RESOURCE_LOADER, "classpath");
        engine.setProperty("classpath.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        engine.init();
    }

    /**
     * @see org.apache.velocity.runtime.log.LogChute#init(org.apache.velocity.runtime.RuntimeServices)
     */
    @Override
    public void init(RuntimeServices services) throws Exception {}

    /**
     * @see org.apache.velocity.runtime.log.LogChute#isLevelEnabled(int)
     */
    @Override
    public boolean isLevelEnabled(int level) {
        return true;
    }

    /**
     * @see org.apache.velocity.runtime.log.LogChute#log(int, java.lang.String)
     */
    @Override
    public void log(int level, String message) {
        System.err.println(message);
    }

    /**
     * @see org.apache.velocity.runtime.log.LogChute#log(int, java.lang.String, java.lang.Throwable)
     */
    @Override
    public void log(int level, String message, Throwable throwable) {
        System.err.println(message);
        throwable.printStackTrace();
    }

    /**
     * 
     */
    public void merge(final Work work) throws Exception {
        merge(TEMPLATES, context(work));
    }

    private void merge(final String[] names, final VelocityContext context) throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, JDOMException, IOException {
        for (String name:names) {
            merge(name, context);
        }
    }
    
    /**
     * Merges template with given name.
     * @param name not null
     * @param work not null
     * @throws IOException 
     * @throws JDOMException 
     * @throws MethodInvocationException 
     * @throws ParseErrorException 
     * @throws ResourceNotFoundException 
     */
    private void merge(final String name, final VelocityContext context) throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, JDOMException, IOException {
        StringWriter writer = new StringWriter();
        engine.getTemplate(template(name)).merge(context, writer);
        System.out.println(writer);
    }

    /**
     * @return
     * @throws IOException 
     * @throws JDOMException 
     * @throws ResourceDefinitionException 
     */
    private VelocityContext context(final Work work) throws JDOMException, IOException, ResourceDefinitionException {
        VelocityContext context = new VelocityContext();
        context.put("work", work);
        context.put("indent", new Indentation());
        return context;
    }


    /**
     * Returns full template path.
     * @param string name not null
     * @return not null
     */
    private String template(String name) {
        return "org/apache/rat/whisker/template/velocity/" + name.toLowerCase() + ".vm";
    }

    
    public void write(final Collection<Directory> directories) throws Exception {
        merge(DIRECTORIES_REPORT_TEMPLATE, context(directories));
    }

    /**
     * @param directories
     * @return
     */
    private VelocityContext context(Collection<Directory> directories) {
        VelocityContext context = new VelocityContext();
        context.put("dirs", directories);
        return context;
    }

    
    /**
     * @param licenseDescriptor
     * @param withBase
     * @throws Exception 
     */
    public void merge(LicenseAnalyst analyst) throws Exception {
        merge(MISSING_LICENSE_REPORT_TEMPLATE, context(analyst));
    }

    /**
     * @param analyse
     * @return
     */
    private VelocityContext context(LicenseAnalyst analyst) {
        VelocityContext context = new VelocityContext();
        context.put("analyst", analyst);
        return context;
    }

    /**
     * @param withBase
     * @throws IOException 
     * @throws JDOMException 
     * @throws MethodInvocationException 
     * @throws ParseErrorException 
     * @throws ResourceNotFoundException 
     */
    public void generateTemplate(Collection<Directory> withBase) throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, JDOMException, IOException {
        merge(XML_TEMPLATE, context(withBase));
    }
}
