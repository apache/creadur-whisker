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

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;


/**
 * Generates licensing related materials such as LICENSE and NOTICE documents
 * for assembled applications.
 * @goal generate
 */
public class GenerateMojo extends AbstractMojo {


    /**
     * This file contains a description of the licensing qualities of
     * the expected contents of the assembled application.
     * 
     * @required
     * @parameter expression="${apacheWhiskerDescriptor}"
     */
    private File descriptor;
    
    /**
     * Generate licensing related materials such as LICENSE and NOTICE documents.
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException { 
        if (descriptor.exists()) {
            if (descriptor.canRead()) {
                 
            } else {
                throw new MojoExecutionException("Read permission requires on Whisker descriptor file: " + descriptor);
            }
        } else {
            throw new MojoExecutionException("Whisker descriptor file is missing: " + descriptor);
        }
    }

}
