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
package org.apache.creadur.whisker.cli;

import org.apache.commons.logging.Log;

/**
 * Logs important messages to system err.
 */
public final class SystemLog implements Log {

    /**
     * Ignored.
     * @param message possibly null
     * @see org.apache.commons.logging.Log#debug(java.lang.Object)
     */
    @Override
    public void debug(final Object message) { }

    /**
     * Ignored.
     * @param message possibly null
     * @param t possibly null
     * @see Log#debug(java.lang.Object, java.lang.Throwable)
     */
    @Override
    public void debug(final Object message, final Throwable t) { }

    /**
     * Logs to stderr.
     * @param message possibly null
     * @see org.apache.commons.logging.Log#error(java.lang.Object)
     */
    @Override
    public void error(final Object message) {
        err(message);
    }

    /**
     * Logs to stderr.
     * @param message not null
     */
    private void err(final Object message) {
        System.err.println(message);
    }

    /**
     * Logs to stderr.
     * @param message possibly null
     * @param t possibly null
     * @see Log#error(java.lang.Object, java.lang.Throwable)
     */
    @Override
    public void error(final Object message, final Throwable t) {
        err(message, t);
    }

    /**
     * Logs to stderr.
     * @param message possibly null
     * @param t possibly null
     */
    private void err(final Object message, final Throwable t) {
        err(message);
        t.printStackTrace();
    }

    /**
     * Logs to stderr.
     * @param message possibly null
     * @see org.apache.commons.logging.Log#fatal(java.lang.Object)
     */
    @Override
    public void fatal(final Object message) {
        err(message);
    }

    /**
     * Logs to stderr.
     * @param message possibly null
     * @param t possibly null
     * @see Log#fatal(java.lang.Object, java.lang.Throwable)
     */
    @Override
    public void fatal(final Object message, final Throwable t) {
        err(message, t);
    }

    /**
     * Ignored.
     * @param message possibly null
     * @see Log#info(java.lang.Object)
     */
    @Override
    public void info(final Object message) {
    }

    /**
     * Ignored.
     * @param message possibly null
     * @param t possibly null
     * @see #info(java.lang.Object, java.lang.Throwable)
     */
    @Override
    public void info(final Object message, final Throwable t) {
    }

    /**
     * Disabled.
     * @return false
     * @see org.apache.commons.logging.Log#isDebugEnabled()
     */
    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    /**
     * Enabled.
     * @return true
     * @see org.apache.commons.logging.Log#isErrorEnabled()
     */
    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    /**
     * Enabled.
     * @return true
     * @see org.apache.commons.logging.Log#isFatalEnabled()
     */
    @Override
    public boolean isFatalEnabled() {
        return true;
    }

    /**
     * Disabled.
     * @return false
     * @see org.apache.commons.logging.Log#isInfoEnabled()
     */
    @Override
    public boolean isInfoEnabled() {
        return false;
    }

    /**
     * Disabled.
     * @return false
     * @see org.apache.commons.logging.Log#isTraceEnabled()
     */
    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    /**
     * Disabled.
     * @return false
     * @see org.apache.commons.logging.Log#isWarnEnabled()
     */
    @Override
    public boolean isWarnEnabled() {
        return false;
    }

    /**
     * Ignored.
     * @param message possibly null
     * @see org.apache.commons.logging.Log#trace(java.lang.Object)
     */
    @Override
    public void trace(final Object message) {
    }

    /**
     * Ignored.
     * @param message possibly null
     * @param t possibly null
     * @see Log#trace(java.lang.Object, java.lang.Throwable)
     */
    @Override
    public void trace(final Object message, final Throwable t) {
    }

    /**
     * Ignored.
     * @param message possibly null
     * @see org.apache.commons.logging.Log#warn(java.lang.Object)
     */
    @Override
    public void warn(final Object message) {
    }

    /**
     * Ignored.
     * @param message possibly null
     * @param t possibly null
     * @see Log#warn(java.lang.Object, java.lang.Throwable)
     */
    @Override
    public void warn(final Object message, final Throwable t) {
    }
}
