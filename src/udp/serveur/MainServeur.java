package udp.serveur;

import java.util.ArrayList;
import static udp.ObjetConnecte.MAX;
import static udp.ObjetConnecte.PORT_HOST_STANDARD;

public class MainServeur {
    
    public static void main(String[] args){
        byte[] buffer = new byte[MAX];
        Serveur serveur = new Serveur(PORT_HOST_STANDARD);
        
        while(true){
            System.out.println("Serveur en attente : " + PORT_HOST_STANDARD);
            serveur.dp = serveur.receptionner(buffer);
            
            System.out.println("Connexion nouveau client " + serveur.dp.getAddress().getHostAddress() + " : " + serveur.dp.getPort());
            ArrayList port_réponse = serveur.scanPortLibre(serveur.DEBUT_PLAGE, serveur.FIN_PLAGE);
            Serveur serveur_reponse = new Serveur((int)port_réponse.get(0), serveur.dp);
            
            Thread thread = new Thread(serveur_reponse);
            thread.setDaemon(true);
            thread.start();
        }
    }
}
