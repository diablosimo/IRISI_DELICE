package sample.service;

import sample.bean.*;
import sample.helper.CommandeHelper;
import sample.helper.LigneCommandeHelper;
import sample.helper.PlatHelper;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface AdminService extends Remote {
    public Object[] seConnecter(String login, String password) throws RemoteException;

    public int ajouterCategorie(String libelle) throws RemoteException;

    public int modifierCategorie(Categorie categorie, String libelle) throws RemoteException;

    public int supprimerCategorie(Categorie categorie) throws RemoteException;

    public List<Categorie> findAllCategories() throws RemoteException;

    public int ajouterPlat(Plat plat, File imageFile) throws RemoteException;

    public List<PlatHelper> findByCriteria(String nom, Double prixMin, Double prixMax, Categorie categorie) throws RemoteException;

    public int modifierPlat(Plat plat) throws RemoteException;

    public List<CommandeHelper> findCommandesNoneTraitedCommandes() throws RemoteException;

    public List<LigneCommandeHelper> findLigneCommandesByCommandeID(Long idCommande) throws RemoteException;

    public int traiterCommande(Commande commande, String nvStatut,Long adminId) throws RemoteException;

    public HashMap<String, Integer> findNbCommandeByCategorie() throws RemoteException;

    public HashMap<String, Integer> findNbCommandeByPlat() throws RemoteException;

    public int inscrire(Admin admin) throws RemoteException;

}
