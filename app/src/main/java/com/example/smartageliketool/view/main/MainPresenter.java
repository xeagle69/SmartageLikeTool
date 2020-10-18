package com.example.smartageliketool.view.main;


import com.example.smartageliketool.data.DataRepository;
import com.example.smartageliketool.di.scope.PerActivity;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

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
    public void deActivePost(int postId, int actualPostId, String token, String url) {
        compositeDisposable.add(repository.deActivePost(postId, actualPostId, token, url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ResponseBody -> view.deActivePostSuccess(postId, actualPostId), error -> view.deActivePostFailed(postId, actualPostId, error)
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
    public void testPost(int postId, int actualPostId, String url, Map<String, String> headers) {
        compositeDisposable.add(repository.testPost(postId, actualPostId, url, headers)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(InstaPostResponse -> view.validPost(postId, actualPostId, url, headers,InstaPostResponse), error -> view.inValidPost(postId, actualPostId, url, headers, error)
                ));
    }


    @Override
    public void like(String url, int postIndex, int cookieIndex, Map<String, String> headerCookies) {
        //todo deal with action block here !!!
        compositeDisposable.add(repository.like(url, headerCookies)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(LikeResponse -> view.likeApiProcessIsSuccess(postIndex, cookieIndex), error -> view.likeApiProcessIsFailed(postIndex, cookieIndex, error)
                ));

    }


}
