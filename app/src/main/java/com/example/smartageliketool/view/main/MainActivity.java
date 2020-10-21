package com.example.smartageliketool.view.main;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.HttpException;

public class MainActivity extends BaseActivity implements MainContract.View {

    public static final String TAG = "xeagle69_MainActivity >>";
    private CountDownTimer changeIp;
    private final String COMMAND_FLIGHT_MODE_1 = "settings put global airplane_mode_on";
    private final String COMMAND_FLIGHT_MODE_2 = "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state";
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
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
    private CountDownTimer webViewCountDownTimer = null;
    private Date currentTime;
    private Date startTime;


    private GetCookieResponse publicLastCookie = null;

    @BindView(R.id.txt_main_activity_label)
    TextView txtActivityLabel;

    @BindView(R.id.txt_main_activity_current_like_count_value)
    TextView txtCurrentLikeTv;


    @BindView(R.id.txt_main_activity_changing_ip_status_label)
    TextView txtChangingIp;

    @BindView(R.id.txt_main_like_per_seccond)
    TextView txtLikePerSeccond;

    @BindView(R.id.txt_main_last_like_time)
    TextView txtLastLikeTime;


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
        IntentFilter filter = new IntentFilter(SMS_RECEIVED);
        this.registerReceiver(smsBroadcastReceiver, filter);
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
            txtActivityLabel.setText("IP : " + ip);
            user_status = UserAccountStatus.OK;
            totalLikeCount = prefManager.loadLikeCount();
            startTime = Calendar.getInstance().getTime();
            presenter.getToken("super-admin", "a8k9p763gYv2RBq");

//            sendSms();


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
        if (publicLastCookie == null) {
            if (isNetworkAvailable()) {
                presenter.getCookie(prefManager.loadToken());
            }
        } else {
            likeMainFunction(getPostForCookie(publicLastCookie.getId()));
        }


    }

    @SuppressLint("LongLogTag")
    @Override
    public void postListFailed(Throwable error) {
        Toast.makeText(MainActivity.this, "Post list NOT Received !!!", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Post list NOT Received");
        txtActivityLabel.setText("Post list NOT Received");

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
            txtChangingIp.setVisibility(View.VISIBLE);
            changeIp();
        } else {
            if ((getCookieResponse.getCsrfToken() != null) && (getCookieResponse.getIgWwwClaim() != null) && (getCookieResponse.getInstagramAjax() != null) && (getCookieResponse.getIgAppId() != null)) {
                headerMap.put("cookie", getCookieResponse.getCookie());
                headerMap.put("User-Agent", getCookieResponse.getUserAgent());
                prefManager.saveCfrtoken(getCookieResponse.getCsrfToken());
                prefManager.saveIgClaim(getCookieResponse.getIgWwwClaim());
                prefManager.saveInstagramAjax(getCookieResponse.getInstagramAjax());
                prefManager.saveInstagramAppId(getCookieResponse.getIgAppId());
                shouldOpenWebView = false;
                PostDataBaseEntity post = getPostForCookie(getCookieResponse.getId());


                if (post == null) {
                    if (isNetworkAvailable())
                        presenter.getCookie(prefManager.loadToken());
                } else {
                    presenter.testPostValidity(post, post.getLink(), headerMap);
                }


            } else {
                headerMap.put("cookie", getCookieResponse.getCookie());
                headerMap.put("User-Agent", getCookieResponse.getUserAgent());
                PostDataBaseEntity post = getPostForCookie(getCookieResponse.getId());

                if (post == null) {
                    if (isNetworkAvailable())
                        presenter.getCookie(prefManager.loadToken());
                } else {
                    presenter.testPostValidity(post, post.getLink(), headerMap);
                }


            }
            cookieGetCount++;
        }

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
        txtActivityLabel.setText("Cookie NOT Received");
        if (isNetworkAvailable()) {
            presenter.getCookie(prefManager.loadToken());
        }


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
        prefManager.saveLikeCount(totalLikeCount);


        currentTime = Calendar.getInstance().getTime();
        txtLastLikeTime.setText(currentTime.toString());


        long diffInSec = ((currentTime.getTime() - startTime.getTime()) / 1000);

        float averagePerSecond = ((float) totalLikeCount / (float) diffInSec);


        Log.d(TAG, "total like count :  " + totalLikeCount);

        Log.d(TAG, "average like per seccond : " + Float.toString(averagePerSecond));

        txtLikePerSeccond.setText(Float.toString(averagePerSecond) + " p/s");


        DatabaseModule databaseModule = DatabaseModule.getInstance(MainActivity.this);
        databaseModule.likeTableDao().insertLikeTableEntity(new LikeTable(publicLastCookie.getId(), post.getId()));

        PostDataBaseEntity postForCookie = getPostForCookie(publicLastCookie.getId());
        likeMainFunction(postForCookie);
    }


    @SuppressLint("LongLogTag")
    @Override
    public void likeApiProcessIsFailed(PostDataBaseEntity post, Throwable error) {

        if (isNetworkAvailable()) {
            presenter.getCookie(prefManager.loadToken());
            cookieGetCount--;
        }

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
        if (publicLastCookie == null) {
            if (isNetworkAvailable()) {
                presenter.getCookie(prefManager.loadToken());
            }
        } else {
            likeMainFunction(getPostForCookie(publicLastCookie.getId()));
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
            if (post_size < 11) {
                presenter.getPostListFromBetween(prefManager.loadToken());
            }

            List<PostDataBaseEntity> tempDistinctsPost = new ArrayList<>();

            List<String> distinctsMediaIds = databaseModule.postTableDao().getDistinctPost();
            if (distinctsMediaIds.size() < 7)
                presenter.getPostListFromBetween(prefManager.loadToken());


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
                System.out.println("tempDistinctsPost.size() < 0 - so return null");
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


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    checkAccountStatus(webView);

                                }
                            });


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

                    if (webViewCountDownTimer == null) {
                        Log.d(TAG, "webViewCountDownTimer START");
                        webViewCountDownTimer = new CountDownTimer(300000, 1000) {
                            public void onTick(long millisUntilFinished) {
                            }

                            public void onFinish() {
                                webViewCountDownTimer = null;
                                Log.d(TAG, "webViewCountDownTimer Trigger");
                                recreate();

                            }
                        }.start();
                    }


                    webView.loadUrl(post.getLink() + "?hl=en");
                }

                //****************************************************************************************************************************

            } else {


                presenter.testPostforLikedByMe(post, post.getLink(), headerMap);


                if (webViewCountDownTimer != null) {
                    Log.d(TAG, "webViewCountDownTimer >>>  Cancel");
                    webViewCountDownTimer.cancel();
                    webViewCountDownTimer = null;
                }


            }
        } else {
            shouldOpenWebView = true;
            if (isNetworkAvailable()) {
                presenter.getCookie(prefManager.loadToken());
            }

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


    }

    @SuppressLint("LongLogTag")
    public void checkAccountStatus(WebView webView) {
        Log.d(TAG, "checkAccountStatus");
        webView.evaluateJavascript(RemoteConstants.CHECK_ACCOUNT_STATUS_SCRIPT,
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String html) {


                        if (html.contains(RemoteConstants.SIGN_PLUS_HINT) || html.contains(RemoteConstants.LOGIN_HINT) || html.contains(RemoteConstants.LOCK_ACCOUNT) || html.contains(RemoteConstants.UNUSUAL_HINT) || html.contains(RemoteConstants.COMPRIMISED_HINT) || html.contains(RemoteConstants.ACTION_BLOCK_HINT) || html.contains(RemoteConstants.RESTRICTED_VIDEO_HINT) || html.contains(RemoteConstants.WEBPAGE_NOT_AVAILABLE) || html.contains(RemoteConstants.TO_SECURE_YOUR_ACCOUNT) || html.contains(RemoteConstants.ACCOUNT_OWN_HINT)) {
                            Log.d(TAG, "checkAccountStatus >> SKIP COOKIE CONDITION ONE");
                            webView.stopLoading();
                            webView.loadUrl(RemoteConstants.BLANK_LINK);
                            shouldOpenWebView = true;
                            if (isNetworkAvailable()) {
                                presenter.getCookie(prefManager.loadToken());
                                cookieGetCount--;
                            }

                        }


                        if (html.contains(RemoteConstants.THIS_WAS_ME_HINT)) {
                            Log.d(TAG, "checkAccountStatus >> THIS_WAS_ME_HINT");
                            webView.loadUrl(RemoteConstants.THIS_WAS_ME_SCRIPT);
                        }


                        if (html.contains(RemoteConstants.COOKIE_ACCEPT_HINT)) {
                            Log.d(TAG, "checkAccountStatus >> COOKIE_ACCEPT_HINT");
                            clickCookie(webView);
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


                if (millisUntilFinished < 50000 && millisUntilFinished > 49000) {
                    try {
                        setFlightMode(MainActivity.this);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                if (millisUntilFinished < 40000 && millisUntilFinished > 39000) {
                    try {
                        int enabled = isFlightModeEnabled(MainActivity.this) ? 0 : 1;
                        if (enabled == 0)
                            setFlightMode(MainActivity.this);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                if (millisUntilFinished < 39000)
                    if (isNetworkAvailable()) {
                        this.onFinish();
                    }
            }

            @SuppressLint("LongLogTag")
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
                        txtActivityLabel.setText("IP : " + ip);
                        //ip has been changed , go for next step
                        cookieGetCount = 0;


                        if ((publicLastCookie.getCsrfToken() != null) && (publicLastCookie.getIgWwwClaim() != null) && (publicLastCookie.getInstagramAjax() != null) && (publicLastCookie.getIgAppId() != null)) {
                            headerMap.put("cookie", publicLastCookie.getCookie());
                            headerMap.put("User-Agent", publicLastCookie.getUserAgent());
                            prefManager.saveCfrtoken(publicLastCookie.getCsrfToken());
                            prefManager.saveIgClaim(publicLastCookie.getIgWwwClaim());
                            prefManager.saveInstagramAjax(publicLastCookie.getInstagramAjax());
                            prefManager.saveInstagramAppId(publicLastCookie.getIgAppId());
                            shouldOpenWebView = false;
                            PostDataBaseEntity post = getPostForCookie(publicLastCookie.getId());
                            if (post == null) {
                                if (isNetworkAvailable())
                                    presenter.getCookie(prefManager.loadToken());
                            } else {
                                presenter.testPostValidity(post, post.getLink(), headerMap);
                            }
                        } else {
                            headerMap.put("cookie", publicLastCookie.getCookie());
                            headerMap.put("User-Agent", publicLastCookie.getUserAgent());
                            PostDataBaseEntity post = getPostForCookie(publicLastCookie.getId());


                            if (post == null) {
                                if (isNetworkAvailable())
                                    presenter.getCookie(prefManager.loadToken());
                            } else {
                                presenter.testPostValidity(post, post.getLink(), headerMap);
                            }


                        }


//                        headerMap.put("cookie", publicLastCookie.getCookie());
//                        headerMap.put("User-Agent", publicLastCookie.getUserAgent());
//                        PostDataBaseEntity post = getPostForCookie(publicLastCookie.getId());
//                        Log.d(TAG, "testpost id :" + post.getId());
//                        Log.d(TAG, "testpost link :" + post.getLink());
//                        presenter.testPostValidity(post, post.getLink(), headerMap);
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

    @SuppressLint("LongLogTag")
    @Override
    public void validPost(PostDataBaseEntity postDataBaseEntity, InstaPostResponse instaPostResponse) {
        Log.d(TAG, "test-post-response : validPost");
        if (instaPostResponse.getGraphql().getShortcodeMedia().getViewerHasLiked()) {
            DatabaseModule databaseModule = DatabaseModule.getInstance(this);
            databaseModule.ignoreTableDao().insertIgnoreTableEntity(new IgnoreTable(publicLastCookie.getId(), postDataBaseEntity.getId()));


            PostDataBaseEntity post = getPostForCookie(publicLastCookie.getId());
            if (post == null) {
                if (isNetworkAvailable())
                    presenter.getCookie(prefManager.loadToken());
            } else {
                presenter.testPostValidity(post, post.getLink(), headerMap);
            }


        } else {
            likeMainFunction(postDataBaseEntity);
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void inValidPost(PostDataBaseEntity postDataBaseEntity, Throwable error) {
        if (error instanceof HttpException)
            if (((HttpException) error).code() == 404) {
                Log.d(TAG, "test-post-response : inValidPost");
                DatabaseModule databaseModule = DatabaseModule.getInstance(MainActivity.this);
                databaseModule.postTableDao().deletePost(postDataBaseEntity);
                presenter.deActivePost(postDataBaseEntity, prefManager.loadToken(), RemoteConstants.API_BASE + "cookie_posts/" + postDataBaseEntity.getActualId());
            }
        likeMainFunction(null);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void checkForWebView(PostDataBaseEntity postDataBaseEntity, InstaPostResponse instaPostResponse) {
        Log.d(TAG, "test-post-response : checkForWebView Success");


        if (instaPostResponse.getGraphql().getShortcodeMedia().getViewerHasLiked()) {
            DatabaseModule databaseModule = DatabaseModule.getInstance(this);
            databaseModule.likeTableDao().insertLikeTableEntity(new LikeTable(publicLastCookie.getId(), postDataBaseEntity.getId()));
            shouldOpenWebView = false;
            totalLikeCount++;
            txtCurrentLikeTv.setText(totalLikeCount + "");
            prefManager.saveLikeCount(totalLikeCount);


            currentTime = Calendar.getInstance().getTime();
            txtLastLikeTime.setText(currentTime.toString());

            long diffInSec = ((currentTime.getTime() - startTime.getTime()) / 1000);

            Log.d(TAG, "diffrent in seccond : " + diffInSec);

            float averagePerSecond = ((float) totalLikeCount / (float) diffInSec);


            Log.d(TAG, "total like count :  " + totalLikeCount);

            Log.d(TAG, "average like per seccond : " + Float.toString(averagePerSecond));

            txtLikePerSeccond.setText(Float.toString(averagePerSecond) + " p/s");


            PostDataBaseEntity post = getPostForCookie(publicLastCookie.getId());
            if (post == null) {
                if (isNetworkAvailable())
                    presenter.getCookie(prefManager.loadToken());
            } else {
                presenter.testPostValidity(post, post.getLink(), headerMap);
            }

        } else {
            likeMainFunction(null);
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void postForWebViewError(PostDataBaseEntity postDataBaseEntity, Throwable error) {
        if (error instanceof HttpException)
            if (((HttpException) error).code() == 404) {
                Log.d(TAG, "test-post-response : inValidPost");
                DatabaseModule databaseModule = DatabaseModule.getInstance(MainActivity.this);
                databaseModule.postTableDao().deletePost(postDataBaseEntity);
                presenter.deActivePost(postDataBaseEntity, prefManager.loadToken(), RemoteConstants.API_BASE + "cookie_posts/" + postDataBaseEntity.getActualId());
            }

        likeMainFunction(null);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void checkForLikedByMe(PostDataBaseEntity postDataBaseEntity, InstaPostResponse instaPostResponse) {
        if (instaPostResponse.getGraphql().getShortcodeMedia().getViewerHasLiked()) {
            DatabaseModule databaseModule = DatabaseModule.getInstance(this);
            databaseModule.ignoreTableDao().insertIgnoreTableEntity(new IgnoreTable(publicLastCookie.getId(), postDataBaseEntity.getId()));
            PostDataBaseEntity post = getPostForCookie(publicLastCookie.getId());
            likeMainFunction(post);
        } else {
            Log.d(TAG, "likeMainFunction - likeViaApi");
            //like via api
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append("https://www.instagram.com/web/likes/");
            urlBuilder.append(postDataBaseEntity.getMediaId().trim());
            urlBuilder.append("/like/");
            Log.d(TAG, "likeMainFunction - likeViaApi - url > " + urlBuilder.toString());
            headerMap.put("x-csrftoken", prefManager.loadCfrtoken());
            headerMap.put("x-ig-app-id", prefManager.loadInstagramAppId());
            headerMap.put("x-ig-www-claim", prefManager.loadIgClaim());
            headerMap.put("x-instagram-ajax", prefManager.loadInstagramAjax());
            headerMap.put("x-requested-with", "XMLHttpRequest");
            presenter.like(postDataBaseEntity, urlBuilder.toString(), headerMap);
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void postForLikedByMeError(PostDataBaseEntity postDataBaseEntity, Throwable error) {
        if (error instanceof HttpException)
            if (((HttpException) error).code() == 404) {
                Log.d(TAG, "test-post-response : inValidPost");
                DatabaseModule databaseModule = DatabaseModule.getInstance(MainActivity.this);
                databaseModule.postTableDao().deletePost(postDataBaseEntity);
                presenter.deActivePost(postDataBaseEntity, prefManager.loadToken(), RemoteConstants.API_BASE + "cookie_posts/" + postDataBaseEntity.getActualId());
                PostDataBaseEntity post = getPostForCookie(publicLastCookie.getId());
                likeMainFunction(post);
            }

        likeMainFunction(null);

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


    //**********************************************************************************************
    //send sms start

    private void sendSms() {
        if (
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
                        ||
                        (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED)
        ) {
            try {
                SmsManager smsMgrVar = SmsManager.getDefault();
                smsMgrVar.sendTextMessage("8080", null, "0", null, null);
                Toast.makeText(getApplicationContext(), "Sms Sent",
                        Toast.LENGTH_LONG).show();
            } catch (Exception ErrVar) {
                Toast.makeText(getApplicationContext(), ErrVar.getMessage().toString(),
                        Toast.LENGTH_LONG).show();
                ErrVar.printStackTrace();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS}, 10);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {

            if (permissions[0].equals(Manifest.permission.SEND_SMS)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSms();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 10);
                    }
                }
            }
        }


    }

    private final BroadcastReceiver smsBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SMS_RECEIVED)) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    // get sms objects
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    if (pdus.length == 0) {
                        return;
                    }
                    // large message might be broken into many
                    SmsMessage[] messages = new SmsMessage[pdus.length];
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < pdus.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        sb.append(messages[i].getMessageBody());
                    }
                    String sender = messages[0].getOriginatingAddress();
                    String message = sb.toString();


                    String[] messageArray = message.split(" ");

                    int indexAmount = 0;
                    boolean isInternetMessage = false;
                    for (int i = 0; i < messageArray.length; i++) {
                        if (messageArray[i].equals("مگابایت")) {
                            indexAmount = (i - 1);
                            isInternetMessage = true;
                        }
                    }

                    if (isInternetMessage) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle(sender)
                                .setMessage(messageArray[indexAmount] + " Mb")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();
                    }


                    abortBroadcast();
                }
            }
        }
    };


    //send sms end
    //**********************************************************************************************


}