package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class Launch {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("flight_number")
    private int flightNumber;

    @SerializedName("date_utc")
    private Date dateUtc;

    @SerializedName("details")
    private String details;

    @SerializedName("success")
    private Boolean success;

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public int getFlightNumber() { return flightNumber; }
    public Date getDateUtc() { return dateUtc; }
    public String getDetails() { return details; }
    public Boolean isSuccess() { return success; }
}
