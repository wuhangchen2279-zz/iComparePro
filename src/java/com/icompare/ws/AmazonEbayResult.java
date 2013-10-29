/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.icompare.utility;

import java.util.List;

/**
 *
 * @author xXx
 */
public class AmazonEbayResult {
    private String title;
    private String price;
    private String usedPrice;
    private String newPrice;
    private String limitedPrice;
    private String condition;
    private List<String> sellers;
    private String reviewsUrl;

    public String getReviewsUrl() {
        return reviewsUrl;
    }

    public void setReviewsUrl(String reviewsUrl) {
        this.reviewsUrl = reviewsUrl;
    }
    
    

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    
    
    public List<String> getSellers() {
        return sellers;
    }

    public void setSellers(List<String> sellers) {
        this.sellers = sellers;
    }
    
    

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsedPrice() {
        return usedPrice;
    }

    public void setUsedPrice(String usedPrice) {
        this.usedPrice = usedPrice;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public String getLimitedPrice() {
        return limitedPrice;
    }

    public void setLimitedPrice(String limitedPrice) {
        this.limitedPrice = limitedPrice;
    }
    
    
    
}
