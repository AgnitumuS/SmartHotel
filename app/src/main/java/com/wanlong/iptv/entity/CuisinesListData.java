package com.wanlong.iptv.entity;

import java.io.Serializable;

/**
 * Created by lingchen on 2018/1/30. 15:48
 * mail:lingchen52@foxmail.com
 */
public class CuisinesListData implements Serializable {


    /**
     * id : 2
     * name : 小炒肉
     * image : /img/cuisine/20180315152550WWNPcmxBPT0.
     * classfication : 湘菜
     * price : 22
     */

    private String id;
    private String name;
    private String image;
    private String classfication;
    private String price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getClassfication() {
        return classfication;
    }

    public void setClassfication(String classfication) {
        this.classfication = classfication;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
