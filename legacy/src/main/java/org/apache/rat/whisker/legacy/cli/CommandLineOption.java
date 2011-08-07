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
package org.apache.rat.whisker.legacy.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

/**
 * Describes an option on the command line.
 */
public enum CommandLineOption {
    
    LICENSE_DESCRIPTION("license-descriptor", 'l', "use given license descriptor", true, "file");
    
    /**
     * Creates options for the command line.
     * @return not null
     */
    public static Options options() {
        Options options = new Options();
        for (final CommandLineOption option: values()) {
            options.addOption(option.create());
        }
        return options;
    }

    
    private final String longName;
    private final char shortName;
    private final String description;
    private final boolean required;
    private final String argument;
    
    private CommandLineOption(final String longName, 
            final char shortName, 
            final String description, 
            final boolean required,
            final String argument) {
        this.longName = longName;
        this.shortName = shortName;
        this.description = description;
        this.required = required;
        this.argument = argument;
    }
    
    public String getLongName() {
        return longName;
    }

    public char getShortName() {
        return shortName;
    }

    public String getDescription() {
        return description;
    }

    @SuppressWarnings("static-access")
    public Option create() {
        final OptionBuilder builder = OptionBuilder
            .isRequired(required)
            .withDescription(getDescription())
            .withLongOpt(getLongName());
        if (argument != null) {
            builder.hasArg().withArgName(argument);
        }
        return builder.create(getShortName());
    }

    /**
     * @param commandLine
     * @return
     */
    public String getOptionValue(CommandLine commandLine) {
        return commandLine.getOptionValue(getShortName());
    }
}