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
package org.apache.creadur.whisker.it;

import static org.apache.creadur.whisker.it.CheckHelpers.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileVerifier {

    final File licenseFile;
    final List<Check> checks;
    final String title;


    public FileVerifier(final File licenseFile, final String displayName) {
        this.licenseFile = licenseFile;
        this.checks = new ArrayList<Check>();
        this.title = "FAILURES in " + displayName + ":";
    }

    public FileVerifier expectThat(final Check check) {
        this.checks.add(check);
        return this;
    }

    public String failures() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(licenseFile), "UTF-8"));
        try {
            String line = null;
            do {
                line = checkLine(reader.readLine());
            } while (line != null);
        } finally {
            reader.close();
        }

        final Results results = new Results().titled(title);
        return to(results).report(checks).collate();
    }

    private String checkLine(final String line) {
        doCheck(line).with(checks);
        return line;
    }
}
