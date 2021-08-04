package com.gitlab.sszuev.squares;

import groovy.util.Eval;

import java.io.PrintStream;

/**
 * Created by @ssz on 28.07.2021.
 */
public class App {

    public static void main(String... args) {
        CLI input = null;
        try {
            input = CLI.parse(args);
        } catch (ExitException ex) {
            System.err.println(ex.getMessage());
            System.exit(ex.getCode());
        }
        System.out.println("--" .repeat(input.size()));
        System.out.println("size = " + input.size() + "; formula = '" + input.formula() + "'");
        System.out.println("--" .repeat(input.size()));

        plotSquare(System.out, input.size(), input.formula());
    }

    public static void plotSquare(PrintStream out, int size, String formula) {
        for (int y = size - 1; y >= 0; y--) {
            for (int x = 0; x < size; x++) {
                boolean res = (Boolean) Eval.xy(x, y, formula);
                String point = res ? "#" : ".";
                out.print(point + " ");
            }
            out.println();
        }
    }

}
