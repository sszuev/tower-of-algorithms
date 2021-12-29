package com.gitlab.sszuev.compression;

import com.gitlab.sszuev.compression.impl.*;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.LongConsumer;
import java.util.stream.Collectors;

/**
 * Created by @ssz on 29.12.2021.
 */
public class App {
    public static void main(String... args) throws IOException {
        CLI input = parseOrExit(args);
        FileCodec codec = createFileCodec(input);

        Files.createFile(input.target);
        System.out.println("The target file: " + input.target.toRealPath());

        System.out.print("Process: ");
        if (input.mode == Mode.ENCODE) {
            codec.encode(() -> FileCodec.newReadChannel(input.source),
                    () -> FileCodec.newWriteChannel(input.target), input.buffer);
        } else {
            codec.decode(() -> FileCodec.newReadChannel(input.source),
                    () -> FileCodec.newWriteChannel(input.target), input.buffer);
        }
        System.out.println();
        System.out.println("Done.");
    }

    private static CLI parseOrExit(String... args) {
        try {
            return CLI.parse(args);
        } catch (ExitException ex) {
            System.err.println(ex.getMessage());
            System.exit(ex.getCode());
            return null;
        }
    }

    private static FileCodec createFileCodec(CLI input) throws IOException {
        long size = Files.size(input.source);
        long step = size / 42;
        LongAdder byteCounter = new LongAdder();
        LongAdder percentCounter = new LongAdder();
        return input.codec.createCodec(value -> {
            if (value < 0) {
                return;
            }
            byteCounter.add(value);
            if (byteCounter.longValue() > percentCounter.longValue()) {
                System.out.print("#");
                percentCounter.add(step);
            }
        });
    }

    enum Codec {
        STANDARD_ZIP {
            @Override
            public FileCodec createCodec(LongConsumer listener) {
                return new JDKZipCodecImpl(listener);
            }

            @Override
            public String ext() {
                return "zip";
            }

            @Override
            boolean match(String id) {
                return name().equals(id.trim().toUpperCase(Locale.ROOT));
            }
        },
        COMMONS_ZIP {
            @Override
            public FileCodec createCodec(LongConsumer listener) {
                return new ApacheZipCodecImpl(listener);
            }

            @Override
            public String ext() {
                return "zip";
            }

            @Override
            boolean match(String id) {
                return name().equals(id.trim().toUpperCase(Locale.ROOT));
            }
        },
        STANDARD_GZIP {
            @Override
            public FileCodec createCodec(LongConsumer listener) {
                return new JDKGZipCodecImpl(listener);
            }

            @Override
            public String ext() {
                return "gz";
            }
        },
        NAIVE_RLE {
            @Override
            public FileCodec createCodec(LongConsumer listener) {
                return new SimpleRLECodecImpl(listener);
            }

            @Override
            public String ext() {
                return "serl";
            }
        },
        OPTIMISED_RLE {
            @Override
            public FileCodec createCodec(LongConsumer listener) {
                return new EnhancedRLECodecImpl(listener);
            }

            @Override
            public String ext() {
                return "erle";
            }
        };

        public abstract FileCodec createCodec(LongConsumer listener);

        public abstract String ext();

        boolean match(String id) {
            return name().equalsIgnoreCase(id = id.trim()) || id.equalsIgnoreCase(ext());
        }

        public static Codec of(String ext) {
            for (Codec c : values()) {
                if (c.match(ext)) {
                    return c;
                }
            }
            throw new IllegalArgumentException("Can't find " + ext);
        }
    }

    enum Mode {
        ENCODE, DECODE
    }

    static class CLI {
        private static final int DEFAULT_BUFFER_SIZE = 8192;
        private static final Set<String> HELP_REQUEST = Set.of("/?", "h");

        private final Mode mode;
        private final Path source;
        private final Path target;
        private final Codec codec;
        private final int buffer;

        CLI(Mode mode, Path source, Path target, Codec codec, int buffer) {
            this.mode = mode;
            this.source = source;
            this.target = target;
            this.codec = codec;
            this.buffer = buffer;
        }

        public static CLI parse(String... args) throws ExitException {
            Options options = buildOptions();
            try {
                if (args.length == 0 || Arrays.stream(args).anyMatch(HELP_REQUEST::contains)) {
                    throw new ExitException(0, printHelp(options), null);
                }
                CommandLine cmd = new DefaultParser().parse(options, args);
                if (cmd.hasOption("h")) {
                    throw new ExitException(args.length == 1 ? 0 : 1, printHelp(options), null);
                }
                Mode mode = parseMode(cmd);
                Path source = parseSource(cmd);
                Codec codec = parseCodec(cmd);
                Path target = parseTarget(cmd, codec, mode, source);
                int buffer = parseBuffer(cmd);
                return new CLI(mode, source, target, codec, buffer);
            } catch (ParseException e) {
                throw new ExitException(2, e.getMessage(), e);
            } catch (IOException e) {
                throw new ExitException(3, String.format("%s: %s", e.getClass().getSimpleName(), e.getMessage()), e);
            }
        }

        private static Mode parseMode(CommandLine cmd) throws ParseException {
            if (cmd.hasOption("-e")) {
                return Mode.ENCODE;
            }
            if (cmd.hasOption("-d")) {
                return Mode.DECODE;
            }
            throw new ParseException("Mode is mandatory");
        }

