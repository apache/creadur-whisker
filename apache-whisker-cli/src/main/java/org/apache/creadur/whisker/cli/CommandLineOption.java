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

    /** License descriptor command line argument. */
    LICENSE_DESCRIPTION("license-descriptor", 'l',
            "use given license descriptor", true, "file", false),
    /** Application source command line argument. */
    SOURCE("source", 's', "application source", false, "dir", false),
    /** Generation command line argument. */
    ACT_TO_GENERATE("generate", 'g',
            "generate license and notice", false, null, true),
    /** Audit command line argument. */
    ACT_TO_AUDIT("audit", 'a', "report audit details", false, null, true),
    /** Generate skeleton mete-data command line argument. */
    ACT_TO_SKELETON("skeleton", 't',
            "generates skeleton meta-data", false, null, true),
    /** Print help then exit, ignoring other options. */
    PRINT_HELP("help", 'h', "print help then exit, ignoring other options.", false, null, false);

    /**
     * Creates options for the command line.
     * @return not null
     */
    public static Options options() {
        final Options options = new Options();
        final OptionGroup acts = new OptionGroup();
        acts.setRequired(true);
        for (final CommandLineOption option : values()) {
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

    /** The long name used for this command line argument. */
    private final String longName;
    /** The character short for this command line argument.*/
    private final char shortName;
    /** A description of this command line argument suitable for user.*/
    private final String description;
    /** Is this a mandatory argument? */
    private final boolean required;
    /** The argument name. */
    private final String argument;
    /** Is this argument within the act group? */
    private final boolean isAct;

    /**
     * Describes one argument.
     * @param longName not null
     * @param shortName not null
     * @param description not null
     * @param required is this mandatory?
     * @param argument possibly null
     * @param isAct is this argument an act?
     */
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

    /**
     * Gets the long name of this command line argument.
     * @return not null
     */
    public String getLongName() {
        return longName;
    }

    /**
     * Gets the short name of this command line argument.
     * @return the character short for this option
     */
    public char getShortName() {
        return shortName;
    }

    /**
     * Gets the description for this option.
     * @return not null
     */
    public String getDescription() {
        return description;
    }

    /**
     * Builds a representation.
     * @return not null
     */
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
     * Gets an option value from the command line.
     * @param commandLine not null
     * @return not null
     */
    public String getOptionValue(final CommandLine commandLine) {
        return commandLine.getOptionValue(getShortName());
    }

    /**
     * Is the short name option set?
     * @param commandLine not null
     * @return true when the option is present, false otherwise
     */
    public boolean isSetOn(final CommandLine commandLine) {
        return commandLine.hasOption(getShortName());
    }
}
