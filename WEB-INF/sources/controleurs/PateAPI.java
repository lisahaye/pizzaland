package controleurs;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import dao.PateDao;
import dto.Pate;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/pates/*")
public class PateAPI extends API {

    private static final PateDao DAO = new PateDao();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String[] parameter = getParametre(req);

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
        Pate ingredient = getById(res, id);
        if (ingredient != null) {
            String attribut = parameter[2];
            if ("nom".equals(attribut))
                send(res, ingredient.getName());
            else if ("id".equals(attribut))
                send(res, ingredient.getId());
            else
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private List<Pate> getAll(HttpServletResponse res) {
        try {
            List<Pate> pates = DAO.findAll();
            if (!pates.isEmpty()) {
                res.setStatus(HttpServletResponse.SC_OK);
                return pates;
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

    public Pate getById(HttpServletResponse res, int id) {
        try {
            Pate pate = DAO.findById(id);
            if (pate != null) {
                res.setStatus(HttpServletResponse.SC_OK);
                return pate;
            } else
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (SQLException e) {
            e.getStackTrace();
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        try {
            String requestBody = req.getReader().lines().reduce("", String::concat);
            Pate pate = OBJECT_MAPPER.readValue(requestBody, Pate.class);
            DAO.save(pate);
            res.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
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
        else {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public void patch(HttpServletRequest req, HttpServletResponse res, int id) {
        try {
            Pate pate = DAO.findById(id);
            if (pate == null) {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            else {
                String requestBody = req.getReader().lines().reduce("", String::concat);
                JsonNode json = OBJECT_MAPPER.readTree(requestBody);
                JsonNode jsonName = json.get("name");
    
                String newName = (jsonName != null)? jsonName.asText() : null;
    
                if (newName != null) {
                    pate.setName(newName);
                    
                    int code = DAO.update(pate);
                    if (code < 0)
                        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    else
                        res.setStatus(HttpServletResponse.SC_OK);
                } else
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse res) {
        String[] parameter = getParametre(req);
        if (2 == parameter.length) {
            int id = isNumber(parameter[1]);

            if (id != -1) {
                delete(res, id);
            } else {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    private void delete(HttpServletResponse res, int id) {
        try {
            int code = DAO.delete(id);
            if (code == 0)
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            else if (code == -1)
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            else
                res.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}