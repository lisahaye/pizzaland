package controleurs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.APITokenDao;
import dto.APIToken;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


 

@WebServlet("/tokens/*")
public class TokenAPI extends API {

    private static final APITokenDao DAO = new APITokenDao();
    
    private List<APIToken> getAll(HttpServletResponse res) {
        try {
            List<APIToken> tokens = DAO.findAll();
            if (!tokens.isEmpty()) {
                res.setStatus(HttpServletResponse.SC_OK);
                return tokens;
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

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
        send(res, getAll(res));
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) {
        if (req.getMethod().equalsIgnoreCase("GET")) {
            doGet(req, res);
        }
        else {
            super.service(req, res);
        }
    }
}
