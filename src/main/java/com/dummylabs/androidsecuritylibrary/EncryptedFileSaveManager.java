package com.dummylabs.androidsecuritylibrary;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Ge0rge on 21.05.2016.
 */
public class EncryptedFileSaveManager {
    final String LOG_TAG = "FileSaveManagerLog";
    private Context context;
    protected static final String UTF8 = "utf-8";
    private static byte[] key = {-62, 43, -112, -20, 77, 50, -110, -28, -28, 16, 48, 45, -33, 34, 3, 29};
    SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");


    EncryptedFileSaveManager(Context context){
        this.context = context;
    }

    void writeFile(String FILENAME, String[] data) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter
                    (context.openFileOutput(FILENAME, Context.MODE_PRIVATE)));
            for (String item : data) {
                bw.write(encrypt(item));
                bw.newLine();
            }
            bw.close();
            Log.d(LOG_TAG, "Файл записаний");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ArrayList<String> readFile(String FILENAME, boolean encrypt) {
        ArrayList<String> stringList = new ArrayList<String>();
        String encryptedString = "";
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(context.openFileInput(FILENAME)));
            String str = "";
            if(encrypt) {
                while ((str = br.readLine()) != null) {
                    encryptedString = decrypt(str);
                    stringList.add(encryptedString);
                }
            }
            else {
                while ((str = br.readLine()) != null) {
                    encryptedString = str;
                    stringList.add(encryptedString);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringList;
    }

    void writeFileIS(String FILENAME_SD, String DIR_SD, String[] data) {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "Обраний для запису розділ не доступний: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath()+ "/" + DIR_SD);
        // создаем каталог
        sdPath.mkdirs();
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FILENAME_SD);
        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            // пишем данные
            for (String item : data) {
                bw.write(encrypt(item));
                bw.newLine();
            }
            // закрываем поток
            bw.close();
            Log.d(LOG_TAG, "Файл записаний в пам'яті пристрою: " + sdFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ArrayList<String> readFileIS(String FILENAME_SD, String DIR_SD, boolean encrypt) {
        ArrayList<String> stringList = new ArrayList<String>();
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "Обраний розділ пам'яті не доступний: " + Environment.getExternalStorageState());
            return null;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FILENAME_SD);
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new FileReader(sdFile));
            String str = "";
            // читаем содержимое
            if(encrypt) {
                while ((str = br.readLine()) != null) {
                    stringList.add(decrypt(str));
                }
            }
            else {
                while ((str = br.readLine()) != null) {
                    stringList.add(str);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringList;
    }

    protected String encrypt(String value) {

        try {
            final byte[] bytes;
            if (value != null) {
                bytes = value.getBytes(UTF8);
            } else {
                bytes = new byte[0];
            }
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return new String(Base64.encode(cipher.doFinal(bytes), Base64.NO_WRAP), UTF8);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    protected String decrypt(String value) {
        try {
            final byte[] bytes;
            if (value != null) {
                bytes = Base64.decode(value, Base64.DEFAULT);
            } else {
                bytes = new byte[0];
            }
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return new String(cipher.doFinal(bytes), UTF8);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
