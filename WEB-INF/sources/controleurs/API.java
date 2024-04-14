package controleurs;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dao.APITokenDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public abstract class API extends HttpServlet {

    protected static final ObjectMapper OBJECT_MAPPER;
    protected static final String CONTENT_TYPE = "application/json";
    protected static final String CHARACTER_ENCODING = "UTF-8";
    protected static final String AUTHORIZATION_HEADER = "Authorization";
    protected static final String TOKEN_PREFIX = "Bearer ";
    public static final APITokenDao DAO = new APITokenDao(); 

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    public void formatResponse(HttpServletResponse response) {
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHARACTER_ENCODING);
    }

    public void send(HttpServletResponse res, Object obj) {
        try {
            res.getWriter().write(OBJECT_MAPPER.writeValueAsString(obj));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int isNumber(String stringNumber) {
        try {
            return Integer.parseInt(stringNumber);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public String[] getParametre(HttpServletRequest request) {
        String path = request.getPathInfo();
        if (path == null) {
            path = "/";
        } else if (path.charAt(path.length() - 1) != '/') {
            path = path + '/';
        }
        return path.split("/");
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) {
        formatResponse(res);
        try {
            if (authenticate(req)) {
                if (req.getMethod().equalsIgnoreCase("PATCH")) {
                    doPatch(req, res);
                } else {
                    super.service(req, res);
                }
            }
            else {
                if (req.getMethod().equalsIgnoreCase("GET")) {
                    doGet(req, res);
                }
                else {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    send(res, "Unauthorized");
                }
            }
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }

    }

    private boolean authenticate(HttpServletRequest req) {
        String authHeader = req.getHeader(AUTHORIZATION_HEADER);
        if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
            String token = authHeader.replace(TOKEN_PREFIX, "");
            String[] tokens = token.split("/");
            if (tokens.length == 2) {
                try {  
                    return null != DAO.findByUserPassword(tokens[0], tokens[1]);
                } catch (Exception e) {
                    return false;  
                }
            }
        }
        return false;    
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse res) {
    }
}
