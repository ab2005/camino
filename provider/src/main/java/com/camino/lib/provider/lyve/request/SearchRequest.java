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

public class SearchRequest {
    @SerializedName("path")
    @Expose
    public String path = "";
    @SerializedName("query")
    @Expose
    public String query;
    @SerializedName("start")
    @Expose
    public Integer start;
    @SerializedName("max_results")
    @Expose
    public Integer maxResults = 1024;
    @SerializedName("mode")
    @Expose
    public String mode = "filename";
    @SerializedName("include_media_info")
    @Expose
    public boolean includeMediaInfo;
    @SerializedName("include_full_path")
    @Expose
    public boolean includeFullPath;

    public SearchRequest withPath(String path) {
        this.path = path;
        return this;
    }
    public SearchRequest withQuery(String query) {
        this.query = query;
        return this;
    }

    public SearchRequest withStart(int start) {
        this.start = start;
        return this;
    }

    public SearchRequest withMaxResults(int maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    public SearchRequest withMode(String mode) {
        this.mode = mode;
        return this;
    }

    public SearchRequest withIcludeMediaInfo(boolean b) {
        this.includeMediaInfo = b;
        return this;
    }

    public SearchRequest withIcludeFullPath(boolean b) {
        this.includeFullPath = b;
        return this;
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}