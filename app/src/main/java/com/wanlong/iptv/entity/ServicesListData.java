package com.wanlong.iptv.entity;

import java.io.Serializable;

/**
 * Created by lingchen on 2018/3/19. 09:08
 * mail:lingchen52@foxmail.com
 */
public class ServicesListData implements Serializable {


    /**
     * id : 4
     * item : 健身房
     * caption : 健身加油站
     * description : 健身体魄，健康生活
     * image : /img/cuisine/
     * classfication : 健身
     * price : 20
     */

    private String id;
    private String item;
    private String caption;
    private String description;
    private String image;
    private String classfication;
    private String price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
