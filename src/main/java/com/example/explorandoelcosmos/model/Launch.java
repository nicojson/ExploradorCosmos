package com.example.explorandoelcosmos.model;
import com.google.gson.annotations.SerializedName;

// Un POJO (Plain Old Java Object) simple para almacenar los datos del lanzamiento.
// Deberías añadir más campos según el schema.md (ej: docs/launches/v5/schema.md)
public class Launch {

    @SerializedName("name")
    private String name;

    @SerializedName("flight_number")
    private int flightNumber;

    @SerializedName("details")
    private String details;

    @SerializedName("links")
    private Links links;

    // --- Getters y Setters ---

    public String getName() {
        return name;
    }

    public Links getLinks() {
        return links;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(int flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "Launch #" + flightNumber + ": " + name + "\nDetails: " + details;
    }
}