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

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("client")
    @Expose
    public Client client;

    public LoginRequest withEmail(String email) {
        this.email = email;
        return this;
    }

    public LoginRequest withPassword(String password) {
        this.password = password;
        return this;
    }

    public LoginRequest withClient(Client client) {
        this.client = client;
        return this;
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
