package com.codelab.loc.comp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class LOCCounter {
    private boolean enabledCountComments = true;
    protected boolean isIncluded=false;
    protected List<String> lines;
    protected List<String> includes;

    protected abstract int countLines();

    public boolean hasIncluded(){
        return isIncluded;
    }

    public LOCCounter(){
        lines = new ArrayList<String>();
    }
    public void enabledCommentState(boolean flag) {
        enabledCountComments = flag;
    }

    public int getLinesInFile(String filename) {
        int result = 0;

        try {
            BufferedReader stream = new BufferedReader(new FileReader(filename));
            result = getLinesInStream(stream);
            stream.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return result;
    }

    public int getLinesInStream(BufferedReader inputStream) {
        Scanner scanner = new Scanner(inputStream);

        // loop and scan the file stream until the end of file
        while (scanner.hasNext()) {
            lines.add(scanner.nextLine());
        }

        return countLines();
    }

    public void resetCountedLines(){
        if(lines.size() > 0){
            lines.clear();
        }

        if(includes != null && includes.size()>0){
            includes.clear();
            isIncluded=false;
        }
    }
}
