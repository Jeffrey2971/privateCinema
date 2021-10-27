package com.jeffrey.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author jeffrey
 * @ClassName: SetMimeType
 * @Description:
 * @date: 2021/10/25 10:39 下午
 * @version:
 * @since JDK 1.8
 */


public class SetMimeType extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String file = req.getParameter("file");
        resp.setContentType(getServletContext().getMimeType(file));
        resp.setContentLengthLong(new File(getServletContext().getRealPath(file)).length());
        resp.setBufferSize(3145728);
        req.getRequestDispatcher(file).forward(req,resp);
    }
}
