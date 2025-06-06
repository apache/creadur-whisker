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
import static org.apache.creadur.whisker.it.Not.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AnyCheck implements Check {

    final List<Check> checks;

    public AnyCheck(Collection<Check> checks) {
        super();
        this.checks = new ArrayList<Check>(checks);
    }

    public void check(final String line) {
        doCheck(line).with(checks);
    }

    public boolean hasPassed() {
        return checks.isEmpty() || anyPassed(checks);
    }

    public void report(final Results results) {
       if (not(hasPassed())) {
           to(results).report(checks);
       }
    }

}
