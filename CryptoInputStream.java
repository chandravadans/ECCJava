//package ecc.io;

import java.io.*;
//import ecc.*;

public class CryptoInputStream extends InputStream {
    private DataInputStream in;
    private CryptoSystem cs;
    private Key key;
    private byte[] buffer;
    private int top;
    private int blocksize;

    public CryptoInputStream(InputStream in, CryptoSystem cs, Key key) {
	this.in = new DataInputStream(in);
	this.cs = cs;
	this.key = key;
	buffer = new byte[0];
    }

    public int read() throws IOException {
	if(top == buffer.length) {
	    try {
		blocksize = in.readInt();
	    } catch (EOFException e) {
		return -1;
	    }
	    byte[] cipher = new byte[blocksize];
	    in.read(cipher);
	    buffer = cs.decrypt(cipher, key);
	    top = 0;
	}
	top++;
	return buffer[top-1];
    }

    public void close() throws IOException {
	in.close();
    }
}
