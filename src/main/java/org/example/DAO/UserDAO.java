package org.example.DAO;

import org.example.model.User;
import java.util.List;

public interface UserDAO {
    User findByUsernameAndPassword(String username, String password);
    void save(User user);
    User findById(Long id);
    List<User> findAll();
    User findByUsername(String username);

    User findByEmail(String email); // ✅ Thêm để kiểm tra email trùng

    // ✅ Thêm phương thức tìm kiếm
    List<User> findUsersByFollowerAndFollowing(int minFollowers, int minFollowing);


}
