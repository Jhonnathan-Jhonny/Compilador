package org.example.parser;

import org.example.lexer.Token;
import org.example.lexer.TokenType;
import java.io.IOException;

/**
 * Gramática utilizada para analisar expressões matemáticas:
 *
 * Expr   → Term   ( ("+" | "-") Term )*
 * Term   → Factor ( ("*" | "/") Factor )*
 * Factor → INT | FLOAT | "(" Expr ")"
 *
 * Ordem de precedência:
 *  1. Parênteses ()
 *  2. Multiplicação e divisão (*, /)
 *  3. Soma e subtração (+, -)
 *
 * Implementação baseada na técnica de descida recursiva.
 */

public class Parser {

    private final TokenBuffer buffer;

    public Parser(TokenBuffer buffer) {
        this.buffer = buffer;
    }

    public void parse() throws IOException {
        expr();
        buffer.match(TokenType.EOF); // Garante que todos os tokens foram consumidos
    }

    // Regra Expr → Term ( ("+" | "-") Term )*
    private void expr() throws IOException {
        if (isPrefixExpression()) {
            parsePrefix();
        } else {
            parseInfix();
        }
    }

    private boolean isPrefixExpression() {
        Token la = buffer.lookAhead(1);
        return la.type() == TokenType.OP_SUM ||
                la.type() == TokenType.OP_MINUS ||
                la.type() == TokenType.OP_MUL ||
                la.type() == TokenType.OP_DIV;
    }

    private void parsePrefix() throws IOException {
        // Consome o operador (+, -, *, /)
        Token op = buffer.lookAhead(1);
        buffer.match(op.type());

        // Primeiro operando (pode ser outra expressão prefixa ou um termo simples)
        expr();

        // Segundo operando
        expr();
    }

    private void parseInfix() throws IOException {
        term();
        while (lookAhead(TokenType.OP_SUM, TokenType.OP_MINUS)) {
            buffer.match(buffer.lookAhead(1).type());
            term();
        }
    }

    // Regra Term → Factor ( ("*" | "/") Factor )*
    private void term() throws IOException {
        factor();
        while (lookAhead(TokenType.OP_MUL, TokenType.OP_DIV)) {
            buffer.match(buffer.lookAhead(1).type());
            factor();
        }
    }

    // Regra Factor → INT | FLOAT | "(" Expr ")"
    private void factor() throws IOException {
        Token la = buffer.lookAhead(1);
        if (la.type() == TokenType.OP_PLUS || la.type() == TokenType.OP_MINUS) {
            buffer.match(la.type());
            factor(); // Recursão para lidar com o operando
        } else if (la.type() == TokenType.INT || la.type() == TokenType.FLOAT) {
            buffer.match(la.type());
        } else if (la.type() == TokenType.ABRE_PAR) {
            buffer.match(TokenType.ABRE_PAR);
            expr();
            buffer.match(TokenType.FECHA_PAR);
        } else {
            throw new SyntaxError(la, TokenType.INT, TokenType.FLOAT, TokenType.ABRE_PAR);
        }
    }

    private boolean lookAhead(TokenType... expected) {
        Token la = buffer.lookAhead(1);
        for (TokenType type : expected) {
            if (la.type() == type) return true;
        }
        return false;
    }
}
