/*Adrien COMTE 11206512*/
package udp.serveur;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
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
        boolean stop = true;
        
        try {
            envoyer("serveur prêt", dp.getAddress(), dp.getPort());
        } catch (UnsupportedEncodingException ex) {
            System.err.println("Problème dans la réponse du serveur au client");
        }
        
        do
        {
            try {
                byte[] buffer = new byte[MAX];
                dp = receptionner(buffer);

                String reçu = new String(dp.getData());
                System.out.println("Message recu (" + ds.getLocalPort() + ") : " + reçu);
                if (reçu.equals("stop                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            ")) stop = false;
                envoyer(reçu, dp.getAddress(), dp.getPort());
            } catch (UnsupportedEncodingException ex) {
                System.err.println("Problème dans la réponse du serveur au client");
            }
        }while(stop);
        
        System.out.println("Port fermé (client " + dp.getPort() + ") : " + ds.getLocalPort());
        ds.close();
    }
}
