package udp.serveur;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import udp.ObjetConnecte;

public class Serveur extends ObjetConnecte implements Runnable{
    public static int DEBUT_PLAGE = 10000;
    public static int FIN_PLAGE = 11000;
    
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
        Serveur serveur = new Serveur(PORT_HOST_STANDARD);
        byte[] buffer = new byte[MAX];

        while(true){
        try
        {
            System.out.println("Serveur en attente : " + PORT_HOST_STANDARD);
            DatagramPacket dp = serveur.receptionner(buffer);
            InetAddress ia = dp.getAddress();
            int port_co = dp.getPort();
            System.out.println("Connexion nouveau client " + ia.getHostAddress() + " : " + port_co);
            
            ArrayList port_réponse = serveur.scanPortLibre(DEBUT_PLAGE, FIN_PLAGE);
            ds_reponse = new DatagramSocket( (int)port_réponse.get(0));
            serveur.répondre("serveur prêt", ia, port_co);
            buffer = new byte[MAX];
            dp = serveur.recevoir(buffer);
            String reçu = new String(dp.getData());
            System.out.println("Message recu : " + reçu);
            serveur.répondre(reçu, dp.getAddress(), dp.getPort());
        }
        catch(IOException ex) {
            System.err.println("Problème de reception du serveur !");
            System.exit(1);
        }
        }
    }
    
    public void run()
    {
        
    }
}
