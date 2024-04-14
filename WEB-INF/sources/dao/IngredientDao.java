package dao;

import dto.Ingredient;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe gère l'accès aux données concernant les ingrédients dans la base de données.
 * Elle fournit des méthodes pour rechercher, insérer, mettre à jour et supprimer des ingrédients.
 * Cette classe étend la classe abstraite Dao pour bénéficier de la connexion à la base de données.
 * 
 * @see Dao
 * @author Samy Van Calster
 * @author Lisa Haye
 */
public class IngredientDao extends Dao {

    /**
     * Recherche un ingrédient dans la base de données en fonction de son identifiant.
     *
     * @param pno L'identifiant de l'ingrédient à rechercher.
     * @return L'ingrédient trouvé, ou null si aucun ingrédient correspondant n'est trouvé.
     * @throws SQLException Si une erreur survient lors de l'exécution de la requête SQL.
     */
    public Ingredient findById(int pno) throws SQLException {
        Ingredient res = null;
        final String QUERY = "SELECT * FROM ingredients WHERE ino = ?";
        
        try (PreparedStatement ps = con.prepareStatement(QUERY)) {
            ps.setInt(1, pno);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("i_nom");
                Double price = rs.getDouble("i_prix");
                res = new Ingredient(pno, name, price);
            }
        }
        return res;
    }

    /**
     * Recherche tous les ingrédients disponibles dans la base de données.
     *
     * @return Une liste contenant tous les ingrédients disponibles.
     * @throws SQLException Si une erreur survient lors de l'exécution de la requête SQL.
     */
    public List<Ingredient> findAll() throws SQLException {
        List<Ingredient> ingredients = new ArrayList<>();
        final String QUERY = "SELECT * FROM ingredients";

        try (Statement statement = con.createStatement()) {
            ResultSet rs = statement.executeQuery(QUERY);

            int ino;
            String name;
            double price;

            while (rs.next()) {
                ino = rs.getInt("ino");
                name = rs.getString("i_nom");
                price = rs.getDouble("i_prix");
                ingredients.add(new Ingredient(ino, name, price));
            }
        }
        return ingredients;
    }

    /**
     * Enregistre un nouvel ingrédient dans la base de données.
     *
     * @param ingredient L'ingrédient à enregistrer.
     * @return true si l'enregistrement est réussi, false sinon.
     */
    public boolean save(Ingredient ingredient) {
        final String QUERY = "INSERT INTO ingredients(i_nom, i_prix) VALUES (?, ?)";
        
        try (PreparedStatement ps = con.prepareStatement(QUERY)) {
            ps.setString(1, ingredient.getName());
            ps.setDouble(2, ingredient.getPrice());

            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Met à jour un ingrédient existant dans la base de données.
     *
     * @param ingredient L'ingrédient à mettre à jour.
     * @return Le nombre de lignes affectées par la mise à jour.
     */
    public int update(Ingredient ingredient) {
        final String QUERY = "UPDATE ingredients SET i_nom = ?, i_prix = ? WHERE ino = ?";

        try (PreparedStatement ps = con.prepareStatement(QUERY)) {
            ps.setString(1, ingredient.getName());
            ps.setDouble(2, ingredient.getPrice());
            ps.setInt(3, ingredient.getId());

            return ps.executeUpdate();
        } catch (SQLException e) {
            return -1;
        }
    }

    /**
     * Supprime un ingrédient de la base de données.
     *
     * @param ingredient L'ingrédient à supprimer.
     * @return Le nombre de lignes affectées par la suppression.
     */
    public int delete(Ingredient ingredient) {
        return delete(ingredient.getId());
    }

    /**
     * Supprime un ingrédient de la base de données.
     *
     * @param id L'identifiant de l'ingrédient à supprimer.
     * @return Le nombre de lignes affectées par la suppression.
     */
    public int delete(int id) {
        final String QUERY = "DELETE FROM ingredients WHERE ino = ?";
        
        try (PreparedStatement ps = con.prepareStatement(QUERY)) {
            ps.setInt(1, id);
            
            return ps.executeUpdate();
        } catch (SQLException e) {
            return -1;
        }
    }
}
