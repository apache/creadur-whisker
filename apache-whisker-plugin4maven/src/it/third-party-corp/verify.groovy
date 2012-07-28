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

assert !new File(basedir, "target/NOTICE").exists()

def license = new File(basedir, "target/LICENSE")
assert license.exists()  

def reader = new BufferedReader(new InputStreamReader(new FileInputStream(license), "UTF-8"))  

def alv2 = false
def foundation = false
def boo = false
def line = reader.readLine()
while (line != null) {
    if (line.contains("Apache License")) {
        apache = true
    }
    if (line.contains("The Example Foundation")) {
        foundation = true
    }
    if (line.contains("boo.png")) {
        boo = true
    }
    line = reader.readLine()
}
assert boo
assert apache
assert foundation

    
return true;