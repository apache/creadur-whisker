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
package org.apache.creadur.whisker.model;

import java.util.Collection;
import java.util.TreeSet;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Collates resources within directories, flattening the
 * model.
 */
public class ResourceNamesCollator extends Visitor {

    /** Resources collected. */
    private final Collection<Pair<WithinDirectory, Resource>> resources 
        = new TreeSet<Pair<WithinDirectory, Resource>>();
    /** Duplicate resources discovered. */
    private final Collection<Pair<WithinDirectory, Resource>> duplicates 
        = new TreeSet<Pair<WithinDirectory, Resource>>();
    /** Last directory visited. */
    private WithinDirectory lastDirectory;

    /**
     * Gets the names of the resources collected.
     * @return not null
     */
    public Collection<String> getNames() {
        final Collection<String> names = new TreeSet<String>();
        for (final Pair<WithinDirectory, Resource> pair : this.resources) {
            names.add(pair.getRight().getName());
        }
        return names;
    }

    /**
     * Gets the duplicate resources discovered.
     * @return not null
     */
    public Collection<Pair<WithinDirectory, Resource>> getDuplicates() {
        return this.duplicates;
    }

    /**
     * Sets the last directory visited.
     * @see Visitor#visit(WithinDirectory)
     * @param directory not null
     */
    @Override
    public void visit(final WithinDirectory directory) {
        this.lastDirectory = directory;
    }

    /**
     * Collects this resource.
     * @see Visitor#visit(Resource)
     * @param resource not null
     */
    @Override
    public void visit(final Resource resource) {
        if (!this.resources.add(new ImmutablePair<WithinDirectory, Resource>(
                this.lastDirectory, resource))) {
            // Already added
            if (this.lastDirectory == null) {
                // Issue with logic which will result in a null pointer later
                throw new IllegalArgumentException(
                        "Expected directory to be present");
            }
            this.duplicates.add(new ImmutablePair<WithinDirectory, Resource>(
                    this.lastDirectory, resource));
      }
    }
}
