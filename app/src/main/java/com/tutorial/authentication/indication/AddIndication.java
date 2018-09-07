package com.tutorial.authentication.indication;

import android.os.Environment;
import android.util.Log;

import com.firebase.client.core.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class AddIndication   {
    final static String fileName = "Indication.txt";
    final static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/android-firebase-authentication-master/" ;
    final static String TAG = AddIndication.class.getName();

    public static  String ReadFile( Context context){
        String line = null;

        try {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(new File(path + fileName));

                System.out.println(fileInputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while ( (line = bufferedReader.readLine()) != null )
            {
                stringBuilder.append(line + System.getProperty("line.separator"));

                System.out.println(line);
            }
            fileInputStream.close();
            line = stringBuilder.toString();

            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
        }
        catch(IOException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return line;
    }

}
