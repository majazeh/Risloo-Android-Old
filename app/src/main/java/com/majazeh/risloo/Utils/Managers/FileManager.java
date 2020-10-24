package com.majazeh.risloo.Utils.Managers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileManager {


    public static boolean writeObjectToCache(Context context, JSONObject jsonObject, String fileName, String fileSubName) {
        try {
            File file = new File(context.getCacheDir(), fileName + "/" + fileSubName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(jsonObject.toString());
            oos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean writeArrayToCache(Context context, JSONArray jsonArray, String fileName, String fileSubName) {
        try {
            File file = new File(context.getCacheDir(), fileName + "/" + fileSubName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(jsonArray.toString());
            oos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean writeSampleAnswerToCache(Context context, JSONObject jsonObject, String fileName) {
        try {
            File file = new File(context.getCacheDir(), "Samples/" + fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            JSONArray jsonArray = new JSONArray();
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray items = data.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONArray jsonArray1 = new JSONArray();
                jsonArray1.put(i);
                if (items.getJSONObject(i).has("user_answered")) {
                    jsonArray1.put(items.getJSONObject(i).getString("user_answered"));
                } else {
                    jsonArray.put("");
                }
            }

            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(jsonObject.toString());
            oos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean writePrerequisiteAnswerToCache(Context context, JSONArray jsonArray, String fileName) {
        try {
            File file = new File(context.getCacheDir(), "Prerequisites/" + fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            JSONArray jsonArray1 = new JSONArray();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray jsonArray2 = new JSONArray();
                if (jsonArray.getJSONObject(i).has("user_answered")) {
                    jsonArray2.put(String.valueOf(i + 1));
                    jsonArray2.put(jsonArray.getJSONObject(i).getString("user_answered"));
                } else {
                    jsonArray2.put(String.valueOf(i + 1));
                    jsonArray2.put("");
                }
                jsonArray1.put(jsonArray2);
            }
            writeArrayToCache(context, jsonArray1, "prerequisitesAnswers", fileName);

            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(jsonArray.toString());
            oos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static JSONObject readObjectFromCache(Context context, String fileName, String fileSubName) {
        JSONObject jsonObject = null;
        try {
            File file = new File(context.getCacheDir(), fileName + "/" + fileSubName);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                jsonObject = new JSONObject((String) ois.readObject());
                ois.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (EOFException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject;
    }

    public static JSONArray readArrayFromCache(Context context, String fileName, String fileSubName) {
        JSONArray jsonArray = null;
        try {
            File file = new File(context.getCacheDir(), fileName + "/" + fileSubName);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                jsonArray = new JSONArray((String) ois.readObject());
                ois.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonArray;
    }

    public static boolean hasCache(Context context, String fileName, String fileSubName) {
        return new File(context.getCacheDir(), fileName + "/" + fileSubName).exists();
    }

    public static void deleteCache(Context context, String fileName, String fileSubName) {
        new File(context.getCacheDir(), fileName + "/" + fileSubName).delete();
    }

    public static boolean writeToExternal(Context context, JSONArray jsonArray, String fileName) {
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName + ".csv");
            FileOutputStream fos = new FileOutputStream(file);
            for (int i = 0; i < jsonArray.length(); i++) {
                fos.write(jsonArray.getJSONObject(i).getString("index").getBytes("UTF-8"));
                fos.write(",".getBytes("UTF-8"));
                fos.write(jsonArray.getJSONObject(i).getString("answer").getBytes("UTF-8"));
                fos.write("\n".getBytes("UTF-8"));
            }
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean writeToCSV(Context context, JSONArray jsonArray, String fileName) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName + ".csv", Context.MODE_PRIVATE);
            for (int i = 0; i < jsonArray.length(); i++) {
                fos.write(jsonArray.getJSONObject(i).getString("index").getBytes("UTF-8"));
                fos.write(",".getBytes("UTF-8"));
                fos.write(jsonArray.getJSONObject(i).getString("answer").getBytes("UTF-8"));
                fos.write("\n".getBytes("UTF-8"));
            }
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static File readFromCSV(Context context, String fileName) {
        return new File(context.getCacheDir(), fileName + ".csv");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void deletePage(Context context, String fileName, String fileSubName, int page, int count){
        File file = new File(context.getCacheDir(), fileName + "/" + fileSubName);
        if (file.exists()){
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            JSONObject jsonObject = new JSONObject((String) ois.readObject());
            JSONArray data = jsonObject.getJSONArray("data");
                for (int i = (page-1)*count; i <page*count ; i++) {
                    data.remove(i);
                }
            ois.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            // it doesnt exist
        }
    }













    public static void writeUriToCache(Context context, Uri uri, String path) {
        InputStream is = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        try {
            is = context.getContentResolver().openInputStream(uri);
            fos = new FileOutputStream(path, false);
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

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap readBitmapFromCache(Context context, String fileName) {
        Bitmap bitmap;
        try {
            File file = new File(context.getCacheDir(), fileName);
            if (!file.exists()) {
               return null;
            }
            FileInputStream fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis);
            fis.close();
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void deleteFolderFromCache(Context context, String fileName) {
        File file = new File(context.getCacheDir(), fileName);
        if (file.exists()) {
            String[] children = file.list();
            for (String child : children) {
                File subFile = new File(file, child);
                if (subFile.exists()) {
                    subFile.delete();
                }
            }
            file.delete();
        }
    }

    public static void deleteFileFromCache(Context context, String fileName) {
        File file = new File(context.getCacheDir(), fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    public static File getFileFromCache(Context context, String fileName) {
        File file = new File(context.getCacheDir(), fileName);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

}