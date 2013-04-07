package ecc.gui;

import java.net.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

//import ecc.elliptic.*;
//import ecc.*;
//import ecc.io.*;

public class View extends JFrame implements ActionListener
{
	public final JButton ENCRYPT = new JButton("Encrypt");
	public final JButton DECRYPT = new JButton("Decrypt");
	//	public final JButton GENKEY = new JButton("Gen. key");
	public final JButton LOADKEY = new JButton("Load key");
	public final JButton SAVEKEY = new JButton("Display key");
	public final JButton QUIT = new JButton("Quit");
	public final String TITLE = "JECC";
	//File chosen;
	public String Path;

	private JScrollPane scroll_pane;
	private JLabel infoLabel = new JLabel("Welcome to JECC               ");
	private JLabel statusLabel = new JLabel();
	private JEditorPane pane;
	private CryptoSystem cs;
	private File targetFile;
	private Key pk;
	private Key sk;

	public View(int width, int height, CryptoSystem cs)
	{
		try{
			System.out.print("Loading " + TITLE +"...");

			Container cp = getContentPane();
			cp.setLayout(new BorderLayout());

			pane = new JEditorPane();
			pane.setEditable(false);
			pane.setFont(new Font("TimesNewRoman", Font.BOLD, 12));
			String text="Welcome to the encryption phase. Here the text from the file displayed in the last step will be encrypted, using the standard key for ECC( "+cs+" ). You can view the secret and public keys used by clicking the Display Key button, or load a set of keys from a file.";
			pane.setText(text);


			scroll_pane = new JScrollPane(pane);
			cp.add(scroll_pane, BorderLayout.CENTER);

			JPanel top = new JPanel(new FlowLayout());
			top.add(ENCRYPT);    ENCRYPT.addActionListener(this);
			top.add(DECRYPT);    DECRYPT.addActionListener(this);
			//			top.add(GENKEY);     GENKEY.addActionListener(this);
			top.add(LOADKEY);    LOADKEY.addActionListener(this);
			top.add(SAVEKEY);    SAVEKEY.addActionListener(this);
			top.add(infoLabel); 
			top.add(QUIT);       QUIT.addActionListener(this);
			cp.add(top, BorderLayout.NORTH);

			JPanel bottom = new JPanel(new FlowLayout());
			bottom.add(statusLabel);
			cp.add(bottom, BorderLayout.SOUTH);

			addWindowListener(new ExitController());

			setTitle(TITLE);
			setSize(width,height);
			setVisible(true);

			this.cs = cs;

			if (cs != null)
			{
				setInfo("Using: " + this.cs);
				sk = cs.generateKey();
				pk = sk.getPublic();
			}
			else setInfo("No CS loaded");

			setStatus("Ready");
			System.out.println("[OK]");
			System.out.println("Using: " + cs);
			System.out.println("Ready");
		}
		catch(Exception e) 
		{ 
			pane.setText("\n\tError\n\tE GUI-error in " + TITLE);
		}

	}

	public void setStatus(String s)
	{
		statusLabel.setText(s);
		statusLabel.repaint();
		try 
		{
			Thread.sleep(10);
		} catch(InterruptedException e){;}
	}

