package com.scm.helpers;

public enum MessageType {

    success("bg-green-100 text-green-800 border border-green-300"),
    warning("bg-yellow-100 text-yellow-800 border border-yellow-300"),
    danger("bg-red-100 text-red-800 border border-red-300");

    private final String cssClass;

    MessageType(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getCssClass() {
        return cssClass;
    }
}
