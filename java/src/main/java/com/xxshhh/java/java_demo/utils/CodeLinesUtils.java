package com.xxshhh.java.java_demo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

public class CodeLinesUtils {

    /**
     * 计算单个路径
     *
     * @param path 路径
     */
    public static void calculate(String path) {
        if (path == null) {
            System.out.println("File does not exist.");
            return;
        }

        List<String> paths = new ArrayList<>();
        paths.add(path);
        calculate(paths);
    }

    /**
     * 计算多个路径
     *
     * @param paths 路径列表
     */
    public static void calculate(List<String> paths) {
        if (paths == null || paths.size() == 0) {
            System.out.println("File does not exist.");
            return;
        }

        List<File> allFiles = new ArrayList<>();
        for (String path : paths) {
            foreachAllFiles(path, allFiles);
        }
        show(allFiles);
    }

    private static void show(List<File> allFiles) {
        if (allFiles == null || allFiles.size() == 0) {
            System.out.println("File does not exist.");
            return;
        }
        System.out.println("--------------------------------------------------");
        System.out.println("Index" + "\t" + "File Name" + "\t" + "Lines" + "\t");
        int totalLines = 0;
        int maxLines = 0;
        int minLines = 0;
        long start = System.currentTimeMillis();
        for (int i = 0; i < allFiles.size(); i++) {
            File file = allFiles.get(i);
            if (file == null) {
                continue;
            }
            int index = i + 1;
            String fileName = file.getName();
            int lines = calculateFileLines(file);
            if (i == 0) {
                maxLines = lines;
                minLines = lines;
            }
            if (lines > maxLines) {
                maxLines = lines;
            }
            if (lines < minLines) {
                minLines = lines;
            }
            totalLines += lines;
            System.out.println(index + "\t" + fileName + "\t" + lines + "\t");
        }
        long end = System.currentTimeMillis();
        long time = end - start;
        System.out.println("Cost Time: " + time + "ms");
        System.out.println("Total Lines: " + totalLines);
        System.out.println("Max Lines: " + maxLines);
        System.out.println("Min Lines: " + minLines);
        System.out.println("--------------------------------------------------");
    }

    private static void foreachAllFiles(String path, List<File> allFiles) {
        if (path == null || allFiles == null) {
            return;
        }
        File rootFile = new File(path);
        if (!rootFile.exists()) {
            return;
        }
        if (!rootFile.isDirectory()) {
            allFiles.add(rootFile);
            return;
        }
        File[] subFiles = rootFile.listFiles();
        if (subFiles == null || subFiles.length == 0) {
            return;
        }
        for (File subFile : subFiles) {
            foreachAllFiles(subFile.getAbsolutePath(), allFiles);
        }
    }

    private static int calculateFileLines(File file) {
        if (file == null) {
            return 0;
        }
        int lineCount = 0;
        char[] buf = new char[8192];
        LineNumberReader reader = null;
        try {
            reader = new LineNumberReader(new InputStreamReader(new FileInputStream(file)));
            while (reader.read(buf) != -1) {
            }
            lineCount = reader.getLineNumber() + 1; // +1是因为lineNumber是从0开始的
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return lineCount;
    }

}
