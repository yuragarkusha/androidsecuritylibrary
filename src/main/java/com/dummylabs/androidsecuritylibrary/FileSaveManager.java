package com.dummylabs.androidsecuritylibrary;

import android.content.Context;
import android.os.Environment;
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

public class FileSaveManager {

    final String LOG_TAG = "FileSaveManagerLog";
    private Context context;

    FileSaveManager(Context context){
        this.context = context;
    }

    void writeFile(String FILENAME, String[] data) {
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(context.openFileOutput(FILENAME, Context.MODE_PRIVATE)));
            // пишем данные
            for (String item : data) {
                bw.write(item);
                bw.newLine();
            }
            // закрываем поток
            bw.close();
            Log.d(LOG_TAG, "Файл записаний");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ArrayList<String> readFile(String FILENAME) {
        ArrayList<String> stringList = new ArrayList<String>();
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(context.openFileInput(FILENAME)));
            String str = "";
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                stringList.add(str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringList;
    }

//    public void write(ArrayList<String> information){
//        BufferedWriter bufferedWriter = null;
//        try {
//            bufferedWriter = new BufferedWriter(new OutputStreamWriter
//                    (context.openFileOutput("File", Context.MODE_PRIVATE)));
//            for (String item : information) {
//                bufferedWriter.write(item);
//                bufferedWriter.newLine();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    ArrayList<String> read() {
        ArrayList<String> stringList = new ArrayList<String>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(context.openFileInput("File")));
            String str = "";
            while ((str = br.readLine()) != null) {
                stringList.add(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringList;
    }

    void writeFileSD(String FILENAME_SD, String DIR_SD, String[] data) {
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
                bw.write(item);
                bw.newLine();
            }
            // закрываем поток
            bw.close();
            Log.d(LOG_TAG, "Файл записаний в пам'яті пристрою: " + sdFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ArrayList<String> readFileSD(String FILENAME_SD, String DIR_SD) {
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
            while ((str = br.readLine()) != null) {
                stringList.add(str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringList;
    }
}

