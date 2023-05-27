package coderunner;

import coderunner.test.Test;
import coderunner.test.TestResult;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
public class Task {
    private final String sourceFile;
    private final List<Test> tests;
    private int testIndex;

    public Task(String sourceFile, List<String> inputFiles, List<String> outputFiles, List<String> testFiles) throws IOException {
        if (inputFiles.size() != testFiles.size()) {
            throw new IllegalArgumentException("The two input lists should be the same length");
        }

        this.sourceFile = sourceFile;
        this.tests = new ArrayList<>();
        TaskResult compilationResult = this.compile();

        if (compilationResult.getTaskCode() == TaskCode.COMPILE_ERROR) {
            throw new RuntimeException("cannot compile");
        }



        String compiledFilePath = this.sourceFile.substring(this.sourceFile.lastIndexOf("\\") + 1, this.sourceFile.lastIndexOf("."));

        System.out.println(compiledFilePath);

        for (int i = 0; i < inputFiles.size(); i++) {
            this.tests.add(new Test(
                    compiledFilePath,
                    inputFiles.get(i),
                    outputFiles.get(i),
                    testFiles.get(i)
            ));
        }

        this.testIndex = 0;
    }

    private void filter() {

    }

    private TaskResult compile() throws IOException {
        String sourceFileDir = this.sourceFile;
        String workingDirectory = System.getProperty("user.dir");

        Process compilation = Runtime.getRuntime().exec("javac " + (workingDirectory + "\\") + this.sourceFile + " -d " + (workingDirectory + "\\"));

        StringBuilder successfulOutput = new StringBuilder();

        try (
            BufferedReader compilationIn = new BufferedReader(new InputStreamReader(compilation.getInputStream()));
            BufferedReader compilationErr = new BufferedReader(new InputStreamReader(compilation.getErrorStream()))
        ) {
            StringBuilder fullErrorText = new StringBuilder();
            String errorText;


            while ((errorText = compilationErr.readLine()) != null) {
                fullErrorText.append(errorText);
            }

            errorText = fullErrorText.toString();

//            System.out.println(errorText);

            if (errorText.length() != 0) {
                return new TaskResult(TaskCode.COMPILE_ERROR, errorText);
            }

            // Standard out -> successful compile
            String successText;


            while ((successText = compilationIn.readLine()) != null) {
                successfulOutput.append(successText);
            }

//            System.out.println(successfulOutput);
        }

        return new TaskResult(TaskCode.SUCCESSFUL, successfulOutput.toString());
    }

    public TestResult nextTest() throws IOException, InterruptedException {
        Test currentTest = this.tests.get(this.testIndex);

        this.testIndex++;

        currentTest.execute();
        return currentTest.test();
    }

    public Test getCurrentTest() {
        return this.tests.get(this.testIndex - 1);
    }

    public boolean hasNextTest() {
        return this.testIndex < this.tests.size();
    }

    private List<String> readFileList(List<String> files) throws IOException {
        List<String> fileContents = new ArrayList<>(files.size());

        for (String file : files) {
            StringBuilder sb = new StringBuilder();

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                int currentChar;

                while ((currentChar = br.read()) != -1) {
                    sb.append((char) currentChar);
                }
            }

            fileContents.add(sb.toString());
        }

        return fileContents;
    }
}
