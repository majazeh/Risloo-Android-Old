package com.majazeh.risloo.Models.Remotes.Generators;

import android.content.Context;
import android.util.Log;

import com.majazeh.risloo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

public class JsonGenerator {

    private static String json = "";

    public String getJson(Context context, String file_name) {
        try {
            InputStream inputStream = context.getAssets().open(file_name);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void saveJsonSampleToCache(Context context, JSONObject jsonObject, String answer_file) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(context.getCacheDir(), "") + File.separator + answer_file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(jsonObject.toString());
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONObject readJsonSampleFromCache(Context context, String answer_file) {
        JSONObject jsonObject;
        try {
            FileInputStream fis = new FileInputStream(new File(context.getCacheDir() + File.separator + answer_file));
            ObjectInputStream ois = new ObjectInputStream(fis);
            jsonObject = new JSONObject((String) ois.readObject());
            ois.close();
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

    public void saveJsonAnswerToCache(Context context, JSONArray jsonArray, String answer_file) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(context.getCacheDir(), "") + File.separator + answer_file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(jsonArray.toString());
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONArray readJsonAnswerFromCache(Context context, String answer_file) {
        JSONArray jsonArray;
        try {
            FileInputStream fis = new FileInputStream(new File(context.getCacheDir() + File.separator + answer_file));
            ObjectInputStream ois = new ObjectInputStream(fis);
            jsonArray = new JSONArray((String) ois.readObject());
            ois.close();
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

    public boolean saveToCSV(Context context, JSONArray jsonArray, String file_name) {
        try {
            FileOutputStream fos = context.openFileOutput(file_name, Context.MODE_PRIVATE);

            for (int i = 0; i < jsonArray.length(); i++) {
                fos.write(String.valueOf(i).getBytes("UTF-8"));
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

}