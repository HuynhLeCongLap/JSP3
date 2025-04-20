<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*, org.example.model.User" %>
<html>
<head>
    <title>Tìm kiếm người dùng</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            background-color: #f4f4f4;
        }

        .container {
            text-align: center;
            width: 100%;
            max-width: 600px;
            padding: 20px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        h2 {
            margin-bottom: 20px;
        }

        form {
            margin-bottom: 20px;
        }

        input[type="number"] {
            padding: 8px;
            margin: 5px;
            width: 100px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        button {
            padding: 8px 16px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        button:hover {
            background-color: #45a049;
        }

        ul {
            list-style-type: none;
            padding: 0;
        }

        ul li {
            padding: 5px 0;
        }

        .error {
            color: red;
        }

        .img-container {
            text-align: center;
        }

        .home-button {
            margin-top: 20px;
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            text-decoration: none;
        }

        .home-button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Tìm kiếm người dùng:</h2>

    <form action="search-users" method="get">
        Min Followers:
        <input type="number" name="minFollowers" value="<%= request.getParameter("minFollowers") != null ? request.getParameter("minFollowers") : "3" %>">
        Min Following:
        <input type="number" name="minFollowing" value="<%= request.getParameter("minFollowing") != null ? request.getParameter("minFollowing") : "5" %>">
        <button type="submit">Tìm kiếm</button>
    </form>

    <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
    %>
    <p class="error"><%= error %></p>
    <%
        }

        List<User> resultList = (List<User>) request.getAttribute("resultList");
        if (resultList == null || resultList.isEmpty()) {
    %>
    <div class="img-container">
        <img src="/img/not-found.png" alt="Not found" style="width: 300px;">
    </div>
    <%
    } else {
    %>
    <ul>
        <% for (User user : resultList) { %>
        <li>
            <strong><%= user.getUsername() %></strong> - ID: <%= user.getId() %>
        </li>
        <% } %>
    </ul>
    <%
        }
    %>

    <!-- Nút bấm về trang chủ -->
    <a href="home" class="home-button">Về trang chủ</a>
</div>
</body>
</html>
