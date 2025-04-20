package org.example.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

@WebFilter("/*")
public class AuthFilter implements Filter {

    private static final Set<String> PUBLIC_PAGES = Set.of(
            "/login", "/register", "/login.jsp", "/register.jsp"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String contextPath = httpRequest.getContextPath();
        String path = httpRequest.getRequestURI().substring(contextPath.length());

        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);
        boolean isPublicPage = PUBLIC_PAGES.contains(path);
        boolean isStaticResource = path.matches(".*(\\.css|\\.js|\\.png|\\.jpg|\\.jpeg|\\.gif|\\.woff|\\.ttf|\\.svg)$");
        boolean isAdminPage = path.startsWith("/admin/");

        if (isLoggedIn) {
            String role = (String) session.getAttribute("role");

            if (isPublicPage) {
                // Redirect người dùng đã đăng nhập khỏi trang login/register
                if ("ADMIN".equals(role)) {
                    httpResponse.sendRedirect(contextPath + "/admin/dashboard");
                } else {
                    httpResponse.sendRedirect(contextPath + "/home");
                }
                return;
            }

            // Ngăn người dùng thường truy cập trang admin
            if (isAdminPage && !"ADMIN".equals(role)) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            // Cho phép các request hợp lệ tiếp tục
            chain.doFilter(request, response);
            return;
        }

        // Nếu chưa đăng nhập
        if (isPublicPage || isStaticResource) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(contextPath + "/login");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}
}
