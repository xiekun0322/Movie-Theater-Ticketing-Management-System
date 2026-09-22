package com.maoyan.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 映射路径：当浏览器访问 /cinema 时，执行这个 Servlet
@WebServlet("/cinema")
public class CinemaServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 获取前端传来的参数（比如电影ID）
        String movieId = request.getParameter("movieId");
        
        // 2. 调用 Service 层查询数据库，获取该电影的影院排片信息
        // List<Cinema> cinemaList = cinemaService.getCinemasByMovieId(movieId);
        
        // 3. 将数据存入 request 域中，以便在 JSP 页面中展示
        // request.setAttribute("cinemaList", cinemaList);
        // request.setAttribute("movieId", movieId);
        
        // 4. 请求转发到 JSP 页面（注意：转发是在服务器内部进行的，浏览器地址栏不变）
        // request.getRequestDispatcher("/cinema.jsp").forward(request, response);
        
        // 或者：重定向到静态 HTML 页面（注意：重定向浏览器地址栏会改变，且 HTML 无法直接读取 request 域中的数据）
        response.sendRedirect(request.getContextPath() + "/cinema.html?movieId=" + movieId);
    }
}
