package com.letsmobility.avaasasales.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.letsmobility.avaasasales.room.StringTypeConverter;

import java.util.ArrayList;

@Entity(tableName = "VisitDetail")
public class Visitdetail implements Parcelable {

    private String propertyName;
    private String propertyAddress;
    private String area;
    private String pincode;
    @NonNull
    @PrimaryKey
    private String latitute;
    private String longitude;
    private String isLikeProduct;
    private String followDate;
    private String followTime;
    private String rate;
    private String salesPersonRemark;
    private String contactedPersonName;
    private String conatctedPersonContact;
    private String alternateContactNumber;
    private String contactedPersonDesignation;
    private String contactedPersonReview;

    @ColumnInfo(name = "propertyPhotosUrl")
    @TypeConverters({StringTypeConverter.class})
    private ArrayList<String> propertyPhotosUrl = new ArrayList<>();

    private String contactPersonPicUrl;
    private String salesPersonEmail;

    public String getAlternateContactNumber() {
        return alternateContactNumber;
    }

    public void setAlternateContactNumber(String alternateContactNumber) {
        this.alternateContactNumber = alternateContactNumber;
    }

    public Visitdetail() {
    }

    protected Visitdetail(Parcel in) {
        propertyName = in.readString();
        propertyAddress = in.readString();
        area = in.readString();
        pincode = in.readString();
        latitute = in.readString();
        longitude = in.readString();
        isLikeProduct = in.readString();
        followDate = in.readString();
        followTime = in.readString();
        rate = in.readString();
        salesPersonRemark = in.readString();
        contactedPersonName = in.readString();
        conatctedPersonContact = in.readString();
        alternateContactNumber = in.readString();
        contactedPersonDesignation = in.readString();
        contactedPersonReview = in.readString();
        propertyPhotosUrl = in.createStringArrayList();
        contactPersonPicUrl = in.readString();
        salesPersonEmail = in.readString();
    }

    public static final Creator<Visitdetail> CREATOR = new Creator<Visitdetail>() {
        @Override
        public Visitdetail createFromParcel(Parcel in) {
            return new Visitdetail(in);
        }

        @Override
        public Visitdetail[] newArray(int size) {
            return new Visitdetail[size];
        }
    };

    @NonNull
    public String getLatitute() {
        return latitute;
    }

    public void setLatitute(String latitute) {
        this.latitute = latitute;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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

    public ArrayList<String> getPropertyPhotosUrl() {
        return propertyPhotosUrl;
    }

    public void setPropertyPhotosUrl(ArrayList<String> propertyPhotosUrl) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(propertyName);
        dest.writeString(propertyAddress);
        dest.writeString(area);
        dest.writeString(pincode);
        dest.writeString(latitute);
        dest.writeString(longitude);
        dest.writeString(isLikeProduct);
        dest.writeString(followDate);
        dest.writeString(followTime);
        dest.writeString(rate);
        dest.writeString(salesPersonRemark);
        dest.writeString(contactedPersonName);
        dest.writeString(conatctedPersonContact);
        dest.writeString(alternateContactNumber);
        dest.writeString(contactedPersonDesignation);
        dest.writeString(contactedPersonReview);
        dest.writeStringList(propertyPhotosUrl);
        dest.writeString(contactPersonPicUrl);
        dest.writeString(salesPersonEmail);
    }
}
