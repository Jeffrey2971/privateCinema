package com.jeffrey.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author jeffrey
 * @ClassName: BaseServlet
 * @Description:
 * @date: 2021/9/28 2:28 上午
 * @version:
 * @since JDK 1.8
 */


public abstract class BaseServlet extends HttpServlet {

    public static final Long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("请求方法：" + req.getParameter("action"));
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        String action = req.getParameter("action");
        if (action != null) {
            try {
                Method actionMethod = getClass().getDeclaredMethod(action, HttpServletRequest.class, HttpServletResponse.class);
                actionMethod.setAccessible(true);
                actionMethod.invoke(this, req, resp);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            PrintWriter writer = resp.getWriter();
            writer.write("参数错误");
            writer.close();
        }
    }
}
