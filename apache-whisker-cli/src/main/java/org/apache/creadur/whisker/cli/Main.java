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
import org.apache.rat.whisker.app.Act;
import org.apache.rat.whisker.app.Whisker;
import org.apache.rat.whisker.app.load.StreamableResourceFactory;
import org.apache.rat.whisker.app.out.WriteResultsToSystemOutFactory;
import org.apache.rat.whisker.out.velocity.VelocityEngine;

/**
 * Command line interface for whisker.
 */
public final class Main {

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
     * @throws Exception when application unexpectedly fails
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
                new StreamableResourceFactory().streamFromClassPathResource(
                        CommandLineOption.LICENSE_DESCRIPTION
                            .getOptionValue(commandLine)));
        whisker.setWriterFactory(new WriteResultsToSystemOutFactory());
        if (CommandLineOption.ACT_TO_AUDIT.isSetOn(commandLine)) {
            whisker.setAct(Act.AUDIT);
        } else if (CommandLineOption.ACT_TO_GENERATE.isSetOn(commandLine)) {
            whisker.setAct(Act.GENERATE);
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
            System.out.println(e.getMessage());
            help();
            return SYSTEM_EXIT_CLI_PARSE_FAILED;
        }
    }

    /**
     * Do these command line arguments ask for help?
     * @param args not null
     * @return true when command line contains option for help,
     * false otherwise
     * @throws ParseException 
     */
    private boolean printHelp(String[] args) throws ParseException {
        final CommandLineOption help = CommandLineOption.PRINT_HELP;
        return help.isSetOn(
                parser().parse(new Options().addOption(
                        help.create()), args));
    }

    /**
     * Prints out help.
     */
    private void help() {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(APP_NAME, options());
    }

    /**
     * Builds options for the command line.
     * @return not null
     */
    private Options options() {
        return CommandLineOption.options();
    }
}
