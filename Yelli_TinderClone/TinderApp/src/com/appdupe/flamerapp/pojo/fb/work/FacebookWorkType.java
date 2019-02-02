
package com.appdupe.flamerapp.pojo.fb.work;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class FacebookWorkType {

    @SerializedName("end_date")
    @Expose
    private String endDate;
    @Expose
    private FacebookEmployerType employer;
    @Expose
    private FacebookPositionType position;
    @SerializedName("start_date")
    @Expose
    private String startDate;

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public FacebookEmployerType getEmployer() {
        return employer;
    }

    public void setEmployer(FacebookEmployerType employer) {
        this.employer = employer;
    }

    public FacebookPositionType getPosition() {
        return position;
    }

    public void setPosition(FacebookPositionType position) {
        this.position = position;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

}
