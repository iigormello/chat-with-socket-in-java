
package br.com.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MELLO
 */
public class ChatServer {
    
    private ArrayList<PrintWriter> escritores = new ArrayList<>();
    
    public ChatServer(){
        ServerSocket server;
        Scanner leitor;
        try {
            server = new ServerSocket(5000);
            while(true){
                Socket socket = server.accept();
                new Thread(new EscutaCliente(socket)).start();
                escritores.add(new PrintWriter(socket.getOutputStream()));
            }
            
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void encaminhaParaTodos(String texto){
        for(PrintWriter w : escritores){
            try{
                w.println(texto);
                w.flush();
            } catch(Exception e){}
        }
    }
    
    private class EscutaCliente implements Runnable {
        
        Scanner leitor;
        
        public EscutaCliente(Socket socket){
            try {
                leitor = new Scanner(socket.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        @Override
        public void run() {
            String texto;
            try {
                while((texto = leitor.nextLine()) != null){
                    System.out.println(texto);
                    encaminhaParaTodos(texto);
                }
            } catch (Exception e) {
            }
        }
    
    }
    
    public static void main(String[] args) {
        new ChatServer();
    }
    
}
