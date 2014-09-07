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
def mit = false
def thirdParty = false
def apacheOrg = false
def five = false
def copyright = false
def one = false
def two = false
def three = false
def six = false
def seven = false
def eight = false
def line = reader.readLine()
while (line != null) {
    if (line.contains("Apache License")) {
        apache = true
    }
    if (line.contains("Permission is hereby granted, free  of charge, to any person obtaining")) {
        mit = true
    }
    if (line.contains("Copyright (c) 3535 Someone")) {
        copyright = true
    }
    if (line.contains("This distribution contains third party resources.")) {
        thirdParty = true
    }
    if (line.contains("Apache Software Foundation")) {
        apacheOrg = true
    }
    
    if (line.contains("five.js")) {
        five = true
    }
    if (line.contains("one.text")) {
        one = true
    }
    if (line.contains("two.html")) {
        two = true
    }
    if (line.contains("three.py")) {
        three = true
    }
    if (line.contains("six.rb")) {
        six = true
    }
    if (line.contains("seven.coffee")) {
        seven = true
    }
    if (line.contains("eight.java")) {
        eight = true
    }
    
    line = reader.readLine()
}
assert apacheOrg
assert apache
assert thirdParty
assert mit
assert !five
assert !one
assert !two
assert !three
assert !six
assert seven
assert eight
    
return true;