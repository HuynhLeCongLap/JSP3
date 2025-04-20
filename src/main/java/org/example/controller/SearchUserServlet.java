package org.example.controller;


import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.DAO.UserDAO;
import org.example.DAO.UserDAOImpl;
import org.example.model.User;

import java.io.IOException;
import java.util.List;

@WebServlet("/search-users")
public class SearchUserServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy các tham số từ form tìm kiếm
        int minFollowers = Integer.parseInt(request.getParameter("minFollowers"));
        int minFollowing = Integer.parseInt(request.getParameter("minFollowing"));

        // Tìm kiếm người dùng từ DAO
        List<User> resultList = userDAO.findUsersByFollowerAndFollowing(minFollowers, minFollowing);

        // Đưa kết quả vào request attribute
        request.setAttribute("resultList", resultList);

        // Chuyển tiếp đến trang tìm kiếm (search.jsp)
        request.getRequestDispatcher("/search.jsp").forward(request, response);
    }
}

