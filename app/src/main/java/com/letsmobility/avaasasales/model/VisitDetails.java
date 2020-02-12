package com.letsmobility.avaasasales.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "VisitDetails")
public class VisitDetails implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "propertyName")
    private String propertyName;
    @ColumnInfo(name = "propertyAddress")
    private String propertyAddress;
    @ColumnInfo(name = "area")
    private String area;
    @ColumnInfo(name = "pincode")
    private String pincode;
    @ColumnInfo(name = "isLikeProduct")
    private String isLikeProduct;
    @ColumnInfo(name = "followDate")
    private String followDate;
    @ColumnInfo(name = "followTime")
    private String followTime;
    @ColumnInfo(name = "rate")
    private String rate;
    @ColumnInfo(name = "salesPersonRemark")
    private String salesPersonRemark;
    @ColumnInfo(name = "contactedPersonName")
    private String contactedPersonName;
    @ColumnInfo(name = "conatctedPersonContact")
    private String conatctedPersonContact;
    @ColumnInfo(name = "alternateContactNumber")
    private String alternateContactNumber;
    @ColumnInfo(name = "contactedPersonDesignation")
    private String contactedPersonDesignation;
    @ColumnInfo(name = "contactedPersonReview")
    private String contactedPersonReview;
    @ColumnInfo(name = "propertyPhotosUrl")
    private List<String> propertyPhotosUrl = new ArrayList<>();
    @ColumnInfo(name = "contactPersonPicUrl")
    private String contactPersonPicUrl;
    @ColumnInfo(name = "salesPersonEmail")
    private String salesPersonEmail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getIsLikeProduct() {
        return isLikeProduct;
    }

    public void setIsLikeProduct(String isLikeProduct) {
        this.isLikeProduct = isLikeProduct;
    }

    public String getFollowDate() {
        return followDate;
    }

    public void setFollowDate(String followDate) {
        this.followDate = followDate;
    }

    public String getFollowTime() {
        return followTime;
    }

    public void setFollowTime(String followTime) {
        this.followTime = followTime;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getSalesPersonRemark() {
        return salesPersonRemark;
    }

    public void setSalesPersonRemark(String salesPersonRemark) {
        this.salesPersonRemark = salesPersonRemark;
    }

    public String getContactedPersonName() {
        return contactedPersonName;
    }

    public void setContactedPersonName(String contactedPersonName) {
        this.contactedPersonName = contactedPersonName;
    }

    public String getConatctedPersonContact() {
        return conatctedPersonContact;
    }

    public void setConatctedPersonContact(String conatctedPersonContact) {
        this.conatctedPersonContact = conatctedPersonContact;
    }

    public String getAlternateContactNumber() {
        return alternateContactNumber;
    }

    public void setAlternateContactNumber(String alternateContactNumber) {
        this.alternateContactNumber = alternateContactNumber;
    }

    public String getContactedPersonDesignation() {
        return contactedPersonDesignation;
    }

    public void setContactedPersonDesignation(String contactedPersonDesignation) {
        this.contactedPersonDesignation = contactedPersonDesignation;
    }

    public String getContactedPersonReview() {
        return contactedPersonReview;
    }

    public void setContactedPersonReview(String contactedPersonReview) {
        this.contactedPersonReview = contactedPersonReview;
    }

    public List<String> getPropertyPhotosUrl() {
        return propertyPhotosUrl;
    }

    public void setPropertyPhotosUrl(List<String> propertyPhotosUrl) {
        this.propertyPhotosUrl = propertyPhotosUrl;
    }

    public String getContactPersonPicUrl() {
        return contactPersonPicUrl;
    }

    public void setContactPersonPicUrl(String contactPersonPicUrl) {
        this.contactPersonPicUrl = contactPersonPicUrl;
    }

    public String getSalesPersonEmail() {
        return salesPersonEmail;
    }

    public void setSalesPersonEmail(String salesPersonEmail) {
        this.salesPersonEmail = salesPersonEmail;
    }
}
