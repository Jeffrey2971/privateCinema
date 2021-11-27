package com.jeffrey.filter;

import com.google.gson.Gson;
import com.jeffrey.pojo.IpResponse;
import com.jeffrey.service.UploadService;
import com.jeffrey.service.impl.UploadServiceImpl;
import com.jeffrey.utils.GetRequestAddress;
import com.jeffrey.utils.RequestUtils;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestFilter implements Filter {

    private static final UploadService UPLOAD_SERVICE = new UploadServiceImpl();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String url = "http://ip-api.com/json/IP?fields=country,status";

        String ipAddress = GetRequestAddress.getIPAddress((HttpServletRequest) request);

        if (!"127.0.0.1".equals(ipAddress) && !"0:0:0:0:0:0:0:1".equals(ipAddress) && !UPLOAD_SERVICE.isExistsTable(ipAddress, "white_list")) {
            IpResponse bean = new Gson().fromJson(RequestUtils.get(url.replace("IP", ipAddress)), IpResponse.class);
            System.out.println(bean);
            HttpServletResponse resp = (HttpServletResponse) response;

            if (UPLOAD_SERVICE.isExistsTable(ipAddress, "black_list")) {
                resp.setStatus(403);
                System.out.println("拦截恶意请求，响应 403：" + ipAddress);
                return;
            } else {
                if ("success".equals(bean.getStatus())) {
                    if (!"China".equals(bean.getCountry())) {
                        UPLOAD_SERVICE.addBlackListId(ipAddress);
                        resp.setStatus(403);
                        System.out.println("拦截恶意请求，响应 403：" + ipAddress);
                        return;
                    }
                }
            }
        }
        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {

    }
}
