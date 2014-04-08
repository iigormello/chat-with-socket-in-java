package br.com.network;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author MELLO
 */
public class ChatCliente extends JFrame {
    
    private JTextField textoParaEnviar;
    private JButton botao;
    private Socket socket;
    private PrintWriter escritor;
    private String nome;
    private JTextArea textoRecebido;
    private Scanner leitor;
    
    public ChatCliente(String nome){
        super("Chat : " + nome);
        this.nome = nome;
        
        Font font = new Font("Serif", Font.PLAIN, 26);
        
        textoParaEnviar = new JTextField();
        botao = new JButton("Enviar");
        textoRecebido = new JTextArea();
        
        textoParaEnviar.setFont(font);
        botao.setFont(font);
        textoRecebido.setFont(font);
        JScrollPane scroll = new JScrollPane(textoRecebido);
        
        
        botao.addActionListener(new EnviarListener());
        Container enviar = new JPanel();
        enviar.setLayout(new BorderLayout());
        enviar.add(textoParaEnviar);
        enviar.add(BorderLayout.CENTER, textoParaEnviar);
        enviar.add(BorderLayout.EAST, botao);
       
        getContentPane().add(BorderLayout.CENTER, scroll);
        getContentPane().add(BorderLayout.SOUTH, enviar);
       
        configurarRede();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setVisible(true);
    }
    
    private class EscutaServidor implements Runnable {

        @Override
        public void run() {
            String texto;
            try {
                while((texto = leitor.nextLine()) != null){
                    textoRecebido.append(texto + "\n");
                }
            } catch(Exception e){}
        }
        
    }

    private class EnviarListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            escritor.println(nome + " : " + textoParaEnviar.getText());
            escritor.flush();
            textoParaEnviar.setText("");
            textoParaEnviar.requestFocus();
        }
    }
    
    private void configurarRede(){
        try {
            this.socket = new Socket("192.168.1.2", 5000);
            this.escritor = new PrintWriter(this.socket.getOutputStream());
            this.leitor = new Scanner(socket.getInputStream());
            new Thread(new EscutaServidor()).start();
        } catch (IOException ex) {
            Logger.getLogger(ChatCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void main(String[] args) {
        new ChatCliente("Ricardo");
        new ChatCliente("Sandra");
        
    }
    
}
