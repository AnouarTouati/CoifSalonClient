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

public class CommonMehods {

   static String bitmapToString(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 85, byteArrayOutputStream);
        byte[] byteImage = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(byteImage, Base64.DEFAULT);
    }

    static Bitmap convertStringToBitmap(String image) {
        byte[] bytes;
        bytes = Base64.decode(image, Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    static String loadJSONFile(String jsonFileName, Context context) {
        String[] mFileList = context.fileList();
        Boolean fileExists = false;

        for (int i = 0; i < mFileList.length; i++) {
            if (jsonFileName.equals(mFileList[i])) {
                fileExists = true;
                break;
            }
        }
        if (fileExists) {
            String jsonAsString = null;

            try {
                FileInputStream fis = context.openFileInput(jsonFileName);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String text;
                while ((text = br.readLine()) != null) {
                    sb.append(text);
                }
                jsonAsString = sb.toString();

                fis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return jsonAsString;
        } else {

            FileOutputStream fos = null;
            JSONObject emptyJSONObject = new JSONObject();
            try {
                fos = context.openFileOutput("ShopsData.txt", MODE_PRIVATE);
                fos.write(emptyJSONObject.toString().getBytes());
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return emptyJSONObject.toString();
        }


    }

    static float[] convertFloatListToPrimitivefloatArray(List<Double> list){

        float[] temp=new float[list.size()];
        //dont use list.toArray(temp) it causes Storage exception Storing Double in Float
        //using array below we enfore casting of Double to Float to float
        for(int i=0;i<list.size();i++){
            temp[i]=list.get(i).floatValue();
        }

        return  temp;
    }
}
