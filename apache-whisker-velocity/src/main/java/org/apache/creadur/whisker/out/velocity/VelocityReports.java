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
package org.apache.creadur.whisker.out.velocity;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.creadur.whisker.app.Configuration;
import org.apache.creadur.whisker.app.ResultWriterFactory;
import org.apache.creadur.whisker.app.analysis.LicenseAnalyst;
import org.apache.creadur.whisker.model.Descriptor;
import org.apache.creadur.whisker.scan.Directory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;

/**
 * Wraps velocity engine.
 */
public class VelocityReports implements LogChute {
    /** XML generation template. */
    private static final Product[] PRODUCTS_THAT_GENERATE_TEMPLATES
        = {Product.XML_TEMPLATE};
    /** Missing license report. */
    private static final Product[] PRODUCTS_THAT_VALIDATE
        = {Product.MISSING_LICENSE_REPORT_TEMPLATE};
    /** Directories report. */
    private static final Product[] PRODUCTS_THAT_REPORT_ON_DIRECTORIES
        = {Product.DIRECTORIES_REPORT_TEMPLATE};
    /** Legal documents. */
    private static final Product[] PRODUCTS_THAT_GENERATE_LICENSING_MATERIALS
        = {Product.LICENSE, Product.NOTICE};

    /** Makes writes, not null. */
    private final ResultWriterFactory writerFactory;
    /** Merges templates, not null. */
    private final VelocityEngine engine;
    /** Logs messages, not null. */
    private final Log log;

    /**
     * Constructs a reporter using Apache Velocity.
     * @param writerFactory not null
     * @param log not null
     */
    public VelocityReports(
            final ResultWriterFactory writerFactory, final Log log) {
        this.writerFactory = writerFactory;
        this.log = log;
        engine = new VelocityEngine();
        engine.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM, this);
        engine.setProperty(VelocityEngine.RESOURCE_LOADER, "classpath");
        engine.setProperty("classpath.resource.loader.class",
            "org.apache.velocity.runtime.resource.loader."
                + "ClasspathResourceLoader");
        engine.init();
    }

    /**
     * Unused.
     * @param services unused
     * @see LogChute#init(RuntimeServices)
     */
    public final void init(final RuntimeServices services) { }

    /**
     * Indicates whether logging is enabled.
     * @param level at this level
     * @return true when log level is enabled, false otherwise
     * @see LogChute#isLevelEnabled(int)
     */
    public final boolean isLevelEnabled(final int level) {
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
            default:
                return false;
        }
    }

    /**
     * Logs a message.
     * @param level at level
     * @param message possibly null
     * @see LogChute#log(int, String)
     */
    public final void log(final int level, final String message) {
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
            default:
                log.trace(message);
        }
    }

    /**
     * Logs a message from Velocity.
     * @param level log level
     * @param message possibly null
     * @param throwable possibly null
     * @see LogChute#log(int, String, Throwable)
     */
    public final void log(final int level,
            final String message, final Throwable throwable) {
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
            default:
                log.trace(message, throwable);
        }
    }

    /**
     * Reports on work.
     * @param work not null
     * @param configuration not null
     * @throws Exception when generation fails
     */
    public final void generate(final Descriptor work,
            final Configuration configuration) throws Exception {
        final List<Product> products = new ArrayList<Product>();
        for (Product product: PRODUCTS_THAT_GENERATE_LICENSING_MATERIALS) {
            switch (product) {
                case NOTICE:
                    if (!work.isNoticeRequired()) {
                        break;
                    }
                default:
                    products.add(product);
            }


        }
        final Product[] pruductArray = new Product[products.size()];
        merge(products.toArray(pruductArray), context(work, configuration));
    }

    /**
     * Merges context with product templates, and writes results.
     * @param products not null
     * @param context not null
     * @throws Exception when merger fails
     */
    private void merge(final Product[] products,
            final VelocityContext context) throws Exception {
        for (final Product product : products) {
            merge(product, context);
        }
    }

    /**
     * Merges context with product template, and writes results.
     * @param product not null
     * @param context not null
     * @throws Exception when generate fails
     */
    private void merge(
            final Product product, final VelocityContext context)
                throws Exception {
        final Writer writer = product.writerFrom(writerFactory);
        engine.getTemplate(
                template(product.getTemplate())).merge(context, writer);
        IOUtils.closeQuietly(writer);
    }

    /**
     * Creates a context, and loads it for descriptor work.
     * @param work not null
     * @param configuration not null
     * @return not null
     */
    private VelocityContext context(final Descriptor work,
            final Configuration configuration) {
        final VelocityContext context = new VelocityContext();
        context.put("work", work);
        context.put("indent", new Indentation());
        context.put("helper", new RenderingHelper(work, configuration));
        return context;
    }


    /**
     * Returns the full template path.
     * @param name not null
     * @return not null
     */
    private String template(final String name) {
        return "org/apache/creadur/whisker/template/velocity/"
                + name.toLowerCase() + ".vm";
    }

    /**
     * Generates a directory report.
     * @param directories not null
     * @throws Exception when reporting fails
     */
    public final void report(
            final Collection<Directory> directories) throws Exception {
        merge(PRODUCTS_THAT_REPORT_ON_DIRECTORIES, context(directories));
    }

    /**
     * Creates a content, and loads it with the directories.
     * @param directories not null
     * @return not null
     */
    private VelocityContext context(
            final Collection<Directory> directories) {
        final VelocityContext context = new VelocityContext();
        context.put("dirs", directories);
        return context;
    }

    /**
     * Reports on analysis.
     * @param analyst not null
     * @throws Exception when validation fails
     */
    public final void validate(
            final LicenseAnalyst analyst) throws Exception {
        merge(PRODUCTS_THAT_VALIDATE, context(analyst));
    }

    /**
     * Creates a context, and loads it with the analyst.
     * @param analyst not null
     * @return not null
     */
    private VelocityContext context(final LicenseAnalyst analyst) {
        final VelocityContext context = new VelocityContext();
        context.put("analyst", analyst);
        return context;
    }

    /**
     * Generates template.
     * @param withBase not null
     * @throws Exception when generation fails
     */
    public final void generateTemplate(
            final Collection<Directory> withBase) throws Exception {
        merge(PRODUCTS_THAT_GENERATE_TEMPLATES, context(withBase));
    }
}
