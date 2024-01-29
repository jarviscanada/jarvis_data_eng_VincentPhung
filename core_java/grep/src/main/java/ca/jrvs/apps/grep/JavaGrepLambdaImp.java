package ca.jrvs.apps.grep;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaGrepLambdaImp extends JavaGrepImp {

    private String regex;
    private String rootPath;
    private String outFile;

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
        }

        JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
        javaGrepLambdaImp.setRegex(args[0]);
        javaGrepLambdaImp.setRootPath(args[1]);
        javaGrepLambdaImp.setOutFile(args[2]);


        try {
            javaGrepLambdaImp.process();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public List<String> readLines(File inputFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            return reader.lines()
                    .filter(line -> line != null)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read lines from file: " + inputFile, e);
        }

    }

    @Override
    public List<File> listFiles(String rootDir) {
        try {
            File directory = new File(rootDir);

            if (!directory.isDirectory()) {
                throw new IllegalArgumentException("Not a valid directory: " + rootDir);
            }

            return Arrays.stream(directory.listFiles())
                    .filter(file -> file != null)
                    .collect(Collectors.toList());
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Error listing files in directory: " + rootDir, e);
        }
    }
}