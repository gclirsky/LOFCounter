package com.codelab.loc.comp;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Reporter {
    private List<CountedFile> reporter;
    private LOCCounter LOC;

    private boolean recurseSubDirectory;
    private String path = "";
    private String fileExt = "";
    private File[] fileArray;

    private int totalLOC = 0;
    private StringBuilder indent;

    public final static DecimalFormat kCountFormat = new DecimalFormat("00000");

    public Reporter(String dirPath, String fileType) throws FileNotFoundException {
        // Process parameters
        // If valid path convert to a list of files
        if (dirPath != null) {
            path = dirPath;
            File directory = new File(path);
            fileArray = directory.listFiles();

            if(fileArray == null) {
                throw new FileNotFoundException("Invalid file path: " + path);
            }
        }

        if (fileType != null) {
            fileExt = fileType;
        }
        reporter = new ArrayList<CountedFile>();
        recurseSubDirectory = true;
    }

    public Reporter(File[] list) {
        fileArray = list;
        reporter = new ArrayList<CountedFile>();
        recurseSubDirectory = true;
    }

    public void setRecurseSubDirectoryOff() {
        recurseSubDirectory = false;
    }

    public void countLines() {
        LOC = LOCCounterFactory.createCounter(fileExt);  // instance of the counter
        totalLOC = 0;
        indent = new StringBuilder("");
        reporter = processFiles(fileArray);
    }

    public int getTotal() {
        return totalLOC;
    }

    public List<CountedFile> getReporter() {
        List<CountedFile> sorted = new ArrayList<CountedFile>(reporter);
        java.util.Collections.sort(sorted);
        return sorted;
    }

    private List<CountedFile> processFiles(File[] files) {
        List<CountedFile> result = new ArrayList<CountedFile>();
        if (files == null) {
            return result;
        }

        for (int idx = 0; idx < files.length; idx++) {
            File file = files[idx];
            String filename = file.toString();

            if (filename.substring(filename.lastIndexOf(".") + 1).equalsIgnoreCase(fileExt)) {
                if (file.isDirectory()) {
                    // Should we recurse into this directory?
                    if (recurseSubDirectory && !file.getName().startsWith(".")) {
                        result.addAll(processFiles(file.listFiles()));
                    }
                } else {
                    BufferedReader stream = null;
                    int currentLOC = 0;
                    try {
                        stream = new BufferedReader(new FileReader(file));
                        currentLOC = LOC.getLinesInStream(stream);

                        CountedFile entry = null;

                        if(LOC.isIncluded){
                            entry = new CountedFile(filename, currentLOC, LOC.includes);
                        } else{
                            entry = new CountedFile(filename, currentLOC);
                        }

                        result.add(entry);
                        totalLOC = totalLOC + currentLOC;
                        stream.close();
                    } catch (FileNotFoundException ex) {
                        System.err.println(ex.getMessage());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } finally {
                        // close stream (if not closed above)
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (Exception exc) {
                                exc.printStackTrace();
                            }
                        }

                        LOC.resetCountedLines();
                    }   // end finally
                }   // end else: not directory

            }// end loop
        }

        return result;
    }

    public void print() {
        List<CountedFile> sortedResult = getReporter();
        for (int i = 0; i < sortedResult.size(); i++) {
            System.out.print(sortedResult.get(i).toString());
        }
        System.out.println();
        System.out.println("Total lines: " + Reporter.kCountFormat.format(getTotal()));
    }
}
