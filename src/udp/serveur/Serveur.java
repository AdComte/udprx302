package udp.serveur;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import udp.ObjetConnecte;

public class Serveur extends ObjetConnecte implements Runnable{
    private static final int DEBUT_PLAGE = 10000;
    private static final int FIN_PLAGE = 11000;
    private static DatagramPacket dp;
    private static DatagramSocket ds_reponse;
    
    public Serveur(int port)
    {
        super(port);
    }
        
    public void répondre(String message, InetAddress iadresse, int port) throws UnsupportedEncodingException
    {
        byte[] message_bin = message.getBytes("ascii");
        DatagramPacket dp = new DatagramPacket(message_bin, message_bin.length, iadresse, port);
        
        try
        {
            ds_reponse.send(dp);
        } catch (IOException ex)
        {
            System.err.println("le DatagramSocket n'a pas envoyé le DatagramPacket");
            System.exit(1);
        }
    }
    
    public DatagramPacket recevoir(byte[] buffer) //Attention à la taille du buffer
    {
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
        try
        {
            ds_reponse.receive(dp);
        } catch (IOException ex)
        {
            System.err.println("le DatagramSocket n'a pas reçu le DatagramPacket"
                    + " (peut etre un dépassement de length sur le buffer)");
            System.exit(1);
        }
        return dp;
    }
    
    public static void main(String[] args){
        byte[] buffer = new byte[MAX];

        while(true){
            Serveur serveur = new Serveur(PORT_HOST_STANDARD);
            System.out.println("Serveur en attente : " + PORT_HOST_STANDARD);
            serveur.dp = serveur.receptionner(buffer);
            serveur.ds.close();
            Thread thread = new Thread(serveur);
            thread.start();
        }
    }
    
    @Override
    public void run()
    {
        try {
            
            InetAddress ia = dp.getAddress();
            int port_co = dp.getPort();
            System.out.println("Connexion nouveau client " + ia.getHostAddress() + " : " + port_co);
            
            ArrayList port_réponse = scanPortLibre(DEBUT_PLAGE, FIN_PLAGE);
            ds_reponse = new DatagramSocket( (int)port_réponse.get(0));
            répondre("serveur prêt", ia, port_co);
            byte[] buffer = new byte[MAX];
            dp = recevoir(buffer);
            String reçu = new String(dp.getData());
            System.out.println("Message recu : " + reçu);
            répondre(reçu, dp.getAddress(), dp.getPort());
            ds_reponse.close();
        } catch (SocketException ex) {
            Logger.getLogger(Serveur.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Serveur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
