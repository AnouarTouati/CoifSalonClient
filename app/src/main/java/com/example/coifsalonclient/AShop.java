package com.example.coifsalonclient;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

class AShop  implements  Parcelable{
    private String shopName;
    private String shopUid;
    private String shopMainPhotoReference;
    private String emailAddress;
    private Boolean isEmployee = false;
    private Boolean isBusinessOwner = false;
    private String selectedState;
    private String SelectedCommune;
    private Boolean usesCoordinates = false;
    private Double shopLatitude;
    private Double shopLongitude;
    private Boolean isMen = true;
    private String shopPhoneNumber;
    private String facebookLink;
    private String instagramLink;
    private Boolean coiffure = false;
    private Boolean makeUp = false;
    private Boolean meches = false;
    private Boolean tinte = false;
    private Boolean pedcure = false;
    private Boolean manage = false;
    private Boolean manicure = false;
    private Boolean coupe = false;
    private String saturday;
    private String sunday;
    private String monday;
    private String tuesday;
    private String wednesday;
    private String thursday;
    private String friday;

    //////////////////////////////////////////////////////////////////////////////
    //FRAGMENT 1 SERVICES
    private List<String> servicesHairCutsNames = new ArrayList<>();
    private List<String> servicesHairCutsPrices = new ArrayList<>();
    private List<String> servicesHairCutsDuration = new ArrayList<>();
    private String successfullyBookedHaircut = null;
    private String successfullyBookedShop = null;
    ///////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////
    //FRAGMENT 3 REVIEWS
    private List<String> reviewersNames = new ArrayList<>();
    private List<String> reviewersComments = new ArrayList<>();
    private List<String> reviewersCommentDate = new ArrayList<>();
    private List<Float> reviewersGivenStars = new ArrayList<>();
    ///////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////
    //FRAGMENT 4 PORTFOLIO
    private List<String> freshPhotosReferencesFromServer = new ArrayList<>();
    //////////////////////////////////////////////////////////////////////////////


    public  AShop(){
      shopLatitude=0d;
      shopLongitude=0d;
      saturday=sunday=monday=tuesday=thursday=friday="NoNoNoNo";
    }


    protected AShop(Parcel in) {
        shopName = in.readString();
        shopUid = in.readString();
        shopMainPhotoReference = in.readString();
        emailAddress = in.readString();
        byte tmpIsEmployee = in.readByte();
        isEmployee = tmpIsEmployee == 0 ? null : tmpIsEmployee == 1;
        byte tmpIsBusinessOwner = in.readByte();
        isBusinessOwner = tmpIsBusinessOwner == 0 ? null : tmpIsBusinessOwner == 1;
        selectedState = in.readString();
        SelectedCommune = in.readString();
        byte tmpUsesCoordinates = in.readByte();
        usesCoordinates = tmpUsesCoordinates == 0 ? null : tmpUsesCoordinates == 1;
        if (in.readByte() == 0) {
            shopLatitude = null;
        } else {
            shopLatitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            shopLongitude = null;
        } else {
            shopLongitude = in.readDouble();
        }
        byte tmpIsMen = in.readByte();
        isMen = tmpIsMen == 0 ? null : tmpIsMen == 1;
        shopPhoneNumber = in.readString();
        facebookLink = in.readString();
        instagramLink = in.readString();
        byte tmpCoiffure = in.readByte();
        coiffure = tmpCoiffure == 0 ? null : tmpCoiffure == 1;
        byte tmpMakeUp = in.readByte();
        makeUp = tmpMakeUp == 0 ? null : tmpMakeUp == 1;
        byte tmpMeches = in.readByte();
        meches = tmpMeches == 0 ? null : tmpMeches == 1;
        byte tmpTinte = in.readByte();
        tinte = tmpTinte == 0 ? null : tmpTinte == 1;
        byte tmpPedcure = in.readByte();
        pedcure = tmpPedcure == 0 ? null : tmpPedcure == 1;
        byte tmpManage = in.readByte();
        manage = tmpManage == 0 ? null : tmpManage == 1;
        byte tmpManicure = in.readByte();
        manicure = tmpManicure == 0 ? null : tmpManicure == 1;
        byte tmpCoupe = in.readByte();
        coupe = tmpCoupe == 0 ? null : tmpCoupe == 1;
        saturday = in.readString();
        sunday = in.readString();
        monday = in.readString();
        tuesday = in.readString();
        wednesday = in.readString();
        thursday = in.readString();
        friday = in.readString();
        servicesHairCutsNames = in.createStringArrayList();
        servicesHairCutsPrices = in.createStringArrayList();
        servicesHairCutsDuration = in.createStringArrayList();
        successfullyBookedHaircut = in.readString();
        successfullyBookedShop = in.readString();
        reviewersNames = in.createStringArrayList();
        reviewersComments = in.createStringArrayList();
        reviewersCommentDate = in.createStringArrayList();
        freshPhotosReferencesFromServer = in.createStringArrayList();

    }

