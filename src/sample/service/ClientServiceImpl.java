package sample.service;

import sample.bean.*;
import sample.helper.CommandeHelper;
import sample.helper.LigneCommandeHelper;
import sample.helper.PlatHelper;
import sample.util.*;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ClientServiceImpl extends UnicastRemoteObject implements ClientService {
    private static final long serialVersionUID = 1L;

    public ClientServiceImpl() throws RemoteException {
    }

    @Override
    public Object[] seConnecter(String login, String password) {
        if (login == null || login.isEmpty()) return new Object[]{-1, null};
        else if (password == null || password.isEmpty()) return new Object[]{-2, null};
        else {
            Client client = findByLogin(login);
            if (client == null) return new Object[]{-3, null};
            else if (!HashageUtil.sha256(password).equals(client.getPassword())) return new Object[]{-4, null};
            else {
                return new Object[]{1, client};
            }
        }
    }

    @Override
    public int inscrire(Client client) throws RemoteException {
        int result = -3;
        if (client == null || client.getNom() == null || client.getPrenom() == null || client.getPassword() == null || client.getEmail() == null || client.getPassword() == null || client.getAdresse() == null || client.getAdresse() == null) {
            result = -1;
        } else {
            try {
                Client dbClient = findByLogin(client.getEmail());
                if (dbClient != null) {
                    result = -2;
                } else {
                    String query = "INSERT INTO Client(email, password, nom, prenom, adresse, numtel) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement ps = ConnectDB.prepare(query);
                    ps.setString(1, client.getEmail());
                    ps.setString(2, HashageUtil.sha256(client.getPassword()));
                    ps.setString(3, client.getNom());
                    ps.setString(4, client.getPrenom());
                    ps.setString(5, client.getAdresse());
                    ps.setString(6, client.getNumTel());
                    ConnectDB.executePreparedStatment(ps);
                    result = 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public HashMap<String, List<Plat>> getMenuItems() throws RemoteException {
        HashMap<String, List<Plat>> mapPLats = new HashMap<>();
        List<Categorie> categories = findAllCategories();
        List<Plat> plats;
        String query = "SELECT * FROM Plat p  WHERE p.categorieId = ? ORDER BY p.prix ASC";
        PreparedStatement preparedStatement;
        ResultSet rs;
        for (Categorie categorie : categories) {
            try {
                plats = new ArrayList<>();
                preparedStatement = ConnectDB.prepare(query);
                preparedStatement.setLong(1, categorie.getId());
                rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    File file = Util.blobToFile(rs.getBlob("IMAGE"), rs.getString("NOM") + ".jpg");
                    plats.add(new Plat(rs.getLong("ID"), rs.getString("NOM"), rs.getDouble("PRIX"), file, rs.getLong("categorieId")));
                }
                mapPLats.put(categorie.getLibelle(), plats);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return mapPLats;
    }


    public List<Categorie> findAllCategories() {
        List<Categorie> categories = new ArrayList<>();
        Connection connection = null;
        Statement cs = null;
        ResultSet rs = null;
        String query = "SELECT * FROM CATEGORIE";
        try {
            connection = ConnectDB.getConnection();
            cs = connection.createStatement();
            rs = cs.executeQuery(query);
            while (rs.next()) {
                categories.add(new Categorie(rs.getLong("ID"), rs.getString("LIBELLE")));
            }
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        } finally {
            try {
                if (cs != null) cs.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return categories;
        }
    }

    @Override
    public int commander(Commande commande, List<LigneCommande> ligneCommandes, Client client) throws RemoteException {
        if (ligneCommandes == null || ligneCommandes.isEmpty() || commande == null || commande.getAdresse() == null || commande.getClientId() == null)
            return -1;
        String query = "INSERT INTO commande(adresse, dateCommande, statut, clientId) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = ConnectDB.prepareWithKey(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            ps.setString(1, commande.getAdresse());
            Calendar cal = Calendar.getInstance();
            ps.setTimestamp(2, new java.sql.Timestamp(cal.getTimeInMillis()));
            ps.setString(3, commande.getStatut());
            ps.setLong(4, commande.getClientId());
            Long commandeId = ConnectDB.getKeyExecutePreparedStatmentWithKey(ps);
            if (commandeId == null) return -2;
            else {
                insertLigneCommandes(commandeId, ligneCommandes);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sendReceptionConfirmationMail(client, null);

        return 1;
    }

    private void insertLigneCommandes(Long commandeId, List<LigneCommande> ligneCommandes) {
        String query = "INSERT INTO lignecommande(commandeId, quantite, platId) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = ConnectDB.prepare(query);
            for (LigneCommande ligneCommande : ligneCommandes) {
                ps.setLong(1, commandeId);
                ps.setInt(2, ligneCommande.getQuantite());
                ps.setLong(3, ligneCommande.getPlatId());
                ConnectDB.executePreparedStatment(ps);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<CommandeHelper> findCommandesOfConnectedClient(Long idClient) {
        if (idClient == null) return null;
        List<CommandeHelper> commandes = new ArrayList<>();
        ResultSet rs;
        String query = "SELECT c.id, c.dateCommande, c.adresse, SUM(p.prix*lc.quantite) AS montantTotal, c.statut\n" +
                "FROM COMMANDE c, lignecommande lc, plat p \n" +
                "WHERE lc.commandeId=c.id AND lc.platId=p.id\n" +
                "and clientId = " + idClient + " GROUP BY c.id ORDER BY dateCommande";
        try {
            rs = ConnectDB.executeQuery(query);
            while (rs.next()) {
                commandes.add(new CommandeHelper(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getString(5)));
            }
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return commandes;
    }

    @Override
    public List<LigneCommandeHelper> findLigneCommandesByCommandeID(Long idCommande) throws RemoteException {
        if (idCommande == null) return null;
        List<LigneCommandeHelper> ligneCommandes = new ArrayList<>();
        ResultSet rs;
        String query = "SELECT lc.id, p.nom, lc.quantite, p.prix, (p.prix*lc.quantite) AS montantLigne \n" +
                "FROM lignecommande lc, plat p WHERE p.id=lc.platId \n" +
                "AND lc.commandeId= " + idCommande;
        try {
            rs = ConnectDB.executeQuery(query);
            while (rs.next()) {
                ligneCommandes.add(new LigneCommandeHelper(rs.getLong(1), rs.getString(2), rs.getInt(3), rs.getDouble(4), rs.getDouble(5)));
            }
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return ligneCommandes;
    }

    @Override
    public int resetPassword(Client newClient) throws RemoteException {
        Client bdClient= findByLogin(newClient.getEmail());
        String hashPswd=HashageUtil.sha256(newClient.getPassword());
        if (bdClient==null){
            return -1;
        }else if (bdClient.getPassword().equals(hashPswd)){
            return -2;
        }else {
            updatePassword(bdClient.getId(),hashPswd);
            sendResetPasswordMail(bdClient,newClient.getPassword());
            return 1;
        }
    }

    private void updatePassword(Long id, String hashPswd) {
        String query="UPDATE CLIENT SET password = ? WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = ConnectDB.prepare(query);
            ps.setString(1, hashPswd);
            ps.setLong(2, id);
            ConnectDB.executePreparedStatment(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public Client findByLogin(String login) {
        String query = "SELECT * FROM CLIENT WHERE EMAIL = '" + login + "'";
        Connection connection = null;
        Statement cs = null;
        ResultSet rs = null;
        Client bdclient = null;
        try {
            connection = ConnectDB.getConnection();
            cs = connection.createStatement();
            cs = connection.createStatement();
            rs = cs.executeQuery(query);
            while (rs.next()) {
                bdclient = new Client(rs.getLong("ID"), rs.getString("NOM"), rs.getString("PRENOM"), rs.getString("EMAIL"), rs.getString("PASSWORD"), rs.getString("ADRESSE"), rs.getString("NUMTEL"));
            }
            return bdclient;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        } finally {
            try {
                if (cs != null) cs.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return bdclient;
        }
    }

    private void sendReceptionConfirmationMail(Client client, String fileAttachement) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Africa/Casablanca"));
        String message = "Cher(e)" + client.getNom() + " " + client.getPrenom() + ",\n" +
                "Votre commande effectuée le " + dtf.format(now) + ", a bien été enregisté. \n" +
                "Elle vous sera livrée le plus tôt possible.";

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    EmailUtil.sendMail(message, client.getEmail(), "Confirmation de commande.", null);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread=new Thread(runnable);
        thread.start();
    }

    private void sendResetPasswordMail(Client client, String newPassword) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Africa/Casablanca"));
        String message = "Cher(e)" + client.getNom() + " " + client.getPrenom() + ",\n" +
                "Votre mot de passe a été changer le " + dtf.format(now) + " . \n" +
                "votre nouveau mot de passe est: "+ newPassword ;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    EmailUtil.sendMail(message, client.getEmail(), "Réinitialisation du mot de passe.", null);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread=new Thread(runnable);
        thread.start();
    }
}
