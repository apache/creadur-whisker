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
 * 
 */
public class ResourceNamesCollator extends Visitor {
    
    private final Collection<Pair<WithinDirectory, Resource>> resources = new TreeSet<Pair<WithinDirectory, Resource>>();
    private final Collection<Pair<WithinDirectory, Resource>> duplicates = new TreeSet<Pair<WithinDirectory, Resource>>();
    private WithinDirectory lastDirectory; 

    /**
     * @return the names
     */
    public Collection<String> getNames() {
        final Collection<String> names = new TreeSet<String>();
        for (Pair<WithinDirectory, Resource> pair: resources) {
            names.add(pair.getRight().getName());
        }
        return names;
    }

    /**
     * @return the duplicates
     */
    public Collection<Pair<WithinDirectory, Resource>> getDuplicates() {
        return duplicates;
    }

    
    
    /**
     * @see org.apache.rat.whisker.legacy.out.Visitor#visit(org.apache.rat.whisker.legacy.out.WithinDirectory)
     */
    @Override
    public void visit(WithinDirectory directory) {
        this.lastDirectory = directory;
    }

    /**
     * @see org.apache.rat.whisker.legacy.out.Visitor#visit(org.apache.rat.whisker.legacy.out.Resource)
     */
    @Override
    public void visit(Resource resource) {
        if (resources.add(new ImmutablePair<WithinDirectory, Resource>(lastDirectory, resource))) {
            // Fine
        } else {
            // Already added
            if (lastDirectory == null) {
                // Issue with logic which will result in a null pointer later
                throw new IllegalArgumentException("Expected directory to be present");
            }
            duplicates.add(new ImmutablePair<WithinDirectory, Resource>(lastDirectory, resource));
        }
    }
}
