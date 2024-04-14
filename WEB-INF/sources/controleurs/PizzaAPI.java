package controleurs;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dao.PizzaDao;
import dto.Ingredient;
import dto.Pate;
import dto.Pizza;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/pizzas/*")
public class PizzaAPI extends API {

    private static final PizzaDao DAO = new PizzaDao();

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

    private void get3parametre(HttpServletResponse res, String[] parameter, int id) {
        Pizza pizza = getById(res, id);
        if (pizza != null) {
            String attribut = parameter[2];
            if ("id".equals(attribut))
                send(res, pizza.getId());
            else if ("nom".equals(attribut))
                send(res, pizza.getName());
            else if ("pate".equals(attribut))
                send(res, pizza.getName());
            else if ("prix".equals(attribut))
                send(res, pizza.getId());
            else if ("ingredients".equals(attribut))
                send(res, pizza.getIngredients());
            else if ("prixFinal".equals(attribut))
                send(res, pizza.getPrice());
            else
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private List<Pizza> getAll(HttpServletResponse res) {
        try {
            List<Pizza> pizza = DAO.findAll();
            if (!pizza.isEmpty()) {
                res.setStatus(HttpServletResponse.SC_OK);
                return pizza;
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

    private Pizza getById(HttpServletResponse res, int id) {
        try {
            Pizza pizza = DAO.findById(id);
            if (pizza != null) {
                res.setStatus(HttpServletResponse.SC_OK);
                return pizza;
            } else
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (SQLException e) {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void doPatch(HttpServletRequest req, HttpServletResponse res) {
        String[] parameter = getParametre(req);

        if (2 == parameter.length) {
            int id = isNumber(parameter[1]);

            if (id != -1) {
                patch(req, res, id);
            } else {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    public void patch(HttpServletRequest req, HttpServletResponse res, int id) {
        try {
            Pizza pizza = DAO.findById(id);

            String requestBody = req.getReader().lines().reduce("", String::concat);
            JsonNode json = OBJECT_MAPPER.readTree(requestBody);

            if (pizza != null) {
                if (json.has("name"))
                    pizza.setName(json.get("name").asText());
                if (json.has("pate")) {
                    JsonNode pateNode = json.get("pate");
                    if (pateNode.has("id") && pateNode.has("name")) {
                        int pateId = pateNode.get("id").asInt();
                        String pateName = pateNode.get("name").asText();
                        pizza.setPate(new Pate(pateId, pateName));
                    }
                }
                if (json.has("price"))
                    pizza.setPrice(json.get("price").asDouble());
                if (json.has("ingredients")) {
                    List<Ingredient> ingredients = new ArrayList<>();
                    JsonNode ingredientsNode = json.get("ingredients");
                    for (JsonNode ingredientNode : ingredientsNode) {
                        if (ingredientNode.has("id") && ingredientNode.has("name") && ingredientNode.has("price")) {
                            int ingredientId = ingredientNode.get("id").asInt();
                            String ingredientName = ingredientNode.get("name").asText();
                            double ingredientPrice = ingredientNode.get("price").asDouble();
                            ingredients.add(new Ingredient(ingredientId, ingredientName, ingredientPrice));
                        }
                    }
                    pizza.setIngredients(ingredients);
                }
                int code = DAO.update(pizza);
                if (code == 0)
                    res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                else if (code == -1)
                    res.setStatus(HttpServletResponse.SC_CONFLICT);
                else
                    res.setStatus(HttpServletResponse.SC_OK);
            } else {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }

        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        String[] parameter = getParametre(req);

        if (parameter.length == 0) {

            try {
                String requestBody = req.getReader().lines().reduce("", String::concat);
                ObjectMapper objectMapper = new ObjectMapper();
                Pizza newPizza = objectMapper.readValue(requestBody, Pizza.class);

                PizzaDao pizzaDao = new PizzaDao();
                pizzaDao.save(newPizza);

                res.setStatus(HttpServletResponse.SC_CREATED); 
            } catch (IOException | SQLException e) {
                e.printStackTrace();
                res.setStatus(HttpServletResponse.SC_CONFLICT);
            }
        } else if (parameter.length == 3) {

            int pno = isNumber(parameter[1]);
            int ino = isNumber(parameter[2]);

            if (pno != -1 && ino != -1) {
                addIngredient(res, pno, ino);
            } else {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    private void addIngredient(HttpServletResponse res, int pno, int ino) {
        try {
            DAO.addIngredient(pno, ino);
            res.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_CONFLICT);
            e.printStackTrace();
        }
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse res) {
        String[] parameter = getParametre(req);
        if (parameter.length == 2) {
            int pno = isNumber(parameter[1]);

            if (pno != -1) {
                delete(res, pno);
            } else {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else if (parameter.length == 3) {

            int pno = isNumber(parameter[1]);
            int ino = isNumber(parameter[2]);

            if (pno != -1 && ino != -1) {
                deleteIngredient(res, pno, ino);
            } else {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    private void deleteIngredient(HttpServletResponse res, int pno, int ino) {
        try {
            int code = DAO.deleteIngredient(pno, ino);
            if (0 < code)
                res.setStatus(HttpServletResponse.SC_OK);
            else
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    private void delete(HttpServletResponse res, int id) {
        try {
            int code = DAO.delete(id);
            if (0 < code)
                res.setStatus(HttpServletResponse.SC_OK);
            else
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }
}