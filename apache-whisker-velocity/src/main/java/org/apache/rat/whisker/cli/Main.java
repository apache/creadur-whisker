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
package org.apache.rat.whisker.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.rat.whisker.app.Act;
import org.apache.rat.whisker.app.Whisker;

/**
 * Command line interface for whisker.
 */
public class Main {

    /**
     * 
     */
    private static final String APP_NAME = "apache-rat-whisker";
    
    /**
     * Returns okay to system.
     */
    private static final int SYSTEM_EXIT_OK = 0;
    private static final int SYSTEM_EXIT_CLI_PARSE_FAILED = 1;

    /**
     * Bootstraps application.
     * @param args 
     */
    public static void main(String[] args) throws Exception {
        System.exit(new Main(app()).run(args));
    }
    
    private static Whisker app() {
        return new Whisker();
    }
    
    private final Whisker whisker;

    public Main(Whisker whisker) {
        super();
        this.whisker = whisker;
    }

    private CommandLineParser parser() {
        return new GnuParser();
    }
    
    public CommandLine parse(final String[] args) throws ParseException {
        return parser().parse(options(), args);
    }

    public Whisker configure(final String[] args) throws ParseException {
        return configure(parse(args));
    }
    
    /**
     * Configures the application from the command line given.
     * @param parse commandLine not null
     * @return not null
     * @throws MissingOptionException 
     */
    private Whisker configure(final CommandLine commandLine) throws MissingOptionException {
        whisker.setSource(CommandLineOption.SOURCE.getOptionValue(commandLine));
        whisker.setLicenseDescriptor(CommandLineOption.LICENSE_DESCRIPTION.getOptionValue(commandLine));
        if (CommandLineOption.ACT_TO_AUDIT.isSetOn(commandLine)) {
            whisker.setAct(Act.AUDIT);
        } else if (CommandLineOption.ACT_TO_GENERATE.isSetOn(commandLine)) {
            whisker.setAct(Act.GENERATE);
        }
        
        if (whisker.getSource() == null && whisker.getAct().isSourceRequired()) {
            throw new MissingOptionException("-" + CommandLineOption.SOURCE.getShortName() + " " + CommandLineOption.SOURCE.getDescription());
        }
        return whisker;
    }

    public int run(final String[] args) throws Exception {
        try {
            configure(args).act();
            return SYSTEM_EXIT_OK;
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            help();
            return SYSTEM_EXIT_CLI_PARSE_FAILED;
        }
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
