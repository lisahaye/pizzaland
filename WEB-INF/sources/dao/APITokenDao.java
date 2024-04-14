package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.APIToken;

public class APITokenDao extends Dao{

    public APIToken findByUserPassword(String user, String password) throws SQLException {
        APIToken res = null;
        
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM tokens WHERE user_name = ? AND password = ?;")) {
            ps.setString(1, user);
            ps.setString(2, password);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) 
                res = new APIToken(resultSet.getString("user_name"), resultSet.getString("password"));
        }
        return res;
    }

    public List<APIToken> findAll() throws SQLException {
        List<APIToken> res = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM tokens;")) {
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) 
                res.add(new APIToken(resultSet.getString("user_name"), resultSet.getString("password")));
        }
        return res;
    }

    public static void main(String[] args) throws SQLException {
        System.out.println(new APITokenDao().findAll());
    }
}
