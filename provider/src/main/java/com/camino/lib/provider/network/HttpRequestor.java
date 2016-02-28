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

package com.camino.lib.provider.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * An interface that the Provider client library uses to make HTTP requests.
 * If you're fine with the standard Java {@link java.net.HttpURLConnection}
 * implementation, then just use {@link StandardHttpRequestor#Instance}.
 */
public abstract class HttpRequestor {
    /**
     * The default socket connect/read/write timeout.
     */
    public static final int DefaultTimeoutMillis = 20 * 1000;

    public abstract Response doGet(String url, Iterable<Header> headers) throws IOException;

    public abstract Uploader startPost(String url, Iterable<Header> headers) throws IOException;

    public abstract Uploader startPut(String url, Iterable<Header> headers) throws IOException;

    /**
     * A simple structure holding an HTTP header, which is key/value pair.
     * Used with {@link HttpRequestor}.
     */
    public static final class Header {
        public final String key;
        public final String value;

        public Header(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    public static abstract class Uploader {
        public final OutputStream body;

        protected Uploader(OutputStream body) {
            this.body = body;
        }

        public abstract void close();

        public abstract void abort();

        public abstract Response finish() throws IOException;
    }

    public static final class Response {
        public final int statusCode;
        public final InputStream body;
        public final Map<String, ? extends List<String>> headers;

        public Response(int statusCode, InputStream body, Map<String, ? extends List<String>> headers) {
            this.statusCode = statusCode;
            this.body = body;
            this.headers = headers;
        }
    }

}
