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

package com.camino.lib.provider.gdrive.service;


import com.camino.lib.provider.gdrive.responce.AccessToken;
import com.camino.lib.provider.gdrive.responce.UserCode;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GoogleAccountsService {
    String BASE_URL = "https://accounts.google.com";
    String ACCESS_GRANT_TYPE = "http://oauth.net/grant_type/device/1.0";
    String REFRESH_GRANT_TYPE = "refresh_token";

    @POST("/o/oauth2/device/code")
    @FormUrlEncoded
    UserCode getUserCode(@Field("client_id") String clientId,
                         @Field("scope") String scope);

    @POST("/o/oauth2/token")
    @FormUrlEncoded
    AccessToken getAccessToken(@Field("client_id") String clientId,
                               @Field("client_secret") String clientSecret,
                               @Field("code") String code,
                               @Field("grant_type") String grantType);

    @POST("/o/oauth2/token")
    @FormUrlEncoded
    AccessToken refreshAccessToken(@Field("client_id") String clientId,
                                   @Field("client_secret") String clientSecret,
                                   @Field("refresh_token") String refreshToken,
                                   @Field("grant_type") String grantType);
}
