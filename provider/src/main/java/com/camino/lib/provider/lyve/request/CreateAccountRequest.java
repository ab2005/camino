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

public class CreateAccountRequest {
    private String email;
    private String password;
    private Name name;
    private Client client;
    private Boolean isInternal;

    /**
     * @return The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public CreateAccountRequest withEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public CreateAccountRequest withPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * @return The name
     */
    public Name getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(Name name) {
        this.name = name;
    }

    public CreateAccountRequest withName(Name name) {
        this.name = name;
        return this;
    }

    /**
     * @return The client
     */
    public Client getClient() {
        return client;
    }

    /**
     * @param client The client
     */
    public void setClient(Client client) {
        this.client = client;
    }

    public CreateAccountRequest withClient(Client client) {
        this.client = client;
        return this;
    }

    /**
     * @return The isInternal
     */
    public Boolean getIsInternal() {
        return isInternal;
    }

    /**
     * @param isInternal The is_internal
     */
    public void setIsInternal(Boolean isInternal) {
        this.isInternal = isInternal;
    }

    public CreateAccountRequest withIsInternal(Boolean isInternal) {
        this.isInternal = isInternal;
        return this;
    }

}
