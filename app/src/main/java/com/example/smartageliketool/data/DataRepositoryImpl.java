package com.example.smartageliketool.data;




import com.example.smartageliketool.data.model.cookie.GetCookieResponse;
import com.example.smartageliketool.data.model.instapost.InstaPostResponse;
import com.example.smartageliketool.data.model.like.LikeResponseDto;
import com.example.smartageliketool.data.model.postList.PostEntity;
import com.example.smartageliketool.data.model.token.TokenDto;
import com.example.smartageliketool.data.model.token.TokenResponseDto;
import com.example.smartageliketool.data.model.updateCookie.UpdateCookieDto;
import com.example.smartageliketool.data.remote.ApiManager;
import com.example.smartageliketool.di.scope.PerActivity;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Single;
import okhttp3.ResponseBody;

@PerActivity
public class DataRepositoryImpl implements DataRepository {

    private ApiManager apiManager;


    @Inject
    public DataRepositoryImpl(ApiManager apiManager) {
        this.apiManager = apiManager;
    }


    @Override
    public Single<TokenResponseDto> getToken(String userName, String password) {

//        return apiManager.getToken(userName,password);
        TokenDto tokenDto = new TokenDto();
        tokenDto.setUserName(userName);
        tokenDto.setPassword(password);
        return apiManager.getToken(tokenDto);
    }

    @Override
    public Single<InstaPostResponse> testPost(String url, Map<String, String> headers) {
        return apiManager.testPost(url,headers);
    }


    @Override
    public Single<List<PostEntity>> getPostList(String token) {
        return apiManager.getPostList(token);
    }

    @Override
    public Single<ResponseBody> deActivePost(String token, String url) {
        return apiManager.deActivePost(token,url);
    }


    @Override
    public Single<GetCookieResponse> getCookie(String token) {
        return apiManager.getCookie(token);
    }

    @Override
    public Single<GetCookieResponse> updateCookie(String url, String token, UpdateCookieDto updateCookieDto) {
        return apiManager.updateCookie(url,token,updateCookieDto);
    }

    @Override
    public Single<GetCookieResponse> deleteCookie(String url, String token) {
        return apiManager.deleteCookie(url,token);
    }


    @Override
    public Single<LikeResponseDto> like(String url, Map<String, String> headers) {
        return apiManager.like(url, headers);
    }
}
