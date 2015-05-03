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
    protected static final int DEBUT_PLAGE = 10000;
    protected static final int FIN_PLAGE = 11000;
    protected static DatagramPacket dp;
    
    public Serveur(int port)
    {
        super(port);
    }
        
    public Serveur(int port, DatagramPacket dp)
    {
        super(port);
        this.dp = dp;
    }
    
    @Override
    public void run()
    {
        try {
            envoyer("serveur prêt", dp.getAddress(), dp.getPort());
            
            byte[] buffer = new byte[MAX];
            dp = receptionner(buffer);
            
            String reçu = new String(dp.getData());
            System.out.println("Message recu : " + reçu);
            
            envoyer(reçu, dp.getAddress(), dp.getPort());
            ds.close();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Serveur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
