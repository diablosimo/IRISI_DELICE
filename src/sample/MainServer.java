package sample;

import sample.service.AdminServiceImpl;
import sample.service.ClientServiceImpl;
import sample.util.EmailUtil;

import javax.mail.MessagingException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;

public class MainServer {
    public static void main(String[] args) {
        try {// Cration de l’OD
            AdminServiceImpl od = new AdminServiceImpl();
            LocateRegistry.createRegistry(1099);
            // Enregistrement de l’OD dans RMI
            Naming.rebind("admin_delice", (Remote) od);
            System.out.println("L’Objet Distant (OD) est Enregistré dans RMI... Serveur Prêt");
        } catch (Exception e) {
            System.out.println("Serveur Non Lancé!!!");
            e.printStackTrace();
        }
        try {// Cration de l’OD
            LocateRegistry.createRegistry(1098);
            ClientServiceImpl od = new ClientServiceImpl();
            // Enregistrement de l’OD dans RMI
            Naming.rebind("client_delice", (Remote) od);
            System.out.println("L’Objet Distant (OD) est Enregistré dans RMI... Serveur Prêt");
        } catch (Exception e) {
            System.out.println("Serveur Non Lancé!!!");
            e.printStackTrace();
        }


    }
}
