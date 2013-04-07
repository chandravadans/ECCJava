import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Screen1 extends JFrame implements ActionListener
{
	JTextArea ta;
	JLabel path, title;
	JTextField pathtf;
	FileDialog fd;
	JButton Load,Close,Next,fdl;
	JPanel p1, p2;
	String s,file,p;
        //Chatter ch;
	File targetFile;
	String Path;

        public Screen1()//Chatter ch)
	{
		try
		{
			setLayout(null);
			path = new JLabel("Path  :");
			title=new JLabel("Choose a text file for encryption");
			pathtf = new JTextField(15);
			pathtf.setFont(new Font("TimesNewRoman", Font.BOLD, 12));
			pathtf.setEditable(false);


                        //this.ch = ch;

			fdl = new JButton("Select File");
			fdl.setMnemonic('S');
			fdl.addActionListener(this);

			Load = new JButton("Load");
			Load.setMnemonic('L');
			Load.addActionListener(this);

			
			addc(path, 50, 40, 180, 25);
			addc(pathtf, 120, 40, 120, 25);
			addc(fdl, 300, 40, 100, 25);
			addc(Load, 50, 280, 90, 25);


			Next= new JButton("Next");
			Next.setMnemonic('N');
			Next.addActionListener(this);

			Close = new JButton("Close");
			Close.setMnemonic('C');
			Close.addActionListener(this);

			ta = new JTextArea();
			ta.setFont(new Font("Times New Roman",Font.BOLD,12));
			ta.setEditable(false);
			ta.setText("<<Please select the file having the plain text, and click on Load. The text should appear here.>>");

			addc(new JScrollPane(ta,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),50,100,350,150 );
			ta.setLineWrap(true);
			addc(Close, 190, 280, 90, 25);
			addc(Next, 300, 280, 90, 25);

			setTitle("Pick a text file for encrypting using ECC");
			setVisible(true);
			setSize(450, 400);
			setResizable(false);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
		}
		catch(Exception e)
		{
			System.out.println("Exception Here"+e);
			System.exit(0);
		}
	}
	public void addc(JComponent c, int x, int y, int w, int h)
	{
		c.setBounds(x, y, w, h);
		add(c);
	}

	public void actionPerformed(ActionEvent a)
	{
		JButton b=(JButton) a.getSource();
		if (b == Close)
			System.exit(0);
		if (b == Next)
		{ 	
			s=ta.getText();
			secp112r1 curve=new secp112r1();
			try
			{
				EllipticCurve ec = new EllipticCurve(new secp256r1());
                                View v = new View(600,600, new ECCryptoSystem(ec));//ch);
				v.Path=this.Path;
			}
			catch(Exception e){System.out.println("EC Exception");}
		
			//Screen2 f2 = new Screen2();
			//View v = new View(600,600, new RSACryptoSystem());
			//f2.s2 = new String(s);
			//f2.sbyte = new byte[s.length()];
			//dispose();
		}
		if (b == fdl)
		{

			//JFileChooser x=new JFileChooser();
			File f=new File(".");
			String currdir=new String("hi");
			try
			{
				currdir=f.getCanonicalPath();
			}
			catch(Exception e)
			{
				System.out.println("\n Error getting the current dir");
			}
		
			JFileChooser chooser = new JFileChooser(currdir);
			int returnVal = chooser.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) 
			{
				targetFile= chooser.getSelectedFile();
				Path=targetFile.getPath();
				System.out.println("\nPath is "+Path);
			//	return targetFile;
			}
			else 
				System.out.println("Error in opening File");

			/*fd = new FileDialog(this, "Select File", FileDialog.LOAD);
			  fd.setVisible(true);
			  file = fd.getFile();
			  p = fd.getDirectory();
			  pathtf.setText(p + "" + file);
			  file = pathtf.getText();*/
		}
		if (b == Load)
		{
			try
			{
				ta.setText("");
				FileInputStream fin = new FileInputStream(Path);
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				while (fin.available() > 0)
				{ bout.write(fin.read()); }
				fin.close();
				s = bout.toString();
				ta.insert(s,0);
				ta.setFont(new Font("TimesNewRoman", Font.BOLD, 12));	
			}
			catch (Exception e)
			{
				System.out.println("Error at File loading :" + e);
			}
		}
	}
}
