package com.udacity.gradle.builditbigger;

/**
 * Created by figengungor on 5/8/2018.
 */

public class JokeResult {
    String exception;
    String result;

    public JokeResult(String exception, String result) {
        this.exception = exception;
        this.result = result;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
