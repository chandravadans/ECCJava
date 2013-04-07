//package ecc.io;

import java.io.*;
//import ecc.*;
//import ecc.elliptic.*;
//import ecc.rsa.*;
import java.util.zip.*;
import java.math.*;

public class TestCryptoStreams {
    public static void main(String[] args) {

	if(args.length > 3) {
	    System.out.println("at most three filenames...");
	}
	String[] temp = new String[]{"/users/trold/Text/elmo.txt","cipher.bin","plain.txt"};
	System.arraycopy(args, 0, temp, 0, args.length);
	args = temp;
	try {
	    System.out.print("Initializing... ");
	    long time = System.currentTimeMillis();
	    EllipticCurve ec = new EllipticCurve(new secp160r1());
	    ECCryptoSystem cs = new ECCryptoSystem(ec);
	    Key sk = cs.generateKey();
	    Key pk = sk.getPublic();
	    /*RSAKey sk = new RSAKey(1024);
	    RSAKey pk = sk.getPublic();
	    CryptoSystem cs = new RSACryptoSystem();*/
	    /*CryptoSystem cs = new SillyCryptoSystem();
	    Key sk = null;
	    Key pk = null;*/
	    System.out.print("done");
	    System.out.println(" ("+(System.currentTimeMillis()-time)+" ms)");
	    InputStream in = new FileInputStream(new File(args[0]));
	    OutputStream out = new CryptoOutputStream(new FileOutputStream(new File(args[1])), cs, pk);
	    int read;
	    System.out.print("Encrypting... ");
	    time = System.currentTimeMillis();
	    int bytes = 0;
	    while((read = in.read()) != -1) {
		out.write(read);
		bytes++;
	    }
	    out.flush();
	    in.close();
	    out.close();
	    System.out.print("done");
	    time = System.currentTimeMillis()-time;
	    System.out.println(" ("+time+" ms) ["+(bytes*1000/time)+" bytes/sec]");
	    //System.exit(1);
	    in = new CryptoInputStream(new FileInputStream(new File(args[1])), cs, sk);
	    out = new FileOutputStream(new File(args[2]));
	    System.out.print("Decrypting... ");
	    time = System.currentTimeMillis();
	    bytes = 0;
	    while((read = in.read()) != -1) {
		out.write(read);
		bytes++;
	    }
	    out.flush();
	    in.close();
	    out.close();
	    System.out.print("done");
	    time = System.currentTimeMillis()-time;
	    System.out.println(" ("+time+" ms) ["+(bytes*1000/time)+" bytes/sec]");
	} catch (IOException e) {
	    System.out.println("TestCryptoStreams: "+e);
	} catch (InsecureCurveException e) {
	    System.out.println("TestCryptoStreams: "+e);
	}
    }
}
