package com.example.smartageliketool.data;




import com.example.smartageliketool.data.model.cookie.GetCookieResponse;
import com.example.smartageliketool.data.model.instapost.InstaPostResponse;
import com.example.smartageliketool.data.model.like.LikeResponseDto;
import com.example.smartageliketool.data.model.postList.PostEntity;
import com.example.smartageliketool.data.model.token.TokenResponseDto;

import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import okhttp3.ResponseBody;

public interface DataRepository {

    Single<TokenResponseDto> getToken(String userName, String password);

    Single<InstaPostResponse> testPost(int postId, int actualPostId, String url, Map<String, String> headers);

    Single<List<PostEntity>> getPostList(String token);

    Single<ResponseBody> deActivePost(int postId, int actualPostId, String token, String url);

    Single<GetCookieResponse> getCookie(String token);

    Single<LikeResponseDto> like(String url, Map<String, String> headers);

}
