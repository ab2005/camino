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

package com.camino.lib.provider.lyve.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Client {

    @SerializedName("client_id")
    @Expose
    public String clientId;
    @SerializedName("client_platform")
    @Expose
    public String clientPlatform;
    @SerializedName("client_type")
    @Expose
    public String clientType;
    @SerializedName("client_version")
    @Expose
    public String clientVersion;
    @SerializedName("display_name")
    @Expose
    public String displayName;

    public Client withClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public Client withClientPlatform(String clientPlatform) {
        this.clientPlatform = clientPlatform;
        return this;
    }

    public Client withClientType(String clientType) {
        this.clientType = clientType;
        return this;
    }

    public Client withClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
        return this;
    }

    public Client withDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

}
