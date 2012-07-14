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
package org.apache.creadur.whisker.out.velocity;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.creadur.whisker.app.Result;
import org.apache.creadur.whisker.app.ResultWriterFactory;

public class StringResultWriterFactory implements ResultWriterFactory {
    
    Map<Result, List<StringWriter>> results = new HashMap<Result, List<StringWriter>>();
    
    public StringResultWriterFactory() {
        for (Result result: Result.values()) {
            results.put(result, new ArrayList<StringWriter>());
        }
    }

    public Writer writerFor(Result result) throws IOException {
        final StringWriter writer = new StringWriter();
        results.get(result).add(writer);
        return writer;
    }
    
    public int requestsFor(Result result) {
        return results.get(result).size();
    }
    
    public String firstOutputFor(Result result) {
        return results.get(result).get(0).toString();
    }
}
