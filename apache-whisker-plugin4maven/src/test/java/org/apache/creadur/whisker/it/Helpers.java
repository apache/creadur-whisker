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

import static org.apache.creadur.whisker.it.Not.not;
import static org.apache.creadur.whisker.it.CheckHelpers.*;

import java.io.File;

public class Helpers {

    public static Check aLineContainsResource(String name) {
        return new AnyLineContainsCheck(name);
    }

    public static Check aLineContainsCopyrightNotice(String copyrightNotice) {
        return new AnyLineContainsCheck(copyrightNotice);
    }

    public static Check aLineContainsAL2() {
        return new AnyLineContainsCheck("Version 2.0, January 2004");
    }

    public static Check aLineContainsMIT() {
        return new AnyLineContainsCheck("Permission is hereby granted, free of charge, to any person");
    }

    public static Check aLineContainsCDDL1() {
        return aLineContainsEither(
                "COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Version 1.0",
                "COMMON DEVELOPMENT AND DISTRIBUTION LICENSE Version 1.0 (CDDL-1.0)");
    }

    public static Check aLineContains(String value) {
        return new AnyLineContainsCheck(value);
    }

    private static final String PATH_TO_LICENSE_FILE = "target/LICENSE";

    public static boolean noticeIsMissing(File basedir) {
        return fileIsMissing(basedir, "target/NOTICE");
    }

    private static boolean fileIsMissing(File basedir, final String string) {
        return not(new File(basedir, string).exists());
    }

    public static boolean licenseIsMissing(File basedir) {
        return fileIsMissing(basedir, PATH_TO_LICENSE_FILE);
    }

    public static LicenseVerifier licenseIn(File basedir) {
        return new LicenseVerifier(new File(basedir, PATH_TO_LICENSE_FILE));
    }

    public static Results results() {
        return new Results();
    }
}
