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
package org.apache.creadur.whisker.plugin.maven;

import java.io.File;

import org.apache.creadur.whisker.app.Act;
import org.apache.creadur.whisker.app.Whisker;
import org.apache.creadur.whisker.app.load.StreamableResourceFactory;
import org.apache.creadur.whisker.app.out.WriteResultsIntoDirectoryFactory;
import org.apache.creadur.whisker.out.velocity.VelocityEngine;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.*;

/**
 * Generates licensing related materials such as LICENSE and NOTICE documents
 * for assembled applications. The plugin is not bound to a specific lifecycle phase.
 */
@Mojo(name = "generate", requiresProject = false)
public class GenerateMojo extends AbstractMojo {

    /**
     * Destination for generated materials
     */
	@Parameter(defaultValue = "${project.build.directory}")
    private File outputDirectory;

    /**
     * The licensing materials will be encoding thus.
     */
	@Parameter(property = "outputEncoding", defaultValue = "${project.build.sourceEncoding}")
    private String outputEncoding;

    /**
     * This file contains a description of the licensing qualities of
     * the expected contents of the assembled application.
     */
	@Parameter(property = "apacheWhiskerDescriptor", required = true)
    private File descriptor;

    /**
     * Generate licensing related materials such as LICENSE and NOTICE documents.
     * @throws MojoExecutionException when Whisker fails,
     * or when configured cannot be executed
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute() throws MojoExecutionException {
        if (descriptor.exists()) {
            if (descriptor.canRead()) {
                 try {
                    new Whisker().setLicenseDescriptor(new StreamableResourceFactory().streamFromFileResource(descriptor))
                        .setEngine(new VelocityEngine(new MojoToJCLLog(getLog())))
                        .setWriterFactory(new WriteResultsIntoDirectoryFactory(outputDirectory, outputEncoding))
                        .setAct(Act.GENERATE).act();
                } catch (Exception e) {
                    throw new MojoExecutionException("Whisker failed to generate materials: " + e.getMessage(), e);
                }
            } else {
                throw new MojoExecutionException("Read permission requires on Whisker descriptor file: " + descriptor);
            }
        } else {
            throw new MojoExecutionException("Whisker descriptor file is missing: " + descriptor);
        }
    }

}
