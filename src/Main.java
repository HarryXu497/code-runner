import coderunner.CodeRunner;
import coderunner.Submission;
import coderunner.Task;
import coderunner.TaskResult;
import coderunner.test.TestResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<String> inputFiles = new ArrayList<>();
        inputFiles.add("problem1\\tests\\test1.txt");

        List<String> outputFiles = new ArrayList<>();
        outputFiles.add("problem1\\output\\out1.txt");

        List<String> answerFiles = new ArrayList<>();
        answerFiles.add("problem1\\answers\\ans1.txt");

        try {
            CodeRunner codeRunner = new CodeRunner();

            Task task1 = new Task("problem1\\Main.java", inputFiles, outputFiles, answerFiles);
            Task task2 = new Task("problem1\\Main.java", inputFiles, outputFiles, answerFiles);

            codeRunner.enqueue(new Submission(task1, "1"));

            Thread.sleep(1000);

            codeRunner.enqueue(new Submission(task2, "2"));

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}