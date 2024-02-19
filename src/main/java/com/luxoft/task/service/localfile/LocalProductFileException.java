package com.luxoft.task.service.localfile;

import java.io.IOException;

public class LocalProductFileException extends IOException {

    private final String filePath;
    private final IOException e;

    public LocalProductFileException(String filePath, IOException e) {
        this.filePath = filePath;
        this.e = e;
    }

    public String getFilePath() {
        return filePath;
    }

    public IOException getException() {
        return e;
    }
}
