package org.example.parser;

import org.example.lexer.Token;
import org.example.lexer.TokenType;
import java.io.IOException;

/**
 Gramática e lexema

 grammar MathExpr;

 // Regras de parser
 expr: term expr_prime;
 expr_prime: (PLUS | MINUS) term expr_prime | ;
 term: factor term_prime;
 term_prime: (MULT | DIV) factor term_prime | ;
 factor: power factor_prime;
 factor_prime: POW power factor_prime | ;
 power: LPAREN expr RPAREN | PLUS power | MINUS power | INT | FLOAT;

 // Regras de lexer
 PLUS: '+';
 MINUS: '-';
 MULT: '*';
 DIV: '/';
 POW: '^';
 LPAREN: '(';
 RPAREN: ')';
 INT: [0-9]+;
 FLOAT: [0-9]+ '.' [0-9]+;
 WS: [ \t\r\n]+ -> skip;

**/

public class Parser {

    private final TokenBuffer buffer;

    public Parser(TokenBuffer buffer) {
        this.buffer = buffer;
    }

    public void parse() throws IOException {
        Token la = buffer.lookAhead(1);

        if (isUnaryOperator(la.type())) {
            parsePrefix();
        } else {
            expr(); // infixa normal
        }

        buffer.match(TokenType.EOF); // garante que tudo foi consumido
    }

    private void parsePrefix() throws IOException {
        Token op = buffer.match(buffer.lookAhead(1).type());
        // Prefixo é sempre operador seguido de duas expressões
        // E cada "expr" aqui pode ser outra prefixa ou valor terminal
        parsePrefixOperand(); // primeiro operando
        parsePrefixOperand(); // segundo operando
    }

    private void parsePrefixOperand() throws IOException {
        Token la = buffer.lookAhead(1);

        if (isUnaryOperator(la.type())) {
            parsePrefix(); // outra operação prefixa
        } else if (la.type() == TokenType.INT || la.type() == TokenType.FLOAT) {
            buffer.match(la.type()); // valor terminal
        } else if (la.type() == TokenType.ABRE_PAR) {
            buffer.match(TokenType.ABRE_PAR);
            expr(); // expressão infixa dentro de parênteses
            buffer.match(TokenType.FECHA_PAR);
        } else {
            throw new SyntaxError(la, TokenType.INT, TokenType.FLOAT, TokenType.ABRE_PAR);
        }
    }


    private void expr() throws IOException {
        term();
        while (lookAhead(TokenType.OP_SUM, TokenType.OP_MINUS)) {
            buffer.match(buffer.lookAhead(1).type());
            term();
        }
    }

    private boolean isUnaryOperator(TokenType type) {
        return switch (type) {
            case OP_SUM, OP_MINUS, OP_MUL, OP_DIV, OP_POW -> true;
            default -> false;
        };
    }


    private void term() throws IOException {
        factor();
        while (lookAhead(TokenType.OP_MUL, TokenType.OP_DIV)) {
            buffer.match(buffer.lookAhead(1).type());
            factor();
        }
    }

    private void factor() throws IOException {
        power();
        if (lookAhead(TokenType.OP_POW)) {
            buffer.match(TokenType.OP_POW);
            factor();  // Recursão para associatividade à direita
        }
    }

    //Antigo factor
    private void power() throws IOException {
        Token la = buffer.lookAhead(1);
        if (isUnaryOperator(la.type())) {
            buffer.match(la.type());
            power();
        }
        else if (la.type() == TokenType.INT || la.type() == TokenType.FLOAT) {
            buffer.match(la.type());
        }
        else if (la.type() == TokenType.ABRE_PAR) {
            buffer.match(TokenType.ABRE_PAR);
            expr();
            buffer.match(TokenType.FECHA_PAR);
        }
        else {
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
