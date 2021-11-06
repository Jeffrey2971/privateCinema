package com.jeffrey.pojo;

/**
 * @author jeffrey
 * @ClassName: CheckName
 * @Description:
 * @date: 2021/11/5 10:36 下午
 * @version:
 * @since JDK 1.8
 */


public class Upload {
    private int status;
    private String msg;

    public Upload() {
    }

    public Upload(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "CheckName{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
