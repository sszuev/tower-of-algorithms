package com.gitlab.sszuev.squares;

import groovy.util.Eval;
import org.apache.commons.cli.*;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by @ssz on 01.08.2021.
 */
class CLI {
    private static final int MINIMAL_SIZE = 5;
    private static final int DEFAULT_SIZE = 25;
    private final int size;
    private final String formula;

    private CLI(int size, String formula) {
        this.size = size;
        this.formula = formula;
    }

    public static CLI parse(String... args) throws ExitException {
        Options options = buildOptions();
        try {
            CommandLine cmd = new DefaultParser().parse(options, args);
            if (cmd.hasOption("h") || args.length == 0) {
                throw new ExitException(args.length == 1 ? 0 : 1, printHelp(options), null);
            }
            return new CLI(parseSize(cmd), parseFormula(cmd));
        } catch (ParseException e) {
            throw new ExitException(2, e.getMessage(), e);
        }
    }

    private static String printHelp(Options options) {
        HelpFormatter hf = new HelpFormatter();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        hf.printHelp(pw, hf.getWidth(), "-s size -f \"formula\"", null, options, hf.getLeftPadding(), hf.getDescPadding(), null);
        pw.flush();
        return sw.toString();
    }

    private static int parseSize(CommandLine cmd) throws ParseException {
        int size = Integer.parseInt(cmd.getOptionValue("s", String.valueOf(DEFAULT_SIZE)));
        if (size < MINIMAL_SIZE) {
            throw new ParseException("Size of area must be greater than " + MINIMAL_SIZE);
        }
        return size;
    }

    private static String parseFormula(CommandLine cmd) throws ParseException {
        String formula = cmd.getOptionValue("f");
        if (formula == null) {
            throw new ParseException("Missing required option: f");
        }
        try {
            if (Eval.xy(MINIMAL_SIZE, MINIMAL_SIZE, formula) instanceof Boolean) {
                return formula;
            }
            throw new ParseException("Not a boolean formula is given: '" + formula + "'.");
        } catch (RuntimeException e) {
            throw new ParseException("Wrong formula is given: '" + formula + "'.");
        }
    }

    private static Options buildOptions() {
        return new Options()
                .addOption(Option.builder("h")
                        .longOpt("help")
                        .desc("Display usage")
                        .build())
                .addOption(Option.builder("s")
                        .longOpt("size")
                        .desc("edge of square, optional. default: " + DEFAULT_SIZE + "")
                        .type(Integer.class)
                        .hasArg()
                        .build())
                .addOption(Option.builder("f")
                        .longOpt("formula")
                        .desc("xy-formula to display, required. example: \"x > y\"")
                        .type(String.class)
                        .hasArg()
                        .build())
                ;
    }

    public int size() {
        return size;
    }

    public String formula() {
        return formula;
    }
}
