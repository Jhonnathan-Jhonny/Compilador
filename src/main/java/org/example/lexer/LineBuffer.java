package org.example.lexer;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;

public class LineBuffer implements Closeable{
    
    private final BufferedReader reader;
    private int row, col;
    private String currentLine;
    private String lastMatch;

    public LineBuffer(String filePath) throws IOException{
        reader = new BufferedReader(new FileReader(filePath));
        currentLine = reader.readLine();
        row = 0;
        col = 0;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public String getReadedLine(){
        return currentLine;
    }

    public Character currentChar(){
        return currentLine.charAt(col);
    }

    public void readNextLine() throws IOException{
        currentLine = reader.readLine();
        row++;
        col = 0;
    }

    public String getLastMatch(){
        return lastMatch;
    }

    public boolean isEndOfLine(){
        return getCol() >= currentLine.length();
    }

    public boolean prefixMatches(TokenPattern pattern){
        if(currentLine == null)
            return false;

        var matcher = pattern.getPattern().matcher(currentLine.substring(col));

        if(matcher.lookingAt()){
            String lexema = matcher.group();
            col += lexema.length();
            lastMatch = lexema;
            return true;
        }

        return false;
    }   

    @Override
    public void close() throws IOException {
        reader.close();
    }

}
