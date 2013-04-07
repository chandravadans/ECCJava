//package ecc.gui;

//import ecc.*;
//import ecc.elliptic.*;
//import ecc.rsa.*;

public class GUITest{

    public static void main(String[] args){
	try{
	    long time = System.currentTimeMillis();
	    EllipticCurve ec = new EllipticCurve(new secp256r1());
	    ECCryptoSystem cs = new ECCryptoSystem(ec);
            new Login(null);
	    //View v = new View(600,600, new RSACryptoSystem());

	}
	catch(Exception e){
	    System.out.println("Error initializing system... exit(0)");
	    System.exit(0);
	}
    }

}
