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
package org.apache.creadur.whisker.it;

public class Results {

    private final StringBuilder builder = new StringBuilder("***FAILED***\n");
    private boolean failed = false;

    public boolean hasFailed() {
        return failed;
    }

    public Results fail(String message) {
        builder.append("  ");
        builder.append(message);
        builder.append("\n");
        failed = true;
        return this;
    }

    public String collate() {
        final String result;
        if (failed) {
            endLine();
            endLine();
            endLine();
            result = builder.toString();
        } else {
            result = null;
        }
        return result;
    }

    private void endLine() {
        builder.append('\n');
    }

    public Results titled(final String title) {
        endLine();
        endLine();
        endLine();
        builder.append('#').append('#').append(' ').append(title);
        endLine();
        endLine();
        return this;
    }
}
