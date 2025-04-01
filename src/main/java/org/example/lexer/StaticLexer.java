package org.example.lexer;

import java.io.FileNotFoundException;
import java.io.IOException;

public class StaticLexer implements Lexer {

    private final CharBuffer buffer;

    public StaticLexer(String filePath) throws FileNotFoundException {
        buffer = new CharBuffer(filePath);
    }

    public Token readNextToken() throws IOException {
        int readedChar;

        while ((readedChar = buffer.readNextChar()) != CharBuffer.EOF) {
            char c = (char) readedChar;

            if (Character.isWhitespace(c)) {
                continue;
            } else {
                throw new TokenNotRecognizedException("O token '" + c + "' não foi reconhecido");
            }

        }

        return new Token(TokenType.EOF, null);
    }
    
    @Override
    public void close() throws IOException {
        buffer.close();
    }
}
