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
        boolean stop = true;
        
        try {
            ia = InetAddress.getByName(IP_Serveur);
        } catch (UnknownHostException ex) {
            System.err.println("Erreur dans l'adresse ip du serveur écrite en ascii !");
        }
        
        System.out.println("On contacte le serveur : " + ia.toString() + ":" + PORT_HOST_STANDARD);
        client.envoyer("hello serveur RX302", ia, PORT_HOST_STANDARD);
        DatagramPacket dp = client.receptionner(buffer);
        int port_co = dp.getPort();
        ia = dp.getAddress();
        System.out.println("Le serveur a ouvert un nouveau port : " + ia.toString() + ":" + port_co);
        
        do
        {
            System.out.println("Saisie du texte à envoyer ('stop' pour quitter)");
            String str = sc.nextLine();
            
            if(str.equals("stop")) stop = false;
            
            client.envoyer(str, ia, port_co);
            dp = client.receptionner(buffer);
            String reponse = new String(dp.getData());
            System.out.println("Serveur " + dp.getAddress().toString() + ":" + dp.getPort() + " : " + reponse);
        }while(stop);
        
        client.ds.close();
    }
}
