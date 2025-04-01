package org.example.lexer;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;

public class CharBuffer implements Closeable{
    
    private final PushbackReader reader;
    public static final int EOF = -1;

    public CharBuffer(String filePath) throws FileNotFoundException{
        reader = new PushbackReader(new FileReader(filePath));
    }

    public int readNextChar() throws IOException{
        return reader.read();
    }

    public void pushback(int c) throws IOException{
        if(c >= 0)
            reader.unread(c);
    }

    public void close() throws IOException{
        reader.close();
    }

}
