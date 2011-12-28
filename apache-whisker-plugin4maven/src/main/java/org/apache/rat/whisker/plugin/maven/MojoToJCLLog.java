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
package org.apache.rat.whisker.plugin.maven;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.maven.plugin.logging.Log;

/**
 * 
 */
public class MojoToJCLLog implements org.apache.commons.logging.Log {
    
    private final Log log;

    public MojoToJCLLog(Log log) {
        super();
        this.log = log;
    }

    /**
     * @see org.apache.commons.logging.Log#debug(java.lang.Object)
     */
    @Override
    public void debug(Object message) {
        log.debug(ObjectUtils.toString(message));
    }

    /**
     * @see org.apache.commons.logging.Log#debug(java.lang.Object, java.lang.Throwable)
     */
    @Override
    public void debug(Object message, Throwable t) {
        log.debug(ObjectUtils.toString(message), t);
    }

    /**
     * @see org.apache.commons.logging.Log#error(java.lang.Object)
     */
    @Override
    public void error(Object message) {
        log.error(ObjectUtils.toString(message));
    }

    /**
     * @see org.apache.commons.logging.Log#error(java.lang.Object, java.lang.Throwable)
     */
    @Override
    public void error(Object message, Throwable t) {
        log.error(ObjectUtils.toString(message), t);
    }

    /**
     * @see org.apache.commons.logging.Log#fatal(java.lang.Object)
     */
    @Override
    public void fatal(Object message) {
        log.error(ObjectUtils.toString(message));
    }

    /**
     * @see org.apache.commons.logging.Log#fatal(java.lang.Object, java.lang.Throwable)
     */
    @Override
    public void fatal(Object message, Throwable t) {
        log.error(ObjectUtils.toString(message), t);
    }

    /**
     * @see org.apache.commons.logging.Log#info(java.lang.Object)
     */
    @Override
    public void info(Object message) {
        log.info(ObjectUtils.toString(message));
    }

    /**
     * @see org.apache.commons.logging.Log#info(java.lang.Object, java.lang.Throwable)
     */
    @Override
    public void info(Object message, Throwable t) {
        log.info(ObjectUtils.toString(message), t);
    }

    /**
     * @see org.apache.commons.logging.Log#isDebugEnabled()
     */
    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    /**
     * @see org.apache.commons.logging.Log#isErrorEnabled()
     */
    @Override
    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    /**
     * @see org.apache.commons.logging.Log#isFatalEnabled()
     */
    @Override
    public boolean isFatalEnabled() {
        return log.isErrorEnabled();
    }

    /**
     * @see org.apache.commons.logging.Log#isInfoEnabled()
     */
    @Override
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    /**
     * @see org.apache.commons.logging.Log#isTraceEnabled()
     */
    @Override
    public boolean isTraceEnabled() {
        return log.isDebugEnabled();
    }

    /**
     * @see org.apache.commons.logging.Log#isWarnEnabled()
     */
    @Override
    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    /**
     * @see org.apache.commons.logging.Log#trace(java.lang.Object)
     */
    @Override
    public void trace(Object message) {
        log.debug(ObjectUtils.toString(message));
    }

    /**
     * @see org.apache.commons.logging.Log#trace(java.lang.Object, java.lang.Throwable)
     */
    @Override
    public void trace(Object message, Throwable t) {
        log.debug(ObjectUtils.toString(message), t);
    }

    /**
     * @see org.apache.commons.logging.Log#warn(java.lang.Object)
     */
    @Override
    public void warn(Object message) {
        log.warn(ObjectUtils.toString(message));
    }

    /**
     * @see org.apache.commons.logging.Log#warn(java.lang.Object, java.lang.Throwable)
     */
    @Override
    public void warn(Object message, Throwable t) {
        log.warn(ObjectUtils.toString(message), t);
    }

}
