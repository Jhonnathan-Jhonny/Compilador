package org.example;

import org.example.lexer.Lexer;
import org.example.lexer.RegexLexer;
import org.example.lexer.Token;
import org.example.lexer.TokenType;
import org.example.parser.Parser;
import org.example.parser.TokenBuffer;
import java.io.UnsupportedEncodingException;

public class Main {
     public static void main(String[] args) throws UnsupportedEncodingException {
         if (args.length < 1) {
             System.err.println("Uso: java Main <arquivo>");
             return;
         }

         String filePath = args[0];

         try (Lexer lexer = new RegexLexer(filePath)) {
             // Primeiro mostra os tokens
             System.out.println("=== TOKENS ===");
             Token token;
             while ((token = lexer.readNextToken()).type() != TokenType.EOF) {
                 System.out.println(token);
             }

             // Depois recria o lexer e buffer para o parser
             try (Lexer lexerForParser = new RegexLexer(filePath);
                  TokenBuffer buffer = new TokenBuffer(lexerForParser)) {

                 System.out.println("\n=== ANALISE SINTATICA ===");
                 Parser parser = new Parser(buffer);
                 parser.parse();
                 System.out.println("Expressao valida!");
             }
         } catch (Exception e) {
             System.err.println("Erro: " + e.getMessage());
         }
     }
 }

//    # Compile tudo diretamente para a pasta 'out'
//javac -d out src/main/java/org/example/Main.java src/main/java/org/example/lexer/*.java src/main/java/org/example/parser/*.java
//
//    # Execute a partir da raiz do projeto
//java -cp out org.example.Main src/main/java/org/example/entrada.txt
