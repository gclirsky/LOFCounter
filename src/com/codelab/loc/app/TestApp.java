package com.codelab.loc.app;

import com.codelab.loc.comp.Reporter;

import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestApp {


    public static void main(String[] args) {
        try {
            System.out.println("File analysis starts ...");
            if (args.length > 0) {
                System.out.println("Directory input: " + args[0]);
                if (args[1] == null) {
                    System.out.println("Please input a type of the target counting file.");
                    return;
                }

                System.out.println("File type input: " + args[1]);

                String dirpath = args[0];
                String filetype = args[1];

                if (dirpath.lastIndexOf(".") < dirpath.length() && dirpath.lastIndexOf(".") >= 0) {
                    System.out.println("Please input a valid directory path");
                    return;
                }

                Reporter reporter = new Reporter(dirpath, filetype);
                reporter.countLines();
                reporter.print();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.err.println("Cannot find any files. " + ex.getMessage());
        }
    }
}
