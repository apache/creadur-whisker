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
package org.apache.creadur.whisker.plugin.maven;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.maven.plugin.logging.Log;

/**
 * Adapts commons logging calls to Maven.
 */
public class MojoToJCLLog implements org.apache.commons.logging.Log {

    /** Maven's log */
    private final Log log;

    /**
     * Constructs a log than delegates to Maven.
     * @param log not null
     */
    public MojoToJCLLog(final Log log) {
        super();
        this.log = log;
    }

    /**
     * Delegates to Maven.
     * @param message possibly null
     * @see org.apache.commons.logging.Log#debug(java.lang.Object)
     */
    public void debug(final Object message) {
        log.debug(new ToStringBuilder(message).toString());
    }

    /**
     * Delegates to Maven.
     * @param message possibly null
     * @param t cause
     * @see org.apache.commons.logging.Log#debug(java.lang.Object, java.lang.Throwable)
     */
    public void debug(final Object message, final Throwable t) {
        log.debug(new ToStringBuilder(message).toString(), t);
    }

    /**
     * Delegates to Maven.
     * @param message possibly null
     * @see org.apache.commons.logging.Log#error(java.lang.Object)
     */
    public void error(final Object message) {
        log.error(new ToStringBuilder(message).toString());
    }

    /**
     * Delegates to Maven.
     * @param message possibly null
     * @param t cause
     * @see org.apache.commons.logging.Log#error(java.lang.Object, java.lang.Throwable)
     */
    public void error(final Object message, final Throwable t) {
        log.error(new ToStringBuilder(message).toString(), t);
    }

    /**
     * Delegates to Maven.
     * @param message possibly null
     * @see org.apache.commons.logging.Log#fatal(java.lang.Object)
     */
    public void fatal(final Object message) {
        log.error(new ToStringBuilder(message).toString());
    }

    /**
     * Delegates to Maven.
     * @param message possibly null
     * @param t cause
     * @see org.apache.commons.logging.Log#fatal(java.lang.Object, java.lang.Throwable)
     */
    public void fatal(final Object message, final Throwable t) {
        log.error(new ToStringBuilder(message).toString(), t);
    }

    /**
     * Delegates to Maven.
     * @param message possibly null
     * @see org.apache.commons.logging.Log#info(java.lang.Object)
     */
    public void info(final Object message) {
        log.info(new ToStringBuilder(message).toString());
    }

    /**
     * Delegates to Maven.
     * @param message possibly null
     * @param t cause
     * @see org.apache.commons.logging.Log#info(java.lang.Object, java.lang.Throwable)
     */
    public void info(final Object message, final Throwable t) {
        log.info(new ToStringBuilder(message).toString(), t);
    }

    /**
     * Delegates to Maven.
     * @return true when debug is enabled in Maven
     * @see org.apache.commons.logging.Log#isDebugEnabled()
     */
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    /**
     * Delegates to Maven.
     * @return true when error is enabled in Maven
     * @see org.apache.commons.logging.Log#isErrorEnabled()
     */
    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    /**
     * Delegates to Maven.
     * @return true when error is enabled in Maven
     * @see org.apache.commons.logging.Log#isFatalEnabled()
     */
    public boolean isFatalEnabled() {
        return log.isErrorEnabled();
    }

    /**
     * Delegates to Maven.
     * @return true when info is enabled in Maven
     * @see org.apache.commons.logging.Log#isInfoEnabled()
     */
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    /**
     * Delegates to Maven.
     * @return true when trace is enabled in Maven
     * @see org.apache.commons.logging.Log#isTraceEnabled()
     */
    public boolean isTraceEnabled() {
        return log.isDebugEnabled();
    }

    /**
     * Delegates to Maven.
     * @return true when warn is enabled in Maven
     * @see org.apache.commons.logging.Log#isWarnEnabled()
     */
    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    /**
     * Delegates to Maven.
     * @param message possibly null
     * @see org.apache.commons.logging.Log#trace(java.lang.Object)
     */
    public void trace(final Object message) {
        log.debug(new ToStringBuilder(message).toString());
    }

    /**
     * Delegates to Maven.
     * @param message possibly null
     * @param t cause
     * @see org.apache.commons.logging.Log#trace(java.lang.Object, java.lang.Throwable)
     */
    public void trace(final Object message, final Throwable t) {
        log.debug(new ToStringBuilder(message).toString(), t);
    }

    /**
     * Delegates to Maven.
     * @param message possibly null
     * @see org.apache.commons.logging.Log#warn(java.lang.Object)
     */
    public void warn(final Object message) {
        log.warn(new ToStringBuilder(message).toString());
    }

    /**
     * Delegates to Maven.
     * @param message possibly null
     * @param t cause
     * @see org.apache.commons.logging.Log#warn(java.lang.Object, java.lang.Throwable)
     */
    public void warn(final Object message, final Throwable t) {
        log.warn(new ToStringBuilder(message).toString(), t);
    }
}
