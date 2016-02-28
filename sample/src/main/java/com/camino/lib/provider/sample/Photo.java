package com.camino.lib.provider.sample;

import java.util.Date;

import io.realm.RealmObject;

public class Photo extends RealmObject {
    //Metadata
    public String name;
    public String pathLower;
    public String parentSharedFolderId;

    // Folder
    public String sharedFolderId;

    // File
    public String id;
    public Date clientModified;
    public Date serverModified;
    public String rev;
    public long size;
    public String imageUri;

    // Media
    public int width;
    public int height;
    public double latitude;
    public double longitude;
    public Date timeTaken;

    // Palette

    // Address
}
