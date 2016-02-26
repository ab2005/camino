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

import com.camino.lib.provider.Provider;
import com.camino.lib.provider.lyve.LyveCloudProvider;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MediaMetadata implements Provider.MediaMetadata {

    @SerializedName(".tag")
    @Expose
    public String Tag;
    @SerializedName("dimensions")
    @Expose
    public Dimensions dimensions;
    @SerializedName("location")
    @Expose
    public Location location;
    @SerializedName("time_taken")
    @Expose
    public String timeTaken;
    @SerializedName("duration")
    @Expose
    public Integer duration;

    @Override
    public Provider.Size dimensions() {
        return new Provider.Size(dimensions.width, dimensions.height);
    }

    @Override
    public double latitude() {
        return location.latitude;
    }

    @Override
    public double longitude() {
        return location.longitude;
    }

    @Override
    public Date timeTaken() {
        return LyveCloudProvider.dateFromString(timeTaken);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
