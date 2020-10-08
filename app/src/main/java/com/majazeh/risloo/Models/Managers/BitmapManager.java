package com.majazeh.risloo.Models.Managers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BitmapManager {

    public static byte[] bitmapToByte(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static String bitmapToString(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] encodedByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(encodedByte, Base64.DEFAULT);
    }

    public static Bitmap byteToBitmap(byte[] value) {
        return BitmapFactory.decodeByteArray(value, 0, value.length);
    }

    public static Bitmap stringToBitmap(String value) {
        byte[] decodedByte = Base64.decode(value, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public static Bitmap scaleToCenter(Bitmap image) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        int newDimension = Math.min(imageWidth, imageHeight);

        if (newDimension > 960) {
            newDimension = 960;
        }

        int left = (newDimension - imageWidth) / 2;
        int top = (newDimension - imageHeight) / 2;

        int right = left + imageWidth;
        int bottom =top + imageHeight;

        RectF targetRect = new RectF(left, top, right, bottom);

        Bitmap bitmap = Bitmap.createBitmap(newDimension, newDimension, image.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(image, null, targetRect, null);

        return bitmap;
    }

    public static Bitmap modifyOrientation(Bitmap bitmap, String path) {
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateOrientation(bitmap, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateOrientation(bitmap, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateOrientation(bitmap, 270);
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    return flipOrientation(bitmap, true, false);
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    return flipOrientation(bitmap, false, true);
                default:
                    return bitmap;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap rotateOrientation(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flipOrientation(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

}