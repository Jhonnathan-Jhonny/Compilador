package org.example.parser;

import org.example.lexer.Token;
import org.example.lexer.TokenType;
import java.util.Arrays;

public class SyntaxError extends RuntimeException{

    public SyntaxError(Token recebido, TokenType...esperado){
        super("Erro Sintático: Foi recebido " + recebido.toString() 
            + " mas era esperado [" + String.join(", ", 
                Arrays.stream(esperado).map(TokenType::toString).toList()
            ) + "]");
    }

}
