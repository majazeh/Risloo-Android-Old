package com.majazeh.risloo.Utils.Managers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileManager {

    public static void writeObjectToCache(Context context, JSONObject jsonObject, String fileName) {
        try {
            File file = new File(context.getCacheDir(), fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(jsonObject.toString());
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeArrayToCache(Context context, JSONArray jsonArray, String fileName) {
        try {
            File file = new File(context.getCacheDir(), fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(jsonArray.toString());
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeBitmapToCache(Context context, Bitmap bitmap, String fileName) {
        try {
            File file = new File(context.getCacheDir(), fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeAnswersToExternal(Context context, JSONArray jsonArray, String fileName) {
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName + ".csv");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);

            for (int i = 0; i < jsonArray.length(); i++) {
                fos.write(jsonArray.getJSONObject(i).getString("index").getBytes("UTF-8"));
                fos.write(",".getBytes("UTF-8"));
                fos.write(jsonArray.getJSONObject(i).getString("answer").getBytes("UTF-8"));
                fos.write("\n".getBytes("UTF-8"));
            }

            fos.flush();
            fos.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public static void writeUriToCache(Context context, Uri uri, String fileName) {
        InputStream is = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        try {
            is = context.getContentResolver().openInputStream(uri);
            fos = new FileOutputStream(fileName, false);
            bos = new BufferedOutputStream(fos);

            byte[] buf = new byte[1024];

            is.read(buf);
            do {
                bos.write(buf);
            } while (is.read(buf) != -1);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (fos != null) fos.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static JSONObject readObjectFromCache(Context context, String fileName) {
        try {
            File file = new File(context.getCacheDir(), fileName);
            if (!file.getParentFile().exists()) {
                return null;
            }
            if (!file.exists()) {
                return null;
            }
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            JSONObject jsonObject = new JSONObject(ois.readObject().toString());
            ois.close();
            return jsonObject;
        } catch (IOException | JSONException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONArray readArrayFromCache(Context context, String fileName) {
        try {
            File file = new File(context.getCacheDir(), fileName);
            if (!file.getParentFile().exists()) {
                return null;
            }
            if (!file.exists()) {
                return null;
            }
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            JSONArray jsonArray = new JSONArray(ois.readObject().toString());
            ois.close();
            return jsonArray;
        } catch (IOException | JSONException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap readBitmapFromCache(Context context, String fileName) {
        try {
            File file = new File(context.getCacheDir(), fileName);
            if (!file.getParentFile().exists()) {
                return null;
            }
            if (!file.exists()) {
                return null;
            }
            FileInputStream fis = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            fis.close();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File readFileFromCache(Context context, String fileName) {
        File file = new File(context.getCacheDir(), fileName);
        if (!file.getParentFile().exists()) {
            return null;
        }
        if (!file.exists()) {
            return null;
        }
        return file;
    }

    public static void deleteFileFromCache(Context context, String fileName) {
        File file = new File(context.getCacheDir(), fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    public static void deleteFolderFromCache(Context context, String fileName) {
        File file = new File(context.getCacheDir(), fileName);
        if (file.exists()) {
            String[] children = file.list();
            if (children != null) {
                for (String child : children) {
                    File subFile = new File(file, child);
                    if (subFile.exists()) {
                        subFile.delete();
                    }
                }
            }
            file.delete();
        }
    }

    public static void deletePageFromCache(Context context, String fileName, int page, int count) {
        try {
            File file = new File(context.getCacheDir(), fileName);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);

                JSONObject jsonObject = new JSONObject(ois.readObject().toString());
                JSONArray data = jsonObject.getJSONArray("data");

                for (int i = (page-1)*count; i <page*count ; i++) {
                    data.remove(i);
                }

                ois.close();
            }
        } catch (IOException | JSONException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void deletePageFromCache(Context context, String fileName) {
        File file = new File(context.getCacheDir(), fileName);
        if (file.exists()) {
           file.delete();
        }
    }

    public static boolean hasFileInCache(Context context, String fileName) {
        return new File(context.getCacheDir(), fileName).exists();
    }

}