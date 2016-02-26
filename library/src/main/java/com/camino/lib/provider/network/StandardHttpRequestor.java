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
import java.net.Proxy;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * {@link   com.seagate.alto.provider.network.HttpRequestor} implementation that uses Java's standard library
 * {@link HttpsURLConnection}.  If you just want a connection with the
 * default settings, use the predefined {@link #Instance}.
 * <p/>
 * <p>
 * If you want to customize the way the connection is configured, create a
 * subclass that overrides {@link #configureConnection}.
 * </p>
 */
public class StandardHttpRequestor extends com.dropbox.core.http.HttpRequestor {
    /**
     * A thread-safe instance of {@code StandardHttpRequestor} that connects directly
     * (as opposed to using a proxy).
     */
    public static final StandardHttpRequestor Instance = new StandardHttpRequestor();
    private final Proxy proxy;

    /**
     * Creates an instance that uses a direct HTTP connection (as opposed to
     * using a proxy).
     */
    public StandardHttpRequestor() {
        this(Proxy.NO_PROXY);
    }

    /**
     * Creates an instance that connects through the given proxy.
     */
    public StandardHttpRequestor(Proxy proxy) {
        this.proxy = proxy;
    }

    private static Response toResponse(HttpsURLConnection conn) throws IOException {
        int responseCode = conn.getResponseCode();
        InputStream bodyStream;
        if (responseCode >= 400) {
            bodyStream = conn.getErrorStream();
        } else {
            bodyStream = conn.getInputStream();
        }
        return new Response(conn.getResponseCode(), bodyStream, conn.getHeaderFields());
    }

    @Override
    public Response doGet(String url, Iterable<Header> headers) throws IOException {
        HttpsURLConnection conn = prepRequest(url, headers);
        conn.setRequestMethod("GET");
        conn.connect();
        return toResponse(conn);
    }

    @Override
    public Uploader startPost(String url, Iterable<Header> headers) throws IOException {
        HttpsURLConnection conn = prepRequest(url, headers);
        conn.setRequestMethod("POST");
        return new Uploader(conn);
    }

    @Override
    public Uploader startPut(String url, Iterable<Header> headers) throws IOException {
        HttpsURLConnection conn = prepRequest(url, headers);
        conn.setRequestMethod("PUT");
        return new Uploader(conn);
    }

    /**
     * Can be overridden to configure the underlying {@link HttpsURLConnection} used to make
     * network requests.  If you override this method, you should probably call
     * {@code super.configureConnection(conn)} in your overridden method.
     */
    protected void configureConnection(HttpsURLConnection conn) throws IOException {
    }

    private HttpsURLConnection prepRequest(String url, Iterable<Header> headers) throws IOException {
        URL urlObject = new URL(url);
        HttpsURLConnection conn = (HttpsURLConnection) urlObject.openConnection(this.proxy);

        SSLConfig.apply(conn);
        conn.setConnectTimeout(DefaultTimeoutMillis);
        conn.setReadTimeout(DefaultTimeoutMillis);
        conn.setUseCaches(false);
        conn.setAllowUserInteraction(false);

        configureConnection(conn);

        for (Header header : headers) {
            conn.addRequestProperty(header.key, header.value);
        }

        return conn;
    }

    private static class Uploader extends com.dropbox.core.http.HttpRequestor.Uploader {
        private /*@Nullable*/ HttpsURLConnection conn;

        public Uploader(HttpsURLConnection conn)
                throws IOException {
            super(getOutputStream(conn));
            conn.connect();
            this.conn = conn;
        }

        private static OutputStream getOutputStream(HttpsURLConnection conn)
                throws IOException {
            conn.setDoOutput(true);
            return conn.getOutputStream();
        }

        @Override
        public void abort() {
            HttpsURLConnection conn = this.conn;
            if (conn == null) {
                throw new IllegalStateException("Can't abort().  Uploader already closed.");
            }
            conn.disconnect();
        }

        @Override
        public void close() {
            HttpsURLConnection conn = this.conn;
            if (conn == null) return;
            conn.disconnect();
        }

        @Override
        public Response finish() throws IOException {
            HttpsURLConnection conn = this.conn;
            if (conn == null) {
                throw new IllegalStateException("Can't finish().  Uploader already closed.");
            }
            this.conn = null;
            return toResponse(conn);
        }
    }
}
