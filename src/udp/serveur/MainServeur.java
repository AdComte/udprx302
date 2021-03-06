/*Adrien COMTE 11206512*/
package udp.serveur;

import java.util.ArrayList;
import static udp.ObjetConnecte.MAX;
import static udp.ObjetConnecte.PORT_HOST_STANDARD;
import static udp.serveur.Serveur.DEBUT_PLAGE;
import static udp.serveur.Serveur.FIN_PLAGE;

public class MainServeur {
    
    public static void main(String[] args){
        byte[] buffer = new byte[MAX];
        Serveur serveur = new Serveur(PORT_HOST_STANDARD);
        
        while(true){
            System.out.println("Serveur en attente : " + PORT_HOST_STANDARD);
            serveur.dp = serveur.receptionner(buffer);
            String reçu = new String(serveur.dp.getData());
            if(reçu.equals("hello serveur RX302                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             ")){
                ArrayList port_réponse = serveur.scanPortLibre(DEBUT_PLAGE, FIN_PLAGE);
                Serveur serveur_reponse = new Serveur((int)port_réponse.get(0), serveur.dp);

                System.out.println("Connexion nouveau client " + serveur.dp.getAddress().getHostAddress() + ":" + serveur.dp.getPort() + " sur le port local " + (int)port_réponse.get(0));

                Thread thread = new Thread(serveur_reponse);
                thread.setDaemon(true);
                thread.start();
            }else{
                System.err.println("Echec de la connexion");
            }
        }
    }
}
