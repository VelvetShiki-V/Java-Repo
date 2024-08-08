package com.vs.common;

public class CustomException {
    public static class InvalidTokenException extends RuntimeException {
        public InvalidTokenException(String msg) {
            super(msg);
        }
    }

    public static class AccessDeniedException extends RuntimeException {
        public AccessDeniedException(String msg) {
            super(msg);
        }
    }

    public static class DataNotFoundException extends RuntimeException {
        public DataNotFoundException(String msg) {
            super(msg);
        }
    }
}
