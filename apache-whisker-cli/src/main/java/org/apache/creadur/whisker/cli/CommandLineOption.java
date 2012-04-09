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
package org.apache.creadur.whisker.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;

/**
 * Describes an option on the command line.
 */
public enum CommandLineOption {
    
    LICENSE_DESCRIPTION("license-descriptor", 'l', "use given license descriptor", true, "file", false),
    SOURCE("source", 's', "application source", false, "dir", false),
    ACT_TO_GENERATE("generate", 'g', "generate license and notice", false, null, true),
    ACT_TO_AUDIT("audit", 'a', "report audit details", false, null, true);
    
    /**
     * Creates options for the command line.
     * @return not null
     */
    public static Options options() {
        final Options options = new Options();
        final OptionGroup acts = new OptionGroup();
        acts.setRequired(true);
        for (final CommandLineOption option: values()) {
            final Option cliOption = option.create();
            if (option.isAct) {
                acts.addOption(cliOption);
            } else {
                options.addOption(cliOption);
            }
        }
        options.addOptionGroup(acts);
        return options;
    }

    
    private final String longName;
    private final char shortName;
    private final String description;
    private final boolean required;
    private final String argument;
    private final boolean isAct;
    
    private CommandLineOption(final String longName, 
            final char shortName, 
            final String description, 
            final boolean required,
            final String argument,
            final boolean isAct) {
        this.longName = longName;
        this.shortName = shortName;
        this.description = description;
        this.required = required;
        this.argument = argument;
        this.isAct = isAct;
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

    /**
     * @param commandLine
     * @return
     */
    public boolean isSetOn(CommandLine commandLine) {
        return commandLine.hasOption(getShortName());
    }
}