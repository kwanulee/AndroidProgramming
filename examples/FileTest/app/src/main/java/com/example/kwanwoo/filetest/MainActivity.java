package com.example.kwanwoo.filetest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {
    final static String TAG = "FILETEST";
    private TextView result;
    private EditText input;

    final int REQUEST_SAVETO_EXTERNAL_STORAGE = 1;
    final int REQUEST_LOADFROM_EXTERNAL_STORAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = (EditText) findViewById(R.id.editText1);
        result = (TextView) findViewById(R.id.textView1);
        Button iSave = (Button) findViewById(R.id.button1);
        Button iLoad = (Button) findViewById(R.id.button2);
        Button rawLoad = (Button) findViewById(R.id.button3);
        Button eSave = (Button) findViewById(R.id.button4);
        Button eLoad = (Button) findViewById(R.id.button5);


        iSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToInternalStorage();
            }
        });

        iLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               loadFromIntenalStorage();
            }
        });

        rawLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               loadFromRawResource();
            }
        });


        eSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isExternalStorageWritable())
                    return;     // 외부메모리를 사용하지 못하면 끝냄

                String[] PERMISSIONS_STORAGE = {
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };

                if (ContextCompat.checkSelfPermission(MainActivity.this,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            PERMISSIONS_STORAGE,
                            REQUEST_SAVETO_EXTERNAL_STORAGE
                    );
                } else {
                    saveToExtenalStorage();
                }
            }
        });

        eLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isExternalStorageReadable())
                    return;     // 외부메모리를 사용하지 못하면 끝냄

                String[] PERMISSIONS_STORAGE = {
                        Manifest.permission.READ_EXTERNAL_STORAGE
                };

                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            PERMISSIONS_STORAGE,
                            REQUEST_LOADFROM_EXTERNAL_STORAGE
                    );
                } else {
                    loadFromExternalStorage();
                }
            }
        });
    }

    private void saveToInternalStorage() {
        String data = input.getText().toString();

        try {
            FileOutputStream fos = openFileOutput
                                        ("myfile.txt", // 파일명 지정
                                        Context.MODE_APPEND);// 저장모드
            PrintWriter out = new PrintWriter(fos);
            out.println(data);
            out.close();

            result.setText("file saved");
        } catch (Exception e) {
            result.setText("Exception: internal file writing");
        }
    }

    private void loadFromIntenalStorage() {
        try {
            FileInputStream fis = openFileInput("myfile.txt");//파일명
            BufferedReader buffer = new BufferedReader
                    (new InputStreamReader(fis));
            String str = buffer.readLine(); // 파일에서 한줄을 읽어옴

            // 파일에서 읽은 데이터를 저장하기 위해서 만든 변수
            StringBuffer data = new StringBuffer();
            while (str != null) {
                data.append(str + "\n");
                str = buffer.readLine();
            }
            buffer.close();
            result.setText(data);
        } catch (FileNotFoundException e) {
            result.setText("File Not Found");
        } catch (Exception e) {
            result.setText("Exception: internal file reading");
        }
    }

    private void loadFromRawResource() {
        try {
            InputStream is = getResources().openRawResource(R.raw.description);
            BufferedReader buffer = new BufferedReader
                    (new InputStreamReader(is));

            String str = buffer.readLine(); // 파일에서 한줄을 읽어옴

            // 파일에서 읽은 데이터를 저장하기 위해서 만든 변수
            StringBuffer data = new StringBuffer();

            while (str != null) {
                data.append(str + "\n");
                str = buffer.readLine();
            }
            buffer.close();
            result.setText(data);
        } catch (Exception e) {
            result.setText("Exception: raw resource file reading");
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            result.setText("외부메모리 읽기 쓰기 모두 가능");
            return true;
        }
        result.setText("외부메모리 마운트 안됨");
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            result.setText("외부메모리 읽기만 가능");
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case REQUEST_SAVETO_EXTERNAL_STORAGE:
                    saveToExtenalStorage();
                    break;
                case REQUEST_LOADFROM_EXTERNAL_STORAGE:
                    loadFromExternalStorage();
                    break;
            }

        } else {
            Toast.makeText(getApplicationContext(),"접근 권한이 필요합니다",Toast.LENGTH_SHORT).show();
        }
    }


    private void saveToExtenalStorage() {
        String data = input.getText().toString();
        Log.i(TAG, getLocalClassName() + ":file save start");
        try {
            // 공유 디렉토리 (sdcard/Download) 사용할 경우
            File path = Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_DOWNLOADS);

            //  앱 전용 저장소 (sdcard/Android/data/com.example.kwanwoo.filetest/files/를 사용할 경우
//          File path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);


            File f = new File(path, "external.txt"); // 경로, 파일명
            FileWriter write = new FileWriter(f, true);

            PrintWriter out = new PrintWriter(write);
            out.println(data);
            out.close();
            result.setText("저장완료");
            Log.i(TAG, getLocalClassName() + ":file saved");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadFromExternalStorage(){
        try {
            // 공유 디렉토리 (sdcard/Download) 사용할 경우
            File path = Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_DOWNLOADS);

            //  앱 전용 저장소 (sdcard/Android/data/com.example.kwanwoo.filetest/files/를 사용할 경우
//            File path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);

            File f = new File(path, "external.txt");
            StringBuffer data = new StringBuffer();

            BufferedReader buffer = new BufferedReader
                    (new FileReader(f));
            String str = buffer.readLine();
            while (str != null) {
                data.append(str + "\n");
                str = buffer.readLine();
            }
            result.setText(data);
            buffer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
