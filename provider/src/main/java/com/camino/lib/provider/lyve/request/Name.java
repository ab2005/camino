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

public class Name {

    private String givenName;
    private String surname;
    private String familiarName;
    private String displayName;

    /**
     * @return The givenName
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * @param givenName The given_name
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public Name withGivenName(String givenName) {
        this.givenName = givenName;
        return this;
    }

    /**
     * @return The surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @param surname The surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Name withSurname(String surname) {
        this.surname = surname;
        return this;
    }

    /**
     * @return The familiarName
     */
    public String getFamiliarName() {
        return familiarName;
    }

    /**
     * @param familiarName The familiar_name
     */
    public void setFamiliarName(String familiarName) {
        this.familiarName = familiarName;
    }

    public Name withFamiliarName(String familiarName) {
        this.familiarName = familiarName;
        return this;
    }

    /**
     * @return The displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName The display_name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Name withDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

}
