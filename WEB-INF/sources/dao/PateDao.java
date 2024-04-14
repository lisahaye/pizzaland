package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dto.Pate;

/**
 * Cette classe gère l'accès aux données concernant les pâtes dans la base de données.
 * Elle fournit des méthodes pour rechercher, insérer, mettre à jour et supprimer des pâtes.
 * 
 * @see Dao
 * @author Samy Van Calster
 */
public class PateDao extends Dao {

    /**
     * Recherche une pâte dans la base de données en fonction de son identifiant.
     *
     * @param id L'identifiant de la pâte à rechercher.
     * @return La pâte trouvée, ou null si aucune pâte correspondante n'est trouvée.
     * @throws SQLException Si une erreur survient lors de l'exécution de la requête SQL.
     */
    public Pate findById(int id) throws SQLException {
        Pate res = null;
        final String QUERY = "SELECT * FROM pates WHERE dno = " + id;
        
        try (Statement statement = con.createStatement()) {
            ResultSet resultSet = statement.executeQuery(QUERY);

            if (resultSet.next()) {
                res = new Pate(id, resultSet.getString("d_nom"));
            }
        }
        return res;
    }

    /**
     * Recherche toutes les pâtes disponibles dans la base de données.
     *
     * @return Une liste contenant toutes les pâtes disponibles.
     * @throws SQLException Si une erreur survient lors de l'exécution de la requête SQL.
     */
    public List<Pate> findAll() throws SQLException {
        List<Pate> pates = new ArrayList<>();
        final String QUERY = "SELECT * FROM pates";
        
        try (Statement statement = con.createStatement()) {
            ResultSet resultSet = statement.executeQuery(QUERY);

            int dno;
            String name;

            while (resultSet.next()) {
                dno = resultSet.getInt("dno");
                name = resultSet.getString("d_nom");
                pates.add(new Pate(dno, name));
            }
        }
        return pates;
    }

    /**
     * Enregistre une nouvelle pâte dans la base de données.
     *
     * @param pate La pâte à enregistrer.
     * @return true si l'enregistrement est réussi, false sinon.
     */
    public boolean save(Pate pate) {
        final String QUERY = "INSERT INTO pates (d_nom) VALUES (?)";
        
        try (PreparedStatement ps = con.prepareStatement(QUERY)) {
            ps.setString(1, pate.getName());
            
            int rowsInserted = ps.executeUpdate();
            return 0 < rowsInserted;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Met à jour une pâte existante dans la base de données.
     *
     * @param pate La pâte à mettre à jour.
     * @return Le nombre de lignes affectées par la mise à jour.
     */
    public int update(Pate pate) {
        final String QUERY = "UPDATE pates SET d_nom = ? WHERE dno = ?";
        
        try (PreparedStatement ps = con.prepareStatement(QUERY)) {
            ps.setString(1, pate.getName());
            ps.setInt(2, pate.getId());
            
            return ps.executeUpdate();
        } catch (SQLException e) {
            return -1;
        }
    }

    /**
     * Supprime une pâte de la base de données en fonction de son identifiant.
     *
     * @param id L'identifiant de la pâte à supprimer.
     * @return Le nombre de lignes affectées par la suppression.
     */
    public int delete(int id) {
        final String QUERY = "DELETE FROM pates WHERE dno = ?";
        
        try (PreparedStatement ps = con.prepareStatement(QUERY)) {
            ps.setInt(1, id);
            
            return ps.executeUpdate();
        } catch (SQLException e) {
            return -1;
        }
    }

    public static void main(String[] args) throws SQLException {
        PateDao pd = new PateDao();
        System.out.println(pd.findAll());
    }
}
