//package ecc.io;

//import ecc.*;

public class SillyCryptoSystem implements CryptoSystem {
    public byte[] encrypt(byte[] plain, int numbytes, Key ek) {
	if(plain.length == numbytes) return plain;
	byte[] temp = new byte[numbytes];
	System.arraycopy(plain, 0, temp, 0, numbytes);
	return temp;
    }

    public byte[] decrypt(byte[] cipher, Key dk) {
	return cipher;
    }
    
    public Key generateKey() {
	return null;
    }

    public int blockSize() {
	return 20;
    }

    public String toString() {
	return "SillyCryptoSystem";
    }
}
