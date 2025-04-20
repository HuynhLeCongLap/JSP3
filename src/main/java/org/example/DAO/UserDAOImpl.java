package org.example.DAO;

import org.example.model.User;
import org.example.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    private final DatabaseConnection databaseConnection = new DatabaseConnection();

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapRowToUser(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (username, password, role, created_at, email, date_of_birth) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole());
            statement.setTimestamp(4, Timestamp.valueOf(user.getCreatedAt()));
            statement.setString(5, user.getEmail());
            statement.setDate(6, Date.valueOf(user.getDateOfBirth())); // Chuyển LocalDate -> java.sql.Date

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapRowToUser(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                users.add(mapRowToUser(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapRowToUser(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private User mapRowToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setRole(resultSet.getString("role"));
        user.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        return user;
    }
    @Override
    public List<User> findUsersByFollowerAndFollowing(int minFollowers, int minFollowing) {
        List<User> userList = new ArrayList<>();

        String sql = """
        SELECT u.id, u.username,
               COUNT(DISTINCT f1.follower_id) AS followers,
               COUNT(DISTINCT f2.following_id) AS following
        FROM users u
        LEFT JOIN follows f1 ON f1.following_id = u.id
        LEFT JOIN follows f2 ON f2.follower_id = u.id
        GROUP BY u.id, u.username
        HAVING followers >= ? OR following >= ?
    """;

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, minFollowers);
            statement.setInt(2, minFollowing);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String username = resultSet.getString("username");
                int followerCount = resultSet.getInt("followers");
                int followingCount = resultSet.getInt("following");

                User user = new User(id);
                user.setUsername(username);
                user.setFollowerCount(followerCount);  // Gán số lượng followers
                user.setFollowingCount(followingCount);  // Gán số lượng following

                userList.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }
    @Override
    public User findByEmail(String email) {
        User user = null;
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = databaseConnection.getConnection();  // ← sửa dòng này
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setEmail(rs.getString("email"));

                Date dob = rs.getDate("date_of_birth");
                if (dob != null) {
                    user.setDateOfBirth(dob.toLocalDate());
                }

                Timestamp created = rs.getTimestamp("created_at");
                if (created != null) {
                    user.setCreatedAt(created.toLocalDateTime());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }




}