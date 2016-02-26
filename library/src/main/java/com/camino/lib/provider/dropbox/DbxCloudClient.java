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

package com.camino.lib.provider.dropbox;

import com.camino.lib.provider.lyve.request.DownloadRequest;
import com.camino.lib.provider.lyve.request.ListFolderRequest;
import com.camino.lib.provider.lyve.request.LoginRequest;
import com.camino.lib.provider.lyve.request.SearchRequest;
import com.camino.lib.provider.lyve.response.ListFolderResponse;
import com.camino.lib.provider.lyve.response.SearchResponse;
import com.camino.lib.provider.lyve.response.Token;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Streaming;

public interface DbxCloudClient {
    @POST("v1/auth/login")
    Call<Token> login(@Body LoginRequest req);

    @POST("2/files/list_folder")
    Call<ListFolderResponse> listFolder(@Body ListFolderRequest req);

    @POST("2/files/search")
    Call<SearchResponse> search(@Body SearchRequest req);

    @POST("2/files/download")
    @Streaming
    Call<ResponseBody> download(@Header("Dropbox-API-Arg") DownloadRequest req);
}
