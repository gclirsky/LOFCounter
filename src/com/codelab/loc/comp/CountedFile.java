package com.codelab.loc.comp;

import java.util.List;

public class CountedFile implements Comparable<CountedFile> {

    private final String filename;
    private final int count;
    private String[] includes;

    public CountedFile(String filename, int count) {
        this.count = count;
        this.filename = filename;
    }

    public CountedFile(String filename, int count, List<String> includes) {
        this(filename, count);
        if(includes.size() > 0) {
            this.includes = new String[includes.size()];
        }
        includes.toArray(this.includes);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(Reporter.kCountFormat.format(count) + " " + filename + "\n");
        for (String inc : includes) {
            builder.append("\t\t" + inc + "\n");
        }
        builder.append("\n");
//        return Reporter.kCountFormat.format(count) + " " + filename + "\n\t";
        return builder.toString();
    }

    @Override
    public int compareTo(CountedFile other) {
        return this.filename.compareTo(other.filename);
    }
}
