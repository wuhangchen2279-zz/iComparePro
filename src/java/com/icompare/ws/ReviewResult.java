/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.icompare.ws;

import java.util.List;

/**
 *
 * @author xXx
 */
public class ReviewResult {
    private int positiveAmount;
    private int negativeAmount;
    private int neturalAmount;
    private List<SampleReview> sampleReviews;

    public int getPositiveAmount() {
        return positiveAmount;
    }

    public void setPositiveAmount(int positiveAmount) {
        this.positiveAmount = positiveAmount;
    }

    public int getNegativeAmount() {
        return negativeAmount;
    }

    public void setNegativeAmount(int negativeAmount) {
        this.negativeAmount = negativeAmount;
    }

    public int getNeturalAmount() {
        return neturalAmount;
    }

    public void setNeturalAmount(int neturalAmount) {
        this.neturalAmount = neturalAmount;
    }

    public List<SampleReview> getSampleReviews() {
        return sampleReviews;
    }

    public void setSampleReviews(List<SampleReview> sampleReviews) {
        this.sampleReviews = sampleReviews;
    }
    
    
}
