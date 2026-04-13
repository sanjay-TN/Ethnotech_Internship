package com.codingplatform.service;

import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class CodeExecutionService {

    public String execute(String code, String language, String input) {

        try {

            switch (language.toLowerCase()) {

                case "python":
                    return runPython(code, input);

                case "java":
                    return runJava(code, input);

                default:
                    return "Unsupported language";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    // 🔥 PYTHON EXECUTION
    private String runPython(String code, String input) throws Exception {

        File file = File.createTempFile("code", ".py");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(code);
        }

        ProcessBuilder pb = new ProcessBuilder("python", file.getAbsolutePath());
        Process process = pb.start();

        writeInput(process, input);

        String output = readOutput(process);

        file.delete();

        return output;
    }

    // 🔥 JAVA EXECUTION
    private String runJava(String code, String input) throws Exception {

        File dir = new File(System.getProperty("java.io.tmpdir"));
        File file = new File(dir, "Main.java");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(code);
        }

        // ✅ Compile
        ProcessBuilder compile = new ProcessBuilder("javac", file.getAbsolutePath());
        Process compileProcess = compile.start();
        compileProcess.waitFor();

        // ✅ Run
        ProcessBuilder run = new ProcessBuilder("java", "-cp", dir.getAbsolutePath(), "Main");
        Process runProcess = run.start();

        writeInput(runProcess, input);

        String output = readOutput(runProcess);

        file.delete();

        return output;
    }

    // 🔥 COMMON: Write input
    private void writeInput(Process process, String input) throws Exception {
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(process.getOutputStream())
        );
        writer.write(input);
        writer.newLine();
        writer.flush();
        writer.close();
    }

    // 🔥 COMMON: Read output
    private String readOutput(Process process) throws Exception {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
        );
        String output = reader.readLine();
        return output != null ? output.trim() : "";
    }
}