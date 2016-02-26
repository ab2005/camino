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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceInfo {

    @SerializedName("device_id")
    @Expose
    public String deviceId;
    @SerializedName("device_make")
    @Expose
    public String deviceMake;
    @SerializedName("device_model")
    @Expose
    public String deviceModel;
    @SerializedName("created")
    @Expose
    public String created;
    @SerializedName("device_size")
    @Expose
    public Long deviceSize;
    @SerializedName("container_id")
    @Expose
    public String containerId;
    @SerializedName("expiration")
    @Expose
    public String expiration;
    @SerializedName("display_name")
    @Expose
    public String displayName;

}
