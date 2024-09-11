package com.vs.cloud_user.exception;

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

    public static class BadRequestException extends RuntimeException {
        public BadRequestException(String msg) {
            super(msg);
        }
    }
}
