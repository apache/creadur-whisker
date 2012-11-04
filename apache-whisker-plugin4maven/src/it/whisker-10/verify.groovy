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
     license.expectThat(aLineContainsResource("waf"))
     license.expectThat(aLineContainsResource("httpd.conf" ))
     license.expectThat(aLineContainsResource("ports.conf" ))
     license.expectThat(aLineContainsResource("vhostexample.conf" ))
     license.expectThat(aLineContainsResource("sites-available/default" ))
     license.expectThat(aLineContainsResource("sites-available/default-ssl" ))
     license.expectThat(aLineContainsResource("vpcdnsmasq.conf" ))
     license.expectThat(aLineContainsResource("dnsmasq.conf" ))
     license.expectThat(aLineContainsResource("conntrackd.conf.templ" ))
     license.expectThat(aLineContainsResource("sshd_config" ))
     license.expectThat(aLineContainsResource("XenServerJava" ))
     license.expectThat(aLineContainsResource("jquery.js"))
     license.expectThat(aLineContainsResource("excanvas.js"))
     license.expectThat(aLineContainsResource("jquery.js"))
     license.expectThat(aLineContainsResource("jquery.validate.js" ))
     license.expectThat(aLineContainsResource("jquery.md5.js"))
     license.expectThat(aLineContainsResource("jquery.cookies.js" ))
     license.expectThat(aLineContainsResource("jquery.easing.js" ))
     license.expectThat(aLineContainsResource("reset.css"))
     license.expectThat(aLineContainsResource("jquery.flot.js"))
     license.expectThat(aLineContainsResource("jquery.flot.crosshair.js"))
     license.expectThat(aLineContainsResource("jquery.flot.fillbetween.js"))
     license.expectThat(aLineContainsResource("jquery.flot.image.js"))
     license.expectThat(aLineContainsResource("jquery.flot.navigate.js"))
     license.expectThat(aLineContainsResource("jquery.flot.resize.js"))
     license.expectThat(aLineContainsResource("jquery.flot.selection.js"))
     license.expectThat(aLineContainsResource("jquery.flot.stack.js"))
     license.expectThat(aLineContainsResource("jquery.flot.symbol.js"))
     license.expectThat(aLineContainsResource("jquery.flot.threshold.js"))
     license.expectThat(aLineContainsResource("jquery.pie.js"))
     license.expectThat(aLineContainsResource("jquery.colorhelpers.js"))
     license.expectThat(aLineContainsResource("css/jquery-ui.css" ))
     license.expectThat(aLineContainsResource("js/jquery-ui.js" ))
     license.expectThat(aLineContainsResource("index.html"))
     license.expectThat(aLineContainsResource("qunit.css"))
     license.expectThat(aLineContainsResource("qunit.js"))
     license.expectThat(aLineContainsResource("ScriptRunner.java"))
     license.expectThat(aLineContainsResource("libvirt-java-0.4.9"))
     license.expectThat(aLineContainsResource("cloud-axis.jar"))
     license.expectThat(aLineContainsResource("cloud-commons-codec-1.5.jar"))
     license.expectThat(aLineContainsResource("cloud-commons-collections-3.2.1.jar"))
     license.expectThat(aLineContainsResource("cloud-commons-configuration-1.8.jar"))
     license.expectThat(aLineContainsResource("cloud-commons-dbcp-1.4.jar"))
     license.expectThat(aLineContainsResource("cloud-commons-httpclient-3.1.jar"))
     license.expectThat(aLineContainsResource("cloud-commons-lang-2.6.jar"))
     license.expectThat(aLineContainsResource("cloud-commons-logging-1.1.1.jar"))
     license.expectThat(aLineContainsResource("cloud-commons-pool-1.5.6.jar"))
     license.expectThat(aLineContainsResource("cloud-log4j.jar"))
     license.expectThat(aLineContainsResource("cloud-log4j-extras.jar"))
     license.expectThat(aLineContainsResource("cloud-ws-commons-util-1.0.2.jar"))
     license.expectThat(aLineContainsResource("cloud-xmlrpc-client-3.1.3.jar"))
     license.expectThat(aLineContainsResource("cloud-xmlrpc-common-3.1.3.jar"))
     license.expectThat(aLineContainsResource("cloud-cglib.jar"))
     license.expectThat(aLineContainsResource("cloud-commons-discovery.jar"))
     license.expectThat(aLineContainsResource("cloud-jasypt-1.9.jar"))
     license.expectThat(aLineContainsResource("cloud-ehcache.jar"))
     license.expectThat(aLineContainsResource("cloud-ejb-api-3.0.jar"))
     license.expectThat(aLineContainsResource("cloud-jstl-1.2.jar"))
     license.expectThat(aLineContainsResource("cloud-email.jar"))
     license.expectThat(aLineContainsResource("cloud-google-gson-1.7.1.jar"))
     license.expectThat(aLineContainsResource("cloud-javax.persistence-2.0.0.jar"))
     license.expectThat(aLineContainsResource("CAStorSDK.jar"))
     license.expectThat(aLineContainsResource("cloud-jsch-0.1.42.jar"))
     license.expectThat(aLineContainsResource("cloud-wsdl4j-1.6.2.jar"))
     license.expectThat(aLineContainsResource("cloud-wsdl4j.jar"))
     license.expectThat(aLineContainsResource("cloud-xstream-1.3.1.jar"))
     license.expectThat(aLineContainsResource("cloud-bcprov-jdk16-1.45.jar"))
     license.expectThat(aLineContainsResource("cloud-trilead-ssh2-build213.jar"))
     license.expectThat(aLineContainsResource("jetty-6.1.26.jar"))
     license.expectThat(aLineContainsResource("jetty-util-6.1.26.jar"))
     license.expectThat(aLineContainsResource("cloud-junit.jar"))
     license.expectThat(aLineContainsResource("cloud-backport-util-concurrent-3.0.jar"))
     license.expectThat(aLineContainsResource("swift" ))
     license.expectThat(aLineContainsResource("swift" ))
     license.expectThat(aLineContainsResource("XmlSchema-1.4.3.jar"))
     license.expectThat(aLineContainsResource("antlr-2.7.6.jar"))
     license.expectThat(aLineContainsResource("slf4j-api-1.5.11.jar"))
     license.expectThat(aLineContainsResource("slf4j-jdk14-1.5.11.jar"))
     license.expectThat(aLineContainsResource("apache-log4j-extras-1.0.jar" ))
     license.expectThat(aLineContainsResource("axiom-api-1.2.8.jar"))
     license.expectThat(aLineContainsResource("axiom-impl-1.2.8.jar"))
     license.expectThat(aLineContainsResource("axis2-1.5.1.jar"))
     license.expectThat(aLineContainsResource("axis2-codegen-1.4.1.jar"))
     license.expectThat(aLineContainsResource("axis2-adb-1.5.1.jar"))
     license.expectThat(aLineContainsResource("axis2-ant-plugin-1.5.1.jar"))
     license.expectThat(aLineContainsResource("axis2-jaxbri-1.5.1.jar"))
     license.expectThat(aLineContainsResource("axis2-jaxws-1.5.1.jar"))
     license.expectThat(aLineContainsResource("axis2-jibx-1.5.1.jar"))
     license.expectThat(aLineContainsResource("axis2-json-1.5.1.jar"))
     license.expectThat(aLineContainsResource("axis2-kernel-1.5.1.jar"))
     license.expectThat(aLineContainsResource("axis2-transport-http-1.5.1.jar"))
     license.expectThat(aLineContainsResource("axis2-transport-local-1.5.1.jar"))
     license.expectThat(aLineContainsResource("axis2-webapp-1.5.1.war"))
     license.expectThat(aLineContainsResource("commons-codec-1.4.jar"))
     license.expectThat(aLineContainsResource("commons-collections-3.1.jar"))
     license.expectThat(aLineContainsResource("commons-fileupload-1.2.jar"))
     license.expectThat(aLineContainsResource("commons-httpclient-3.1.jar"))
     license.expectThat(aLineContainsResource("commons-io-1.4.jar"))
     license.expectThat(aLineContainsResource("commons-logging-1.1.1.jar"))
     license.expectThat(aLineContainsResource("httpcore-4.0.jar"))
     license.expectThat(aLineContainsResource("log4j-1.2.15.jar"))
     license.expectThat(aLineContainsResource("neethi-2.0.4.jar"))
     license.expectThat(aLineContainsResource("rampart-lib"))
     license.expectThat(aLineContainsResource("woden-api-1.0M8.jar"))
     license.expectThat(aLineContainsResource("woden-impl-dom-1.0M8.jar"))
     license.expectThat(aLineContainsResource("xercesImpl.jar"))
     license.expectThat(aLineContainsResource("xml-apis.jar"))
     license.expectThat(aLineContainsResource("wss4j-1.5.8.jar"))
     license.expectThat(aLineContainsResource("cloud-gson.jar"))
     license.expectThat(aLineContainsResource("dom4j-1.6.1.jar"))
     license.expectThat(aLineContainsResource("jaxb-api-2.1.jar"))
     license.expectThat(aLineContainsResource("jaxb-impl-2.1.7.jar"))
     license.expectThat(aLineContainsResource("jaxb-xjc-2.1.7.jar"))
     license.expectThat(aLineContainsResource("jta-1.1.jar"))
     license.expectThat(aLineContainsResource("jsch-0.1.42.jar"))
     license.expectThat(aLineContainsResource("json_simple-1.1.jar"))
     license.expectThat(aLineContainsResource("mail-1.4.jar"))
     license.expectThat(aLineContainsResource("junit-4.8.1.jar"))
     license.expectThat(aLineContainsResource("javassist-3.9.0.GA.jar" ))
     return license.failures()
}