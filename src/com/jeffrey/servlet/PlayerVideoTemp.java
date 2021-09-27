package com.jeffrey.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
        resp.setContentType("video/mp4");
        String choose = req.getParameter("item");
        String realPath = getServletContext().getRealPath("/static/video/" + choose);
        System.out.println(realPath);

        String[] files = new File(realPath).list();
        System.out.println(Arrays.toString(files));
        if (files != null) {
            List<String> lists = Arrays.asList(files);

            lists.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            });
            System.out.println(lists);
            req.setAttribute("ver", choose);
            req.setAttribute("list", lists);
            req.getRequestDispatcher("/static/jsp/movieList.jsp").forward(req, resp);
        }
    }

}
