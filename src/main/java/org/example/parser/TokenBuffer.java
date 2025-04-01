package org.example.parser;

import org.example.lexer.Token;
import java.io.Closeable;
import java.io.IOException;
import java.util.LinkedList;
import org.example.lexer.Lexer;
import org.example.lexer.TokenType;

public class TokenBuffer implements Closeable {

    private final int SIZE;
    private boolean reachedEndOfFile;
    private final LinkedList<Token> buffer;
    private final Lexer lexer;

    public TokenBuffer(Lexer lexer) throws IOException {
        SIZE = 10;
        buffer = new LinkedList<>();
        this.lexer = lexer;
        reachedEndOfFile = false;
        confirmToken();
    }

    private void confirmToken() throws IOException {
        if (!buffer.isEmpty())
            buffer.poll();

        while (buffer.size() < SIZE && !reachedEndOfFile) {
            var tk = lexer.readNextToken();
            buffer.addLast(tk);

            if (tk.type() == TokenType.EOF)
                reachedEndOfFile = true;
        }
    }

    public Token lookAhead(int k) {
        if (buffer.isEmpty())
            return null;

        k = Math.max(k - 1, 0);
        return k >= buffer.size() ? buffer.getLast() : buffer.get(k);
    }

    public void match(TokenType type) throws IOException {
        var la = lookAhead(1);

        if (la.type() == type) {
            confirmToken();
            return;
        }

        throw new SyntaxError(la, type);
    }

    @Override
    public void close() throws IOException {
        if (lexer != null)
            lexer.close();
    }

}
