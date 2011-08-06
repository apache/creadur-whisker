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
package org.apache.rat.whisker.legacy.cli;

import org.apache.rat.whisker.legacy.app.Act;
import org.apache.rat.whisker.legacy.app.Whisker;
import org.apache.rat.whisker.legacy.app.WhiskerVelocity;

/**
 * Runs Whisker.
 */
public class Main {

    /**
     * Returns okay to system.
     */
    private static final int SYSTEM_EXIT_OK = 0;

    /**
     * Bootstraps application.
     * @param args 
     */
    public static void main(String[] args) throws Exception {
        app().setLicenseDescriptor("org/apache/rat/whisker/samples/james/war.xml").setBase("war").setAct(Act.GENERATE).act();
        System.exit(SYSTEM_EXIT_OK);
    }
    
    private static Whisker app() {
        return new WhiskerVelocity();
    }
}
