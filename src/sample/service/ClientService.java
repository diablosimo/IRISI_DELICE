package sample.service;

import sample.bean.Client;
import sample.bean.Commande;
import sample.bean.LigneCommande;
import sample.bean.Plat;
import sample.helper.CommandeHelper;
import sample.helper.LigneCommandeHelper;
import sample.helper.PlatHelper;
import sample.helper.PlatView;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

public interface ClientService extends Remote {
    public Object[] seConnecter(String login, String password) throws RemoteException;

    public int inscrire(Client client) throws RemoteException;

    public HashMap<String, List<Plat>> getMenuItems() throws RemoteException;

    public int commander(Commande commande, List<LigneCommande> ligneCommandes,Client client) throws RemoteException;

    public List<CommandeHelper> findCommandesOfConnectedClient(Long idClient) throws RemoteException;

    public List<LigneCommandeHelper> findLigneCommandesByCommandeID(Long idCommande) throws RemoteException;

    public int resetPassword (Client newClient) throws RemoteException;;
}