        private static Path parseSource(CommandLine cmd) throws IOException {
            return Paths.get(cmd.getOptionValue("s")).toRealPath();
        }

        private static Codec parseCodec(CommandLine cmd) throws ParseException {
            String codec = cmd.getOptionValue("c");
            try {
                if (codec.matches("\\d+")) {
                    return Codec.values()[Integer.parseInt(codec)];
                }
                return Codec.of(codec);
            } catch (RuntimeException ex) {
                throw new ParseException("Wrong codec specified, must be one of these: " + shortCodecDescription());
            }
        }

        private static Path parseTarget(CommandLine cmd, Codec codec, Mode mode, Path source) throws IOException {
            boolean overwrite = cmd.hasOption("o");
            String target = cmd.getOptionValue("t");
            if (target != null) {
                Path res = Paths.get(target);
                res = res.isAbsolute() ? res : source.getParent().resolve(res).toAbsolutePath();
                return checkTargetPath(res, overwrite);
            }
            String sourceName = source.getFileName().toString();
            String targetName;
            if (Mode.ENCODE == mode) {
                targetName = String.format("%s.%s", sourceName, codec.ext());
            } else if (sourceName.endsWith(codec.ext())) {
                targetName = sourceName.replaceFirst("(^.+)\\." + codec.ext() + "$", "$1");
            } else {
                targetName = sourceName + ".orig";
            }
            return checkTargetPath(source.getParent().resolve(targetName), overwrite);
        }

        private static Path checkTargetPath(Path res, boolean overwrite) throws IOException {
            if (!Files.exists(res)) {
                return res;
            }
            if (overwrite) {
                Files.delete(res);
                return res;
            }
            throw new FileAlreadyExistsException(res.toString());
        }

        private static int parseBuffer(CommandLine cmd) throws ParseException {
            String res = cmd.getOptionValue("b", String.valueOf(DEFAULT_BUFFER_SIZE));
            try {
                return Integer.parseInt(res);
            } catch (NumberFormatException ex) {
                throw new ParseException("Wrong input for buffer: " + res);
            }
        }

        private static String printHelp(Options options) {
            HelpFormatter hf = new HelpFormatter();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            hf.printHelp(pw, HelpFormatter.DEFAULT_WIDTH * 2, cmdLineSyntax(), null, options,
                    hf.getLeftPadding(), hf.getDescPadding(), null);
            pw.flush();
            return sw.toString();
        }

        private static Options buildOptions() {
            return new Options()
                    .addOption(buildHelpOption())
                    .addOptionGroup(buildDirectionOption())
                    .addOption(Option.builder("s")
                            .longOpt("source")
                            .desc("The source file path (absolute or relative)")
                            .hasArg()
                            .required(true)
                            .build())
                    .addOption(Option.builder("c")
                            .longOpt("codec")
                            .desc("The codec, choose one of these: " + longCodecDescription())
                            .required(true)
                            .hasArg()
                            .build())
                    .addOption(Option.builder("t")
                            .longOpt("target")
                            .desc("The target file path (absolute or relative).\n" +
                                    "Optional: by default the filename is inferred from the source filename, the codec and direction mode")
                            .hasArg()
                            .required(false)
                            .build())
                    .addOption(Option.builder("o")
                            .longOpt("overwrite")
                            .desc("Overwrites the target file if it is exist.\nOptional: by default error in that case.")
                            .required(false)
                            .build())
                    .addOption(Option.builder("b")
                            .longOpt("buffer")
                            .desc("Buffer in bytes.\nOptional: by default it is " + DEFAULT_BUFFER_SIZE)
                            .type(Integer.class)
                            .required(false)
                            .hasArg()
                            .build())
                    ;
        }

        private static Option buildHelpOption() {
            return Option.builder("h")
                    .longOpt("help")
                    .desc("Display usage")
                    .build();
        }

        private static OptionGroup buildDirectionOption() {
            OptionGroup res = new OptionGroup();
            res.setRequired(true);
            res.addOption(Option.builder("e")
                    .longOpt("encode")
                    .desc("Mandatory mode: perform encoding")
                    .build());
            res.addOption(Option.builder("d")
                    .longOpt("decode")
                    .desc("Mandatory mode: perform decoding")
                    .build());
            res.addOption(buildHelpOption());
            return res;
        }

        private static String cmdLineSyntax() {
            return "-e|-d " +
                    "-s <source> " +
                    "-c {" + shortCodecDescription() + "} " +
                    "[[-o] -t <target>] " +
                    "[-b <buffer>]";
        }

        private static String longCodecDescription() {
            return Arrays.stream(Codec.values())
                    .map(x -> String.format("%d(%s)", x.ordinal(), x.name()))
                    .collect(Collectors.joining(", "));
        }

        private static String shortCodecDescription() {
            return Arrays.stream(Codec.values())
                    .map(x -> String.valueOf(x.ordinal()))
                    .collect(Collectors.joining("|"));
        }

    }

    static class ExitException extends RuntimeException {
        private final int code;

        public ExitException(int code, String message, Throwable cause) {
            super(message, cause);
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

}
