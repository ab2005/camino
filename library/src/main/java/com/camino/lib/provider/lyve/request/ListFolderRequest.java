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

import com.camino.lib.provider.lyve.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ListFolderRequest {

    @SerializedName("path")
    @Expose
    public String path;
    @SerializedName("limit")
    @Expose
    public Integer limit;
    @SerializedName("include_deleted")
    @Expose
    public Boolean includeDeleted;
    @SerializedName("include_media_info")
    @Expose
    public Boolean includeMediaInfo;
    @SerializedName("include_child_count")
    @Expose
    public Boolean includeChildCount;

    public ListFolderRequest withPath(String path) {
        this.path = path;
        return this;
    }

    public ListFolderRequest withLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public ListFolderRequest withIncludeDeleted(Boolean includeDeleted) {
        this.includeDeleted = includeDeleted;
        return this;
    }

    public ListFolderRequest withIncludeMediaInfo(Boolean includeMediaInfo) {
        this.includeMediaInfo = includeMediaInfo;
        return this;
    }

    public ListFolderRequest withIncludeChildCount(Boolean includeChildCount) {
        this.includeChildCount = includeChildCount;
        return this;
    }

}