package com.example.service.model;

import com.example.error.PromoStatus;
import org.joda.time.DateTime;

import java.math.BigDecimal;

public class PromoModel {

    private Integer id;
    private String promoName;
    private DateTime startDate;
    private DateTime endDate;
    private Integer itemId;
    private BigDecimal promoPrice;
    private PromoStatus promoStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(BigDecimal promoPrice) {
        this.promoPrice = promoPrice;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public PromoStatus getPromoStatus() {
        return promoStatus;
    }

    public void setPromoStatus(PromoStatus promoStatus) {
        this.promoStatus = promoStatus;
    }
}
