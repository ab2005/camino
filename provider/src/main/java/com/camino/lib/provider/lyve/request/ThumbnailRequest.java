/*
 * Copyright (c) 2015. Seagate Technology PLC. All rights reserved.
 */

package com.camino.lib.provider.lyve.request;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ThumbnailRequest {
    @SerializedName("path")
    @Expose
    public final String path;
    @SerializedName("format")
    @Expose
    public final String format;
    @SerializedName("size")
    @Expose
    public final String size;

    public ThumbnailRequest(String path, String format, String size) {
        this.path = path;
        this.format = format;
        this.size = size;
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
