package com.appdupe.flamerapp.pojo.fb.education;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;


public class FacebookEducationType {

@Expose
private FacebookSchoolType school;
@Expose
private String type;
@Expose
private List<FacebookConcentrationType> concentration = new ArrayList<FacebookConcentrationType>();
@Expose
private FacebookYearType year;

public FacebookSchoolType getSchool() {
return school;
}

public void setSchool(FacebookSchoolType school) {
this.school = school;
}

public String getType() {
return type;
}

public void setType(String type) {
this.type = type;
}

public List<FacebookConcentrationType> getConcentration() {
return concentration;
}

public void setConcentration(List<FacebookConcentrationType> concentration) {
this.concentration = concentration;
}

public FacebookYearType getYear() {
return year;
}

public void setYear(FacebookYearType year) {
this.year = year;
}

}