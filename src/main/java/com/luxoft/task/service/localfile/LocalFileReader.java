package com.luxoft.task.service.localfile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.MalformedInputException;

public interface LocalFileReader {
    InputStream readFile() throws IOException;
}
