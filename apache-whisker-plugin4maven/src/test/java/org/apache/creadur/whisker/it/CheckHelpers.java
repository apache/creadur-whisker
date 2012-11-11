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


import static org.apache.commons.collections.CollectionUtils.*;
import static org.apache.creadur.whisker.it.CheckHasPassedPredicate.*;
import static org.apache.creadur.whisker.it.Not.*;

import java.util.ArrayList;
import java.util.Collection;

public class CheckHelpers {

    public static AnyCheck aLineContainsEither(final String value, final String alternative) {
        return new AnyCheck(listAnyLineContains(value, alternative));
    }

    private static Collection<Check> listAnyLineContains(final String value,
            final String alternative) {
        final Collection<Check> checks = new ArrayList<Check>();
        checks.add(new AnyLineContainsCheck(value));
        checks.add(new AnyLineContainsCheck(alternative));
        return checks;
    }

    public static AllCheck containsBothLines(final String value, final String alternative) {
        return new AllCheck(listAnyLineContains(value, alternative));
    }

    public static boolean anyPassed(final Collection<Check> checks) {
        return exists(checks, checkPassed());
    }

    public static boolean allPassed(final Collection<Check> checks) {
        return not(anyFailed(checks));
    }

    public static boolean anyFailed(final Collection<Check> checks) {
        return exists(checks, checkFailed());
    }

    public static ReportClosure to(final Results results) {
        return new ReportClosure(results);
    }

    public static DoCheckClosure doCheck(final String line) {
        return new DoCheckClosure(line);
    }

}
