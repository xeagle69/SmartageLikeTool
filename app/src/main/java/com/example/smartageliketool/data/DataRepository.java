package com.example.smartageliketool.data;




import com.example.smartageliketool.data.model.cookie.GetCookieResponse;
import com.example.smartageliketool.data.model.instapost.InstaPostResponse;
import com.example.smartageliketool.data.model.like.LikeResponseDto;
import com.example.smartageliketool.data.model.postList.PostEntity;
import com.example.smartageliketool.data.model.token.TokenResponseDto;
import com.example.smartageliketool.data.model.updateCookie.UpdateCookieDto;

import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Url;

public interface DataRepository {

    Single<TokenResponseDto> getToken(String userName, String password);

    Single<InstaPostResponse> testPost( String url, Map<String, String> headers);

    Single<List<PostEntity>> getPostList(String token);

    Single<ResponseBody> deActivePost(String token, String url);

    Single<GetCookieResponse> getCookie(String token);

    Single<GetCookieResponse> updateCookie( String url, String token, UpdateCookieDto updateCookieDto);

    Single<LikeResponseDto> like(String url, Map<String, String> headers);

}
