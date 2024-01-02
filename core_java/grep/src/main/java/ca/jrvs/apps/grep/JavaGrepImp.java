package ca.jrvs.apps.grep;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.log4j.BasicConfigurator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JavaGrepImp implements JavaGrep{
    final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

    private String regex;
    private String rootPath;
    private String outFile;

    public static void main(String[] args){
        if (args.length != 3){
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
        }

        //Add default logger config
        BasicConfigurator.configure();


        JavaGrepImp javaGrepImp = new JavaGrepImp();
        javaGrepImp.setRegex(args[0]);
        javaGrepImp.setRootPath(args[1]);
        javaGrepImp.setOutFile(args[2]);

        try {
            javaGrepImp.process();
        } catch (Exception ex) {
            javaGrepImp.logger.error("Error: Unable to process", ex);
        }
    }


    @Override
    public void process() throws IOException {
        List<String> matchedLines = new ArrayList<>();

        for (File file : listFiles(getRootPath())) { //Return files
            for (String line : readLines(file)) { //Read file, return lines
                if (containsPattern(line)) {
                    matchedLines.add(line);
                }
            }
        }

        writeToFile(matchedLines);

    }

    @Override
    public List<File> listFiles(String rootDir) {
        List<File> fileList = new ArrayList<>();

        try {
            File directory = new File(rootDir);

            if (directory.isDirectory()) {
                File[] files = directory.listFiles();

                if (files != null) {
                    for (File file : files) {
                        fileList.add(file);
                    }
                }
            } else {
                throw new IllegalArgumentException("Not a valid directory: " + rootDir);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error listing files in directory: " + rootDir, e);
        }

        return fileList;
    }

    @Override
    public List<String> readLines(File inputFile) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("failed method reason:", e);
        } catch (IOException e) {
            throw new RuntimeException("failed method reason:", e);
        }

        return lines;
    }

    @Override
    public boolean containsPattern(String line) {
        String regex = getRegex();
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);

        if (matcher.matches()){
            return true;
        }

        return false;
    }

    @Override
    public void writeToFile(List<String> lines) throws IOException {
        String outputPath = getOutFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine(); // Add a newline after each line
            }
        }
    }

    @Override
    public String getRootPath() {
        return rootPath;
    }

    @Override
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public String getRegex() {
        return regex;
    }

    @Override
    public void setRegex(String regex) {
        this.regex = regex;

    }

    @Override
    public String getOutFile() {
        return outFile;
    }

    @Override
    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }
}
