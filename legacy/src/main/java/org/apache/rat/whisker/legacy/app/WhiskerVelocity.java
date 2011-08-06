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
package org.apache.rat.whisker.legacy.app;


import org.apache.rat.whisker.legacy.out.Engine;

/**
 * 
 */
public class WhiskerVelocity extends Whisker {

    protected void doGenerate() throws Exception {
        new Engine().write(getLicenseDescriptor());
    }
    
    /**
     * @see org.apache.rat.whisker.legacy.app.Whisker#doValidate()
     */
    @Override
    protected void doValidate() throws Exception {
        new Engine().check(getLicenseDescriptor(), fileSystem());
    }

    /**
     * @see org.apache.rat.whisker.legacy.app.Whisker#doReport()
     */
    @Override
    protected void doReport() throws Exception {
        new Engine().write(fileSystem());
    }

    /**
     * @see org.apache.rat.whisker.legacy.app.Whisker#doTemplateGeneration()
     */
    @Override
    protected void doTemplateGeneration() throws Exception {
        new Engine().generateTemplate(fileSystem());
    }    
}
