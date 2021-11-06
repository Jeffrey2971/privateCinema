package com.jeffrey.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author jeffrey
 * @ClassName: BaseServlet
 * @Description:
 * @date: 2021/11/5 8:45 下午
 * @version:
 * @since JDK 1.8
 */


public abstract class BaseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        String action = req.getParameter("action");
        try {
            Method actionMethod = this.getClass().getDeclaredMethod(action, HttpServletRequest.class, HttpServletResponse.class);
            actionMethod.invoke(this, req, resp);
        } catch (Exception e) {
            resp.getWriter().write("参数异常：没有找到方法");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
