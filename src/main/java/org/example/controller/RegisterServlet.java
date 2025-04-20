package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.User;
import org.example.DAO.UserDAO;
import org.example.DAO.UserDAOImpl;
import org.example.util.UserValidator;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy thông tin từ form
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String dobStr = request.getParameter("dateOfBirth"); // yyyy-MM-dd

        UserDAO userDAO = new UserDAOImpl();

        try {
            // Kiểm tra username trùng
            if (userDAO.findByUsername(username) != null) {
                request.setAttribute("error", "Username đã tồn tại!");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            // Kiểm tra email trùng
            if (userDAO.findByEmail(email) != null) {
                request.setAttribute("error", "Email đã được sử dụng!");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            // Kiểm tra định dạng ngày sinh
            LocalDate dateOfBirth;
            try {
                dateOfBirth = LocalDate.parse(dobStr); // Parse theo yyyy-MM-dd
            } catch (Exception e) {
                request.setAttribute("error", "Ngày sinh không hợp lệ!");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            // Tạo user
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setEmail(email);
            newUser.setDateOfBirth(dateOfBirth);
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setRole("USER");

            // Kiểm tra tính hợp lệ tổng thể
            if (!UserValidator.isValidEmail(email)) {
                request.setAttribute("error", "Email không hợp lệ!");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            if (!UserValidator.isValidAge(newUser)) {
                request.setAttribute("error", "Bạn phải từ 15 tuổi trở lên!");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            // Lưu user vào database
            userDAO.save(newUser);

            // Chuyển hướng về trang login
            response.sendRedirect(request.getContextPath() + "/login");

        } catch (Exception e) {
            request.setAttribute("error", "Đã xảy ra lỗi khi đăng ký!");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
}
