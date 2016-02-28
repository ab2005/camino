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
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SearchResponse implements Provider.SearchResult {

    @SerializedName("matches")
    @Expose
    public List<Match> matches = new ArrayList<Match>();
    @SerializedName("more")
    @Expose
    public Boolean more;
    @SerializedName("start")
    @Expose
    public Long start;
    @SerializedName("num_found")
    @Expose
    public Integer numFound;

    @Override
    public List<Provider.Metadata> matches() {
        ArrayList<Provider.Metadata> list = new ArrayList<>();
        if (matches != null) {
            for (Match item : matches) {
                list.add(item.metadata);
            }
        }
        return list;
    }

    @Override
    public boolean hasMore() {
        return more;
    }

    @Override
    public long start() {
        return start;
    }
}
