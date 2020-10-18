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
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.smartageliketool.BaseActivity;
import com.example.smartageliketool.BaseApplication;
import com.example.smartageliketool.R;
import com.example.smartageliketool.data.model.cookie.GetCookieResponse;
import com.example.smartageliketool.data.model.instapost.InstaPostResponse;
import com.example.smartageliketool.data.model.post.PostDataBaseEntity;
import com.example.smartageliketool.data.model.postList.PostEntity;
import com.example.smartageliketool.data.model.token.TokenResponseDto;
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


    private Boolean shouldGetCookie;
    private Boolean shouldGetPosts;
    private Boolean shouldOpenWebView;
    private Integer cookieGetCount;
    private Integer cookieLikeCount;
    private Integer postTotalCount;
    private Integer postCurrentIndex;
    private List<PostDataBaseEntity> postDataBaseEntityList;
    private Map<String, String> headerMap;


    //**********************************************************************************************
    @BindView(R.id.txt_main_activity_label)
    TextView txtActivityLabel;

    @BindView(R.id.txt_main_activity_current_cookie_count_value)
    TextView txtCurrentCookieTv;


    @BindView(R.id.txt_main_activity_changing_ip_status_label)
    TextView txtChangingIp;


    @BindView(R.id.webview_main_activity)
    WebView webView;
    //**********************************************************************************************
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
            txtActivityLabel.setText("IP : " + ip);
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
        shouldGetCookie = true;
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
            PostDataBaseEntity postDataBaseEntity = new PostDataBaseEntity(postEntities.get(i).getId(), postEntities.get(i).getLink(), postEntities.get(i).getMediaId(), false, false);
            dataBase.postTableDao().insertPost(postDataBaseEntity);
        }
        if (shouldGetCookie) {
            presenter.getCookie(prefManager.loadToken());
        } else {
            postDataBaseEntityList = new ArrayList<>();
            postDataBaseEntityList = dataBase.postTableDao().getAllPostsFromDataBase();
            postTotalCount = postDataBaseEntityList.size();
            postCurrentIndex = 0;
            presenter.testPost(postCurrentIndex, postDataBaseEntityList.get(postCurrentIndex).getActualId(), postDataBaseEntityList.get(postCurrentIndex).getLink(), headerMap);
        }

    }

    @SuppressLint("LongLogTag")
    @Override
    public void postListFailed(Throwable error) {
        Toast.makeText(MainActivity.this, "Post list NOT Received !!!", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Post list NOT Received");

    }

    @SuppressLint("LongLogTag")
    @Override
    public void validPost(int postId, int actualPostId, String url, Map<String, String> headers, InstaPostResponse instaPostResponse) {
        Log.d(TAG, "Post " + postId + "-" + actualPostId + " is valid ");
        DatabaseModule dataBase = DatabaseModule.getInstance(MainActivity.this);
        if (instaPostResponse.getGraphql().getShortcodeMedia().getViewerHasLiked()) {
            dataBase.postTableDao().setLikeStatusForPostLikedViaCookie(true, actualPostId);
        } else {
            dataBase.postTableDao().setLikeStatusForPostLikedViaCookie(false, actualPostId);
        }
        postCurrentIndex++;
        if (postCurrentIndex < postTotalCount) {
            presenter.testPost(postCurrentIndex, postDataBaseEntityList.get(postCurrentIndex).getActualId(), postDataBaseEntityList.get(postCurrentIndex).getLink(), headerMap);
        } else {
            Log.d(TAG, "Testing Posts Completed");
            Log.d(TAG, "PostCurrentIndex has been reset to default 0");
            postDataBaseEntityList = new ArrayList<>();
            postCurrentIndex = 0;
            postDataBaseEntityList = dataBase.postTableDao().getAllPostsFromDataBaseThatIsUnlikeByTwoFactors(false, false);
            postTotalCount = postDataBaseEntityList.size();
            //it is ready for like process
            shouldGetPosts = false;
            likeMainFunction();


        }

    }


    @SuppressLint("LongLogTag")
    @Override
    public void inValidPost(int postId, int actualPostId, String url, Map<String, String> headers, Throwable error) {
        Log.d(TAG, "Post " + postId + "-" + actualPostId + " is NOT valid ");
        String urlForDeactivePost = "https://nitrolike.instablizer.com/apiv1/cookie_posts/" + actualPostId;
        presenter.deActivePost(
                postId,
                actualPostId,
                prefManager.loadToken(),
                urlForDeactivePost
        );
        postCurrentIndex++;
    }


    @SuppressLint("LongLogTag")
    @Override
    public void deActivePostSuccess(int postId, int actualPostId) {
        Log.d(TAG, "Post " + postId + "-" + actualPostId + " is DeActive Success ");
        DatabaseModule dataBase = DatabaseModule.getInstance(MainActivity.this);
        dataBase.postTableDao().deletePostByActualId(actualPostId);

        if (postCurrentIndex < postTotalCount) {
            presenter.testPost(postCurrentIndex, postDataBaseEntityList.get(postCurrentIndex).getActualId(), postDataBaseEntityList.get(postCurrentIndex).getLink(), headerMap);
        } else {
            Log.d(TAG, "Testing Posts Completed");
            Log.d(TAG, "PostCurrentIndex has been reset to default 0");
            postDataBaseEntityList = new ArrayList<>();
            postCurrentIndex = 0;
            postDataBaseEntityList = dataBase.postTableDao().getAllPostsFromDataBaseThatIsUnlikeByTwoFactors(false, false);
            postTotalCount = postDataBaseEntityList.size();
            //it is ready for like process
            shouldGetPosts = false;
            likeMainFunction();


        }


    }


    @SuppressLint("LongLogTag")
    @Override
    public void deActivePostFailed(int postId, int actualPostId, Throwable error) {
        Log.d(TAG, "Post " + postId + "-" + actualPostId + " is DeActive Failed ");
        DatabaseModule dataBase = DatabaseModule.getInstance(MainActivity.this);
        dataBase.postTableDao().deletePostByActualId(actualPostId);

        if (postCurrentIndex < postTotalCount) {
            presenter.testPost(postCurrentIndex, postDataBaseEntityList.get(postCurrentIndex).getActualId(), postDataBaseEntityList.get(postCurrentIndex).getLink(), headerMap);
        } else {
            Log.d(TAG, "Testing Posts Completed");
            Log.d(TAG, "PostCurrentIndex has been reset to default 0");
            postDataBaseEntityList = new ArrayList<>();
            postCurrentIndex = 0;
            postDataBaseEntityList = dataBase.postTableDao().getAllPostsFromDataBaseThatIsUnlikeByTwoFactors(false, false);
            postTotalCount = postDataBaseEntityList.size();
            //it is ready for like process
            shouldGetPosts = false;
            likeMainFunction();


        }

    }


    @SuppressLint("LongLogTag")
    @Override
    public void getCookieSuccess(GetCookieResponse getCookieResponse) {
        Log.d(TAG, "Cookie Received");
        shouldGetCookie = false;
        cookieGetCount++;


        if (cookieGetCount == 3) {

            DatabaseModule dataBase = DatabaseModule.getInstance(MainActivity.this);
            postDataBaseEntityList = new ArrayList<>();
            postDataBaseEntityList = dataBase.postTableDao().getAllPostsFromDataBase();
            postTotalCount = postDataBaseEntityList.size();
            postCurrentIndex = 0;
            cookieLikeCount = 0;


            headerMap.put("cookie", getCookieResponse.getCookie());
            headerMap.put("User-Agent", getCookieResponse.getUserAgent());

            changeIp();
        } else {
            DatabaseModule dataBase = DatabaseModule.getInstance(MainActivity.this);
            postDataBaseEntityList = new ArrayList<>();
            postDataBaseEntityList = dataBase.postTableDao().getAllPostsFromDataBase();
            postTotalCount = postDataBaseEntityList.size();
            postCurrentIndex = 0;
            cookieLikeCount = 0;


            headerMap.put("cookie", getCookieResponse.getCookie());
            headerMap.put("User-Agent", getCookieResponse.getUserAgent());
            presenter.testPost(postCurrentIndex, postDataBaseEntityList.get(postCurrentIndex).getActualId(), postDataBaseEntityList.get(postCurrentIndex).getLink(), headerMap);

        }


    }

    @SuppressLint("LongLogTag")
    @Override
    public void getCookieFailed(Throwable error) {
        Toast.makeText(MainActivity.this, "Cookie NOT Received !!!", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Cookie NOT Received");
    }


    @SuppressLint("LongLogTag")
    @Override
    public void likeApiProcessIsSuccess(int postIndex, int cookieIndex) {
        postCurrentIndex++;
        if (cookieLikeCount == 10) {
            cookieLikeCount = 0;
            shouldGetCookie = true;
        } else {
            shouldGetCookie = false;
        }
        if (postCurrentIndex < postTotalCount - 1) {
            shouldGetPosts = false;
        } else {
            shouldGetPosts = true;
        }

        //remove from database
        DatabaseModule dataBase = DatabaseModule.getInstance(MainActivity.this);
        dataBase.postTableDao().deletePostByActualId(postDataBaseEntityList.get(postCurrentIndex - 1).getActualId());
        //remove from list
        postDataBaseEntityList.remove(postCurrentIndex - 1);

        likeMainFunction();
    }


    @SuppressLint("LongLogTag")
    @Override
    public void likeApiProcessIsFailed(int postIndex, int cookieIndex, Throwable error) {
        postCurrentIndex++;
        if (cookieLikeCount == 10) {
            cookieLikeCount = 0;
            shouldGetCookie = true;
        } else {
            shouldGetCookie = false;
        }
        if (postCurrentIndex < postTotalCount - 1) {
            shouldGetPosts = false;
        } else {
            shouldGetPosts = true;
        }
        //remove from list
        postDataBaseEntityList.remove(postCurrentIndex - 1);
        likeMainFunction();
    }


    @SuppressLint("LongLogTag")
    @Override
    public void openWebViewGetLikeHeaders(String link, Map<String, String> cookieHeader) {

    }


    private void likeMainFunction() {
        if (shouldGetPosts) {
            presenter.getPostList(prefManager.loadToken());
        } else if (shouldGetCookie) {
            presenter.getCookie(prefManager.loadToken());
        } else {
            if (shouldOpenWebView) {
                //load webView
                //like via java script
                //get 4 headers for like api
            } else {
                //like via api
                StringBuilder urlBuilder = new StringBuilder();
                urlBuilder.append("https://www.instagram.com/web/likes/");
                urlBuilder.append(postDataBaseEntityList.get(postCurrentIndex).getMediaId().trim());
                urlBuilder.append("/like/");
                presenter.like(urlBuilder.toString(), postCurrentIndex, 0, headerMap);
            }
        }
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
                        txtActivityLabel.setText("IP : " + ip);
                        //ip has been changed , go for next step
                        cookieGetCount = 0;
                        presenter.testPost(postCurrentIndex, postDataBaseEntityList.get(postCurrentIndex).getActualId(), postDataBaseEntityList.get(postCurrentIndex).getLink(), headerMap);


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