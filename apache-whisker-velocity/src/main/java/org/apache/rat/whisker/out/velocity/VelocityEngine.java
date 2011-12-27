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
package org.apache.rat.whisker.out.velocity;

import java.util.Collection;

import org.apache.rat.whisker.app.AbstractEngine;
import org.apache.rat.whisker.app.ResultWriterFactory;
import org.apache.rat.whisker.app.analysis.LicenseAnalyst;
import org.apache.rat.whisker.model.Descriptor;
import org.apache.rat.whisker.scan.Directory;

/**
 * 
 */
public class VelocityEngine extends AbstractEngine {

    /**
     * @see org.apache.rat.whisker.app.AbstractEngine#generateTemplate(java.util.Collection, ResultWriterFactory)
     */
    @Override
    public AbstractEngine generateTemplate(Collection<Directory> withBase, ResultWriterFactory writerFactory)
            throws Exception {
        new VelocityReports(writerFactory).generateTemplate(withBase);
        return this;
    }

    /**
     * @see org.apache.rat.whisker.app.AbstractEngine#validate(org.apache.rat.whisker.app.LicenseAnalyst, ResultWriterFactory)
     */
    @Override
    public AbstractEngine validate(LicenseAnalyst analyst, ResultWriterFactory writerFactory) throws Exception {
        new VelocityReports(writerFactory).validate(analyst);
        return this;
    }

    /**
     * @see org.apache.rat.whisker.app.AbstractEngine#report(java.util.Collection, ResultWriterFactory)
     */
    @Override
    public AbstractEngine report(Collection<Directory> directories, ResultWriterFactory writerFactory)
            throws Exception {
        new VelocityReports(writerFactory).report(directories);
        return this;
    }

    /**
     * @see org.apache.rat.whisker.app.AbstractEngine#generate(org.apache.rat.whisker.model.Descriptor, ResultWriterFactory)
     */
    @Override
    public AbstractEngine generate(Descriptor work, ResultWriterFactory writerFactory) throws Exception {
        new VelocityReports(writerFactory).generate(work);
        return this;
    }
    


}
