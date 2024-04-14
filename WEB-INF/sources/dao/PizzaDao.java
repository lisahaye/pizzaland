package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.Ingredient;
import dto.Pate;
import dto.Pizza;

public class PizzaDao extends Dao {

    public Pizza findById(int id) throws SQLException {
        Pizza pizza = null;

        final String QUERY = "SELECT * FROM pizzas LEFT JOIN pates USING(dno) LEFT JOIN recettes USING(pno) LEFT JOIN ingredients USING(ino) WHERE pno = ?";

        try (PreparedStatement ps = con.prepareStatement(QUERY)) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();

            int dno;
            String dNom;

            int pno = id;
            String pNom;
            Double pPrix;

            int ino;
            String iNom;
            Double iPrix;

            if (resultSet.next()) {

                dno = resultSet.getInt("dno");
                dNom = resultSet.getString("d_nom");
                Pate pate = new Pate(dno, dNom);

                pNom = resultSet.getString("p_nom");
                pPrix = resultSet.getDouble("p_prix");
                pizza = new Pizza(pno, pNom, pate, pPrix);

                ino = resultSet.getInt("ino");
                iNom = resultSet.getString("i_nom");
                iPrix = resultSet.getDouble("i_prix");
                Ingredient ingredient = new Ingredient(ino, iNom, iPrix);
                pizza.add(ingredient);

                while (resultSet.next()) {
                    ino = resultSet.getInt("ino");
                    iNom = resultSet.getString("i_nom");
                    iPrix = resultSet.getDouble("i_prix");
                    ingredient = new Ingredient(ino, iNom, iPrix);

                    pizza.add(ingredient);
                }
            }
        }
        return pizza;
    }

    public List<Pizza> findAll() throws SQLException {
        List<Pizza> pizzas = new ArrayList<>();

        final String QUERY = "SELECT * FROM pizzas LEFT JOIN pates USING(dno) LEFT JOIN recettes USING(pno) LEFT JOIN ingredients USING(ino) ORDER BY pno, ino;";

        try (PreparedStatement ps = con.prepareStatement(QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                // Pates
                int dno = resultSet.getInt("dno");
                String dNom = resultSet.getString("d_nom");
                Pate pate = new Pate(dno, dNom);
                // Pizzas
                int pno = resultSet.getInt("pno");
                String pNom = resultSet.getString("p_nom");
                Double pPrix = resultSet.getDouble("p_prix");
                Pizza pizza = new Pizza(pno, pNom, pate, pPrix);
                // Ingredients
                int ino = resultSet.getInt("ino");
                String iNom = resultSet.getString("i_nom");
                Double iPrix = resultSet.getDouble("i_prix");
                Ingredient ingredient = new Ingredient(ino, iNom, iPrix);
                pizza.add(ingredient);

                while (resultSet.next() && resultSet.getInt("pno") == pno) {
                    ino = resultSet.getInt("ino");
                    iNom = resultSet.getString("i_nom");
                    iPrix = resultSet.getDouble("i_prix");
                    ingredient = new Ingredient(ino, iNom, iPrix);
                    pizza.add(ingredient);
                }
                resultSet.previous();
                pizzas.add(pizza);
            }
        }
        return pizzas;
    }

    public int findIdByName(String name) throws SQLException {
        final String QUERY = "SELECT pno FROM pizzas WHERE p_nom = ?;";
        int pno = -1;
        try (PreparedStatement ps = con.prepareStatement(QUERY)) {
            ps.setString(1, name);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                pno = resultSet.getInt("pno");
            }
        }
        return pno;
    }

    public int update(Pizza pizza) throws SQLException {
        final String QUERY = "UPDATE pizzas SET p_nom = ?, dno = ?, p_prix = ? WHERE pno = ?";

        try (PreparedStatement ps = con.prepareStatement(QUERY)) {

            con.setAutoCommit(false); // Commencer une transaction

            deleteIngredient(pizza);
            saveIngredient(pizza);

            ps.setString(1, pizza.getName());
            ps.setInt(2, pizza.getPate().getId());
            ps.setDouble(3, pizza.getPrice());
            ps.setInt(4, pizza.getId());
            int rowsAffected = ps.executeUpdate();

            con.commit();
            con.setAutoCommit(true);

            return rowsAffected;
        } catch (SQLException e) {
            con.rollback();
            con.setAutoCommit(true);
            throw e;
        }
    }

    public void save(Pizza pizza) throws SQLException {
        final String INSERT_PIZZA_QUERY = "INSERT INTO pizzas (p_nom, dno, p_prix) VALUES (?, ?, ?)";

        try (PreparedStatement insertPizzaStatement = con.prepareStatement(INSERT_PIZZA_QUERY)) {

            con.setAutoCommit(false);

            // Insérer la pizza dans la table 'pizzas'
            insertPizzaStatement.setString(1, pizza.getName());
            insertPizzaStatement.setInt(2, pizza.getPate().getId());
            insertPizzaStatement.setDouble(3, pizza.getPrice());
            insertPizzaStatement.executeUpdate();

            int id = findIdByName(pizza.getName());
            pizza.setId(id);
            saveIngredient(pizza);
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            con.rollback();
            con.setAutoCommit(true);
            throw e;
        }
    }

    private int deleteIngredient(Pizza pizza) throws SQLException {
        final String QUERY = "DELETE FROM recettes WHERE pno = ?";

        try (PreparedStatement preparedStatement = con.prepareStatement(QUERY)) {
            preparedStatement.setInt(1, pizza.getId());
            return preparedStatement.executeUpdate();
        }
    }

    private void saveIngredient(Pizza pizza) throws SQLException {
        final String QUERY = "INSERT INTO recettes(pno, ino) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(QUERY)) {
            for (Ingredient ingredient : pizza.getIngredients()) {
                ps.setInt(1, pizza.getId());
                ps.setInt(2, ingredient.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public int delete(int id) throws SQLException {
        final String QUERY = "DELETE FROM pizzas WHERE pno = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(QUERY)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate();
        }
    }

    public int deleteIngredient(int pno, int ino) throws SQLException {
        final String QUERY = "DELETE FROM recettes WHERE pno = ? AND ino = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(QUERY)) {
            preparedStatement.setInt(1, pno);
            preparedStatement.setInt(2, ino);
            return preparedStatement.executeUpdate();
        }
    }

    public boolean addIngredient(int pno, int ino) throws SQLException {
        final String QUERY = "INSERT INTO recettes(pno, ino) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = con.prepareStatement(QUERY)) {
            preparedStatement.setInt(1, pno);
            preparedStatement.setInt(2, ino);
            return preparedStatement.execute();
        }
    }
}