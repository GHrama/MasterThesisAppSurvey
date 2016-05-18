package com.example.ramapriyasridharan.collections;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.kinvey.java.model.KinveyMetaData;

/**
 * Created by ramapriyasridharan on 10.03.16.
 */
public class UserInformationClass extends GenericJson {

    // used to denote which field goes into which column of the kinvey table

    //@Key("_acl")
    //private KinveyMetaData.AccessControlList acl;
    //@Key("_kmd")
    //private KinveyMetaData meta; // Kinvey metadata
    @Key("user_id")
    private String user_id;
    @Key("education_level")
    private String education_level;
    @Key("gender")
    private String gender;
    @Key("education_background")
    private String education_background;
    @Key("birth_year")
    private String birth_year;
    @Key("employment_status")
    private String employment_status;

    public UserInformationClass(){}


  /*  public KinveyMetaData.AccessControlList getAcl() {
        return acl;
    }

    public void setAcl(KinveyMetaData.AccessControlList acl) {
        this.acl = acl;
    }

    public KinveyMetaData getMeta() {
        return meta;
    }

    public void setMeta(KinveyMetaData meta) {
        this.meta = meta;
    }*/

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEducation_level() {
        return education_level;
    }

    public void setEducation_level(String education_level) {
        this.education_level = education_level;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEducation_background() {
        return education_background;
    }

    public void setEducation_background(String education_background) {
        this.education_background = education_background;
    }

    public String getBirth_year() {
        return birth_year;
    }

    public void setBirth_year(String birth_year) {
        this.birth_year = birth_year;
    }

    public String getEmployment_status() {
        return employment_status;
    }

    public void setEmployment_status(String employment_status) {
        this.employment_status = employment_status;
    }
}






