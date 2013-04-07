//package ecc.elliptic;

//import ecc.*;
//import ecc.io.*;
import java.util.*;

public class TestECCrypto {
    public static void main(String[] args) {
	try {
	    EllipticCurve ec = new EllipticCurve(new secp256r1());

	    CryptoSystem cs = new ECCryptoSystem(ec);

	    Key sk = (ECKey)cs.generateKey();
	    Key pk = sk.getPublic();
	    System.out.println(sk+"\n"+pk);

	    //byte[] test1 = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
	    String plain=new String("H");
	    byte[] test1=plain.getBytes();
	    byte[] test2 = cs.encrypt(test1, plain.length(), pk);
	    //String plain=test1.toString();
	    String enc=new String(" ");
	    try
	    {
	    	enc=new String((cs.encrypt(test1,plain.length(),pk)));
	    }
	    catch(Exception e)
	    {
		    System.out.println("Exception!");

	    }
	    System.out.println("Plain Text is : "+plain);
	    
	    System.out.println("Encrypted text is: "+enc);
	    /*for (int i=0;i<(test2.getBytes()).length;i++)
		    System.out.print(test2[i]);*/
	    //if(Arrays.equals(test1, test1)) System.out.println("Testing...");
	    //if(Arrays.equals(test1, test2)) {
		//System.out.println("Succes");
	    //} else {
	//	System.out.print("Fail\n{");
	//	for(int i = 0; i < 20; i++) {
	//	    System.out.print(test2[i]+",");
	//	}
	//	System.out.println("}");
	   // }
	  byte[] dec=cs.decrypt(test2,sk);
	  String decrypt=new String(dec);
	  System.out.println("Decrypted text is: "+decrypt);
	} catch (InsecureCurveException e) {
	    System.out.println("TestCryptoStreams: "+e);
	}
    }
}
