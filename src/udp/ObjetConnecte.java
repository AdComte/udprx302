package udp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;


public class ObjetConnecte {
    public static int MAX = 512;
    public static int PORT_HOST_STANDARD = 8080;
    DatagramSocket ds;

    public ObjetConnecte() //Constructeur vide, ouvre un port disponible, utile pour l'envoie
    {
        try
        {
            ds = new DatagramSocket();
        } catch (SocketException ex)
        {
            System.err.println("Erreur de création de DatagramSocket dans le con"
                    + "structeur");
            System.exit(1);
        }
    }
    
    public ObjetConnecte(int port) //Constructeur avec port spécifié, utile pour recevoir
    {
        try
        {
            ds = new DatagramSocket(port);
        } catch (SocketException ex)
        {
            System.err.println("Erreur de création de DatagramSocket dans le con"
                    + "structeur (port passé en parametre)");
            System.exit(1);
        }
    }
    
    public void envoyer(String message, InetAddress iadresse, int port) throws UnsupportedEncodingException
    {
        byte[] message_bin = message.getBytes("ascii");
        DatagramPacket dp = new DatagramPacket(message_bin, message_bin.length, iadresse, port);
        
        try
        {
            ds.send(dp);
        } catch (IOException ex)
        {
            System.err.println("le DatagramSocket n'a pas envoyé le DatagramPacket");
            System.exit(1);
        }
    }
    
    public DatagramPacket receptionner (byte[] buffer) //Attention à la taille du buffer
    {
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
        try
        {
            ds.receive(dp);
        } catch (IOException ex)
        {
            System.err.println("le DatagramSocket n'a pas reçu le DatagramPacket"
                    + " (peut etre un dépassement de length sur le buffer)");
            System.exit(1);
        }
        return dp;
    }
    
    public ArrayList scanPortLibre(int bornemin, int bornemax)
    {
        if (bornemax < bornemin) {int t = bornemax; bornemax = bornemin; bornemin = t;}
        if (bornemin < 1024) return null;
        if (bornemax > 65535) return null;
        
        ArrayList resultat = new ArrayList();
        System.out.println("Analyse de la disponibilité des ports du port " + bornemin + " au port " + bornemax);
        
        for (int i = bornemin; i <= bornemax; ++i)
        {
            try
            {
                DatagramSocket temp = new DatagramSocket(i);
                resultat.add(i);
                System.out.println("port " + i + " libre et ajouté à la plage");
                temp.close();
            }
            catch(SocketException ex)
            {
                System.err.println("port " + i + " occupé !!");
            }
        }
        return resultat;
    }
}
