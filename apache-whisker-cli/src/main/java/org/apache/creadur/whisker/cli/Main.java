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
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.creadur.whisker.app.Act;
import org.apache.creadur.whisker.app.Whisker;
import org.apache.creadur.whisker.app.load.StreamableResourceFactory;
import org.apache.creadur.whisker.app.out.WriteResultsToSystemOutFactory;
import org.apache.creadur.whisker.out.velocity.VelocityEngine;

/**
 * Command line interface for whisker.
 */
public final class Main {

    /**
     * Prepended to help.
     */
    private static final String HELP_HEADER = "\nPass " +
    		"the descriptor containing license meta-data " +
    		"and the act to be done.\n";

    /**
     * Appended to help.
     */
    private static final String HELP_FOOTER =
        "\nApache Whisker assists assembled applications " +
        "maintain correct legal documentation. " +
        "Whisker is part of the " +
        "Apache Creadur suite of tools for " +
        "auditing and comprehending software distributions, " +
        "and is open source community developed software. " +
        "Get involved at http://www.apache.org.\n\n";

    /**
     * Names the application.
     */
    private static final String APP_NAME = "apache-whisker-cli";

    /**
     * Returns okay to system.
     */
    private static final int SYSTEM_EXIT_OK = 0;
    /** Error code returned to system when parameters cannot be parsed. */
    private static final int SYSTEM_EXIT_CLI_PARSE_FAILED = 1;

    /**
     * Bootstraps application.
     * @param args not null
     * @throws Exception when application fails unexpectedly
     */
    public static void main(final String[] args) throws Exception {
        System.exit(new Main(app()).run(args));
    }

    /**
     * Creates an instance of the application.
     * @return not null
     */
    private static Whisker app() {
        return new Whisker();
    }

    /** The application run. */
    private final Whisker whisker;

    /**
     * Constructs a wrapper for the given application.
     * @param whisker not null
     */
    public Main(final Whisker whisker) {
        super();
        this.whisker = whisker;
    }

    /**
     * Creates a parser for command line parameters.
     * Use GNU-style.
     * @return not null
     */
    private CommandLineParser parser() {
        return new GnuParser();
    }

    /**
     * Parses a line of arguments.
     * @param args not null
     * @return not null
     * @throws ParseException when parsing fails
     */
    public CommandLine parse(final String[] args) throws ParseException {
        return parser().parse(options(), args);
    }

    /**
     * Parses arguments and configures the application.
     * @param args not null
     * @return not null
     * @throws ParseException when arguments cannot be parsed
     */
    public Whisker configure(final String[] args) throws ParseException {
        return configure(parse(args));
    }

    /**
     * Configures the application from the command line given.
     * @param commandLine not null
     * @return not null
     * @throws MissingOptionException when a mandatory option
     * has not been supplied
     */
    private Whisker configure(
            final CommandLine commandLine) throws MissingOptionException {
        whisker.setEngine(new VelocityEngine(new SystemLog()));
        whisker.setSource(CommandLineOption.SOURCE.getOptionValue(commandLine));
        whisker.setLicenseDescriptor(
                new StreamableResourceFactory().streamFromResource(
                        licenseDescriptorName(commandLine)));
        whisker.setWriterFactory(new WriteResultsToSystemOutFactory());
        if (CommandLineOption.ACT_TO_AUDIT.isSetOn(commandLine)) {
            whisker.setAct(Act.AUDIT);
        } else if (CommandLineOption.ACT_TO_GENERATE.isSetOn(commandLine)) {
            whisker.setAct(Act.GENERATE);
        } else if (CommandLineOption.ACT_TO_SKELETON.isSetOn(commandLine)) {
            whisker.setAct(Act.SKELETON);
        }

        if (whisker.getSource() == null
                && whisker.getAct().isSourceRequired()) {
            throw new MissingOptionException("-"
                    + CommandLineOption.SOURCE.getShortName() + " "
                    + CommandLineOption.SOURCE.getDescription());
        }
        return whisker;
    }

    /**
     * Extracts the license descriptor name value,
     * @param commandLine not null
     * @return the value for the license descriptor name
     * passed from the command line
     */
    private String licenseDescriptorName(final CommandLine commandLine) {
        return CommandLineOption.LICENSE_DESCRIPTION
            .getOptionValue(commandLine);
    }

    /**
     * Runs Whisker.
     * @param args not null
     * @return system return code
     * @throws Exception when application unexpectedly fails
     */
    public int run(final String[] args) throws Exception {
        try {
            if (printHelp(args)) {
                help();
            } else {
                configure(args).act();
            }
            return SYSTEM_EXIT_OK;
        } catch (ParseException e) {
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println();
            help();
            return SYSTEM_EXIT_CLI_PARSE_FAILED;
        }
    }

    /**
     * Do these command line arguments ask for help?
     * @param args not null
     * @return true when command line contains option for help,
     * false otherwise
     * @throws ParseException in case options could not be read properly.
     */
    public boolean printHelp(String[] args) throws ParseException {
        final CommandLineOption help = CommandLineOption.PRINT_HELP;
        try {
            return help.isSetOn(
                parser().parse(new Options().addOption(
                        help.create()), args));
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Prints out help.
     */
    private void help() {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(APP_NAME, HELP_HEADER, options(), HELP_FOOTER, true);
    }

    /**
     * Builds options for the command line.
     * @return not null
     */
    private Options options() {
        return CommandLineOption.options();
    }
}
