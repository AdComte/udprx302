package udp.client;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import udp.ObjetConnecte;

public class Client extends ObjetConnecte{
    protected static String IP_Serveur = "localhost";
    
    public Client()
    {
        super();
    }
    
    public static void main(String[] args) throws UnsupportedEncodingException {
        Client client = new Client();
        byte[] buffer = new byte[MAX];
        
        Scanner sc = new Scanner(System.in); //saisie texte au clavier
        InetAddress ia = null;
        
        try {
            ia = InetAddress.getByName(IP_Serveur);
        } catch (UnknownHostException ex) {
            System.err.println("erreur dans l'adresse ip du serveur dans écrite en ascii !");
        }
        System.out.println("On contacte le serveur : " + ia.toString() + ":" + PORT_HOST_STANDARD);
        client.envoyer("hello serveur RX302", ia, PORT_HOST_STANDARD);
        DatagramPacket dp = client.receptionner(buffer);
        
        int port_co = dp.getPort();
        ia = dp.getAddress();
        System.out.println("Le serveur a répondu : " + ia.toString() + ":" + port_co);
        
        System.out.println("Tapez le texte à envoyer");
        String str = sc.nextLine();
        
        client.envoyer(str, ia, port_co);
        dp = client.receptionner(buffer);
        String reponse = new String(dp.getData());
        System.out.println("Le serveur a répondu : " + dp.getAddress().toString() + ":" + dp.getPort());
        System.out.println(reponse);
    }
}
