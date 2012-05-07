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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.lang3.StringUtils;

/**
 * Manages indentation level.
 */
public class Indentation {

    /**
     * Creates appropriate indentation, padding with spaces.
     * @param indentation not null
     * @param source not null
     * @return appropriate indentation, not null
     * @throws IOException when source cannot be read
     */
    public final String indent(
            final int indentation, final Object source) throws IOException {
        return this.indent(indentation, source, ' ');
    }

    /**
     * Creates appropriate indentation.
     * @param indentation not null
     * @param source not null
     * @param pad padding
     * @return appropriate indentation, not null
     * @throws IOException when source cannot be read
     */
    public final String indent(
            final int indentation, final Object source, final char pad)
                throws IOException {
        final String result;
        if (source == null) {
            result = "";
        } else {
            result = prefixLine(source, StringUtils.repeat(pad, indentation));
        }
        return result;
    }

    /**
     * Creates appropriate indentation.
     * @param source not null
     * @param prefix not null
     * @return appropriate indentation, not null
     * @throws IOException when source cannot be read
     */
    private String prefixLine(final Object source, final String prefix)
            throws IOException {
        final String result;
        final StringBuilder builder = new StringBuilder();
        final BufferedReader lineReader =
            new BufferedReader(new StringReader(source.toString()));
        String line = lineReader.readLine();
        while (line != null) {
            builder.append('\n').append(prefix).append(line);
            line = lineReader.readLine();
        }
        result = builder.toString();
        return result;
    }
}
