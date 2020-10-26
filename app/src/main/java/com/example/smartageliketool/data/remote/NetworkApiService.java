package com.example.smartageliketool.data.remote;


import com.example.smartageliketool.data.model.cookie.GetCookieResponse;
import com.example.smartageliketool.data.model.instapost.InstaPostResponse;
import com.example.smartageliketool.data.model.like.LikeResponseDto;
import com.example.smartageliketool.data.model.postList.PostEntity;
import com.example.smartageliketool.data.model.token.TokenDto;
import com.example.smartageliketool.data.model.token.TokenResponseDto;
import com.example.smartageliketool.data.model.updateCookie.UpdateCookieDto;
import com.example.smartageliketool.data.util.RemoteConstants;

import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface NetworkApiService {


    @HTTP(method = "OBTAIN", path = RemoteConstants.GET_TOKEN, hasBody = true)
    Single<TokenResponseDto> getToken(@Body TokenDto tokenDto);

    @GET(RemoteConstants.GET_POSTS)
    Single<List<PostEntity>> getPostList(@Header("Authorization") String token);


    @HTTP(method = "DEACTIVATE")
    Single<ResponseBody> deActivePost(@Header("Authorization") String token, @Url String url);


    @GET(RemoteConstants.GET_COOKIE)
    Single<GetCookieResponse> getCookie(@Header("Authorization") String token);

    @HTTP(method = "DELETE")
    Single<GetCookieResponse> deleteCookie(@Url String url, @Header("Authorization") String token);


    @HTTP(method = "PUT", hasBody = true)
    Single<GetCookieResponse> updateCookie(@Url String url, @Header("Authorization") String token, @Body UpdateCookieDto updateCookieDto);


    @GET
    Single<InstaPostResponse> testPost(@Url String url, @HeaderMap Map<String, String> headerCookies);


    @POST
    Single<LikeResponseDto> like(@Url String url, @HeaderMap Map<String, String> headerCookies);


}
