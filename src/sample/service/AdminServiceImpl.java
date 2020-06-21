package sample.service;

import javafx.scene.chart.PieChart;
import net.sf.jasperreports.engine.JRException;
import sample.bean.*;
import sample.helper.CommandeHelper;
import sample.helper.LigneCommandeHelper;
import sample.helper.PlatHelper;
import sample.util.*;
import sun.awt.SunHints;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AdminServiceImpl extends UnicastRemoteObject implements AdminService {
    private static final long serialVersionUID = 1L;

    public AdminServiceImpl() throws RemoteException {
    }

    @Override
    public Object[] seConnecter(String login, String password) {
        if (login == null || login.isEmpty()) return new Object[]{-1, null};
        else if (password == null || password.isEmpty()) return new Object[]{-2, null};
        else {
            Admin admin = findByLogin(login);
            if (admin == null) return new Object[]{-3, null};
            else if (!HashageUtil.sha256(password).equals(admin.getPassword())) return new Object[]{-4, null};
            else {
                return new Object[]{1, admin};
            }
        }
    }

    @Override
    public int ajouterCategorie(String libelle) {
        int result = -3;
        if (libelle == null || libelle.isEmpty()) result = -1;
        else if (findCategorieByLibelle(libelle) != null) result = -2;
        else {
            Categorie categorie = new Categorie(libelle);
            try {
                String query = DaoEngigne.constructDaoSaveRequette(categorie);
                String res = ConnectDB.exec(query);
                if (res.equals("Success")) result = 1;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public int modifierCategorie(Categorie categorie, String libelle) {
        int result = -3;
        if (libelle == null || libelle.isEmpty()) result = -1;
        if (categorie == null) result = -2;
        else if (libelle.equals(categorie.getLibelle())) result = -4;
        else {
            categorie.setLibelle(libelle);
            try {
                String query = DaoEngigne.constructDaoUpdateRequette(categorie, categorie.getId());
                String res = ConnectDB.exec(query);
                if (res.equals("Success")) result = 1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public int supprimerCategorie(Categorie categorie) {
        int result = -3;
        if (categorie == null || categorie.getId() == null) {
            result = -1;
        } else {
            try {
                String countPlatQuery = "SELECT COUNT(*) AS total FROM Plat WHERE categorieId='" + categorie.getId() + "'";
                ResultSet rs = ConnectDB.executeQuery(countPlatQuery);
                rs.next();
                int countPlat = rs.getInt("total");
                if (countPlat != 0) {
                    result = -2;
                } else {
                    ConnectDB.executeUpdate(DaoEngigne.constructDaoDeleteRequette(categorie));
                    result = 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public Admin findByLogin(String login) {
        String query = "SELECT * FROM ADMIN WHERE LOGIN='" + login + "'";
        Connection connection = null;
        Statement cs = null;
        ResultSet rs = null;
        Admin bdAdmin = null;
        try {
            connection = ConnectDB.getConnection();
            cs = connection.createStatement();
            rs = cs.executeQuery(query);
            while (rs.next()) {
                bdAdmin = new Admin(rs.getLong("ID"), rs.getString("LOGIN"), rs.getString("PASSWORD"), rs.getString("NOM"), rs.getString("PRENOM"));
            }
            return bdAdmin;
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
            return bdAdmin;
        }
    }

    public Categorie findCategorieByLibelle(String libelle) {
        String query = "SELECT * FROM CATEGORIE WHERE LIBELLE='" + libelle + "'";
        Connection connection = null;
        Statement cs = null;
        ResultSet rs = null;
        Categorie bdcategorie = null;
        try {
            connection = ConnectDB.getConnection();
            cs = connection.createStatement();
            rs = cs.executeQuery(query);
            while (rs.next()) {
                bdcategorie = new Categorie(rs.getLong("ID"), rs.getString("LIBELLE"));
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
            return bdcategorie;
        }
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

    public int ajouterPlat(Plat plat, File imageFile) {
        int result = -3;
        if (imageFile == null) result = -1;
        else {

            try {
                String query = "INSERT INTO plat(image, categorieId, prix, nom) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = ConnectDB.prepare(query);
                FileInputStream fis = new FileInputStream(imageFile);
                ps.setBinaryStream(1, fis, imageFile.length());
                ps.setLong(2, plat.getCategorieId());
                ps.setDouble(3, plat.getPrix());
                ps.setString(4, plat.getNom());
                ConnectDB.executePreparedStatment(ps);
                result = 1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public List<PlatHelper> findByCriteria(String nom, Double prixMin, Double prixMax, Categorie categorie) {
        List<PlatHelper> plats = new ArrayList<PlatHelper>();
        Connection connection = null;
        Statement cs = null;
        ResultSet rs = null;
        String query = "SELECT * FROM Plat p , Categorie c  WHERE p.categorieId = c.id";
        if (nom != null && !nom.isEmpty()) {
            query += " AND p.nom LIKE '%" + nom + "%'";
        }
        if (prixMin != null && prixMin != 0) {
            query += " AND p.prix >= " + prixMin;
        }
        if (prixMax != null && prixMax != 0) {
            query += " AND p.prix <= " + prixMax;
        }
        if (categorie != null) {
            query += " AND p.categorieId = " + categorie.getId();
        }
        try {
            connection = ConnectDB.getConnection();
            cs = connection.createStatement();
            rs = cs.executeQuery(query);
            while (rs.next()) {
                File file = Util.blobToFile(rs.getBlob("IMAGE"), rs.getString("NOM") + ".jpg");
                plats.add(new PlatHelper(rs.getLong("ID"), rs.getString("NOM"), rs.getDouble("PRIX"), file, rs.getString("LIBELLE")));
            }
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return plats;
    }

    @Override
    public int modifierPlat(Plat plat) throws RemoteException {
        int result = -1;
        try {
            String query = "Update plat " +
                    "SET nom = ?," +
                    " prix = ?," +
                    " image = ?," +
                    " categorieId= ?" +
                    " WHERE id = ?";
            PreparedStatement ps = ConnectDB.prepare(query);
            FileInputStream fis = new FileInputStream(plat.getImage());
            ps.setBinaryStream(3, fis, plat.getImage().length());
            ps.setLong(4, plat.getCategorieId());
            ps.setLong(5, plat.getId());
            ps.setDouble(2, plat.getPrix());
            ps.setString(1, plat.getNom());
            ConnectDB.executePreparedStatment(ps);
            result = 1;
        } catch (Exception e) {
            e.printStackTrace();
            result = -1;
        }
        return result;
    }

    @Override
    public List<CommandeHelper> findCommandesNoneTraitedCommandes() throws RemoteException {
        List<CommandeHelper> commandes = new ArrayList<>();
        ResultSet rs;
        String query = "SELECT c.id, c.dateCommande, c.adresse, SUM(p.prix*lc.quantite) AS montantTotal, c.statut\n" +
                "FROM COMMANDE c, lignecommande lc, plat p\n" +
                "WHERE lc.commandeId=c.id AND lc.platId=p.id\n" +
                "and c.statut NOT LIKE 'LIVREE' GROUP BY c.id ORDER BY dateCommande";
        try {
            rs = ConnectDB.executeQuery(query);
            while (rs.next()) {
                if (rs.getString(2) == null || rs.getString(2).isEmpty()) continue;
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
    public int traiterCommande(Commande commande, String nvStatut, Long adminID) throws RemoteException {
        if (!verifyNewStatut(commande.getStatut(), nvStatut)) return -1;
        else {
            String query;
            try {
                if (nvStatut.equals("LIVREE")) {
                    query = "UPDATE COMMANDE SET adminId = ?, statut = ?, dateLivraison= ? WHERE id = ?";
                } else {
                    query = "UPDATE COMMANDE SET adminId = ?, statut = ? WHERE id = ?";
                }
                PreparedStatement ps = ConnectDB.prepare(query);
                ps.setLong(1, adminID);
                ps.setString(2, nvStatut);
                if (nvStatut.equals("LIVREE")) {
                    Calendar cal = Calendar.getInstance();
                    ps.setTimestamp(3, new java.sql.Timestamp(cal.getTimeInMillis()));
                    ps.setLong(4, commande.getId());
                } else {
                    ps.setLong(3, commande.getId());
                }
                ConnectDB.executePreparedStatment(ps);
                if (nvStatut.equals("LIVREE")) {
                    createFacture(commande);

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 1;
        }
    }

    private void sendFactureMail(Client client,Commande commande, String fileAttachement) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Africa/Casablanca"));
        String message = "Cher(e)" + client.getNom() + " " + client.getPrenom() + ",\n" +
                "Votre commande effectuée le " + dtf.format(commande.getDateCommande()) + ", a bien été livrée. \n" +
                "Ci-joint vous trouverez votre facture.\n"+ "Merci pour votre confiance, et bonne appétit.";

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    EmailUtil.sendMail(message, client.getEmail(), "Facture IRISI DELICE.", fileAttachement);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread=new Thread(runnable);
        thread.start();
    }

    private String createFacture(Commande commande) {
        try {
            List<LigneCommandeHelper> ligneCommandes = findLigneCommandesByCommandeID(commande.getId());
            Client client = findClientByCommandeID(commande.getId());
            double montantTotal = calculMontantTotal(ligneCommandes);
            String filename = printFacture(ligneCommandes, client, montantTotal, commande.getId());

            sendFactureMail(client,commande,"facture/"+filename);

            return filename;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return "";
    }

    private double calculMontantTotal(List<LigneCommandeHelper> ligneCommandes) {
        double montantTotal = 0;
        for (LigneCommandeHelper lc : ligneCommandes) {
            montantTotal += lc.getPrixUnitaire() * lc.getQuantite();
        }
        return montantTotal;
    }

    private Client findClientByCommandeID(Long commandeID) {
        String query = "SELECT cli.id,cli.email,cli.adresse,cli.nom,cli.prenom,cli.numtel FROM client cli, commande c WHERE cli.id=c.clientId AND c.id= "+commandeID;
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
                bdclient = new Client(rs.getLong("ID"), rs.getString("NOM"), rs.getString("PRENOM"), rs.getString("EMAIL"), rs.getString("ADRESSE"), rs.getString("NUMTEL"));
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

    private String printFacture(List<LigneCommandeHelper> list, Client client, double montantTotal, Long idCommande) {
        Map<String, Object> params = new HashMap<>();
        String fileName = "facture_" + idCommande+".pdf";
        params.put("nom", client.getNom() + " " + client.getPrenom());
        params.put("adresse", client.getAdresse());
        params.put("numTel", client.getAdresse());
        params.put("montantTotal", montantTotal);
        try {
            PdfUtil.generatePdf(list, params, fileName, "/sample/repport/facture.jasper");
        } catch (JRException e) {
            e.printStackTrace();
        }
        return fileName;
    }


    private boolean verifyNewStatut(String oldStatut, String newStatut) {
        if (oldStatut.equals("EN ATTENTE")) {
            if (newStatut.equals("EN ATTENTE")) return false;
        } else if (oldStatut.equals("EN PREPARATION")) {
            if (newStatut.equals("EN ATTENTE")) return false;
            if (newStatut.equals("EN PREPARATION")) return false;
        } else if (oldStatut.equals("EN LIVRAISON")) {
            if (newStatut.equals("EN ATTENTE")) return false;
            if (newStatut.equals("EN PREPARATION")) return false;
            if (newStatut.equals("EN LIVRAISON")) return false;
        }
        return true;
    }

    public HashMap<String, Integer> findNbCommandeByCategorie() throws RemoteException {

        HashMap<String, Integer> result = new HashMap<>();
        ResultSet rs;
        String query = "SELECT cat.libelle, COUNT(c.id) FROM commande c, lignecommande lc, plat p, categorie cat " +
                "WHERE c.id=lc.commandeId AND p.id= lc.platId AND cat.id=p.categorieId GROUP BY cat.id";
        try {
            rs = ConnectDB.executeQuery(query);
            while (rs.next()) {
                result.put(rs.getString(1),rs.getInt(2));
            }
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return result;
    }

    public HashMap<String, Integer> findNbCommandeByPlat() throws RemoteException {

        HashMap<String, Integer> result = new HashMap<>();
        ResultSet rs;
        String query = "SELECT p.nom, COUNT(c.id) FROM commande c, lignecommande lc, plat p WHERE c.id=lc.commandeId AND p.id= lc.platId GROUP BY p.id";
        try {
            rs = ConnectDB.executeQuery(query);
            while (rs.next()) {
                result.put(rs.getString(1),rs.getInt(2));
            }
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return result;
    }

    @Override
    public int inscrire(Admin admin) throws RemoteException {
        int result = -3;
        if (admin == null || admin.getNom() == null || admin.getPrenom() == null || admin.getPassword() == null || admin.getLogin() == null ) {
            result = -1;
        } else {
            try {
                Admin dbAdmin = findByLogin(admin.getLogin());
                if (dbAdmin != null) {
                    result = -2;
                } else {
                    String query = "INSERT INTO Admin(login, password, nom, prenom) VALUES (?, ?, ?, ?)";
                    PreparedStatement ps = ConnectDB.prepare(query);
                    ps.setString(1, admin.getLogin());
                    ps.setString(2, HashageUtil.sha256(admin.getPassword()));
                    ps.setString(3, admin.getNom());
                    ps.setString(4, admin.getPrenom());
                    ConnectDB.executePreparedStatment(ps);
                    result = 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
