package org.example.lexer;

import java.io.Closeable;
import java.io.IOException;

public interface Lexer extends Closeable {
    Token readNextToken() throws IOException;
}
