package com.majazeh.risloo.Models.Managers;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

    public static boolean hasCache(Context context, String fileName, String fileSubName) {
        return new File(context.getCacheDir(), fileName + "/" + fileSubName).exists();
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

    public static void writeToCSV(Context context, JSONArray jsonArray, String fileName) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName + ".csv", Context.MODE_PRIVATE);
            for (int i = 0; i < jsonArray.length(); i++) {
                fos.write(jsonArray.getJSONObject(i).getString("index").getBytes("UTF-8"));
                fos.write(",".getBytes("UTF-8"));
                fos.write(jsonArray.getJSONObject(i).getString("answer").getBytes("UTF-8"));
                fos.write("\n".getBytes("UTF-8"));
            }
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static File readFromCSV(Context context, String fileName) {
        return new File(context.getCacheDir(), fileName + ".csv");
    }

}