package cn.itcast.pojo;

import java.io.Serializable;

/**
 * @Author:Administrator
 * @Date: 2019/11/28 22:38
 */
public class Address implements Serializable {
    private Integer id;
    private String name;
    private String address;
    private String lng;
    private String lat;
    private String img;

    public Address() {
    }

    public Address(Integer id, String name, String address, String lng, String lat, String img) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.lng = lng;
        this.lat = lat;
        this.img = img;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
