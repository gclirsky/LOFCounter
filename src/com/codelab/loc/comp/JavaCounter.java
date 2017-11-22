package com.codelab.loc.comp;

public class JavaCounter extends LOCCounter{
    @Override
    protected int countLines() {
        int loc = 0;
        String curLine;
        boolean inCommentBlock = false;
        char curChar, nextChar;

        //loop through all lines for counting
        for (int lineNum = 0; lineNum < lines.size(); lineNum++) {
            curLine = lines.get(lineNum);
            // the line smaller than 2 chars, e.g. { or } or ;
            if (curLine.length() == 1) {
                //still need to check because won't enter next loop
                if (curLine.charAt(0) == ';' || curLine.charAt(0) == '{' || curLine.charAt(0) == '}') {
                    loc++;
                }
            } else {
                int charPos = 0;
                //loop through current line
                while (charPos < curLine.length() - 1) {
                    curChar = curLine.charAt(charPos);
                    nextChar = curLine.charAt(charPos + 1);

                    //check the current line if beginning char of a comment
                    if (curLine.charAt(charPos) == '/') {
                        // single comment line
                        if (nextChar == '/') {
                            charPos = curLine.length() - 1;
                        }
                        // comment block
                        if (nextChar == '*') {
                            inCommentBlock = true;
                            // going forward by increment of charPosition until end of comment block */
                            charPos++;
                        }
                    }

                    //check for the end of a comment block
                    if (inCommentBlock && curChar == '*' && nextChar == '/') {
                        inCommentBlock = false;
                        // moving forward to check if comment ends
                        charPos++;
                    }
                    //comments ending
                    if (!inCommentBlock) {
                        // new line without starting with comments // or /*
                        if (charPos + 3 < curLine.length()) {
                            String curSubStr = curLine.substring(charPos, charPos + 3);
                            //check for a for loop and decrement the count 2 semicolons here to compensate
                            if (curSubStr.equals("for")) {
                                loc--;
                                loc--;

                                if (loc < 0) loc = 0;
                            }
                        }

                        // end of the line for counting
                        if (curChar == ';' || curChar == '}') {
                            loc++;
                        }

                        if (nextChar == ';' || nextChar == '}') {
                            loc++;
                            charPos++;
                        }
                    }
                    charPos++;
                }

                //check last char
                if (charPos == curLine.length() - 1 && !inCommentBlock) {
                    //check for ; or } to add to location
                    if (curLine.charAt(charPos) == ';' || curLine.charAt(charPos) == '}') {
                        loc++;
                    }
                }
            }
        }
        return loc;
    }
}
