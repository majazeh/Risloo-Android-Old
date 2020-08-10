package com.majazeh.risloo.Utils;

import  android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;

public class IntentCaller {

    public void internet(Context context) {
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }

    public void gallery(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        activity.startActivityForResult(intent, 100);
    }

    public void camera(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File imageFile = null;
        try {
            imageFile = FileManager.createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (imageFile != null) {
            Uri imageURI = FileProvider.getUriForFile(activity, "com.majazeh.risloo.fileprovider", imageFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
            activity.startActivityForResult(intent, 200);
        }
    }

    public void mediaScan(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        File imageFile = new File(FileManager.imagePath);

        if (imageFile != null) {
            Uri imageURI = Uri.fromFile(imageFile);
            intent.setData(imageURI);
            activity.sendBroadcast(intent);
        }
    }

    public void phone(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.fromParts("tel", number , null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }

    public void sms(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.fromParts("sms", number , null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }

    public void email(Context context, String[] emails, String subject, String message, String chooser) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, emails);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("message/rfc822");

        context.startActivity(Intent.createChooser(intent, chooser));
    }

    public void share(Context context, String content, String chooser) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("text/plain");

        context.startActivity(Intent.createChooser(intent, chooser));
    }

    public void googlePlay(Context context) {
        Intent intent;
        try {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (ActivityNotFoundException e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName()));
        }
        context.startActivity(intent);
    }

    public void facebook(Context context, String facebookID) {
        Intent intent;
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + facebookID));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + facebookID));
        }
        context.startActivity(intent);
    }

    public void twitter(Context context, String twitterID) {
        Intent intent;
        try {
            context.getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=" + twitterID));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + twitterID));
        }
        context.startActivity(intent);
    }

    public void telegram(Context context, String telegramID) {
        Intent intent;
        try {
            context.getPackageManager().getPackageInfo("org.telegram.messenger", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=" + telegramID));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/" + telegramID));
        }
        context.startActivity(intent);
    }

    public void instagram(Context context, String instagramID) {
        Intent intent;
        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/" + instagramID));
        intent.setPackage("com.instagram.android");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/" + instagramID));
            context.startActivity(intent);
        }
    }

}