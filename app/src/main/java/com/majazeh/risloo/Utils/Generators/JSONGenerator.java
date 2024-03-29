package com.majazeh.risloo.Utils.Generators;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class JSONGenerator {

    // Vars
    private static String json;

    public static String getJSON(Context context, String fileName) {
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }

}