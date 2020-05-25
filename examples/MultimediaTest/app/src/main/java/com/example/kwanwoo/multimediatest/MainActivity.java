package com.example.kwanwoo.multimediatest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static String TAG="MultimediaTest";
    private ListView mListView;
    private int mSelectePoistion;
    private MediaItemAdapter mAdapter;
    private MediaPlayer mMediaPlayer;
    private MediaRecorder mMediaRecorder;
    private String mVideoFileName = null;
    private String mAudioFileName = null;
    private File mPhotoFile =null;
    private String mPhotoFileName = null;

    private int mPlaybackPosition = 0;   // media play 위치

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) // 액티비티가 재시작되는 경우, 기존에 저장한 상태 복구
            mPhotoFileName = savedInstanceState.getString("mPhotoFileName");

        final Button voiceRecBtn = findViewById(R.id.voiceRecBtn);
        Button videoRecBtn = findViewById(R.id.videoRecBtn);
        Button imageCaptureBtn = findViewById(R.id.imageCaptureBtn);
        Button imagePickBtn = findViewById(R.id.imagePickBtn);

        checkDangerousPermissions();
        initListView();

        voiceRecBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (mMediaRecorder == null) {
                    startAudioRec();
                    voiceRecBtn.setText("녹음중지");
                } else {
                    stopAudioRec();
                    voiceRecBtn.setText("음성녹음");
                }
            }
        });

        videoRecBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dispatchTakeVideoIntent();
            }
        });

        imageCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        imagePickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchPickPictureIntent();
            }
        });

    }

    final int  REQUEST_EXTERNAL_STORAGE_FOR_MULTIMEDIA=1;

    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_EXTERNAL_STORAGE_FOR_MULTIMEDIA);

        }

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // permission was granted
            switch (requestCode) {
                case REQUEST_EXTERNAL_STORAGE_FOR_MULTIMEDIA:
                    initListView();
                    break;
            }
        } else { // permission was denied
            Toast.makeText(getApplicationContext(),"접근 권한이 필요합니다",Toast.LENGTH_SHORT).show();
        }
    }

    private void initListView() {
        mListView = (ListView) findViewById(R.id.listView);
        ArrayList<MediaItem> mediaList = prepareDataSource();
        mAdapter = new MediaItemAdapter(this, R.layout.item, mediaList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (mMediaPlayer != null && mSelectePoistion == position) {
                    if (mMediaPlayer.isPlaying()) { // 현재 재생 중인 미디어를 선택한 경우
                        mPlaybackPosition = mMediaPlayer.getCurrentPosition();
                        mMediaPlayer.pause();
                        Toast.makeText(getApplicationContext(), "음악 파일 재생 중지됨.", Toast.LENGTH_SHORT).show();
                    } else {
                        mMediaPlayer.start();
                        mMediaPlayer.seekTo(mPlaybackPosition);
                        Toast.makeText(getApplicationContext(), "음악 파일 재생 재시작됨.", Toast.LENGTH_SHORT).show();
                    }
                } else {     // 현재 재생중인 미디어가 없거나, 다른 미디어를 선택한 경우
                    switch (((MediaItem) mAdapter.getItem(position)).type) {
                        case MediaItem.AUDIO:
                            try {
                                playAudio(((MediaItem) mAdapter.getItem(position)).uri);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(getApplicationContext(), "음악 파일 재생 시작됨.", Toast.LENGTH_SHORT).show();
                            break;
                        case MediaItem.VIDEO:
                            Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                            intent.putExtra("video_uri", ((MediaItem) mAdapter.getItem(position)).uri.toString());
                            startActivity(intent);
                            break;
                        case MediaItem.IMAGE:
                            Toast.makeText(getApplicationContext(), "이미지 항목 선.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    mSelectePoistion = position;
                }
            }
        });
    }

    private ArrayList<MediaItem> prepareDataSource() {
        final String AUDIO_URL1 = "http://www.robtowns.com/music/blind_willie.mp3";
        final String AUDIO_URL2 = "http://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3";

        final String VIDEO_URL ="https://kwanulee.github.io/AndroidProgramming/multimedia/media/makerton.mp4";
        ArrayList mediaList = new ArrayList<MediaItem>();

        // Raw 리소스 데이터 추가
        String rawResourceName1 = "instrumental";
        String rawResourceName2 = "scott_holmes_summer_fun";

        mediaList.add(new MediaItem(MediaItem.RAW,
                rawResourceName1 + ".mp3",
                Uri.parse("android.resource://" + getPackageName() + "/raw/" + rawResourceName1)));

        mediaList.add(new MediaItem(MediaItem.RAW,
                rawResourceName2 + ".mp3",
                Uri.parse("android.resource://" + getPackageName() + "/raw/" + rawResourceName2)));

        // Web 데이터 추가
        mediaList.add(new MediaItem(MediaItem.WEB, AUDIO_URL1, Uri.parse(AUDIO_URL1)));
        mediaList.add(new MediaItem(MediaItem.WEB, AUDIO_URL2, Uri.parse(AUDIO_URL2)));

        mediaList.add(new MediaItem(MediaItem.WEB, VIDEO_URL, Uri.parse(VIDEO_URL), MediaItem.VIDEO));

        // sdcard/Android/data/com.example.kwanwoo.multimediatest/fiels/Pictures 데이터 추가
        File file = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                Uri uri = Uri.parse("file://" + getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() +"/"+ f.getName());
                if (f.getName().contains(".jpg")) {
                    MediaItem item = new MediaItem(MediaItem.SDCARD, f.getName(), uri, MediaItem.IMAGE);
                    mediaList.add(item);
                }
            }
        }

        // sdcard/Music 데이터 추가
        file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                Log.i(TAG, "File name=" + f.getName());
                Uri uri = Uri.parse("file://" + Environment.getExternalStorageDirectory().getPath() + "/Music/" + f.getName());
                MediaItem item = new MediaItem(MediaItem.SDCARD, f.getName(), uri);
                mediaList.add(item);
            }
        }

        // sdcard/Android/data/com.example.kwanwoo.multimediatest/fiels/Movies 데이터 추가
        file = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                Log.i(TAG, "File name=" + f.getName());
                Uri uri = Uri.parse("file://" + getExternalFilesDir(Environment.DIRECTORY_MOVIES).getPath()  +"/"+ f.getName());

                MediaItem item = new MediaItem(MediaItem.SDCARD, f.getName(), uri, MediaItem.VIDEO);
                mediaList.add(item);
            }
        }
        return mediaList;
    }

    private void playAudio(Uri uri) throws Exception {
        killMediaPlayer();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setDataSource(getApplicationContext(), uri);
        mMediaPlayer.prepare();
        mMediaPlayer.start();
    }

    private void playAudioByURL(String url) throws Exception {
        killMediaPlayer();
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            Log.i(TAG, "url="+url);
        } catch (IllegalArgumentException e) {
            Log.i(TAG, "IllegalArgumentException");
            Toast.makeText(this, "IllegalArgumentException", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.i(TAG, "IOException:"+e.getMessage());
            Toast.makeText(this, "IOException", Toast.LENGTH_SHORT).show();
        }
    }

    private void startAudioRec()  {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        mAudioFileName = "VOICE" + currentDateFormat() + ".mp4";
        mMediaRecorder.setOutputFile(Environment.getExternalStorageDirectory().getPath() + "/Music/" + mAudioFileName);

        try {
            mMediaRecorder.prepare();
            Toast.makeText(getApplicationContext(), "녹음을 시작하세요.", Toast.LENGTH_SHORT).show();
            mMediaRecorder.start();
        } catch (Exception ex) {
            Log.e("SampleAudioRecorder", "Exception : ", ex);
        }
    }

    private void stopAudioRec()  {

        mMediaRecorder.stop();
        mMediaRecorder.release();
        mMediaRecorder = null;

        Uri uri = Uri.parse("file://" + Environment.getExternalStorageDirectory().getPath() + "/Music/"+ mAudioFileName);
        mAdapter.addItem(new MediaItem(MediaItem.SDCARD, mAudioFileName,uri));
        Toast.makeText(getApplicationContext(), "녹음이 중지되었습니다.", Toast.LENGTH_SHORT).show();

    }

    private String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }

    private void killMediaPlayer() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static final int REQUEST_IMAGE_PICK = 0;

    private void dispatchPickPictureIntent() {
        Intent pickPictureIntent = new Intent(Intent.ACTION_PICK);
        pickPictureIntent.setType("image/*");

        if (pickPictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pickPictureIntent,REQUEST_IMAGE_PICK);
        }
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //1. 카메라 앱으로 찍은 이미지를 저장할 파일 객체 생성
            mPhotoFileName = "IMG"+currentDateFormat()+".jpg";
            mPhotoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), mPhotoFileName);

            if (mPhotoFile !=null) {
                //2. 생성된 파일 객체에 대한 Uri 객체를 얻기
                Uri imageUri = FileProvider.getUriForFile(this, "com.example.kwanwoo.multimediatest", mPhotoFile);

                //3. Uri 객체를 Extras를 통해 카메라 앱으로 전달
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else
                Toast.makeText(getApplicationContext(), "file null", Toast.LENGTH_SHORT).show();
        }
    }

    static final int REQUEST_VIDEO_CAPTURE = 2;

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            //1. 카메라 앱으로 찍은 동영상을 저장할 파일 객체 생성
            mVideoFileName = "VIDEO"+currentDateFormat()+".mp4";
            File destination = new File(getExternalFilesDir(Environment.DIRECTORY_MOVIES), mVideoFileName);

            if (destination != null) {
                //2. 생성된 파일 객체에 대한 Uri 객체를 얻기
                Uri videoUri = FileProvider.getUriForFile(this, "com.example.kwanwoo.multimediatest", destination);

                //3. Uri 객체를 Extras를 통해 카메라 앱으로 전달
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            Uri imgUri = data.getData();
            try {
                Bitmap imgBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);

                mPhotoFileName = "IMG"+currentDateFormat()+".jpg";
                mPhotoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), mPhotoFileName);

                imgBitmap.compress(Bitmap.CompressFormat.JPEG,100,
                        new FileOutputStream(mPhotoFile));
                mAdapter.addItem(new MediaItem(MediaItem.SDCARD, mPhotoFileName, Uri.fromFile(mPhotoFile), MediaItem.IMAGE));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (mPhotoFileName != null) {
                mPhotoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), mPhotoFileName);
                mAdapter.addItem(new MediaItem(MediaItem.SDCARD, mPhotoFileName, Uri.fromFile(mPhotoFile), MediaItem.IMAGE));
            } else
                Toast.makeText(getApplicationContext(), "mPhotoFile is null", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            if (mVideoFileName != null) {
                File destination = new File(getExternalFilesDir(Environment.DIRECTORY_MOVIES), mVideoFileName);
                mAdapter.addItem(new MediaItem(MediaItem.SDCARD, mVideoFileName, Uri.fromFile(destination) ,MediaItem.VIDEO));
            } else
                Toast.makeText(getApplicationContext(), "!!! null video.", Toast.LENGTH_LONG).show();
        }
    }

    /*
        카메라 앱을 통해 이미지를 저장하고 다시 현재 앱으로 돌아오는 경우, 예기치 않게 액티비티가 재시작되는 경우
        기존 상태 (mPhotoFileName)을 저장하는 메소드. 안드로이드 프레임워크에 의해서 자동으로 호출
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("mPhotoFileName",mPhotoFileName);
        super.onSaveInstanceState(outState);
    }

    protected void onStop() {
        super.onStop();
        killMediaPlayer();
    }

    protected void onDestroy() {
        super.onDestroy();
        killMediaPlayer();
    }

}
