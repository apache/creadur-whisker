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
package org.apache.rat.whisker.out.velocity;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.rat.whisker.app.ResultWriterFactory;
import org.apache.rat.whisker.app.analysis.LicenseAnalyst;
import org.apache.rat.whisker.app.analysis.ResourceDefinitionException;
import org.apache.rat.whisker.model.Descriptor;
import org.apache.rat.whisker.scan.Directory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;

/**
 * Wraps velocity engine.
 */
public class VelocityReports implements LogChute {
    
    private static final Product[] PRODUCTS_THAT_GENERATE_TEMPLATES = {Product.XML_TEMPLATE};
    private static final Product[] PRODUCTS_THAT_VALIDATE = {Product.MISSING_LICENSE_REPORT_TEMPLATE};
    private static final Product[] PRODUCTS_THAT_REPORT_ON_DIRECTORIES = {Product.DIRECTORIES_REPORT_TEMPLATE};
    private static final Product[] PRODUCTS_THAT_GENERATE_LICENSING_MATERIALS = {Product.LICENSE, Product.NOTICE};
    
    private final ResultWriterFactory writerFactory;
    private final VelocityEngine engine;
    private final Log log;
             
    public VelocityReports(final ResultWriterFactory writerFactory, final Log log) {
        this.writerFactory = writerFactory;
        this.log = log;
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
        switch (level) {
            case DEBUG_ID:
                return log.isDebugEnabled();
            case TRACE_ID:
                return log.isTraceEnabled();
            case INFO_ID:
                return log.isInfoEnabled();
            case WARN_ID:
                return log.isWarnEnabled();
            case ERROR_ID:
                return log.isErrorEnabled();
        }
        return false;
    }

    /**
     * @see org.apache.velocity.runtime.log.LogChute#log(int, java.lang.String)
     */
    @Override
    public void log(int level, String message) {
        switch (level) {
            case DEBUG_ID:
                log.debug(message);
                break;
            case TRACE_ID:
                log.trace(message);
                break;
            case INFO_ID:
                log.info(message);
                break;
            case WARN_ID:
                log.warn(message);
                break;
            case ERROR_ID:
                log.error(message);
                break;
        }
    }

    /**
     * @see org.apache.velocity.runtime.log.LogChute#log(int, java.lang.String, java.lang.Throwable)
     */
    @Override
    public void log(int level, String message, Throwable throwable) {
        switch (level) {
            case DEBUG_ID:
                log.debug(message, throwable);
                break;
            case TRACE_ID:
                log.trace(message, throwable);
                break;
            case INFO_ID:
                log.info(message, throwable);
                break;
            case WARN_ID:
                log.warn(message, throwable);
                break;
            case ERROR_ID:
                log.error(message, throwable);
                break;
        }    }

    /**
     * 
     */
    public void generate(final Descriptor work) throws Exception {
        merge(PRODUCTS_THAT_GENERATE_LICENSING_MATERIALS , context(work));
    }

    private void merge(final Product[] products, final VelocityContext context) throws Exception {
        for (final Product product:products) {
            merge(product, context);
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
    private void merge(final Product product, final VelocityContext context) throws Exception {
        final Writer writer = product.writerFrom(writerFactory);
        engine.getTemplate(template(product.getTemplate())).merge(context, writer);
        IOUtils.closeQuietly(writer);
    }

    /**
     * @return
     * @throws IOException 
     * @throws JDOMException 
     * @throws ResourceDefinitionException 
     */
    private VelocityContext context(final Descriptor work) throws Exception {
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

    public void report(final Collection<Directory> directories) throws Exception {
        merge(PRODUCTS_THAT_REPORT_ON_DIRECTORIES, context(directories));
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
    public void validate(LicenseAnalyst analyst) throws Exception {
        merge(PRODUCTS_THAT_VALIDATE, context(analyst));
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
    public void generateTemplate(Collection<Directory> withBase) throws Exception {
        merge(PRODUCTS_THAT_GENERATE_TEMPLATES, context(withBase));
    }
}
