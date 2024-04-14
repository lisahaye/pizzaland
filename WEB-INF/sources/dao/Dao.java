package dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Cette classe abstraite représente un DAO (Data Access Object) générique.
 * Elle fournit une connexion à la base de données et une méthode pour fermer cette connexion.
 * Les classes DAO spécifiques pour chaque entité devraient étendre cette classe.
 * 
 * @author Samy Van Calster
 */
public abstract class Dao {
    
    /** La connexion à la base de données */
    protected Connection con;

    /**
     * Constructeur de la classe abstraite Dao.
     * Initialise une connexion à la base de données.
     */
    protected Dao() {
        try {
            con = DataBaseConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            con = null;
        }
    }

    /**
     * Ferme la connexion à la base de données.
     *
     * @return true si la connexion est fermée avec succès, false sinon.
     */
    public boolean close() {
        try {
            con.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}

