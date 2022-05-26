package com.example.mypet;

import android.net.Uri;

import java.io.Serializable;

public class PetProfile implements Serializable {
    private String name;
    private String idPet;
    private Uri uriPet;

    public PetProfile(String name, String idPet, Uri uriPet) {
        this.name = name;
        this.idPet = idPet;
        this.uriPet = uriPet;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdPet() {
        return idPet;
    }

    public void setIdPet(String idPet) {
        this.idPet = idPet;
    }

    public Uri getUriPet() {
        return uriPet;
    }

    public void setUriPet(Uri uriPet) {
        this.uriPet = uriPet;
    }

}
