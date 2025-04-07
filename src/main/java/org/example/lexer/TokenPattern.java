package org.example.lexer;

import java.util.regex.Pattern;

public enum TokenPattern {
    WHITESPACE_PATTERN(Pattern.compile("\\s+"), null),
    INT_PATTERN(Pattern.compile("\\d+"), TokenType.INT),
    FLOAT_PATTERN(Pattern.compile("\\d+\\.\\d+"), TokenType.FLOAT),
    OP_SUM_PATTERN(Pattern.compile("\\+"), TokenType.OP_SUM),
    OP_MINUS_PATTERN(Pattern.compile("-"), TokenType.OP_MINUS),
    OP_MUL_PATTERN(Pattern.compile("\\*"), TokenType.OP_MUL),
    OP_DIV_PATTERN(Pattern.compile("/"), TokenType.OP_DIV),
    ABRE_PAR_PATTERN(Pattern.compile("\\("), TokenType.ABRE_PAR),
    FECHA_PAR_PATTERN(Pattern.compile("\\)"), TokenType.FECHA_PAR),
    OP_POW_PATTERN(Pattern.compile("\\^"), TokenType.OP_POW);

    private final Pattern pattern;
    private final TokenType type;

    TokenPattern(Pattern pattern, TokenType type){
        this.pattern = pattern;
        this.type = type;
    }

    public Pattern getPattern(){
        return pattern;
    }

    public TokenType getType(){
        return type;
    }

}
