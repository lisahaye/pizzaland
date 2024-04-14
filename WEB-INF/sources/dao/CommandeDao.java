package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dto.Commande;
import dto.Ingredient;
import dto.Pate;
import dto.Pizza;

public class CommandeDao extends Dao {

    

    public Commande findById(int id) throws SQLException {
        Commande commande = null;

        final String QUERY = "SELECT * " +
                "FROM commandes " +
                "JOIN panier USING (cno) " +
                "JOIN pizzas USING (pno) " +
                "JOIN pates USING (dno) " +
                "JOIN recettes USING (pno) " +
                "JOIN ingredients USING (ino) " +
                "WHERE commandes.cno = ?";

        try (PreparedStatement ps = con.prepareStatement(QUERY)) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();

            Map<Integer, Pizza> pizzaMap = new HashMap<>();

            while (resultSet.next()) {
                if (commande == null) {
                    String userName = resultSet.getString("c_name");
                    LocalDate orderDate = resultSet.getDate("c_orderDate").toLocalDate();
                    commande = new Commande(id, userName, orderDate, new ArrayList<>());
                }

                int pno = resultSet.getInt("pno");
                Pizza pizza;
                if (!pizzaMap.containsKey(pno)) {
                    String pizzaName = resultSet.getString("p_nom");
                    Double pizzaPrice = resultSet.getDouble("p_prix");
                    int dno = resultSet.getInt("dno");
                    String pateName = resultSet.getString("d_nom");
                    Pate pate = new Pate(dno, pateName);
                    pizza = new Pizza(pno, pizzaName, pate, pizzaPrice);
                    pizza.setIngredients(new ArrayList<>());
                    pizzaMap.put(pno, pizza);
                    commande.getPanier().add(pizza);
                } else {
                    pizza = pizzaMap.get(pno);
                }

                int ingredientId = resultSet.getInt("ino");
                String ingredientName = resultSet.getString("i_nom");
                Double ingredientPrice = resultSet.getDouble("i_prix");
                Ingredient ingredient = new Ingredient(ingredientId, ingredientName, ingredientPrice);
                pizza.getIngredients().add(ingredient);
            }
        }
        return commande;
    }

    public List<Commande> findAll() throws SQLException {
        List<Commande> commandes = new ArrayList<>();

        final String QUERY = "SELECT commandes.*, pizzas.*, pates.*, ingredients.* " +
                             "FROM commandes " +
                             "JOIN panier ON commandes.cno = panier.cno " +
                             "JOIN pizzas ON panier.pno = pizzas.pno " +
                             "JOIN pates ON pizzas.dno = pates.dno " +
                             "JOIN recettes ON pizzas.pno = recettes.pno " +
                             "JOIN ingredients ON recettes.ino = ingredients.ino " +
                             "ORDER BY commandes.cno";

        try (PreparedStatement ps = con.prepareStatement(QUERY)) {
            ResultSet resultSet = ps.executeQuery();

            Map<Integer, Commande> commandeMap = new HashMap<>();
            while (resultSet.next()) {
                int cno = resultSet.getInt("cno");
                Commande commande;
                if (!commandeMap.containsKey(cno)) {
                    String userName = resultSet.getString("c_name");
                    LocalDate orderDate = resultSet.getDate("c_orderDate").toLocalDate();
                    commande = new Commande(cno, userName, orderDate, new ArrayList<>());
                    commandeMap.put(cno, commande);
                    commandes.add(commande);
                } else {
                    commande = commandeMap.get(cno);
                }

                int pno = resultSet.getInt("pno");
                Pizza pizza;
                if (commande.getPanier().isEmpty() || commande.getPanier().get(commande.getPanier().size() - 1).getId() != pno) {
                    String pizzaName = resultSet.getString("p_nom");
                    Double pizzaPrice = resultSet.getDouble("p_prix");
                    int dno = resultSet.getInt("dno");
                    String pateName = resultSet.getString("d_nom");
                    Pate pate = new Pate(dno, pateName);
                    pizza = new Pizza(pno, pizzaName, pate, pizzaPrice);
                    pizza.setIngredients(new ArrayList<>());
                    commande.getPanier().add(pizza);
                } else {
                    pizza = commande.getPanier().get(commande.getPanier().size() - 1);
                }

                int ingredientId = resultSet.getInt("ino");
                String ingredientName = resultSet.getString("i_nom");
                Double ingredientPrice = resultSet.getDouble("i_prix");
                Ingredient ingredient = new Ingredient(ingredientId, ingredientName, ingredientPrice);
                pizza.getIngredients().add(ingredient);
            }
        }

        return commandes;
    }

    public void saveCommande(Commande commande) throws SQLException {
        final String COMMANDE_QUERY = "INSERT INTO commandes (c_name, c_orderDate) VALUES (?, ?)";
       
        con.setAutoCommit(false);

        try (PreparedStatement psCommande = con.prepareStatement(COMMANDE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            
            psCommande.setString(1, commande.getName());
            psCommande.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            psCommande.executeUpdate();

            int cno;
            try (ResultSet generatedKeys = psCommande.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cno = generatedKeys.getInt(1);
                } else {
                    throw new SQLException();
                }
            }
            savePanier(commande, cno);
            con.commit(); 
            con.setAutoCommit(true);

        } catch (SQLException e) {
            con.rollback(); 
            throw e;
        } finally {
            con.setAutoCommit(true);
        }
    }

    private void savePanier(Commande commande, int cno) throws SQLException {
        final String PANIER_QUERY = "INSERT INTO panier (cno, pno) VALUES (?, ?)";
        
        try (PreparedStatement psPanier = con.prepareStatement(PANIER_QUERY)) {  
            for (Pizza pizza : commande.getPanier()) {
                psPanier.setInt(1, cno);
                psPanier.setInt(2, pizza.getId());
                psPanier.addBatch();
            }
            psPanier.executeBatch();
        }    
    }

    public static void main(String[] args) {
        Pate pate = new Pate(1, "Classique");
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient(1, "Sauce tomate", 1.5));
        ingredients.add(new Ingredient(2, "Mozzarella", 2.0));
        Pizza pizza1 = new Pizza(1, "Margherita", pate, 8.99);
        pizza1.setIngredients(ingredients);

        List<Pizza> pizzas = new ArrayList<>();
        pizzas.add(pizza1);

        // Création d'une commande avec les pizzas
        Commande commande = new Commande(1, "John Doe", LocalDate.now(), pizzas);

        // Initialisation de la connexion à la base de données
        CommandeDao commandeDAO = new CommandeDao();
        try {
            // Sauvegarde de la commande
            commandeDAO.saveCommande(commande);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int delete(int cno) throws SQLException {
        String query = "DELETE FROM commandes WHERE cno = ?";
        
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, cno);
            return statement.executeUpdate();
        }
    }
    
    public int deletePizza(int cno, int pno) throws SQLException {
        String query = "DELETE FROM panier WHERE cno = ? AND pno = ?";
        
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, cno);
            statement.setInt(2, pno);
            return statement.executeUpdate();
        }
    }

    public void addPizza(int cno, int pno) throws SQLException {
        String query = "INSERT INTO panier (cno, pno) VALUES (?, ?)";
        
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, cno);
            statement.setInt(2, pno);
            statement.executeUpdate();
        }
    }
}
