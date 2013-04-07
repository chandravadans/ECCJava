//package ecc.io;

import java.io.*;
//import ecc.*;

public class CryptoOutputStream extends OutputStream {
    private DataOutputStream out;
    private CryptoSystem cs;
    private Key key;
    private byte[] buffer;
    private int top;

    public CryptoOutputStream(OutputStream out, CryptoSystem cs, Key key) {
	this.out = new DataOutputStream(out);
	this.cs = cs;
	this.key = key;
	buffer = new byte[cs.blockSize()];
    }

    private void writeOut() throws IOException {
	if(top == 0) return;
	byte[] cipher = cs.encrypt(buffer, top, key);
	out.writeInt(cipher.length);
	out.write(cipher);
	top = 0;
    }

    public void write(int b) throws IOException {
	buffer[top] = (byte) b;
	top++;
	if(top == buffer.length) writeOut();
    }

    public void flush() throws IOException {
	writeOut();
	out.flush();
    }

    public void close() throws IOException {
	out.close();
    }
}
