package org.example.lexer;

public class TokenNotRecognizedException extends RuntimeException {
    public TokenNotRecognizedException(String message){
        super(message);
    }
}
