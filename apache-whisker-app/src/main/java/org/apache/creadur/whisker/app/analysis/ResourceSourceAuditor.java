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

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.creadur.whisker.model.Resource;
import org.apache.creadur.whisker.model.Visitor;
import org.apache.creadur.whisker.model.WithLicense;
import org.apache.creadur.whisker.model.WithinDirectory;

/**
 * Collects resource source details.
 */
public final class ResourceSourceAuditor extends Visitor {
    /** Last license visited. */
    private WithLicense lastLicense;
    /** Last directory visited. */
    private WithinDirectory lastDirectory;
    /** Collects resources that are missing source. */
    private final Collection<
        Pair<WithinDirectory, Resource>> resourcesMissingSource
            = new TreeSet<Pair<WithinDirectory, Resource>>();
    /** Collects resources that match a source. */
    private final Collection<
        Pair<WithinDirectory, Resource>> resourcesWithSource
            = new TreeSet<Pair<WithinDirectory, Resource>>();

    /**
     * Hook for public domain.
     * @return false (no need to traverse public domain)
     * @see Visitor#traversePublicDomain()
     */
    @Override
    public boolean traversePublicDomain() {
        // no need to traverse public domain
        return false;
    }


    /**
     * Gets the resources with source collected during visits.
     * @return the resourcesWithSource, not null
     */
    public Collection<
            Pair<WithinDirectory, Resource>> getResourcesWithSource() {
        return resourcesWithSource;
    }

    /**
     * Gets the resources missing source collected during visits.
     * @return the resourcesMissingSource not null
     */
    public Collection<
            Pair<WithinDirectory, Resource>> getResourcesMissingSource() {
        return resourcesMissingSource;
    }

    /**
     * Accepts a directory visit.
     * @param directory not null
     * @see Visitor#visit(WithinDirectory)
     */
    @Override
    public void visit(final WithinDirectory directory) {
        this.lastDirectory = directory;
    }



    /**
     * Accepts a license visit.
     * @param license not null
     * @see Visitor#visit(WithLicense)
     */
    @Override
    public void visit(final WithLicense license) {
        this.lastLicense = license;
    }

    /**
     * Accepts a visit to a resource.
     * @param resource not null
     * @see Visitor#visit(Resource)
     */
    @Override
    public void visit(final Resource resource) {
        if (lastLicense == null) {
            throw new IllegalArgumentException(
                    "Last license unexpectedly null for resource "
                            + resource);
        } else if (lastLicense.isSourceRequired()) {
            final ImmutablePair<WithinDirectory, Resource> pair =
                    new ImmutablePair<
                        WithinDirectory,
                        Resource>(lastDirectory, resource);
            if (resource.hasSource()) {
                resourcesWithSource.add(pair);
            } else {
                resourcesMissingSource.add(pair);
            }
        }
    }


    /**
     * Gets those resources visited which require source links.
     * @return not null, possibly empty
     */
    public Collection<Resource> getResourcesRequiringSourceLinks() {
        final Collection<Resource> results = new ArrayList<Resource>();
        for (Pair<WithinDirectory, Resource> pair: resourcesWithSource) {
            results.add(pair.getRight());
        }
        return results;
    }

}