	public void setInfo(String s)
	{
		infoLabel.setText(s);
		infoLabel.repaint();
		try {Thread.sleep(10);} catch(InterruptedException e){;}
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == QUIT) 
		{
			System.out.println("Exiting " + TITLE);
			System.exit(0);
		}
		else if (e.getSource() == ENCRYPT) 
		{
			encrypt(); 
			this.setVisible(false);
			try {Thread.sleep(250);} catch(InterruptedException E){;}

			ENCRYPT.setVisible(false);
			LOADKEY.setVisible(false);
			SAVEKEY.setVisible(false);

			this.repaint();
			this.setVisible(true);
		}
		else if (e.getSource() == DECRYPT) 
		{
			this.setVisible(false);
			decrypt();
			try{ Thread.sleep(250);} catch(InterruptedException E){;}
			this.setVisible(true);

		}
		else if (e.getSource() == LOADKEY) {loadKey();}
		else if (e.getSource() == SAVEKEY) {saveKey();}

	}

	public File openFile()
	{
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) 
		{
			targetFile= chooser.getSelectedFile();
			Path=targetFile.getPath();
			System.out.println("\nPath is "+Path);
			return targetFile;
		}
		else 
			return null;
	}    

	public File saveFile()
	{
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showSaveDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) 
		{
			targetFile= chooser.getSelectedFile();
			Path=targetFile.getPath();
			return targetFile;
		}
		else return null;
	}    

	private void encrypt()
	{

		File f = new File(Path);
		if (f == null)
		{
			new JOptionPane("No file selected for encryption!");
			return;
		}
		try
		{
			InputStream in = new FileInputStream(f);
			OutputStream out = new CryptoOutputStream(new FileOutputStream(new File(f.getParent(),f.getName() + ".enc")), cs, pk);
			
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			String s;

			int read;
			setStatus("Encrypting: " + f.getName() + " -> " + "encrypted.txt ...");
			System.out.print("Encrypting: " + f.getName() + " -> " + "encrypted.txt ...");
			
			//For storing encrypted text in encrypted.txt file
			OutputStream eout = new FileOutputStream(new File(f.getParent(),"encrypted.txt"));

			int bytes = 0;
			while((read = in.read()) != -1) 
			{
				bout.write(read); 
				out.write(read);
				bytes++;
			}
			out.flush();
			in.close();
			out.close();
			setStatus("done");

			InputStream enc = new FileInputStream(new File(f.getParent(),f.getName() + ".enc"));
			ByteArrayOutputStream encout = new ByteArrayOutputStream();
			while((read = enc.read()) != -1) 
			{
				encout.write(read); 
			}
		
		
			String encrypted=encout.toString();
			String plain=bout.toString();
			String printed="";
			printed=encrypted;
			eout.write(printed.getBytes());
			eout.flush();
			eout.close();
		
			
			s = "Plain Text: " + plain + "\n" + "Encrypted Text:\n "+ printed;
			pane.setFont(new Font("TimesNewRoman", Font.BOLD, 12));
			pane.setText(s);
			enc.close();

			System.out.println("OK");

		}
		catch(IOException e)
		{
			System.out.println("Error in encryption!");
		}
	}

	private void decrypt()
	{
		Path=Path+".enc";
		File f = new File(Path);
		if (f == null)
		{
			new JOptionPane("No file selected for decryption!");
			return;
		}
		try
		{
			InputStream in = new CryptoInputStream(new FileInputStream(f), cs, sk);
		        OutputStream out = new FileOutputStream(new File(f.getParent(),f.getName().substring(0, f.getName().length()-4) + ".dec"));

			System.out.print("Decrypting: " + f.getName() + " -> " + f.getName().substring(0, f.getName().length()-4) + ".dec ...");
			setStatus("Decrypting: " + f.getName() + " -> " + f.getName().substring(0, f.getName().length()-4) + ".dec ...");
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			String s;

			int bytes = 0;
			int read;
			while((read = in.read()) != -1) 
			{
				out.write(read);
				bout.write(read);
				bytes++;
			}
			s = "Decrypted Text: " + bout.toString();
			pane.setFont(new Font("TimesNewRoman", Font.BOLD, 12));
			pane.setText(s);


			out.flush();
			in.close();
			out.close();
			System.out.println("OK");
			setStatus("done");
		}
		catch(Exception e)
		{
			System.out.println("Error in decryption!");
		}

	}

	private void loadKey() 
	{
		File f = openFile();
		if (f==null) 
		{
			new JOptionPane("No file selected for key");
			return;
		}
		try
		{
			sk=readKey(f);
			System.out.println("sk is:" + sk);
			pk=sk.getPublic();
		}
		catch(Exception e)
		{
			System.out.println("Error loading key!");
			e.printStackTrace();
		}	
	}

	private void saveKey() 
	{
		/*File f = saveFile();
		  if (f==null) 
		  {
		  new JOptionPane("No file selected for key");
		  return;
		  }
		  File fsk=new File(f.getName()+"_secret.txt");
		  System.out.println("Filename :"+fsk);
		  File fpk=new File(f.getName()+"_public.txt");
		  System.out.println("Filename 2: "+fpk);
		  try
		  {
		//if (!sk.isPublic()) 
		writeKey(fsk,sk);
		writeKey(fpk,pk);
		}
		catch(Exception e)
		{
		System.out.println("Error in decryption!");
		}*/
		String keydisp=" "+sk+"\n"+" "+pk;
		pane.setFont(new Font("TimesNewRoman", Font.BOLD, 12));
		pane.setText(keydisp);	
	}

	/** Writes the Key instance to a File f*/
	public void writeKey(File f, Key k)
	{
		try
		{
			FileOutputStream out=new FileOutputStream(f);
			k.writeKey(out);
			out.flush();
			out.close();
		}
		catch(IOException e)
		{
			System.out.println("Error writing key!\n");
			e.printStackTrace(System.out);
			System.exit(0);
		}
	}

	public Key readKey(File f)
	{
		try
		{
			Key k=cs.generateKey();
			FileInputStream in = new FileInputStream(f);
			k=k.readKey(in);
			in.close();
			return k;
		}
		catch(IOException e)
		{
			System.out.println("Error reading key file!\n" + e);
			return null;
		}
	}

	public class ExitController extends WindowAdapter
	{
		public void windowClosing(WindowEvent e)
		{
			System.out.println("Exiting " + TITLE);
			System.exit(0);
		}
	}

}
