package com.example.smartageliketool.view.main;


import android.util.Log;

import com.example.smartageliketool.data.DataRepository;
import com.example.smartageliketool.data.model.post.PostDataBaseEntity;
import com.example.smartageliketool.data.model.updateCookie.UpdateCookieDto;
import com.example.smartageliketool.di.scope.PerActivity;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

@PerActivity
public class MainPresenter implements MainContract.Presenter {

    private DataRepository repository;
    private MainContract.View view;
    private CompositeDisposable compositeDisposable;

    @Inject
    public MainPresenter(DataRepository repository, MainContract.View view) {
        this.view = view;
        this.repository = repository;
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    @Override
    public void getToken(String userName, String password) {
        compositeDisposable.add(repository.getToken(userName, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(TokenResponseDto -> view.tokenSuccess(TokenResponseDto), error -> view.tokenFailed(error)
                ));
    }

    @Override
    public void getPostList(String token) {
        compositeDisposable.add(repository.getPostList(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(postEntities -> view.postListReceivedSuccess(postEntities), error -> view.postListFailed(error)
                ));
    }

    @Override
    public void getPostListFromBetween(String token) {
        compositeDisposable.add(repository.getPostList(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(postEntities -> view.postListReceivedSuccessBetween(postEntities), error -> view.postListFailedBetween(error)
                ));
    }

    @Override
    public void deActivePost(PostDataBaseEntity post, String token, String url) {
        compositeDisposable.add(repository.deActivePost(token, url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ResponseBody -> view.deActivePostSuccess(post), error -> view.deActivePostFailed(post, error)
                ));
    }

    @Override
    public void getCookie(String token) {
        compositeDisposable.add(repository.getCookie(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(GetCookieResponse -> view.getCookieSuccess(GetCookieResponse), error -> view.getCookieFailed(error)
                ));
    }

    @Override
    public void updateCookie(String url, String token, UpdateCookieDto updateCookieDto) {
        compositeDisposable.add(repository.updateCookie(url, token, updateCookieDto)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(GetCookieResponse -> view.updateCookieSuccess(url, updateCookieDto), error -> view.updateCookieFailed(error)
                ));
    }

    @Override
    public void deleteCookie(String url, String token) {
        Integer deleteCookieIndex = Integer.parseInt(url.substring(url.lastIndexOf('/')+1, url.length()));


        compositeDisposable.add(repository.deleteCookie(url, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(GetCookieResponse -> view.deleteCookieSuccess(deleteCookieIndex, url), error -> view.deleteCookieFailed(error)
                ));
    }


    @Override
    public void testPostValidity(PostDataBaseEntity postDataBaseEntity, String url, Map<String, String> headers) {
        String completeUrl = url + "?__a=1";
        Log.d(TAG, "testPostValidity > URL : " + completeUrl);
        compositeDisposable.add(repository.testPost(completeUrl, headers)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(InstaPostResponse -> view.validPost(postDataBaseEntity, InstaPostResponse), error -> view.inValidPost(postDataBaseEntity, error)
                ));
    }

    @Override
    public void testPostforWebView(PostDataBaseEntity postDataBaseEntity, String url, Map<String, String> headers) {
        String completeUrl = url + "?__a=1";
        compositeDisposable.add(repository.testPost(completeUrl, headers)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(InstaPostResponse -> view.checkForWebView(postDataBaseEntity, InstaPostResponse), error -> view.postForWebViewError(postDataBaseEntity, error)
                ));
    }

    @Override
    public void testPostforLikedByMe(PostDataBaseEntity postDataBaseEntity, String url, Map<String, String> headers) {
        String completeUrl = url + "?__a=1";


        compositeDisposable.add(repository.testPost(completeUrl, headers)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(InstaPostResponse -> view.checkForLikedByMe(postDataBaseEntity, InstaPostResponse), error -> view.postForLikedByMeError(postDataBaseEntity, error)
                ));
    }

    @Override
    public void like(PostDataBaseEntity post, String url, Map<String, String> headerCookies) {
        compositeDisposable.add(repository.like(url, headerCookies)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(LikeResponsDto -> view.likeApiProcessIsSuccess(LikeResponsDto, post), error -> view.likeApiProcessIsFailed(post, error)
                ));

    }


}
