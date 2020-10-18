package com.example.smartageliketool.view.main;


import com.example.smartageliketool.data.model.cookie.GetCookieResponse;
import com.example.smartageliketool.data.model.instapost.InstaPostResponse;
import com.example.smartageliketool.data.model.postList.PostEntity;
import com.example.smartageliketool.data.model.token.TokenResponseDto;

import java.util.List;
import java.util.Map;

public interface MainContract {

    interface View {

        void tokenSuccess(TokenResponseDto tokenResponseDto);

        void tokenFailed(Throwable error);


        void validPost(int postId, int actualPostId, String url, Map<String, String> headers, InstaPostResponse instaPostResponse);

        void inValidPost(int postId, int actualPostId, String url, Map<String, String> headers, Throwable error);

        void postListReceivedSuccess(List<PostEntity> postEntities);

        void postListFailed(Throwable error);

        void deActivePostSuccess(int postId, int actualPostId);

        void deActivePostFailed(int postId, int actualPostId, Throwable error);

        void getCookieSuccess(GetCookieResponse getCookieResponse);

        void getCookieFailed(Throwable error);


        void likeApiProcessIsSuccess(int postIndex, int cookieIndex);

        void likeApiProcessIsFailed(int postIndex, int cookieIndex, Throwable error);

        void openWebViewGetLikeHeaders(String link, Map<String, String> cookieHeader);

        void changeIp();


    }

    interface Presenter {
        void onDestroy();

        void getToken(String userName, String password);

        void getPostList(String token);

        void deActivePost(int postId, int actualPostId, String token, String url);

        void getCookie(String token);

        void testPost(int postId, int actualPostId, String url, Map<String, String> headers);

        void like(String url, int postIndex, int cookieIndex, Map<String, String> headers);


    }
}
