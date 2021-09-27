package com.jeffrey.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author jeffrey
 * @ClassName: SelectVersion
 * @Description:
 * @date: 2021/9/10 1:59 下午
 * @version:
 * @since JDK 1.8
 */


public class SelectVersion extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        System.out.println("访问");
        String[] list = new File(getServletContext().getRealPath("/static/video")).list();
        if (list != null) {
            Arrays.sort(list, String::compareTo);
        }
        // new Cookie("prev", "")
        req.setAttribute("ver", list);
        req.getRequestDispatcher("/static/jsp/select.jsp").forward(req, resp);
    }


}
