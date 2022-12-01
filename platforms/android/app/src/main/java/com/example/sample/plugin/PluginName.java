package com.example.sample.plugin;

import android.content.Context;
import android.content.Intent;

import com.hurix.commons.datamodel.IDownloadable;
import com.hurix.commons.utils.Utils;
import com.hurix.customui.datamodel.UserVO;
import com.hurix.downloadbook.controller.UserController;
import com.hurix.kitaboocloud.kitaboosdkrenderer.PlayerActivity;
import com.hurix.kitaboocloud.model.BookVO;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.cordova.hellocordova.MainActivity;
import io.cordova.hellocordova.MainActivity2;

/**
 * This class echoes a string called from JavaScript.
 */
public class PluginName extends CordovaPlugin {


    Context mContext;
    String xmlHighlightData_page2 = "<links>\n" +
            "\t<link>\n" +
            "\t\t<type>highlight</type>\n" +
            "\t\t<name>9786232691421_2_highlight_15.02.22 09:53:49</name>\n" +
            "\t\t<id>75187546869172543</id>\n" +
            "\t\t<color>#009CC7</color>\n" +
            "\t\t<location>20001;20002;20001;20001</location>\n" +
            "\t\t<pageIndex>2</pageIndex>\n" +
            "\t\t<user_type>COLORADO_TEACHER</user_type>\n" +
            "\t\t<properties/>\n" +
            "\t</link>\n" +
            "\t<link>\n" +
            "\t\t<type>bookmark</type>\n" +
            "\t\t<name>9780079018540_1_bookmark_26.3.2021 13:0:26</name>\n" +
            "\t\t<content>\n" +
            "\t\t\t<![CDATA[Cover, Page-Blank]]>\n" +
            "\t\t</content>\n" +
            "\t\t<location>1</location>\n" +
            "\t\t<pageIndex>1</pageIndex>\n" +
            "\t\t<user_type>COLORADO_TEACHER</user_type>\n" +
            "\t</link>\n" +
            "</links >";

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
       mContext = cordova.getActivity().getApplicationContext();
        if(action.equals("new_activity")) {
            this.openNewActivity(mContext);
            return true;
        }
        return false;
    }

    private void openNewActivity(Context context) {

        callActivity("pdf", true);
//        Intent intent = new Intent(context, MainActivity2.class);
//        this.cordova.getActivity().startActivity(intent);
    }



    private void callActivity(String epub, boolean book) {

        BookVO mBookVO = (BookVO) MainActivity.mBookMap.get(epub);
        Intent subActivity = new Intent(mContext, PlayerActivity.class);
        subActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        subActivity.putExtra("media_path", mBookVO.getBookPath());
        subActivity.putExtra("booktype", mBookVO.getBookType());
        subActivity.putExtra("bookDict", mBookVO.getBookDict());
        subActivity.putExtra("book_id", mBookVO.getBookID());
        subActivity.putExtra("asset_type", mBookVO.getBookAssetType() == null ? "DEFAULT" : mBookVO.getBookAssetType());
        subActivity.putExtra("token", MainActivity.mUserVO.getToken());
        subActivity.putExtra("reflow_print", "FALSE");
        subActivity.putExtra("isEncrypt", mBookVO.getIsBookEncrypt());
        subActivity.putExtra("encryptionType", mBookVO.getBookEncryptionType());
        subActivity.putExtra("ISBN", mBookVO.getISBN());
        subActivity.putExtra("UserID", MainActivity.mUserVO.getUserID());
        subActivity.putExtra("Rolename", MainActivity.mUserVO.getRoleName());
        subActivity.putExtra("classID", "null");
        subActivity.putExtra("lastPageSync", mBookVO.getLastPage());
        subActivity.putExtra("version", mBookVO.getBookVersion());
        subActivity.putExtra("searchQuery", mBookVO.getSearchQuery());
        subActivity.putExtra("cloudUserName", "demo.");
        subActivity.putExtra("cloudFirstName", "Demo");
        subActivity.putExtra("cloudLastName", "SDK");
        subActivity.putExtra("cloudProfilePic", "");
        subActivity.putExtra("classAssociate d", false);

        subActivity.putExtra("mathkeyboardenable", "Yes");
        subActivity.putExtra("fetchUgcData", xmlHighlightData_page2);
        mContext.startActivity(subActivity);

    }



}
