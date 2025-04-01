package org.example.lexer;

public record Token(
    TokenType type,
    String lexema
){
    @Override
    public final String toString() {
        return "<" + (type != null ? type.name() : "") + ", " + lexema + ">";
    }
}
