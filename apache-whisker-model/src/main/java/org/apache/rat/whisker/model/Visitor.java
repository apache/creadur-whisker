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
package org.apache.rat.whisker.model;

/**
 * 
 */
public abstract class Visitor {

    // Tune traversal
    public boolean traversePublicDomain() {
        return true;
    }
    
    public boolean traverseWithLicense() {
        return true;
    }
    
    public boolean traverseByOrganisation() {
        return true;
    }
    
    public boolean traverseResource() {
        return true;
    }

    // Classic visitor pattern
    public void visit(final WithinDirectory directory) {};
    public void visit(final WithLicense license) {};
    public void visit(final ByOrganisation byOrganisation) {};
    public void visit(final Resource resource) {};
}
