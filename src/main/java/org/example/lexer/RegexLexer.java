package org.example.lexer;

import java.io.IOException;
import java.util.List;

public class RegexLexer implements Lexer{
    
    private final LineBuffer buffer;

    //Constructor
    public RegexLexer(String filePath) throws IOException{
        buffer = new LineBuffer(filePath);
    }

    public Token readNextToken() throws IOException{
        while(buffer.getReadedLine() != null){
            if(buffer.isEndOfLine()){
                buffer.readNextLine();
            }else if(!buffer.prefixMatches(TokenPattern.WHITESPACE_PATTERN)){
                var checks = List.of(TokenPattern.values());    
                  
                return checks.stream()
                    .filter(buffer::prefixMatches)
                    .findFirst()
                    .map((pattern) -> new Token(pattern.getType(), buffer.getLastMatch()))
                    .orElseThrow(() -> new TokenNotRecognizedException(
                        String.format("Erro na linha %d, coluna %d: O caractere '%c' não é aceito pela linguagem", 
                        buffer.getRow()+1, buffer.getCol()+1, buffer.currentChar())
                    ));
            }
        }

        return new Token(TokenType.EOF, "Refatoração de código");
    }

    @Override
    public void close() throws IOException {
        buffer.close();
    }
}
