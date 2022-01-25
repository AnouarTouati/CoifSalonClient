package com.example.coifsalonclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CommonMethods {

    public static float[] convertFloatListToPrimitivefloatArray(List<Double> list) {

        float[] temp = new float[list.size()];
        //dont use list.toArray(temp) it causes Storage exception Storing Double in Float
        //using array below we enforce casting of Double to Float to float
        for (int i = 0; i < list.size(); i++) {
            temp[i] = list.get(i).floatValue();
        }

        return temp;
    }
}
