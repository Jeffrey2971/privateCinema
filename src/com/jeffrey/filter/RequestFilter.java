package com.jeffrey.filter;

import com.jeffrey.dao.UploadServiceDao;
import com.jeffrey.dao.impl.UploadServiceDaoImpl;
import com.jeffrey.service.UploadService;
import com.jeffrey.service.impl.UploadServiceImpl;
import com.jeffrey.utils.GetRequestAddress;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jeffrey
 * @ClassName: RequestFilter
 * @Description:
 * @date: 2021/11/8 2:33 上午
 * @version:
 * @since JDK 1.8
 */


public class RequestFilter implements Filter {

    private static final UploadService UPLOAD_SERVICE = new UploadServiceImpl();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String ipAddress = GetRequestAddress.getIPAddress((HttpServletRequest) request);
        if (UPLOAD_SERVICE.isBlackUser(ipAddress)) {
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.setStatus(403);
            System.out.println("拦截恶意请求，响应 403：" + ipAddress);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}