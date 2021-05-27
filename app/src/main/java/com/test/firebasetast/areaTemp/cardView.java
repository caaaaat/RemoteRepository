package com.test.firebasetast.areaTemp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class cardView {


    private String locationName;
    private String elementName;
    private String parameterName;
    private String parameterValue;
//    private Map parameter;

    public cardView() {
    }

    public cardView(String locationName, String elementName, String parameterName) {
        this.locationName = locationName;
        this.elementName = elementName;
        this.parameterName = parameterName;
//        this.parameterValue = parameterValue;
    }

    public cardView(String locationName, String elementName, String parameterName, String parameterValue) {
        this.locationName = locationName;
        this.elementName = elementName;
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
    }

//    public Map getParameter() {
//        return parameter;
//    }
//
//    public void setParameter(Map parameter) {
//        this.parameter = parameter;
//    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }









}
