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
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ListFolderResponse implements Provider.ListFolderResult {

    @SerializedName("parent")
    @Expose
    public String parent;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("entries")
    @Expose
    public List<FileMetadata> entries = new ArrayList<FileMetadata>();
    @SerializedName("total_count")
    @Expose
    public Integer totalCount;
    @SerializedName("has_more")
    @Expose
    public Boolean hasMore;
    @SerializedName("cursor")
    @Expose
    public String cursor;

    @Override
    public List<Provider.Metadata> entries() {
        return new ArrayList<Provider.Metadata>(entries);
    }

    @Override
    public String cursor() {
        return cursor;
    }

    @Override
    public boolean hasMore() {
        return hasMore;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
