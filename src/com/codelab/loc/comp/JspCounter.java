package com.codelab.loc.comp;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JspCounter extends LOCCounter {

    private static final Pattern pattern1 = Pattern.compile("^(\\s*<jsp:include page=\")(.*)(\"\\s?/>)");
    private static final Pattern pattern2 = Pattern.compile("^(\\s*<jsp:include page=\")(.*)(\"\\s?>)(</jsp:include>)");

    public JspCounter() {
        super();
        includes = new ArrayList<String>();
    }

    @Override
    protected int countLines() {
        int loc = 0;
        String curLine;
        boolean inCommentBlock = false;
        char curChar, nextChar;

        //loop through all lines for counting
        for (int lineNum = 0; lineNum < lines.size(); lineNum++) {
            curLine = lines.get(lineNum);
            // the line smaller than 3 chars, e.g. <% or %>
            if (curLine.length() == 2) {
                //still need to check because won't enter next loop
                if (curLine.charAt(0) == '<' && curLine.charAt(0) == '%') {
                    loc++;
                }
            } else {
                Matcher matcher = pattern1.matcher(curLine);
                if (matcher.find()) {
//                    for (int i = 0; i < matcher.groupCount(); i++) {
//                        System.out.println("matcher.group(" + i + "):" + matcher.group(i));
//                    }
                    isIncluded = true;
                    includes.add(matcher.group(2));
                }

                if(!isIncluded){
                    matcher = pattern2.matcher(curLine);
                    if (matcher.find()) {
                        isIncluded = true;
                        includes.add(matcher.group(2));
                    }
                }

                int charPos = 0;
                //loop through current line
                while (charPos < curLine.length() - 1) { // end of comments from -->
                    curChar = curLine.charAt(charPos);
                    nextChar = curLine.charAt(charPos + 1);

                    //check the current line if beginning char of a comment
                    if (curChar == '<') {
                        // single comment line
                        if (nextChar == '!') {
                            // <!-- --> ending of the comment at last 3rd index
                            charPos = curLine.length() - 3;
                            inCommentBlock = true;
                        } else {
                            charPos = curLine.length() - 2; // skip to the end for next line
                        }
//                        // comment block
//                        if (nextChar == '*') {
//                            inCommentBlock = true;
//                            // going forward by increment of charPosition until end of comment block */
//                            charPos++;
//                        }


                    }

                    if (inCommentBlock && curChar == '-' && nextChar == '>') {
                        inCommentBlock = false;
                        // moving forward to check if comment ends
                        charPos++;
                    }

//                    if (curChar == '-') {
//                        if (nextChar == '>'  && inCommentBlock) {
//                            charPos++; // = curLine.length() - 1; // it is a comment and go to next line
//                            inCommentBlock=false;
//                        }
//                    }


                    charPos++;
                }

                //check last char
                if (charPos == curLine.length() - 1 && !inCommentBlock) {
                    //check for > to add to location not -->
                    if (curLine.charAt(charPos) == '>') {
                        if (curLine.charAt(charPos - 1) != '-')
                            loc++;
                    } else {
                        loc++;
                    }
                }
            }
        }

        return loc;
    }
}
