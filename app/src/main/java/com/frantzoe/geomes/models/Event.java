package com.frantzoe.geomes.models;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import androidx.annotation.NonNull;

public class Event {

    public static final String DIR_IN = "ic";
    public static final String DIR_OUT = "og";

    private static final int CHARS_LENGTH = 16;
    private static final String CHARS_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    private int _id;
    private String direction;
    private double latitude;
    private double longitude;
    private boolean confirmed;
    private String uid;
    private String phone;
    private String details;
    private String date;

    public Event() {
        //
    }

    public Event(String direction, double latitude, double longitude, boolean confirmed, String phone, String details, String date) {
        this.uid = getUniqueCode();
        this.direction = direction;
        this.latitude = latitude;
        this.longitude = longitude;
        this.confirmed = confirmed;
        this.phone = phone;
        this.details = details;
        this.date = date;
    }

    public Event(int id, String uid, double latitude, double longitude, String direction, boolean confirmed, String phone, String date, String details) {
        this._id = id;
        this.uid = uid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.direction = direction;
        this.confirmed = confirmed;
        this.phone = phone;
        this.date = date;
        this.details = details;
    }

    public Event(String uid, double latitude, double longitude, String direction, boolean confirmed, String phone, String date, String details) {
        this.uid = uid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.direction = direction;
        this.confirmed = confirmed;
        this.phone = phone;
        this.date = date;
        this.details = details;
    }

    public int getId() {
        return _id;
    }

    public String getUid() {
        return uid;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @NonNull
    @Override
    public String toString() {
        return "Event{" +
                "_id=" + _id +
                ", direction=" + direction +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", confirmed=" + confirmed +
                ", uid='" + uid + '\'' +
                ", phone='" + phone + '\'' +
                ", details='" + details + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return Double.compare(event.latitude, latitude) == 0 &&
                Double.compare(event.longitude, longitude) == 0 &&
                Objects.equals(uid, event.uid) &&
                Objects.equals(details, event.details) &&
                Objects.equals(date, event.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude, uid, details, date);
    }

    private String getUniqueCode() {
        final String shuffledString = shuffle();
        final StringBuilder uniqueCode = new StringBuilder();
        for (int i = 0; i < CHARS_LENGTH; i++) {
            final int index = ThreadLocalRandom.current().nextInt(0, CHARS_STRING.length());
            uniqueCode.append(shuffledString.charAt(index));
        }
        return uniqueCode.toString().toString();
    }

    private String shuffle() {
        String input = CHARS_STRING;
        final StringBuilder shuffledString = new StringBuilder();
        while (input.length() != 0) {
            final int index = (int) Math.floor(Math.random() * input.length());
            final char c = input.charAt(index);
            input = input.substring(0, index) + input.substring(index + 1);
            shuffledString.append(c);
        }
        return shuffledString.toString();
    }
}
