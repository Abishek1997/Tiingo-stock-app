package com.example.tiingostock.internal;

import java.io.IOException;

public class NoConnectivityException extends IOException {
    public NoConnectivityException(String errorMessage) {
        super(errorMessage);
    }
}

