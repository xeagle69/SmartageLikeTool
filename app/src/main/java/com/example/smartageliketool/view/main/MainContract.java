package com.example.smartageliketool.view.main;


import com.example.smartageliketool.data.model.cookie.GetCookieResponse;
import com.example.smartageliketool.data.model.instapost.InstaPostResponse;
import com.example.smartageliketool.data.model.like.LikeResponseDto;
import com.example.smartageliketool.data.model.post.PostDataBaseEntity;
import com.example.smartageliketool.data.model.postList.PostEntity;
import com.example.smartageliketool.data.model.token.TokenResponseDto;
import com.example.smartageliketool.data.model.updateCookie.UpdateCookieDto;

import java.util.List;
import java.util.Map;

public interface MainContract {

    interface View {

        void tokenSuccess(TokenResponseDto tokenResponseDto);

        void tokenFailed(Throwable error);

        void postListReceivedSuccess(List<PostEntity> postEntities);

        void postListFailed(Throwable error);


        void getCookieSuccess(GetCookieResponse getCookieResponse);

        void getCookieFailed(Throwable error);


        void updateCookieSuccess(String url,UpdateCookieDto updateCookieDto);

        void updateCookieFailed(Throwable error);


        void deleteCookieSuccess(Integer cookieId,String url);

        void deleteCookieFailed(Throwable error);

        void changeIp();

        void validPost(PostDataBaseEntity postDataBaseEntity, InstaPostResponse instaPostResponse);

        void inValidPost(PostDataBaseEntity postDataBaseEntity, Throwable error);

        void checkForWebView(PostDataBaseEntity postDataBaseEntity, InstaPostResponse instaPostResponse);

        void postForWebViewError(PostDataBaseEntity postDataBaseEntity, Throwable error);

        void checkForLikedByMe(PostDataBaseEntity postDataBaseEntity, InstaPostResponse instaPostResponse);

        void postForLikedByMeError(PostDataBaseEntity postDataBaseEntity, Throwable error);

        void likeApiProcessIsSuccess(LikeResponseDto likeResponseDto, PostDataBaseEntity post);

        void likeApiProcessIsFailed(PostDataBaseEntity post, Throwable error);

        void deActivePostSuccess(PostDataBaseEntity post);

        void deActivePostFailed(PostDataBaseEntity post, Throwable error);

        void postListReceivedSuccessBetween(List<PostEntity> postEntities);

        void postListFailedBetween(Throwable error);
    }

    interface Presenter {
        void onDestroy();

        void getToken(String userName, String password);

        void getPostList(String token);

        void getPostListFromBetween(String token);

        void deActivePost(PostDataBaseEntity post, String token, String url);

        void getCookie(String token);

        void updateCookie(String url, String token, UpdateCookieDto updateCookieDto);

        void deleteCookie(String url, String token);

        void testPostValidity(PostDataBaseEntity postDataBaseEntity, String url, Map<String, String> headers);

        void testPostforWebView(PostDataBaseEntity postDataBaseEntity, String url, Map<String, String> headerss);

        void testPostforLikedByMe(PostDataBaseEntity postDataBaseEntity, String url, Map<String, String> headers);

        void like(PostDataBaseEntity post, String url, Map<String, String> headerCookies);


    }
}