    public static final Creator<AShop> CREATOR = new Creator<AShop>() {
        @Override
        public AShop createFromParcel(Parcel in) {
            return new AShop(in);
        }

        @Override
        public AShop[] newArray(int size) {
            return new AShop[size];
        }
    };

    public String getShopMainPhotoReference() {
        return shopMainPhotoReference;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopUid() {
        return shopUid;
    }

    public String getEmailAddress() {
        return emailAddress;
    }


    public Boolean getEmployee() {
        return isEmployee;
    }

    public Boolean getBusinessOwner() {
        return isBusinessOwner;
    }

    public String getSelectedState() {
        return selectedState;
    }

    public String getSelectedCommune() {
        return SelectedCommune;
    }

    public Boolean getUsesCoordinates() {
        return usesCoordinates;
    }

    public Double getShopLatitude() {
        return shopLatitude;
    }

    public Double getShopLongitude() {
        return shopLongitude;
    }

    public Boolean getMen() {
        return isMen;
    }

    public String getShopPhoneNumber() {
        return shopPhoneNumber;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public String getInstagramLink() {
        return instagramLink;
    }

    public Boolean getCoiffure() {
        return coiffure;
    }

    public Boolean getMakeUp() {
        return makeUp;
    }

    public Boolean getMeches() {
        return meches;
    }

    public Boolean getTinte() {
        return tinte;
    }

    public Boolean getPedcure() {
        return pedcure;
    }

    public Boolean getManage() {
        return manage;
    }

    public Boolean getManicure() {
        return manicure;
    }

    public Boolean getCoupe() {
        return coupe;
    }

    public String getSaturday() {
        return saturday;
    }

    public String getSunday() {
        return sunday;
    }

    public String getMonday() {
        return monday;
    }

    public String getTuesday() {
        return tuesday;
    }

    public String getWednesday() {
        return wednesday;
    }

    public String getThursday() {
        return thursday;
    }

    public String getFriday() {
        return friday;
    }

    public List<String> getServicesHairCutsNames() {
        return servicesHairCutsNames;
    }

    public List<String> getServicesHairCutsPrices() {
        return servicesHairCutsPrices;
    }

    public List<String> getServicesHairCutsDuration() {
        return servicesHairCutsDuration;
    }

    public String getSuccessfullyBookedHaircut() {
        return successfullyBookedHaircut;
    }

    public String getSuccessfullyBookedShop() {
        return successfullyBookedShop;
    }

    public List<String> getReviewersNames() {
        return reviewersNames;
    }

    public List<String> getReviewersComments() {
        return reviewersComments;
    }

    public List<String> getReviewersCommentDate() {
        return reviewersCommentDate;
    }

    public List<Float> getReviewersGivenStars() {
        return reviewersGivenStars;
    }

    public List<String> getFreshPhotosReferencesFromServer() {
        return freshPhotosReferencesFromServer;
    }

    public void setShopMainPhotoReference(String shopMainPhotoReference) {
        this.shopMainPhotoReference = shopMainPhotoReference;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setShopUid(String shopUid) {
        this.shopUid = shopUid;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }


    public void setEmployee(Boolean employee) {
        isEmployee = employee;
    }

    public void setBusinessOwner(Boolean businessOwner) {
        isBusinessOwner = businessOwner;
    }

    public void setSelectedState(String selectedState) {
        this.selectedState = selectedState;
    }

    public void setSelectedCommune(String selectedCommune) {
        SelectedCommune = selectedCommune;
    }

    public void setUsesCoordinates(Boolean usesCoordinates) {
        this.usesCoordinates = usesCoordinates;
    }

    public void setShopLatitude(Double shopLatitude) {
        this.shopLatitude = shopLatitude;
    }

    public void setShopLongitude(Double shopLongitude) {
        this.shopLongitude = shopLongitude;
    }

    public void setMen(Boolean men) {
        isMen = men;
    }

    public void setShopPhoneNumber(String shopPhoneNumber) {
        this.shopPhoneNumber = shopPhoneNumber;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public void setInstagramLink(String instagramLink) {
        this.instagramLink = instagramLink;
    }

    public void setCoiffure(Boolean coiffure) {
        this.coiffure = coiffure;
    }

    public void setMakeUp(Boolean makeUp) {
        this.makeUp = makeUp;
    }

    public void setMeches(Boolean meches) {
        this.meches = meches;
    }

    public void setTinte(Boolean tinte) {
        this.tinte = tinte;
    }

    public void setPedcure(Boolean pedcure) {
        this.pedcure = pedcure;
    }

    public void setManage(Boolean manage) {
        this.manage = manage;
    }

    public void setManicure(Boolean manicure) {
        this.manicure = manicure;
    }

    public void setCoupe(Boolean coupe) {
        this.coupe = coupe;
    }

    public void setSaturday(String saturday) {
        this.saturday = saturday;
    }

    public void setSunday(String sunday) {
        this.sunday = sunday;
    }

    public void setMonday(String monday) {
        this.monday = monday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday = tuesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday = wednesday;
    }

    public void setThursday(String thursday) {
        this.thursday = thursday;
    }

    public void setFriday(String friday) {
        this.friday = friday;
    }

    public void setServicesHairCutsNames(List<String> servicesHairCutsNames) {
        this.servicesHairCutsNames = servicesHairCutsNames;
    }

    public void setServicesHairCutsPrices(List<String> servicesHairCutsPrices) {
        this.servicesHairCutsPrices = servicesHairCutsPrices;
    }

    public void setServicesHairCutsDuration(List<String> servicesHairCutsDuration) {
        this.servicesHairCutsDuration = servicesHairCutsDuration;
    }

    public void setSuccessfullyBookedHaircut(String successfullyBookedHaircut) {
        this.successfullyBookedHaircut = successfullyBookedHaircut;
    }

    public void setSuccessfullyBookedShop(String successfullyBookedShop) {
        this.successfullyBookedShop = successfullyBookedShop;
    }

    public void setReviewersNames(List<String> reviewersNames) {
        this.reviewersNames = reviewersNames;
    }

    public void setReviewersComments(List<String> reviewersComments) {
        this.reviewersComments = reviewersComments;
    }

    public void setReviewersCommentDate(List<String> reviewersCommentDate) {
        this.reviewersCommentDate = reviewersCommentDate;
    }

    public void setReviewersGivenStars(List<Float> reviewersGivenStars) {
        this.reviewersGivenStars = reviewersGivenStars;
    }

    public void setFreshPhotosReferencesFromServer(List<String> freshPhotosReferencesFromServer) {
        this.freshPhotosReferencesFromServer = freshPhotosReferencesFromServer;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(shopName);
        parcel.writeString(shopUid);
        parcel.writeString(shopMainPhotoReference);
        parcel.writeString(emailAddress);
        parcel.writeByte((byte) (isEmployee == null ? 0 : isEmployee ? 1 : 2));
        parcel.writeByte((byte) (isBusinessOwner == null ? 0 : isBusinessOwner ? 1 : 2));
        parcel.writeString(selectedState);
        parcel.writeString(SelectedCommune);
        parcel.writeByte((byte) (usesCoordinates == null ? 0 : usesCoordinates ? 1 : 2));
        if (shopLatitude == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(shopLatitude);
        }
        if (shopLongitude == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(shopLongitude);
        }
        parcel.writeByte((byte) (isMen == null ? 0 : isMen ? 1 : 2));
        parcel.writeString(shopPhoneNumber);
        parcel.writeString(facebookLink);
        parcel.writeString(instagramLink);
        parcel.writeByte((byte) (coiffure == null ? 0 : coiffure ? 1 : 2));
        parcel.writeByte((byte) (makeUp == null ? 0 : makeUp ? 1 : 2));
        parcel.writeByte((byte) (meches == null ? 0 : meches ? 1 : 2));
        parcel.writeByte((byte) (tinte == null ? 0 : tinte ? 1 : 2));
        parcel.writeByte((byte) (pedcure == null ? 0 : pedcure ? 1 : 2));
        parcel.writeByte((byte) (manage == null ? 0 : manage ? 1 : 2));
        parcel.writeByte((byte) (manicure == null ? 0 : manicure ? 1 : 2));
        parcel.writeByte((byte) (coupe == null ? 0 : coupe ? 1 : 2));
        parcel.writeString(saturday);
        parcel.writeString(sunday);
        parcel.writeString(monday);
        parcel.writeString(tuesday);
        parcel.writeString(wednesday);
        parcel.writeString(thursday);
        parcel.writeString(friday);
        parcel.writeStringList(servicesHairCutsNames);
        parcel.writeStringList(servicesHairCutsPrices);
        parcel.writeStringList(servicesHairCutsDuration);
        parcel.writeString(successfullyBookedHaircut);
        parcel.writeString(successfullyBookedShop);
        parcel.writeStringList(reviewersNames);
        parcel.writeStringList(reviewersComments);
        parcel.writeStringList(reviewersCommentDate);
        parcel.writeStringList(freshPhotosReferencesFromServer);
    }
}
