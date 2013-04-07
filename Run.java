//package ecc.elliptic;

import java.math.BigInteger;
import java.io.*;
import java.util.*;
import java.util.zip.*;

public class Run{

    public static void main(String[] args){
	try{
	    
	    BigInteger a = new BigInteger("1");
	    
	    BigInteger b = new BigInteger("6");
	    
	    BigInteger mod = new BigInteger("11");
	    
	    EllipticCurve e = new EllipticCurve(a, b, mod);

	    System.out.println("EllipticCurve: " + e + " created succesfully!");
	    
	    ECPoint p1 = new ECPoint(e,new BigInteger("2"), new BigInteger("7"));
	    ECPoint p2 = new ECPoint(e,new BigInteger("7"), new BigInteger("2"));
	    ECPoint p3 = new ECPoint(e,new BigInteger("2"), new BigInteger("-7"));

	    System.out.println(p1 +" + " + p2 + " = " + p1.add(p2));
	    System.out.println(p1 +" + " + p1 + " = " + p1.add(p1));
	    System.out.println(p1 +" + " + p3 + " = " + p1.add(p3));
	    System.out.println(p1 +" * " + mod + " = " + p1.multiply(mod));
	    System.out.println(p1 +" + " + e.getZero() + " = " + p1.add(e.getZero()));
	    System.out.println(e.getZero() +" + " + e.getZero() + " = " + e.getZero().add(e.getZero()));

	    System.out.println("Testing secp256r1...");

	    e = new EllipticCurve(new secp256r1());

	    System.out.println("New curve: " + e + " OK!");
	    System.out.println("Generator: " + e.getGenerator());
	    System.out.println("Order: " + e.getOrder());
	    System.out.println("Testing decompression of compression...");

	    p1 = new ECPoint(e.getGenerator().compress(), e);
	    if (e.getGenerator().getx().compareTo(p1.getx()) ==0) System.out.println("x values agree...");
	    else {
		System.out.println("x values disagree...");
		System.out.println("x-before:");
		System.out.println(e.getGenerator().gety());
		System.out.println("y-after:");
		System.out.println(p1.gety());
	    }
	    if (e.getGenerator().gety().compareTo(p1.gety()) ==0) System.out.println("y values agree...");
	    else {
		System.out.println("y values disagree...");
		System.out.println("y-before:");
		System.out.println(e.getGenerator().gety());
		System.out.println("y-after:");
		System.out.println(p1.gety());
	    }
	} catch (Exception e){
	    System.out.println("Run: "+e);
	}
    }
}
