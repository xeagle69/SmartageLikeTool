package com.example.smartageliketool.data.remote;



import com.example.smartageliketool.data.model.cookie.GetCookieResponse;
import com.example.smartageliketool.data.model.instapost.InstaPostResponse;
import com.example.smartageliketool.data.model.like.LikeResponseDto;
import com.example.smartageliketool.data.model.postList.PostEntity;
import com.example.smartageliketool.data.model.token.TokenDto;
import com.example.smartageliketool.data.model.token.TokenResponseDto;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import okhttp3.ResponseBody;

@Singleton
public class ApiManager {

    private NetworkApiService apiService;

    @Inject
    public ApiManager(NetworkApiService apiService) {
        this.apiService = apiService;
    }


    public Single<TokenResponseDto> getToken(TokenDto tokenDto) {
        return apiService.getToken(tokenDto);
    }

    public Single<List<PostEntity>> getPostList(String token) {
        return apiService.getPostList(token);
    }

    public Single<ResponseBody> deActivePost(String token, String url) {
        return apiService.deActivePost(token, url);
    }

    public Single<GetCookieResponse> getCookie(String token) {
        return apiService.getCookie(token);
    }

    public Single<InstaPostResponse> testPost(String url, Map<String, String> headers) {
        return apiService.testPost(url, headers);
    }


    public Single<LikeResponseDto> like(String url, Map<String, String> headers) {
        return apiService.like(url, headers);
    }


}
