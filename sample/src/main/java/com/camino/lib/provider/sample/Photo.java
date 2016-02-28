package com.camino.lib.provider.sample;

import android.util.Log;

import com.camino.lib.provider.Provider;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;

public class Photo extends RealmObject {
    private static final String TAG = Photo.class.getName();
            ;
    // Metadata
    private String name;
    private String pathLower;
    private String parentSharedFolderId;

    private String id;

    // Folder
    private String sharedFolderId;

    // File
    private Date serverModified;
    private Date clientModified;
    private String rev;
    private long size;
    private String imageUri;

    // Media
    private int width;
    private int height;
    private double latitude;
    private double longitude;
    private Date timeTaken;

    // Palette

    // Address

    public static Photo create(Realm realm, Provider.Metadata o) {
        Photo p = realm.createObject(Photo.class);
        try {
            p.setName(o.name());
            p.setPathLower(o.pathLower());
            p.setParentSharedFolderId(o.parentSharedFolderId());
            if (o instanceof Provider.FolderMetadata) {
                p.setSharedFolderId(((Provider.FolderMetadata) o).sharedFolderId());
            } else if (o instanceof Provider.FileMetadata) {
                p.setClientModified(((Provider.FileMetadata) o).clientModified());
                p.setServerModified(((Provider.FileMetadata) o).serverModified());
                p.setRev(((Provider.FileMetadata) o).rev());
                p.setSize(p.size);
                p.setImageUri(((Provider.FileMetadata) o).imageUri().toString());
                if (((Provider.FileMetadata) o).mediaInfo() != null) {
                    Provider.MediaMetadata info = ((Provider.FileMetadata) o).mediaInfo().metadata();
                    if (info != null) {
                        p.setWidth(info.dimensions().getWidth());
                        p.setHeight(info.dimensions().getHeight());
                        p.setLatitude(info.latitude());
                        p.setLongitude(info.longitude());
                        p.setTimeTaken(info.timeTaken());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "create failed " + e);
        }
        return p;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPathLower() {
        return pathLower;
    }

    public void setPathLower(String pathLower) {
        this.pathLower = pathLower;
    }

    public String getParentSharedFolderId() {
        return parentSharedFolderId;
    }

    public void setParentSharedFolderId(String parentSharedFolderId) {
        this.parentSharedFolderId = parentSharedFolderId;
    }

    public String getSharedFolderId() {
        return sharedFolderId;
    }

    public void setSharedFolderId(String sharedFolderId) {
        this.sharedFolderId = sharedFolderId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getClientModified() {
        return clientModified;
    }

    public void setClientModified(Date clientModified) {
        this.clientModified = clientModified;
    }

    public Date getServerModified() {
        return serverModified;
    }

    public void setServerModified(Date serverModified) {
        this.serverModified = serverModified;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
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

    public Date getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Date timeTaken) {
        this.timeTaken = timeTaken;
    }
}
