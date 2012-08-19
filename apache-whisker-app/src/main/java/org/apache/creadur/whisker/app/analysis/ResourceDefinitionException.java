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
package org.apache.creadur.whisker.app.analysis;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Indicates an issue with a resource definition.
 */
public final class ResourceDefinitionException extends Exception {

    /**
     * Composes an informative error message.
     * @param issues not null
     * @return informative error message, not null
     */
    private static String message(
            final Map<
                ResourceDefinitionError,
                Collection<ResourceDescription>> issues) {
        final StringBuilder builder =
                new StringBuilder("Resources definitions are incorrect. ");
        for (Map.Entry<
                ResourceDefinitionError,
                Collection<ResourceDescription>>
                    entry: issues.entrySet()) {
            final ResourceDefinitionError error = entry.getKey();
            final Collection<ResourceDescription> resources = entry.getValue();
            if (!resources.isEmpty()) {
                builder.append(error.getDescription()).append(": ");
                boolean firstTime = true;
                for (final ResourceDescription description: resources) {
                    if (firstTime) {
                        firstTime = false;
                    } else {
                        builder.append("; ");
                    }
                    builder
                        .append(description.getResource())
                        .append(" in ")
                        .append(description.getDirectory());
                }
                builder.append(". ");
            }
        }
        return builder.toString();
    }

    /** For serialisation. */
    private static final long serialVersionUID = -455455829914484243L;

    /**
     * Causal issues.
     */
    private final Map<
        ResourceDefinitionError,
        Collection<ResourceDescription>> issues;

    /**
     * Constructs an exception caused by the given issues.
     * @param issues not null
     */
    public ResourceDefinitionException(
            final Map<ResourceDefinitionError,
            Collection<ResourceDescription>> issues) {
        super(message(issues));
        this.issues = Collections.unmodifiableMap(issues);
    }

    /**
     * Gets causal issues.
     * @return the issues not null
     */
    public Map<
                ResourceDefinitionError,
                Collection<ResourceDescription>> getIssues() {
        return issues;
    }
}
