package com.jeffrey.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author jeffrey
 * @ClassName: CheckNameServlet
 * @Description:
 * @date: 2021/10/31 11:27 下午
 * @version:
 * @since JDK 1.8
 */


public class CheckNameServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        String name = req.getParameter("name").replace(".zip", "");
        File file = new File(getServletContext().getRealPath("/static/video"));
        File[] list = file.listFiles();
        if (list != null) {
            ArrayList<String> array = new ArrayList<>();
            for (File item : list) {
                array.add(item.getName());
            }
            System.out.println(array.contains(name));
            resp.getWriter().write(array.contains(name) ? "1" : "0");
        }
    }
}
