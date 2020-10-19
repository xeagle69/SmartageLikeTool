package com.example.smartageliketool.view.main;


import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.smartageliketool.BaseActivity;
import com.example.smartageliketool.BaseApplication;
import com.example.smartageliketool.R;
import com.example.smartageliketool.data.model.UserAccountStatus;
import com.example.smartageliketool.data.model.cookie.GetCookieResponse;
import com.example.smartageliketool.data.model.ignoreTable.IgnoreTable;
import com.example.smartageliketool.data.model.instapost.InstaPostResponse;
import com.example.smartageliketool.data.model.like.LikeResponseDto;
import com.example.smartageliketool.data.model.likeTable.LikeTable;
import com.example.smartageliketool.data.model.post.PostDataBaseEntity;
import com.example.smartageliketool.data.model.postList.PostEntity;
import com.example.smartageliketool.data.model.token.TokenResponseDto;
import com.example.smartageliketool.data.model.updateCookie.UpdateCookieDto;
import com.example.smartageliketool.data.sqlite.DatabaseModule;
import com.example.smartageliketool.data.util.PrefManager;
import com.example.smartageliketool.data.util.RemoteConstants;
import com.example.smartageliketool.di.views.main.MainActivityModule;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainContract.View {

    public static final String TAG = "xeagle69_MainActivity >>";
    private CountDownTimer changeIp;
    private final String COMMAND_FLIGHT_MODE_1 = "settings put global airplane_mode_on";
    private final String COMMAND_FLIGHT_MODE_2 = "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state";
    private PrefManager prefManager;
    private SharedPreferences prefs;
    private String currentIpAddress;
    private UserAccountStatus user_status;
    private Boolean shouldOpenWebView;
    private Integer cookieGetCount;
    private Integer totalLikeCount;
    private Map<String, String> headerMap;
    private Boolean isPostLikeStarted = false;
    private Boolean isPostLikeChecked = false;
    private Boolean isUserStatusChanged = false;
    private int webViewHeight;


    private GetCookieResponse publicLastCookie = null;

    //    @BindView(R.id.txt_main_activity_label)
//    TextView txtActivityLabel;
//
    @BindView(R.id.txt_main_activity_current_like_count_value)
    TextView txtCurrentLikeTv;


    @BindView(R.id.txt_main_activity_changing_ip_status_label)
    TextView txtChangingIp;


    @BindView(R.id.webview_main_activity)
    WebView webView;

    @Inject
    MainContract.Presenter presenter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (RemoteConstants.isRootGiven()) {
            Toast.makeText(MainActivity.this, "it is Root !!!", Toast.LENGTH_LONG).show();
            //load dagger
            initialDagger();
            //load prefManager
            prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefManager = new PrefManager(prefs);
            //show current ip address
            String ip = getMobileIPAddress(true);
            currentIpAddress = ip;
//            txtActivityLabel.setText("IP : " + ip);
            user_status = UserAccountStatus.OK;
            totalLikeCount = 0;
            presenter.getToken("super-admin", "a8k9p763gYv2RBq");
        } else {
            Toast.makeText(MainActivity.this, "it is NOT Root !!!", Toast.LENGTH_LONG).show();
        }


    }

    private void initialDagger() {
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        (((BaseApplication) getApplication()).getAppComponent())
                .mainActivitySubcomponent(new MainActivityModule(this))
                .inject(this);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }


    @SuppressLint("LongLogTag")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void tokenSuccess(TokenResponseDto tokenResponseDto) {
        prefManager.saveToken(tokenResponseDto.getToken());
        Log.d(TAG, "Token Saved");
        //***************************************************************************************************************
        headerMap = new HashMap<>();
        shouldOpenWebView = true;
        cookieGetCount = 0;

        presenter.getPostList(prefManager.loadToken());
    }

    @SuppressLint("LongLogTag")
    @Override
    public void tokenFailed(Throwable error) {
        Toast.makeText(MainActivity.this, "Token NOT Received !!!", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Token NOT Received");
    }

    @SuppressLint("LongLogTag")
    @Override
    public void postListReceivedSuccess(List<PostEntity> postEntities) {
        Log.d(TAG, "Post list Received");
        DatabaseModule dataBase = DatabaseModule.getInstance(MainActivity.this);
        for (int i = 0; i < postEntities.size(); i++) {
            PostDataBaseEntity postDataBaseEntity = new PostDataBaseEntity(postEntities.get(i).getId(), postEntities.get(i).getLink(), postEntities.get(i).getMediaId());
            dataBase.postTableDao().insertPost(postDataBaseEntity);
        }
        if (publicLastCookie == null)
            presenter.getCookie(prefManager.loadToken());
        else
            likeMainFunction(getPostForCookie(publicLastCookie.getId()));

    }

    @SuppressLint("LongLogTag")
    @Override
    public void postListFailed(Throwable error) {
        Toast.makeText(MainActivity.this, "Post list NOT Received !!!", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Post list NOT Received");

    }

    @SuppressLint("LongLogTag")
    public void getCookieSuccess(GetCookieResponse getCookieResponse) {
        Log.d(TAG, "Cookie Received");
        if (publicLastCookie != null) {
            deleteCookieInfo(publicLastCookie.getId());
        }
        isUserStatusChanged = false;
        user_status = UserAccountStatus.OK;
        publicLastCookie = getCookieResponse;
        if (cookieGetCount == 5) {
            changeIp();
        } else {
            headerMap.put("cookie", getCookieResponse.getCookie());
            headerMap.put("User-Agent", getCookieResponse.getUserAgent());
            PostDataBaseEntity post = getPostForCookie(getCookieResponse.getId());
            presenter.testPostValidity(post, post.getLink(), headerMap);
        }
        cookieGetCount++;
    }

    private void deleteCookieInfo(int cookie_id) {
        DatabaseModule databaseModule = DatabaseModule.getInstance(this);
        List<LikeTable> cookieLikes = databaseModule.likeTableDao().getLikesByCookie(cookie_id);
        List<Integer> postIds = new ArrayList<>();
        for (LikeTable likeTable : cookieLikes) {
            postIds.add(likeTable.getPostId());
        }
        databaseModule.likeTableDao().deleteCookieId(cookie_id);
        databaseModule.ignoreTableDao().deleteCookieId(cookie_id);
        for (Integer post_id : postIds) {
            databaseModule.postTableDao().deletePostById(post_id);
        }
    }


    @SuppressLint("LongLogTag")
    @Override
    public void getCookieFailed(Throwable error) {
        Toast.makeText(MainActivity.this, "Cookie NOT Received !!!", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Cookie NOT Received");
    }

    @Override
    public void updateCookieSuccess(String url, UpdateCookieDto updateCookieDto) {

    }

    @Override
    public void updateCookieFailed(Throwable error) {

    }


    @SuppressLint("LongLogTag")
    @Override
    public void likeApiProcessIsSuccess(LikeResponseDto likeResponseDto, PostDataBaseEntity post) {
        Log.d(TAG, "likeApiProcessIsSuccess");
        totalLikeCount++;
        txtCurrentLikeTv.setText(totalLikeCount + "");
        DatabaseModule databaseModule = DatabaseModule.getInstance(MainActivity.this);
        databaseModule.likeTableDao().insertLikeTableEntity(new LikeTable(publicLastCookie.getId(), post.getId()));
        likeMainFunction(getPostForCookie(publicLastCookie.getId()));
    }


    @Override
    public void likeApiProcessIsFailed(PostDataBaseEntity post, Throwable error) {
        DatabaseModule databaseModule = DatabaseModule.getInstance(MainActivity.this);
        databaseModule.likeTableDao().insertLikeTableEntity(new LikeTable(publicLastCookie.getId(), post.getId()));
        likeMainFunction(getPostForCookie(publicLastCookie.getId()));
    }

    @Override
    public void deActivePostSuccess(PostDataBaseEntity post) {
        likeMainFunction(getPostForCookie(publicLastCookie.getId()));
    }

    @Override
    public void deActivePostFailed(PostDataBaseEntity post, Throwable error) {
        System.out.println("Deactivation failed");
        likeMainFunction(getPostForCookie(publicLastCookie.getId()));
    }

    @Override
    public void postListReceivedSuccessBetween(List<PostEntity> postEntities) {
        DatabaseModule dataBase = DatabaseModule.getInstance(MainActivity.this);
        for (int i = 0; i < postEntities.size(); i++) {
            PostDataBaseEntity postDataBaseEntity = new PostDataBaseEntity(postEntities.get(i).getId(), postEntities.get(i).getLink(), postEntities.get(i).getMediaId());
            dataBase.postTableDao().insertPost(postDataBaseEntity);
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void postListFailedBetween(Throwable error) {
        Toast.makeText(MainActivity.this, "Post list NOT Received !!!", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Post list NOT Received");
    }


    private PostDataBaseEntity getPostForCookie(int cookieId) {

        DatabaseModule databaseModule = DatabaseModule.getInstance(MainActivity.this);
        int numOfCookieLike = databaseModule.likeTableDao().getCountByCookie(publicLastCookie.getId());
        int numOfCookieIngnore = databaseModule.ignoreTableDao().getCountByCookie(publicLastCookie.getId());

        if (numOfCookieIngnore + numOfCookieLike > 10) {
            return null;
        } else {

            int post_size = databaseModule.postTableDao().getPostTableSize();
            System.out.println("Post size : " + post_size);
            if (post_size < 20) {
                presenter.getPostListFromBetween(prefManager.loadToken());
            }

            List<PostDataBaseEntity> tempDistinctsPost = new ArrayList<>();

            List<String> distinctsMediaIds = databaseModule.postTableDao().getDistinctPost();


            List<IgnoreTable> ignoreTableList = databaseModule.ignoreTableDao().getIgnoresByCookie(cookieId);
            List<LikeTable> likeTableList = databaseModule.likeTableDao().getLikesByCookie(cookieId);


            for (String mediaId : distinctsMediaIds) {
                List<PostDataBaseEntity> mediaIdPosts = databaseModule.postTableDao().getPostByMediaId(mediaId);

                boolean isLike = false;
                boolean isIgnore = false;
                PostDataBaseEntity postTemp = new PostDataBaseEntity();
                for (PostDataBaseEntity post : mediaIdPosts) {

                    for (IgnoreTable item : ignoreTableList) {
                        if (post.getId() == item.getPostId()) {
                            isIgnore = true;
                            postTemp = post;
                            break;
                        }
                    }

                    for (LikeTable item : likeTableList) {
                        if (post.getId() == item.getPostId()) {
                            isLike = true;
                            postTemp = post;
                            break;
                        }
                    }


                }

                if (isLike == false && isIgnore == false) {
                    for (PostDataBaseEntity finalPost : mediaIdPosts) {
                        if (finalPost.getId() != postTemp.getId()) {
                            tempDistinctsPost.add(finalPost);
                            break;
                        }
                    }

                }

            }

            if (tempDistinctsPost.size() > 0) {
                System.out.println("getPostForCookie post is " + tempDistinctsPost.get(0).getLink());
                return tempDistinctsPost.get(0);
            } else {
                return null;
            }
        }

    }


    @SuppressLint("LongLogTag")
    private void likeMainFunction(PostDataBaseEntity post) {
        if (post != null) {
            if (shouldOpenWebView) {
                Log.d(TAG, "likeMainFunction - shouldOpenWebView");
                Log.d(TAG, "likeMainFunction " + post.getLink());
                //load webView
                //****************************************************************************************************************************
                if (webView != null) {
                    user_status = UserAccountStatus.OK;
                    webView.clearHistory();
                    webView.clearCache(true);
                    CookieManager.getInstance().removeAllCookies(null);
                    CookieManager cookieMgr = CookieManager.getInstance();
                    cookieMgr.removeAllCookies(null);
                    webView.getSettings().setDomStorageEnabled(true);
                    webView.getSettings().setLoadsImagesAutomatically(false);
                    webView.getSettings().setJavaScriptEnabled(true);


                    webView.setWebViewClient(new WebViewClient() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            Log.d(TAG, "likeMainFunction - shouldOpenWebView - onPageFinished - url >>>  " + url);
                            webViewHeight = view.getContentHeight();
                            clickCookie(webView);
                            super.onPageFinished(view, url);
                        }


                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Nullable
                        @Override
                        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                            if (!isUserStatusChanged) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        checkAccountStatus(webView);


                                        if (user_status != UserAccountStatus.OK) {
                                            isUserStatusChanged = true;
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
//                                                    webView.stopLoading();
                                                    webView.loadUrl(RemoteConstants.BLANK_LINK);
                                                    shouldOpenWebView = true;
                                                    presenter.getCookie(prefManager.loadToken());
                                                }
                                            });
                                        }
                                    }
                                });
                            }


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (request.getUrl().getPath() != null) {
                                        if (request.getUrl().getPath().contains("web/likes")) {
                                            extractHeaderParams(request.getRequestHeaders(), post);
                                            webView.loadUrl(RemoteConstants.BLANK_LINK);
                                            presenter.testPostforWebView(post, post.getLink(), headerMap);
                                        }
                                    }
                                }

                            });


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!isPostLikeChecked) {
                                        webView.evaluateJavascript(RemoteConstants.IS_POST_AVALABLE_SCRIPT,
                                                new ValueCallback<String>() {
                                                    @Override
                                                    public void onReceiveValue(String html) {

                                                        if (html.contains("Save") && html.contains("Share Post") && html.contains("More options")) {
                                                            isPostLikeStarted = true;
                                                            isPostLikeChecked = true;
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });


                            if (isPostLikeStarted) {
                                isPostLikeStarted = false;
                                isPostLikeChecked = false;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        likePost(webView);
                                    }
                                });
                            }


                            return super.shouldInterceptRequest(view, request);

                        }

                    });
                    String[] cookiesList = publicLastCookie.getCookie().trim().split(RemoteConstants.SEMICOLON_PATTERN);
                    for (String ccookie : cookiesList) {
                        CookieManager.getInstance().setCookie(RemoteConstants.INSTAGRAM_HOME_PLUS, ccookie.trim());
                    }
                    webView.loadUrl(post.getLink());
                }

                //****************************************************************************************************************************

            } else {
                Log.d(TAG, "likeMainFunction - likeViaApi");
                //like via api
                StringBuilder urlBuilder = new StringBuilder();
                urlBuilder.append("https://www.instagram.com/web/likes/");
                urlBuilder.append(post.getMediaId().trim());
                urlBuilder.append("/like/");
                Log.d(TAG, "likeMainFunction - likeViaApi - url > " + urlBuilder.toString());
                headerMap.put("x-csrftoken", prefManager.loadCfrtoken());
                headerMap.put("x-ig-app-id", prefManager.loadInstagramAppId());
                headerMap.put("x-ig-www-claim", prefManager.loadIgClaim());
                headerMap.put("x-instagram-ajax", prefManager.loadInstagramAjax());
                headerMap.put("x-requested-with", "XMLHttpRequest");
                presenter.like(post, urlBuilder.toString(), headerMap);
            }
        } else {
            shouldOpenWebView = true;
            presenter.getCookie(prefManager.loadToken());
        }
    }


    private void clickCookie(WebView webView) {
        webView.evaluateJavascript(RemoteConstants.IS_POST_AVALABLE_SCRIPT,
                new ValueCallback<String>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onReceiveValue(String html) {
                        Log.d(TAG, " clickCookie onReceiveValue - > loadUrl(RemoteConstants.CLICK_COOKIE_SCRIPT)");
                        webView.loadUrl(RemoteConstants.CLICK_COOKIE_SCRIPT);
                    }
                });
    }

    @SuppressLint("LongLogTag")
    public void extractHeaderParams(Map<String, String> headerMap, PostDataBaseEntity postDataBaseEntity) {
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            if (entry.getKey().equalsIgnoreCase("x-ig-www-claim"))
                if (entry.getValue() != null) {
                    Log.d(TAG, "likeMainFunction - shouldOpenWebView - shouldInterceptRequest - x-ig-www-claim >>>  " + entry.getValue());
                    prefManager.saveIgClaim(entry.getValue());
                }
            if (entry.getKey().equalsIgnoreCase("x-instagram-ajax"))
                if (entry.getValue() != null) {
                    Log.d(TAG, "likeMainFunction - shouldOpenWebView - shouldInterceptRequest - x-instagram-ajax >>>  " + entry.getValue());
                    prefManager.saveInstagramAjax(entry.getValue());
                }
            if (entry.getKey().equalsIgnoreCase("x-csrftoken"))
                if (entry.getValue() != null) {
                    Log.d(TAG, "likeMainFunction - shouldOpenWebView - shouldInterceptRequest - x-csrftoken >>>  " + entry.getValue());
                    prefManager.saveCfrtoken(entry.getValue());
                }
            if (entry.getKey().equalsIgnoreCase("x-ig-app-id"))
                if (entry.getValue() != null) {
                    Log.d(TAG, "likeMainFunction - shouldOpenWebView - shouldInterceptRequest - x-ig-app-id >>>  " + entry.getValue());
                    prefManager.saveInstagramAppId(entry.getValue());
                }
        }


        Log.d(TAG, "update Cookie");

        StringBuilder urlStringBuilder = new StringBuilder();
        urlStringBuilder.append(RemoteConstants.API_BASE);
        urlStringBuilder.append("valid_cookies/");
        urlStringBuilder.append(publicLastCookie.getId());

        UpdateCookieDto updateCookieDto = new UpdateCookieDto(
                prefManager.loadIgClaim(),
                prefManager.loadInstagramAjax(),
                prefManager.loadCfrtoken(),
                prefManager.loadInstagramAppId()
        );
        presenter.updateCookie(urlStringBuilder.toString(), prefManager.loadToken(), updateCookieDto);

        //TODO update 4 parameter for cookie
    }

    public void checkAccountStatus(WebView webView) {
        webView.evaluateJavascript(RemoteConstants.CHECK_ACCOUNT_STATUS_SCRIPT,
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String html) {

                        if (html.contains(RemoteConstants.LOGIN_PLUS_HINT)) {
                            user_status = UserAccountStatus.PAUSED_BY_LOGIN_NEEDED;
                        }

                        if (html.contains(RemoteConstants.COOKIE_ACCEPT_HINT)) {
                            user_status = UserAccountStatus.PAUSED_BY_COOKIE_ACCEPT_NEEDED;
                        }

                    }
                });
    }

    @SuppressLint("LongLogTag")
    private void likePost(WebView webView) {
        Log.d(TAG, "likeMainFunction - shouldOpenWebView - actual - likePost - process ");
        webView.scrollTo(0, (int) (webViewHeight * getResources().getDisplayMetrics().density));
        webView.evaluateJavascript("(function(){return window.document.body.outerHTML})();",
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String html) {
                        if (user_status == UserAccountStatus.OK && html.contains("Like")) {
                            final Handler sendText = new Handler();
                            sendText.postDelayed(new Runnable() {
                                @RequiresApi(api = Build.VERSION_CODES.N)
                                @Override
                                public void run() {
                                    webView.loadUrl("javascript:(function() { " +
                                            "document.querySelector('[aria-label=\"Like\"]').parentElement.click()})()");
                                }
                            }, 1000);
                        }
                    }
                });
    }


    //**********************************************************************************************
    //change ip logic start

    @Override
    public void changeIp() {
        changeIp = new CountDownTimer(60000, 1000) {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onTick(long millisUntilFinished) {


                if (millisUntilFinished < 54000 && millisUntilFinished > 53000) {
                    try {
                        setFlightMode(MainActivity.this);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                if (millisUntilFinished < 44000 && millisUntilFinished > 43000) {
                    try {
                        int enabled = isFlightModeEnabled(MainActivity.this) ? 0 : 1;
                        if (enabled == 0)
                            setFlightMode(MainActivity.this);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                if (millisUntilFinished < 43000)
                    if (isNetworkAvailable()) {
                        this.onFinish();
                    }
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onFinish() {
                if (isNetworkAvailable()) {

                    //show current ip

                    String ip = getMobileIPAddress(true);
                    if (ip.equals(currentIpAddress)) {
                        txtChangingIp.setVisibility(View.VISIBLE);
                        changeIp();
                    } else {
                        txtChangingIp.setVisibility(View.INVISIBLE);
                        currentIpAddress = ip;
//                        txtActivityLabel.setText("IP : " + ip);
                        //ip has been changed , go for next step
                        cookieGetCount = 0;
                        headerMap.put("cookie", publicLastCookie.getCookie());
                        headerMap.put("User-Agent", publicLastCookie.getUserAgent());
                        PostDataBaseEntity post = getPostForCookie(publicLastCookie.getId());
                        presenter.testPostValidity(post, post.getLink(), headerMap);
                    }
                    this.cancel();
                } else {
                    this.cancel();
                    this.start();
                }
            }
        };
        changeIp.start();
    }

    @Override
    public void validPost(PostDataBaseEntity postDataBaseEntity, InstaPostResponse instaPostResponse) {
        if (instaPostResponse.getGraphql().getShortcodeMedia().getViewerHasLiked()) {
            DatabaseModule databaseModule = DatabaseModule.getInstance(this);
            databaseModule.ignoreTableDao().insertIgnoreTableEntity(new IgnoreTable(publicLastCookie.getId(), postDataBaseEntity.getId()));
            PostDataBaseEntity post = getPostForCookie(publicLastCookie.getId());
            presenter.testPostValidity(post, post.getLink(), headerMap);
        } else {
            likeMainFunction(postDataBaseEntity);
        }
    }

    @Override
    public void inValidPost(PostDataBaseEntity postDataBaseEntity, Throwable error) {
        DatabaseModule databaseModule = DatabaseModule.getInstance(MainActivity.this);
        databaseModule.postTableDao().deletePost(postDataBaseEntity);
        presenter.deActivePost(postDataBaseEntity, prefManager.loadToken(), RemoteConstants.API_BASE + "cookie_posts/" + postDataBaseEntity.getActualId());
    }

    @Override
    public void checkForWebView(PostDataBaseEntity postDataBaseEntity, InstaPostResponse instaPostResponse) {
        if (instaPostResponse.getGraphql().getShortcodeMedia().getViewerHasLiked()) {
            DatabaseModule databaseModule = DatabaseModule.getInstance(this);
            databaseModule.likeTableDao().insertLikeTableEntity(new LikeTable(publicLastCookie.getId(), postDataBaseEntity.getId()));
            shouldOpenWebView = false;
            totalLikeCount++;
            txtCurrentLikeTv.setText(totalLikeCount + "");
            PostDataBaseEntity post = getPostForCookie(publicLastCookie.getId());
            presenter.testPostValidity(post, post.getLink(), headerMap);
        } else {
            likeMainFunction(null);
        }
    }

    @Override
    public void postForWebViewError(PostDataBaseEntity postDataBaseEntity, Throwable error) {
        shouldOpenWebView = true;
        likeMainFunction(getPostForCookie(publicLastCookie.getId()));
    }

    @Override
    public void checkForLikedByMe(PostDataBaseEntity postDataBaseEntity, InstaPostResponse instaPostResponse) {
    }

    @Override
    public void postForLikedByMeError(PostDataBaseEntity postDataBaseEntity, Throwable error) {

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private boolean isFlightModeEnabled(Context context) {
        boolean mode = false;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            // API 17 onwards
            mode = Settings.Global.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) == 1;
        } else {
            // API 16 and earlier.
            mode = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1;
        }
        return mode;
    }

    @SuppressLint("LongLogTag")
    private void setFlightMode(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            // API 17 onwards.
            if (isRooted(context)) {
                int enabled = isFlightModeEnabled(context) ? 0 : 1;
                // Set Airplane / Flight mode using su commands.
                String command = COMMAND_FLIGHT_MODE_1 + " " + enabled;
                executeCommandWithoutWait(context, "-c", command);
                command = COMMAND_FLIGHT_MODE_2 + " " + enabled;
                executeCommandWithoutWait(context, "-c", command);
            } else {
                try {
                    // No root permission, just show Airplane / Flight mode setting screen.
                    Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Log.e(TAG, "Setting screen not found due to: " + e.fillInStackTrace());
                }
            }
        } else {
            // API 16 and earlier.
            boolean enabled = isFlightModeEnabled(context);
            Settings.System.putInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, enabled ? 0 : 1);
            Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
            intent.putExtra("state", !enabled);
            sendBroadcast(intent);
        }
    }

    @SuppressLint("LongLogTag")
    private void executeCommandWithoutWait(Context context, String option, String command) {
        boolean success = false;
        String su = "su";
        for (int i = 0; i < 3; i++) {
            // "su" command executed successfully.
            if (success) {
                // Stop executing alternative su commands below.
                break;
            }
            if (i == 1) {
                su = "/system/xbin/su";
            } else if (i == 2) {
                su = "/system/bin/su";
            }
            try {
                // execute command
                Runtime.getRuntime().exec(new String[]{su, option, command});
            } catch (IOException e) {
                Log.e(TAG, "su command has failed due to: " + e.fillInStackTrace());
            }
        }
    }

    private boolean isRooted(Context context) {

        // get from build info
        String buildTags = Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }

        // check if /system/app/Superuser.apk is present
        try {
            return true;
        } catch (Exception e1) {
            // ignore
        }

        // try executing commands
        return canExecuteCommand("/system/xbin/which su")
                || canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su");
    }

    private boolean canExecuteCommand(String command) {
        boolean executedSuccesfully;
        try {
            Runtime.getRuntime().exec(command);
            executedSuccesfully = true;
        } catch (Exception e) {
            executedSuccesfully = false;
        }

        return executedSuccesfully;
    }

    private String getMobileIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> networkInterfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : networkInterfaces) {
                List<InetAddress> inetAddresses = Collections.list(networkInterface.getInetAddresses());
                for (InetAddress inetAddress : inetAddresses) {
                    if (!inetAddress.isLoopbackAddress()) {
                        String sAddr = inetAddress.getHostAddress().toUpperCase();
                        boolean isIPv4;
                        if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                            isIPv4 = true;
                        } else {
                            isIPv4 = false;
                        }

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                // drop ip6 port suffix
                                int delim = sAddr.indexOf('%');
                                return delim < 0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }


    //change ip logic end
    //**********************************************************************************************


}