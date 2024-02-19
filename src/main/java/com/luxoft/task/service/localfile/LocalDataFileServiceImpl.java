package com.luxoft.task.service.localfile;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

import static java.nio.file.Files.newInputStream;
import static java.nio.file.Paths.get;

@Service
public final class LocalDataFileServiceImpl implements LocalFileReader {

    @Value("${datafile.location}")
    private String dataFileLocation;

    @Override
    public InputStream readFile() throws IOException {
        try {
            return newInputStream(get(dataFileLocation));
        } catch (IOException e) {
            throw new LocalProductFileException(dataFileLocation, e);
        }
    }
}