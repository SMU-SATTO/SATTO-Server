package com.example.satto.s31;

public class FileDownloadFailedException extends RuntimeException{
    public FileDownloadFailedException() {
    }

    public FileDownloadFailedException(String message) {
        super(message);
    }
}
