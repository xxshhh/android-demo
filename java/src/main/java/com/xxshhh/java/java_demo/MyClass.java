package com.xxshhh.java.java_demo;

import com.xxshhh.java.java_demo.utils.CodeLinesUtils;

import java.util.ArrayList;
import java.util.List;

public class MyClass {

    public static void main(String[] args) {
        testCodeLinesUtils();
    }

    private static void testCodeLinesUtils() {
        String s1 = null;
        String s2 = "";
        String s3 = "12345";
        String s4 = "D:\\test.txt";
        String s5 = "D:\\test";
        CodeLinesUtils.calculate(s1);
        CodeLinesUtils.calculate(s2);
        CodeLinesUtils.calculate(s3);
        CodeLinesUtils.calculate(s4);
        CodeLinesUtils.calculate(s5);

        List<String> l1 = null;
        List<String> l2 = new ArrayList<>();
        List<String> l3 = new ArrayList<>();
        l3.add(s1);
        l3.add(s2);
        l3.add(s3);
        l3.add(s4);
        l3.add(s5);
        CodeLinesUtils.calculate(l1);
        CodeLinesUtils.calculate(l2);
        CodeLinesUtils.calculate(l3);
    }

}
