package com.jeffrey.listener;

import com.jeffrey.utils.GetRequestAddress;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author jeffrey
 * @ClassName: Listen
 * @Description:
 * @date: 2021/11/4 2:18 上午
 * @version:
 * @since JDK 1.8
 */


public class RequestListener implements ServletRequestListener {
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {

    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        System.out.println(sdf.format(new Date()) + " 请求：" + GetRequestAddress.getIPAddress((HttpServletRequest) sre.getServletRequest()));
    }
}
