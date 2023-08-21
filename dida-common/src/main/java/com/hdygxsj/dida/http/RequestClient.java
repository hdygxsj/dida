/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hdygxsj.dida.http;

import com.alibaba.fastjson.JSON;
import com.hdygxsj.dida.constants.Constants;
import com.hdygxsj.dida.tools.Result;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class RequestClient {

    private OkHttpClient httpClient = null;

    public RequestClient() {
        this.httpClient = new OkHttpClient();
    }

    public RequestClient(OkHttpClient okHttpClient) {
        this.httpClient = okHttpClient;
    }


    @SneakyThrows
    public Result get(String url, Map<String, String> headers, Map<String, Object> params) {
        Headers headersBuilder = new Headers.Builder().build();
        if (headers != null) {
            headersBuilder = Headers.of(headers);
        }
        log.info("GET request to {}, Headers: {}", url, headersBuilder);
        Request request = new Request.Builder()
            .url(url)
            .headers(headersBuilder)
            .get()
            .build();

        Response response = this.httpClient.newCall(request).execute();

        Result responseData = null;
        int responseCode = response.code();
        if (response.body() != null) {
            responseData = JSON.parseObject(response.body().string(),Result.class);
        }
        response.close();
        return responseData;
    }

    @SneakyThrows
    public <T> T get(String url, Map<String, String> headers, Map<String, Object> params,Class<T> clazz) {
        Headers headersBuilder = new Headers.Builder().build();
        if (headers != null) {
            headersBuilder = Headers.of(headers);
        }
        log.info("GET request to {}, Headers: {}", url, headersBuilder);
        Request request = new Request.Builder()
                .url(url)
                .headers(headersBuilder)
                .get()
                .build();

        Response response = this.httpClient.newCall(request).execute();
        T responseData = null;
        int responseCode = response.code();
        if (response.body() != null) {
            if(clazz == String.class){
                return (T) response.body().string();
            }
            responseData = JSON.parseObject(response.body().string(),clazz);
        }
        response.close();
        return  responseData;
    }

    public static String getParams(Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        if (params.size() > 0) {
            for (Map.Entry<String, Object> item : params.entrySet()) {
                Object value = item.getValue();
                if (Objects.nonNull(value)) {
                    sb.append(Constants.AND_MARK);
                    sb.append(item.getKey());
                    sb.append(Constants.EQUAL_MARK);
                    sb.append(value);
                }
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    @SneakyThrows
    public  Result post(String url, Map<String, String> headers, Map<String, Object> params) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put("Content-Type", Constants.REQUEST_CONTENT_TYPE);
        Headers headersBuilder = Headers.of(headers);
        RequestBody requestBody = FormBody.create(MediaType.parse(Constants.REQUEST_CONTENT_TYPE), getParams(params));
        log.info("POST request to {}, Headers: {}, Params: {}", url, headersBuilder, params);
        Request request = new Request.Builder()
            .headers(headersBuilder)
            .url(url)
            .post(requestBody)
            .build();
        Response response = this.httpClient.newCall(request).execute();
        int responseCode = response.code();
        Result responseData = null;
        if (response.body() != null) {
            responseData = JSON.parseObject(response.body().string(), Result.class);
        }
        response.close();
        return responseData;
    }

    @SneakyThrows
    public  <T> T post(String url, Map<String, String> headers, Map<String, Object> params,Class<T> clazz) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put("Content-Type", Constants.REQUEST_CONTENT_TYPE);
        Headers headersBuilder = Headers.of(headers);
        RequestBody requestBody = FormBody.create(MediaType.parse(Constants.REQUEST_CONTENT_TYPE), getParams(params));
        log.info("POST request to {}, Headers: {}, Params: {}", url, headersBuilder, params);
        Request request = new Request.Builder()
                .headers(headersBuilder)
                .url(url)
                .post(requestBody)
                .build();
        Response response = this.httpClient.newCall(request).execute();
        int responseCode = response.code();
        T responseData = null;
        if (response.body() != null) {
            if(clazz == String.class){
                return (T) response.body().string();
            }
            responseData = JSON.parseObject(response.body().string(), clazz);
        }
        response.close();
        return responseData;
    }

    private static String addUrlParams(Map<String, Object> requestParams, @NonNull String url) {
        if (requestParams == null) {
            return url;
        }

        HttpUrl httpUrl = HttpUrl.parse(url);
        if (httpUrl == null) {
            throw new IllegalArgumentException(String.format("url: %s is invalid", url));
        }
        HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
        for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
            urlBuilder.addQueryParameter(entry.getKey(), entry.getValue().toString());
        }
        return urlBuilder.toString();
    }
    @SneakyThrows
    public  <T> T post(String url, Map<String, String> headers, Map<String, Object> params,Map<String, Object> requestBodyMap,Class<T> clazz) {
        String finalUrl = addUrlParams(params, url);
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put("Content-Type", Constants.REQUEST_CONTENT_TYPE);
        Headers headersBuilder = Headers.of(headers);
        RequestBody requestBody = FormBody.create(MediaType.parse(Constants.REQUEST_CONTENT_TYPE), getParams(requestBodyMap));
        log.info("POST request to {}, Headers: {}, Params: {}", finalUrl, headersBuilder, params);
        Request request = new Request.Builder()
                .headers(headersBuilder)
                .url(finalUrl)
                .post(requestBody)
                .build();
        Response response = this.httpClient.newCall(request).execute();
        T responseData = null;
        if (response.body() != null) {
            if(clazz == String.class){
                return (T) response.body().string();
            }
            responseData = JSON.parseObject(response.body().string(), clazz);
        }
        response.close();
        return responseData;
    }

    @SneakyThrows
    public  String post(String url, Map<String, String> headers, Map<String, Object> params,
                       Map<String, Object> requestBodyMap) {
        String finalUrl = addUrlParams(params, url);
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put("Content-Type", Constants.REQUEST_CONTENT_TYPE);
        Headers headersBuilder = Headers.of(headers);
        RequestBody requestBody = FormBody.create(MediaType.parse(Constants.REQUEST_CONTENT_TYPE), getParams(requestBodyMap));
        log.info("POST request to {}, Headers: {}, Params: {}", finalUrl, headersBuilder, params);
        Request request = new Request.Builder()
                .headers(headersBuilder)
                .url(finalUrl)
                .post(requestBody)
                .build();
        Response response = this.httpClient.newCall(request).execute();
        return response.body().string();
    }

    @SneakyThrows
    public  Result put(String url, Map<String, String> headers, Map<String, Object> params) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put("Content-Type", Constants.REQUEST_CONTENT_TYPE);
        Headers headersBuilder = Headers.of(headers);
        RequestBody requestBody = FormBody.create(MediaType.parse(Constants.REQUEST_CONTENT_TYPE), getParams(params));
        log.info("PUT request to {}, Headers: {}, Params: {}", url, headersBuilder, params);
        Request request = new Request.Builder()
            .headers(headersBuilder)
            .url(url)
            .put(requestBody)
            .build();
        Response response = this.httpClient.newCall(request).execute();
        int responseCode = response.code();
        Result responseData = null;
        if (response.body() != null) {
            responseData = JSON.parseObject(response.body().string(),Result.class);
        }
        response.close();
        return responseData;
    }


    @SneakyThrows
    public  Result delete(String url, Map<String, String> headers, Map<String, Object> params) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put("Content-Type", Constants.REQUEST_CONTENT_TYPE);

        Headers headersBuilder = Headers.of(headers);

        log.info("DELETE request to {}, Headers: {}, Params: {}", url, headersBuilder, params);
        Request request = new Request.Builder()
            .headers(headersBuilder)
            .url(url)
            .delete()
            .build();

        Response response = this.httpClient.newCall(request).execute();
        int responseCode = response.code();
        Result responseData = null;
        if (response.body() != null) {
            responseData = JSON.parseObject(response.body().string(), Result.class);
        }
        response.close();
        return responseData;
    }
}
