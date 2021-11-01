package com.jeffrey.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author jeffrey
 * @ClassName: PlayerVideoTemp
 * @Description:
 * @date: 2021/9/9 8:42 下午
 * @version:
 * @since JDK 1.8
 */


public class PlayerVideoTemp extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String choose = req.getParameter("item");

        if (!"".equals(choose)) {
            String realPath = getServletContext().getRealPath("/static/video/" + choose);
            System.out.println(realPath);

            String[] files = new File(realPath).list();

            if (files != null) {
                List<String> lists = Arrays.asList(files);

                lists.sort(String::compareTo);

                req.setAttribute("ver", choose);
                req.setAttribute("list", lists);
                req.getRequestDispatcher("dynamic/jsp/movieList.jsp").forward(req, resp);
            }
        } else {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().write("参数异常");
        }

    }

}
