package com.example.smartageliketool.data.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class RemoteConstants {

    public static String API_BASE = "https://nitrolike.instablizer.com/apiv1/";
    public static final String INSTAGRAM_HOME_PLUS = "https://www.instagram.com/";
    public static final String SEMICOLON_PATTERN = ";";
    public static final String BLANK_LINK = "about:blank";
    public static final String IS_POST_AVALABLE_SCRIPT = "(function(){return window.document.body.outerHTML})();";
    public static final String CLICK_COOKIE_SCRIPT = "javascript:(function() { document.getElementsByClassName('Szr5J')[0].click() })()";
    public static final String CHECK_ACCOUNT_STATUS_SCRIPT = "(function(){return window.document.body.outerHTML})();";
    public static final String LOCK_ACCOUNT = "Your Account Has Been Temporarily Locked";
    public static final String GET_TOKEN = "tokens";
    public static final String GET_POSTS = "cookie_posts";
    public static final String GET_COOKIE = "valid_cookies";


    public static final String IGNORE_POST = "ignores";
    public static final String REPORT_POST = "reports";
    public static final String LIKER = "likers";
    public static final String TRANSFER = "transfers";
    public static final String WITHDRAW = "withdraws";
    public static final String FORGET_PATTERN = "password/reset";
    public static final String PLANS = "plans";
    public static final String BUY_PLAN = "buys";
    public static final String REFERRAL = "referrals";
    public static final String APP_VERSION = "application_versions/android";
    public static final String GET_PODS = "pods";
    public static final String ZARIN_PAL_REQUEST = "zarinpal/buys";
    public static final String SUPPORT_INTENT = "tg://resolve?domain=hyperlike_support";
    public static final String TELECRAM_PACKAGE = "org.telegram.messenger";
    public static final String UNLIKE_HINT = "Unlike";
    public static final String CLICK_LIKE_SCRIPT = "javascript:(function() { document.getElementsByClassName('wpO6b ')[0].click() })()";
    public static final String URL_HINT = "url";
    public static final String FIREBASE_POST_LIKE_EVENT = "post_like_event";
    public static final String FONT_NAME = "fonts/IranSans.ttf";
    public static final String FIREBASE_AUTO_LIKE_EVENT = "autolike_event";
    public static final String FIREBASE_ANTIBLOCK_EVENT = "anti_block_event";
    public static final String BACK_STACK_ORDER_HINT = "order";
    public static final String ENCRYPTION_ALGORITHM = "SHA-256";
    public static final String ID_HINT = "id";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TYPE = "application/json";
    public static final String POP = "-EBWKjlELKMYqRNQ6sYvFo64FtaRLRR5BdHEESmha49TM";
    public static final String DASH = "-";
    public static final String HEADER_HMAC = "hmac";
    public static final String CHALANGE_PATTERN = "challenge";
    public static final String LOGIN_URL_PATTERN = "auth_switcher";
    public static final String COOKIE_HEADER = "Cookie";
    public static final String USERNAME_PATTERN = "username";
    public static final String USERNAME_PATTERN_WITH_PARANTHESES = "{username}";
    public static final String FIREBASE_FIRST_NAME = "first_name";
    public static final String FIREBASE_LAST_NAME = "last_name";
    public static final String FIREBASE_COMPLETE_ADD_INSTAGRAM = "complete_add_instagram";
    public static final String SAMPLE_CAPTION = "hi";
    public static final String POST_ID_PATTENT = "post_id";
    public static final String APP_TYPE_IRN = "IRN";
    public static final String ATSIGN_PATTERN = "@";
    public static final String FEMALE_GENDER = "Female";
    public static final String MALE_GENDER = "Male";
    public static final String METRIX_TOKEN = "2r9nv4";
    public static final String BAZAAR_REDIRECT_LINK = "bazaar://details?id=ir.hyperlikes.likeapp";
    public static final String BAAZAR_PACKAGE = "com.farsitel.bazaar";
    public static final String HYPER_LIKE_PACKAGE = "ir.hyperlikes.likeapp";
    public static final String FIREBASE_START_ADD_INSTAGRAM_EVENT = "start_add_instagram";
    public static final String FIREBASE_BUY_COIN_EVENT = "buy_coin_event";
    public static final String TELEGRAM_RESOLVE = "tg://resolve?domain=hyperlike_support";
    public static final String TAGS_URL = "tags";
    public static final String TRY_AGAIN = "Try Again Later";
    public static boolean LAST_AUTO_LIKE_STATUS = false;
    public static boolean LAST_ANTI_BLOCK_STATUS = false;
    public static boolean LAST_SHOW_IMAGE_STATUS = false;
    public static String SKU_PREMIUM = null;
    public static final String ADD_ACCOUNT = "accounts";
    public static final String LIKE_POST = "likes";
    public static final String GET_ALL_POSTS = "https://nitrolike.instablizer.com/apiv1/posts";
    public static final String INSTAGRA_LOGOUT = "https://instagram.com/accounts/logout/";
    public static final String INSTAGRAM_PATTERN = "/logging/arwing";
    public static final String INSTAGRAM_POST_PATTERN = "/p/";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String INSTAGRAM_LANGUAGE_PREFIX = "?hl=en";
    public static final String AWAKE_TAG = "My Tag";
    public static final String ACTION_BLOCK_HINT = "Action Blocked";
    public static final String RESTRICTED_VIDEO_HINT = "Restricted Video";
    public static final String COMPRIMISED_HINT = "Your Account Was Compromised";
    public static final String UNUSUAL_HINT = "We noticed unusual";
    public static final String ACCOUNT_OWN_HINT = "Help Us Confirm You Own This Account";
    public static final String INSTALL_APP_HINT = "Instagram Is Better on the App";
    public static final String SECURITY_CODE_HINT = "Send Security Code";
    public static final String SECURITY_CODE_PLUS_HINT = "Enter Your Security Code";
    public static final String ADD_PHONE_HINT = "Add Phone Number to Get Back";
    public static final String EMAIL_NEEDED_HINT = "Enter New Email Address";
    public static final String CONFIRM_PROFILE_HINT = "Yes, It's Correct";
    public static final String CONFIRM_PROFILE_SCRIPT = "javascript:(function() { document.getElementsByClassName('_5f5mN    -fzfL    KUBKM      yZn4P   ')[0].click() })()";
    public static final String THIS_WAS_ME_SCRIPT = "javascript:(function() { document.getElementsByClassName('_5f5mN    -fzfL    KUBKM      yZn4P   ')[0].click() })()";
    public static final String IS_POST_AVALABLE_HINT = "The link you followed may be";
    public static final String IS_ACCOUNT_PRIVATE_HINT = "This Account is Private";
    public static final String THIS_WAS_ME_HINT = "This was me";
    public static final String LOGIN_HINT = "Log In";
    public static final String SIGN_PLUS_HINT = "Sign up with email";
    public static final String COOKIE_ACCEPT_HINT = "We use cookies to help personalize";
    public static final String PERSONALIZE_CONTENT = "To help personalize content";
    public static final String SUPPORT_LINK = "https://t.me/hyperlike_support";
    public static final String FIREBASE_EVENT1 = "user_name";
    public static final String FIREBASE_EVENT_ERROR = "glide_placeholder_error";
    public static final String INSTAGRAM_HOME = "https://www.instagram.com";
    public static final String INSTAGRAM_ACCESS_TOOL = "https://www.instagram.com/accounts/access_tool/?__a=1";
    public static final String USER_EDIT_INSTAGRAM = "https://www.instagram.com/accounts/edit/?__a=1";
    public static final String INSTAGRAM_LOGIN = "https://www.instagram.com/accounts/login/?source=auth_switcher";
    public static final String INSTAGRAM_PROFILE = "https://www.instagram.com/username/?__a=1";
    public static final String INSTAGRAM_POST_MODEL = "https://www.instagram.com/p/";
    public static final String NITROLIKE_ADD_POST = "https://nitrolike.instablizer.com/apiv1/accounts/post_id/posts";
    public static final String NITROLIKE_GET_USER = "https://nitrolike.instablizer.com/apiv1/public_accounts/{username}";
    public static String cookieString = "";


    public static boolean isRootGiven() {
        if (isRootAvailable()) {
            Process process = null;
            try {
                process = Runtime.getRuntime().exec(new String[]{"su", "-c", "id"});
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String output = in.readLine();
                if (output != null && output.toLowerCase().contains("uid=0"))
                    return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (process != null)
                    process.destroy();
            }
        }

        return false;
    }

    public static boolean isRootAvailable() {
        for (String pathDir : System.getenv("PATH").split(":")) {
            if (new File(pathDir, "su").exists()) {
                return true;
            }
        }
        return false;
    }

}
