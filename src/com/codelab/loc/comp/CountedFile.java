package com.codelab.loc.comp;

public class CountedFile implements Comparable<CountedFile> {

    private final String filename;
    private final int count;

    public CountedFile(String filename, int count)
    {
        this.count = count;
        this.filename = filename;
    }

    public String toString()
    {
        return Reporter.kCountFormat.format(count) + " " + filename + "\n";
    }

    @Override
    public int compareTo(CountedFile other) {
        return this.filename.compareTo(other.filename);
    }
}
