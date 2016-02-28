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

import android.support.annotation.Nullable;

import com.camino.lib.provider.Provider;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MediaInfo implements Provider.MediaInfo {

    @SerializedName(".tag")
    @Expose
    public String tag;
    @SerializedName("metadata")
    @Expose
    public MediaMetadata metadata;

    @Override
    public
    @Nullable
    Provider.MediaInfo.Tag tag() {
        switch (tag) {
            case "metadata":
                return Provider.MediaInfo.Tag.metadata;
            case "pending":
                return Provider.MediaInfo.Tag.pending;
            default:
                return null;
        }
    }

    @Override
    public Provider.MediaMetadata metadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
