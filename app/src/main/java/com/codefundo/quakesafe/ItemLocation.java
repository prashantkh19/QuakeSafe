package com.codefundo.quakesafe;

import com.google.android.gms.maps.model.LatLng;

public class ItemLocation {
    LatLng latLngA ,latLngB, latLngC, latLngD;
    double height, radius;

    public ItemLocation(LatLng latLngA, LatLng latLngB, LatLng latLngC, LatLng latLngD, double height,double  radius) {
        this.latLngA = latLngA;
        this.latLngB = latLngB;
        this.latLngC = latLngC;
        this.latLngD = latLngD;
        this.height = height;
        this.radius = radius;
    }

    public double getHeight() {
        return height;
    }

    public double getRadius() {
        return radius;
    }

    public LatLng getLatLngA() {
        return latLngA;
    }

    public LatLng getLatLngB() {
        return latLngB;
    }

    public LatLng getLatLngC() {
        return latLngC;
    }

    public LatLng getLatLngD() {
        return latLngD;
    }

    public void setLatLngA(LatLng latLngA) {
        this.latLngA = latLngA;
    }

    public void setLatLngB(LatLng latLngB) {
        this.latLngB = latLngB;
    }

    public void setLatLngC(LatLng latLngC) {
        this.latLngC = latLngC;
    }

    public void setLatLngD(LatLng latLngD) {
        this.latLngD = latLngD;
    }

    public LatLng getCenterLatLng(){
        return new LatLng((latLngA.latitude+latLngB.latitude+latLngC.latitude+latLngD.latitude)/4,(latLngA.longitude+latLngB.longitude+latLngC.longitude+latLngD.longitude)/4);
    }
}
