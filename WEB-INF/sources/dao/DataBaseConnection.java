package dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Classe utilitaire pour établir une connexion à la base de données.
 * 
 * @author Lisa Haye
 */
public class DataBaseConnection {

    private DataBaseConnection() {}

    /**
     * Établit une connexion à la base de données en lisant les informations de connexion depuis un fichier de configuration.
     * @return Une connexion à la base de données.
     */
    public static Connection getConnection() {
        Properties p = new Properties();
        try(InputStream in = DataBaseConnection.class.getClassLoader().getResourceAsStream("config.prop")) {
            p.load(in);
            Class.forName(p.getProperty("driver"));
            String url = p.getProperty("url");
            String nom = p.getProperty("login");
            String mdp = p.getProperty("password");
            return DriverManager.getConnection(url, nom, mdp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

