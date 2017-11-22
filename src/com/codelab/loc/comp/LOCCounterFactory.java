package com.codelab.loc.comp;

public class LOCCounterFactory {
    public static LOCCounter createCounter(String fileType) {
        switch (fileType.toLowerCase()) {
            case "jsp":
                return new JspCounter();
            case "java":
                return new JavaCounter();
        }

        return new DefCounter();
    }
}
