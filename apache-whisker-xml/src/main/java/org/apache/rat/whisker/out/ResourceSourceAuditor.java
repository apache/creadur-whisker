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
package org.apache.rat.whisker.out;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * 
 */
public class ResourceSourceAuditor extends Visitor {

    private WithLicense lastLicense;
    private WithinDirectory lastDirectory;
    
    private final Collection<Pair<WithinDirectory, Resource>> resourcesMissingSource 
        = new TreeSet<Pair<WithinDirectory, Resource>>();
    
    private final Collection<Pair<WithinDirectory, Resource>> resourcesWithSource 
    = new TreeSet<Pair<WithinDirectory, Resource>>();
    
    /**
     * @see org.apache.rat.whisker.legacy.out.Visitor#traversePublicDomain()
     */
    @Override
    public boolean traversePublicDomain() {
        // TODO: no need to traverse public domain 
        // TODO: think about API 
        return false;
    }

    
    /**
     * @return the resourcesWithSource
     */
    public Collection<Pair<WithinDirectory, Resource>> getResourcesWithSource() {
        return resourcesWithSource;
    }

    /**
     * @return the resourcesMissingSource
     */
    public Collection<Pair<WithinDirectory, Resource>> getResourcesMissingSource() {
        return resourcesMissingSource;
    }

    /**
     * @see org.apache.rat.whisker.legacy.out.Visitor#visit(org.apache.rat.whisker.legacy.out.WithinDirectory)
     */
    @Override
    public void visit(WithinDirectory directory) {
        this.lastDirectory = directory;
    }



    /**
     * @see org.apache.rat.whisker.legacy.out.Visitor#visit(org.apache.rat.whisker.legacy.out.WithLicense)
     */
    @Override
    public void visit(WithLicense license) {
        this.lastLicense = license;
    }

    /**
     * @see org.apache.rat.whisker.legacy.out.Visitor#visit(org.apache.rat.whisker.legacy.out.Resource)
     */
    @Override
    public void visit(Resource resource) {
        if (lastLicense == null) {
            throw new IllegalArgumentException("Last license unexpectedly null for resource " + resource);
        } else if (lastLicense.isSourceRequired()) {
            // TODO: Consider using ResourceDescription
            final ImmutablePair<WithinDirectory, Resource> pair = new ImmutablePair<WithinDirectory, Resource>(lastDirectory, resource);
            if (resource.hasSource()) {
                resourcesWithSource.add(pair);
            } else {
                resourcesMissingSource.add(pair);
            }
        } else {
            // Source not required
        }
    }


    /**
     * @return
     */
    public Collection<Resource> getResourcesRequiringSourceLinks() {
        final Collection<Resource> results = new ArrayList<Resource>();
        for (Pair<WithinDirectory, Resource> pair: resourcesWithSource) {
            results.add(pair.getRight());
        }
        return results;
    }
    
}
