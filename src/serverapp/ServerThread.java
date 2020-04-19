package serverapp;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

public class ServerThread extends Thread{
    
    //create variables of server form
    ServerForm sf;
    
    //variables to read and write messages
    ObjectInputStream oin;
    ObjectOutputStream out;
    
    //server socket object to create server
    ServerSocket serverSocket;
    
    //socket object to create socket connection
    Socket socket;
    
    //will pass parameter ServerForm object
    public ServerThread(ServerForm sf){
        this.sf = sf;
        
        try{
            //create server object with specific port number
            serverSocket = new ServerSocket(Settings.port);
            JOptionPane.showMessageDialog(sf, "Server Started");
            start();
            
        }catch(Exception e){
    }
    }
    
    //create a method to send a message
    public void sendMessage(String msg){
        try{
            out.writeObject(msg.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    @Override
        public void run(){
           while(true){
               try{
                   //accepting incoming connections to server using thread
                  socket = serverSocket.accept();
                  //calling method to create input/output objects
                  openReader();
               }catch(Exception e){
                   e.printStackTrace();
               }
           }
        }

    private void openReader() {
        try{
            oin = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            MsgRecThread mrt = new MsgRecThread(sf, oin, out);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public class MsgRecThread extends Thread{
        ServerForm sf;
        ObjectInputStream oin;
        ObjectOutputStream out;
        
        public MsgRecThread(ServerForm sf, ObjectInputStream oin, ObjectOutputStream out){
            this.sf = sf;
            this.oin = oin;
            this.out = out;
            start();
        }
        public void run(){
            while(true){
                try{
                    sf.jtaRec.append(oin.readObject().toString()+"\n");
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
