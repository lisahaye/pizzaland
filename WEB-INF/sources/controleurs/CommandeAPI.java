package controleurs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import dao.CommandeDao;
import dao.PizzaDao;
import dto.Commande;
import dto.Pizza;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/commandes/*")
public class CommandeAPI extends API {
    private static final CommandeDao DAO = new CommandeDao();
    private static final PizzaDao PIZZA_DAO = new PizzaDao();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String[] parameter = getParametre(req);
        formatResponse(res);

        final int NOMBRE_DE_PARAMTRE = parameter.length;
        if (NOMBRE_DE_PARAMTRE == 0) {
            send(res, getAll(res));
        } else if (1 <= NOMBRE_DE_PARAMTRE) {
            int id = isNumber(parameter[1]);
            if (id == -1)
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            else if (NOMBRE_DE_PARAMTRE == 2)
                send(res, getById(res, id));
            else if (NOMBRE_DE_PARAMTRE == 3) {
                get3parametre(res, parameter, id);
            } else
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private List<Commande> getAll(HttpServletResponse res) {
        try {
            List<Commande> commande = DAO.findAll();
            if (!commande.isEmpty()) {
                res.setStatus(HttpServletResponse.SC_OK);
                return commande;
            } else {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return new ArrayList<>();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new ArrayList<>();
        }
    }

    private void get3parametre(HttpServletResponse res, String[] parameter, int id) {
        Commande ingredient = getById(res, id);
        if (ingredient != null) {
            String attribut = parameter[2];
            if ("id".equals(attribut))
                send(res, ingredient.getId());
            else if ("nom".equals(attribut))
                send(res, ingredient.getName());
            else if ("prixfinal".equals(attribut))
                send(res, ingredient.getFinalPrice());
            else if ("date".equals(attribut))
                send(res, ingredient.getDate());
            else if ("panier".equals(attribut))
                send(res, ingredient.getPanier());
            else
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private Commande getById(HttpServletResponse res, int id) {
        try {
            Commande ingredient = DAO.findById(id);
            if (ingredient != null) {
                res.setStatus(HttpServletResponse.SC_OK);
                return ingredient;
            } else
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (SQLException e) {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String[] parameter = req.getPathInfo().split("/");

        if (parameter.length == 2) {
            post(req, res);
        }
        else if (parameter.length == 3) {
            try {
                int cno = Integer.parseInt(parameter[1]);
                int pno = Integer.parseInt(parameter[2]);
                DAO.addPizza(cno, pno);
                res.setStatus(HttpServletResponse.SC_CREATED);
            } catch (Exception e) {
                res.setStatus(HttpServletResponse.SC_CONFLICT);
            }
        } else {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    protected void post(HttpServletRequest req, HttpServletResponse res) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));
            String requestBody = reader.lines().collect(Collectors.joining());

            JsonNode json = OBJECT_MAPPER.readTree(requestBody);

            if (json.has("name") && json.has("date") && json.has("panier")) {

                String name = json.get("name").asText();
                LocalDate date = LocalDate.parse(json.get("date").asText());

                List<Pizza> orderedPizzas = new ArrayList<>();
                JsonNode pizzasNode = json.get("panier");
                if (pizzasNode.getNodeType() == JsonNodeType.ARRAY) {
                    for (JsonNode pizzaNode : pizzasNode) {
                        int pno = pizzaNode.get("id").asInt(); 
                        Pizza pizza = PIZZA_DAO.findById(pno);
                        if (pizza != null) {
                            orderedPizzas.add(pizza);
                        } else {
                            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            return;
                        }
                    }
                } else {
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                Commande commande = new Commande(0, name, date, orderedPizzas);

                DAO.saveCommande(commande);

                res.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }
    

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String[] parameter = req.getPathInfo().split("/");

        if (parameter.length == 2) {
            try {
                int cno = Integer.parseInt(parameter[1]);
                int code = DAO.delete(cno);
                if (code == 0) {
                    res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
                else if (code > 0) {
                    res.setStatus(HttpServletResponse.SC_OK);
                }
                res.setStatus(HttpServletResponse.SC_OK);
                
            } catch (SQLException e) {
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                e.printStackTrace();
            } catch (NumberFormatException e) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else if (parameter.length == 3) {

            try {

                int cno = Integer.parseInt(parameter[1]);
                int pno = Integer.parseInt(parameter[2]);
                int code = DAO.deletePizza(cno, pno);
                if (code == 0) {
                    res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
                else if (code > 0) {
                    res.setStatus(HttpServletResponse.SC_OK);
                }
            } catch (SQLException e) {
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                e.printStackTrace();
            } catch (NumberFormatException e) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
