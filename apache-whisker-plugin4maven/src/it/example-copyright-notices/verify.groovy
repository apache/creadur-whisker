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

import static org.apache.creadur.whisker.it.Helpers.*

results = results();

if (noticeIsMissing(basedir)) {
    results.fail("NOTICE is missing")
}
if (licenseIsMissing(basedir)) {
    results.fail("LICENSE is missing")
}

if (results.hasFailed()) {
    return results.report();
} else {
     license = licenseIn(basedir)
     license.expectThat(aLineContainsCDDL1())
     license.expectThat(aLineContainsCopyrightNotice("Copyright (c) 2525 The Example Project"))
     license.expectThat(aLineContainsMIT())
     license.expectThat(aLineContainsResource("mit.txt"))
     license.expectThat(aLineContainsAL2())
     license.expectThat(noLineContains("Copyright (c) 9595 The Example Project"))
     license.expectThat(noLineContains("This product includes software developed at"))
     license.expectThat(noLineContains("The Example Foundation (http://example.org/)."))
     license.expectThat(aLineContainsResource("apache.txt"))
     notice = noticeIn(basedir)
     notice.expectThat(aLineContains("Copyright (c) 9595 The Example Project"))
     notice.expectThat(aLineContains("This product includes software developed at"))
     notice.expectThat(aLineContains("The Example Foundation (http://example.org/)."))
     notice.expectThat(noLineContains("Copyright (c) 2525 The Example Project"))
     return aggregate(license.failures(), notice.failures())
}