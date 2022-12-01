/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package io.cordova.hellocordova;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.BaseAdapter;

import androidx.core.content.ContextCompat;
import androidx.multidex.MultiDex;

import com.hurix.commons.datamodel.IDownloadable;
import com.hurix.commons.utils.Utils;
import com.hurix.customui.datamodel.UserVO;
import com.hurix.downloadbook.controller.UserController;
import com.hurix.kitaboocloud.kitaboosdkrenderer.PlayerActivity;
import com.hurix.kitaboocloud.kitaboosdkrenderer.ServiceHandler;
import com.hurix.kitaboocloud.kitaboosdkrenderer.sdkUtils.KitabooReader;
import com.hurix.kitaboocloud.model.BookVO;

import org.apache.cordova.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class MainActivity extends CordovaActivity
{
    public static Map<String, IDownloadable> mBookMap = new HashMap<>();
    private String mClassID;
    public static com.hurix.customui.datamodel.UserVO mUserVO;
    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2001;



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // enable Cordova apps to be started in the background
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
            moveTaskToBack(true);
        }
                // Set by <content src="index.html" /> in config.xml
        loadUrl(launchUrl);

//        KitabooReader.setReaderCaller(MainActivity.this);
        saveBookPackage();
        prepareData();
        setBookNUserObject("pdf", false);
        if (!isStoragePermissionGranted()) {
            checkForPermission();
        }
    }

    private boolean isStoragePermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkForPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            return;
        }
    }


    private void prepareData() {
        mUserVO = new UserVO();
        mUserVO.setToken("");
        mUserVO.setUserID(58);
        mUserVO.setRoleName("LEARNER");
        mUserVO.setUserName("TestUser");
        mUserVO.setDisplayName("Test User");

        UserController.getInstance(this).setUserVO(mUserVO);
    }
    private void saveBookPackage() {
        try {
            File bookFilepath = new File("/storage/emulated/0/Android/data/io.cordova.hellocordova/files/.cloudreaderbooks/560935");
            if (!bookFilepath.exists()) {
                File file = new File("/storage/emulated/0/Android/data/io.cordova.hellocordova/files/.cloudreaderbooks/");
                if (!file.exists()) {
                    file.mkdirs();
                }
                String[] files = getAssets().list("BookPackage");

                for (int i = 0; i < files.length; i++) {
                    InputStream fis = getAssets().open("BookPackage/" + files[i]);

                    startExtract(fis, file, files[i]);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setBookNUserObject(String bookType, Boolean isLocalPage) {

        BookVO mBookVO = new BookVO();
        if (bookType.equalsIgnoreCase("pdf")) {

            Utils.setBookFolderPath("/storage/emulated/0/Android/data/io.cordova.hellocordova/files/.cloudreaderbooks/560935");
            mBookVO.setBookPath("/storage/emulated/0/Android/data/io.cordova.hellocordova/files/.cloudreaderbooks/560935");
            mBookVO.setBookType("BOOK");
            mBookVO.setBookAssetType("BOOK");
            mBookVO.setBookID((long) 560935);
            mBookVO.setIsBookEncrypt(false);
            mBookVO.setBookEncryptionType("V1");
            mBookVO.setISBN("8943754378949");
            mBookVO.setLastPage("");
            mBookVO.setBookVersion("1.0");
            mBookVO.setSearchQuery("");
            mBookVO.setBookDict("");
            mClassID = "null";
        } else if (bookType.equalsIgnoreCase("pdf2")) {
            Utils.setBookFolderPath("/storage/emulated/0/Android/data/com.hurix.demoreader/files/.cloudreaderbooks/234567");
            mBookVO.setBookPath("/storage/emulated/0/Android/data/com.hurix.demoreader/files/.cloudreaderbooks/234567");
            mBookVO.setBookType("BOOK");
            mBookVO.setBookAssetType("BOOK");
            mBookVO.setBookID((long) 234567);
            mBookVO.setIsBookEncrypt(false);
            mBookVO.setBookEncryptionType("V1");
            mBookVO.setISBN("9780079017253");
            mBookVO.setLastPage("");
            mBookVO.setBookVersion("1.0");
            mBookVO.setSearchQuery("");
            mBookVO.setBookDict("");
            mClassID = "null";
        }
        mBookMap.put(bookType, mBookVO);


    }


    /*
     *  inputStreamofInputFile : Input stream of input epub file
     *  outputFolder : Folder where your epub will extract
     *  fileName : name of your epub file (ex. 1245545.epub)
     * */
    private void startExtract(InputStream inputStreamofInputFile, File outputFolder, String fileName) {

        try {

            File bookPackZip = new File(outputFolder.getAbsolutePath() + File.separator + fileName);
            if (bookPackZip.exists()) {
                //  bookPackZip.delete();
            }

            String path = bookPackZip.getPath();
            if (path.contains(".zip")) {
                path = path.replace(".zip", "");
            } else if (path.contains(".epub")) {
                path = path.replace(".epub", "");
            }
            File bookFolder = new File(path);
            if (bookFolder.exists()) {
                bookFolder.delete();
            }

            FileOutputStream fos = new FileOutputStream(bookPackZip.getAbsolutePath());
            byte buf[] = new byte[1024];
            int len;
            while ((len = inputStreamofInputFile.read(buf)) > 0)
                fos.write(buf, 0, len);
            fos.close();
            inputStreamofInputFile.close();
            doUnzip(bookPackZip.getPath(), bookFolder.getAbsolutePath());

            if (bookPackZip.exists()) {
                bookPackZip.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void doUnzip(String inputZip, String destinationDirectory) throws IOException {
        int BUFFER = 2048;
        File sourceZipFile = new File(inputZip);
        File unzipDestinationDirectory = new File(destinationDirectory);
        unzipDestinationDirectory.mkdir();
        ZipFile zipFile;
        zipFile = new ZipFile(sourceZipFile, ZipFile.OPEN_READ);
        Enumeration zipFileEntries = zipFile.entries();
        int val = 0;
        while (zipFileEntries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
            String currentEntry = entry.getName();
            val += entry.getSize();
            File destFile = new File(destinationDirectory, currentEntry);
            String canonicalPath = destFile.getCanonicalPath();
            if (!canonicalPath.startsWith(destinationDirectory)) {
                throw new IllegalArgumentException();
            } else {
                File destinationParent = destFile.getParentFile();
                destinationParent.mkdirs();
                try {
                    if (!entry.isDirectory()) {
                        BufferedInputStream is = new BufferedInputStream(zipFile.getInputStream(entry));
                        int currentByte;
                        byte data[] = new byte[BUFFER];
                        FileOutputStream fos = new FileOutputStream(destFile);
                        BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
                        while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                            dest.write(data, 0, currentByte);
                        }
                        dest.flush();
                        dest.close();
                        is.close();
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

        zipFile.close();
    }


}
