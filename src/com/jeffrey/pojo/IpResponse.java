package com.jeffrey.pojo;

/**
 * @author jeffrey
 * @ClassName: IpResponse
 * @Description:
 * @date: 2021/11/27 3:31 下午
 * @version:
 * @since JDK 1.8
 */


public class IpResponse {
    private String status;
    private String country;

    public IpResponse() {
    }

    public IpResponse(String status, String country) {
        this.status = status;
        this.country = country;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "IpResponse{" +
                "status='" + status + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
