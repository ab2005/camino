/*
 * Copyright (c) 2016  ab2005@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.camino.lib.provider.lyve.response;

import android.net.Uri;

import com.camino.lib.provider.Provider;
import com.camino.lib.provider.lyve.LyveCloudProvider;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class FileMetadata implements Provider.FileMetadata {
    @SerializedName("parent")
    @Expose
    public String parent;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("path_lower")
    @Expose
    public String pathLower;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName(".tag")
    @Expose
    public String Tag;
    @SerializedName("child_count")
    @Expose
    public Integer childCount;
    @SerializedName("rev")
    @Expose
    public String rev;
    @SerializedName("size")
    @Expose
    public Long size;
    @SerializedName("is_deleted")
    @Expose
    public Boolean isDeleted;
    @SerializedName("filetype")
    @Expose
    public String filetype;
    @SerializedName("server_modified")
    @Expose
    public String serverModified;
    @SerializedName("client_modified")
    @Expose
    public String clientModified;
    @SerializedName("media_info")
    @Expose
    public MediaInfo mediaInfo;
    @SerializedName("device_info")
    @Expose
    public DeviceInfo deviceInfo;

    @Override
    public String id() {
        return id;
    }

    @Override
    public Date clientModified() {
        return LyveCloudProvider.dateFromString(clientModified);
    }

    @Override
    public Date serverModified() {
        return LyveCloudProvider.dateFromString(serverModified);
    }

    @Override
    public String rev() {
        return rev;
    }

    @Override
    public long size() {
        return size == null ? 0L : size;
    }

    @Override
    public Provider.MediaInfo mediaInfo() {
        return mediaInfo;
    }

    @Override
    public Uri imageUri() {
        return LyveCloudProvider.getImageUri(pathLower, null, null);
    }

    @Override
    public Uri thumbnailUri(String type, String size) {
        return LyveCloudProvider.getImageUri(pathLower, type, size);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String pathLower() {
        return pathLower;
    }

    @Override
    public String parentSharedFolderId() {
        return parent;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
