/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;

/**
 *
 * @author AN
 */
public class UserDAO extends DBContext {

    public ArrayList<User> getAllAccountWorkerAndAdmin() {
        ArrayList<User> listUser = new ArrayList<>();
        connection = getConnection();
        String sql = "SELECT *\n"
                + "  FROM [FPT_Food_PRJ].[dbo].[User]\n"
                + "  Where (role = ? or role = ?) and status = ?";
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, "manager");
            statement.setString(2, "worker");
            statement.setString(3, "active");
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int userID = resultSet.getInt("userID");
                String username = resultSet.getString("username");
                String role = resultSet.getString("role");
                User u = User.builder()
                        .userID(userID)
                        .username(username)
                        .role(role).build();
                listUser.add(u);
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        return listUser;
    }

    public User login(String username, String password) {
        String sql = "SELECT * FROM [User] WHERE username = ? AND [password] = ? AND status = 'active'";

        try {
            connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return User.builder()
                        .userID(rs.getInt("userID"))
                        .username(rs.getString("username"))
                        .password(rs.getString("password"))
                        .role(rs.getString("role"))
                        .fullname(rs.getString("fullname"))
                        .phone(rs.getString("phone"))
                        .status(rs.getString("status"))
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Check trùng username
    public boolean isUsernameExist(String username) {
        String sql = "SELECT 1 FROM [User] WHERE username = ?";
        try {
            connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //Check trùng phone
    public boolean isPhoneExist(String phone) {
        String sql = "SELECT 1 FROM [User] WHERE phone = ?";
        try {
            connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //Insert user mới
    public void register(User user) {
        String sql = "INSERT INTO [User] (username, [password], role, fullname, phone, status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());      // "user"
            ps.setString(4, user.getFullname());
            ps.setString(5, user.getPhone());
            ps.setString(6, user.getStatus());    // "active"
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public int deleteAccount(int userID) {
        connection = getConnection();
        String sql = "UPDATE [dbo].[User]\n"
                + "   SET \n"
                + "      [status] = ?\n"
                + "WHERE userID = ?";
        int resultSet = 0;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, "inactive");
            statement.setInt(2, userID);
            resultSet = statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        return resultSet;
    }

}
