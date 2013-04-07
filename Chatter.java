
 import java.io.*;
 import java.net.*;

 abstract class Chatter
 {
  Socket s;
  //either on the client side or
  //the server side

  boolean d=false;

  public boolean isDecrypt() {
   return d;
  }

  public OutputStream getOutputStream() throws Exception{
   return s.getOutputStream();
  }

  public InputStream getInputStream() throws Exception{
   return s.getInputStream();
  }

  public String readLine()
  {
   String line = "";
   try
   {
     BufferedReader input =
                  new BufferedReader(
                  new InputStreamReader(
                  s.getInputStream()));

     line =  input.readLine();
   }
   catch(Exception e)
   {
     line = e.toString();
   }
   return line;
  }

  public void writeLine(String line)
                             throws Exception
  {
   PrintWriter output =
    new PrintWriter(s.getOutputStream(),true);

   output.println(line);
  }
 }

