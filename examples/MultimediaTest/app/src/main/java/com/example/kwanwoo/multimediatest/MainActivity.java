package com.example.kwanwoo.multimediatest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
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
    private static String TAG = "MultimediaTest";
    static final int REQUEST_IMAGE_PICK = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_VIDEO_CAPTURE = 2;
    static final int REQUEST_RECORD_AUDIO_FOR_MULTIMEDIA = 3;

    private ListView mListView;
    private int mSelectedPosition;
    private MediaItemAdapter mAdapter;
    private MediaPlayer mMediaPlayer;
    private MediaRecorder mMediaRecorder;
    private String mVideoFileName = null;
    private String mAudioFileName = null;
    private String mPhotoFileName = null;
    private File mPhotoFile = null;

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

        mMediaPlayer = new MediaPlayer();

        if (haveRecordAudioPermission())
            initListView();
        else
            requestRecordAudioPermission();

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

    private boolean haveRecordAudioPermission() {
        return ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED;
    }


    private void requestRecordAudioPermission() {
        String[] permissions = {
                Manifest.permission.RECORD_AUDIO
        };
        ActivityCompat.requestPermissions(
                this,
                permissions,
                REQUEST_RECORD_AUDIO_FOR_MULTIMEDIA);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // permission was granted
            switch (requestCode) {
                case REQUEST_RECORD_AUDIO_FOR_MULTIMEDIA:
                    initListView();
                    break;
            }
        } else { // permission was denied
            Toast.makeText(getApplicationContext(), "접근 권한이 필요합니다", Toast.LENGTH_SHORT).show();
        }
    }

    private void initListView() {
        mListView = findViewById(R.id.listView);
        ArrayList<MediaItem> mediaList = prepareDataSource();
        mAdapter = new MediaItemAdapter(this, R.layout.item, mediaList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MediaItem item = (MediaItem) mAdapter.getItem(position);
                switch (item.type) {
                    case MediaItem.AUDIO:
                        if (mMediaPlayer != null && mSelectedPosition == position) {  // 이전 선택과 동일한 항목을 선택한 경우
                            if (mMediaPlayer.isPlaying()) { // 미디어가 재생 중인 경우
                                mPlaybackPosition = mMediaPlayer.getCurrentPosition();
                                mMediaPlayer.pause();
                                Toast.makeText(getApplicationContext(), "음악 파일 재생 중지됨.", Toast.LENGTH_SHORT).show();
                            } else {
                                mMediaPlayer.seekTo(mPlaybackPosition);
                                mMediaPlayer.start();
                                Toast.makeText(getApplicationContext(), "음악 파일 재생 재시작됨.", Toast.LENGTH_SHORT).show();
                            }
                        } else {    // 생성된 미디어가 없거나, 이전 선택과 다른 항목(미디어)을 선택한 경우
                            releaseMediaPlayer();  // 기존 미디어 리소스 해
                            switch (item.source) {
                                case MediaItem.RAW:
                                    int resourceId = getResources().getIdentifier(item.name, "raw", getPackageName());
                                    playAudioByRawResource(resourceId);
                                    break;
                                case MediaItem.WEB:
                                    playAudioByURL(item.name);
                                    break;
                                case MediaItem.SDCARD:
                                    Uri uri = Uri.parse("file://" + getExternalFilesDir(Environment.DIRECTORY_MUSIC).getPath() + "/" + item.name);
                                    playAudioByUri(uri);
                                    break;
                            }
                            Toast.makeText(getApplicationContext(), "음악 파일 재생 시작됨.", Toast.LENGTH_SHORT).show();
                            mSelectedPosition = position;
                            break;
                        }
                        break;
                    case MediaItem.VIDEO:
                        Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                        Uri uri = null;
                        switch (item.source) {
                            case MediaItem.WEB:
                                uri = Uri.parse(item.name);
                                break;
                            case MediaItem.SDCARD:
                                uri = Uri.parse("file://" + getExternalFilesDir(Environment.DIRECTORY_MOVIES).getPath() + "/" + item.name);
                                break;
                            case MediaItem.SHARED:

                        }

                        intent.putExtra("video_uri", uri.toString());
                        startActivity(intent);
                        break;
                    case MediaItem.IMAGE:
                        ImageView imageView = findViewById(R.id.imageView);

                        switch (item.source) {
                            case MediaItem.GALLERY:
                                uri = Uri.parse(item.name);
                                break;
                            case MediaItem.SDCARD:
                                uri = Uri.parse("file://" + getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + "/" + item.name);
                                break;
                            default:
                                uri = Uri.parse("android.resource://" + getPackageName() + "/drawable/ic_launcher_foreground");
                        }

                        imageView.setImageURI(uri);
                        break;
                }


            }
        });
    }

    private ArrayList<MediaItem> prepareDataSource() {
        final String AUDIO_URL = "http://www.robtowns.com/music/blind_willie.mp3";

        final String VIDEO_URL = "https://kwanulee.github.io/AndroidProgramming/multimedia/media/makerton.mp4";
        ArrayList<MediaItem> mediaList = new ArrayList<MediaItem>();

        // Raw 리소스 데이터 추가
        String rawResourceName1 = "instrumental";
        String rawResourceName2 = "scott_holmes_summer_fun";

        // Raw 리소스 데이터 추가
        mediaList.add(new MediaItem(MediaItem.RAW, rawResourceName1, MediaItem.AUDIO));
        mediaList.add(new MediaItem(MediaItem.RAW, rawResourceName2, MediaItem.AUDIO));

        // Web 데이터 추가
        mediaList.add(new MediaItem(MediaItem.WEB, AUDIO_URL, MediaItem.AUDIO));
        mediaList.add(new MediaItem(MediaItem.WEB, VIDEO_URL, MediaItem.VIDEO));

        // sdcard/Android/data/com.example.kwanwoo.multimediatest/fiels/Pictures 데이터 추가
        File file = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                Uri uri = Uri.parse("file://" + getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + "/" + f.getName());
                if (f.getName().contains(".jpg")) {
                    MediaItem item = new MediaItem(MediaItem.SDCARD, f.getName(), MediaItem.IMAGE);
                    mediaList.add(item);
                }
            }
        }

        // sdcard/Android/data/com.example.kwanwoo.multimediatest/fiels/Music 데이터 추가
        file = getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                Log.i(TAG, "File name=" + f.getName());
                Uri uri = Uri.parse("file://" + getExternalFilesDir(Environment.DIRECTORY_MUSIC).getPath() + "/" + f.getName());
                MediaItem item = new MediaItem(MediaItem.SDCARD, f.getName(), MediaItem.AUDIO);
                mediaList.add(item);
            }
        }

        // sdcard/Android/data/com.example.kwanwoo.multimediatest/fiels/Movies 데이터 추가
        file = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                Log.i(TAG, "File name=" + f.getName());
                Uri uri = Uri.parse("file://" + getExternalFilesDir(Environment.DIRECTORY_MOVIES).getPath() + "/" + f.getName());

                MediaItem item = new MediaItem(MediaItem.SDCARD, f.getName(), MediaItem.VIDEO);
                mediaList.add(item);
            }
        }
        return mediaList;
    }

    private void playAudioByRawResource(int resource) {
        mMediaPlayer = MediaPlayer.create(this, resource);
        mMediaPlayer.start();
    }

    private void playAudioByUri(Uri uri) {

        mMediaPlayer = new MediaPlayer();

        try {
            mMediaPlayer.setDataSource(getApplicationContext(), uri);
            mMediaPlayer.prepare();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
    }

    private void playAudioByURL(String url) {

        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer.start();
            }
        });
        mMediaPlayer.prepareAsync();
    }

    private void startAudioRec() {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        mAudioFileName = "VOICE" + currentDateFormat() + ".mp3";
        mMediaRecorder.setOutputFile(getExternalFilesDir(Environment.DIRECTORY_MUSIC).getPath() + "/" + mAudioFileName);

        try {
            mMediaRecorder.prepare();
            Toast.makeText(getApplicationContext(), "녹음을 시작하세요.", Toast.LENGTH_SHORT).show();
            mMediaRecorder.start();
        } catch (Exception ex) {
            Log.e("SampleAudioRecorder", "Exception : ", ex);
        }
    }

    private void stopAudioRec() {

        mMediaRecorder.stop();
        mMediaRecorder.release();
        mMediaRecorder = null;

        Uri uri = Uri.parse("file://" + getExternalFilesDir(Environment.DIRECTORY_MUSIC).getPath() + "/" + mAudioFileName);
        mAdapter.addItem(new MediaItem(MediaItem.SDCARD, mAudioFileName, MediaItem.AUDIO));
        Toast.makeText(getApplicationContext(), "녹음이 중지되었습니다.", Toast.LENGTH_SHORT).show();

    }

    private String currentDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }

    private void releaseMediaPlayer() {
        // 기존 미디어 플레이어가 존재하는 경우 리소스를 해제하고 초기화
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.release();
                mMediaPlayer = null;
            } catch (Exception e) {
                Log.i(TAG,"Execption during mMediaPlayer.release()");
            }
        }
    }

    private void dispatchPickPictureIntent() {
        Intent pickPictureIntent = new Intent(Intent.ACTION_PICK);
        pickPictureIntent.setType("image/*");

        if (pickPictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pickPictureIntent, REQUEST_IMAGE_PICK);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //1. 카메라 앱으로 찍은 이미지를 저장할 파일 객체 생성
            mPhotoFileName = "IMG" + currentDateFormat() + ".jpg";
            mPhotoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), mPhotoFileName);

            if (mPhotoFile != null) {
                //2. 생성된 파일 객체에 대한 Uri 객체를 얻기
                Uri imageUri = FileProvider.getUriForFile(this, "com.example.kwanwoo.multimediatest.fileprovider", mPhotoFile);

                //3. Uri 객체를 Extras를 통해 카메라 앱으로 전달
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else
                Toast.makeText(getApplicationContext(), "file null", Toast.LENGTH_SHORT).show();
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            //1. 카메라 앱으로 찍은 동영상을 저장할 파일 객체 생성
            mVideoFileName = "VIDEO" + currentDateFormat() + ".mp4";
            File destination = new File(getExternalFilesDir(Environment.DIRECTORY_MOVIES), mVideoFileName);

            if (destination != null) {
                //2. 생성된 파일 객체에 대한 Uri 객체를 얻기
                Uri videoUri = FileProvider.getUriForFile(this, "com.example.kwanwoo.multimediatest.fileprovider", destination);

                //3. Uri 객체를 Extras를 통해 카메라 앱으로 전달
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            Uri imgUri = data.getData();

            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageURI(imgUri);

            //saveToExternalFile(imgUri);

            mAdapter.addItem(new MediaItem(MediaItem.GALLERY, imgUri.toString(), MediaItem.IMAGE));
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (mPhotoFileName != null) {
                mPhotoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), mPhotoFileName);

                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageURI(Uri.fromFile(mPhotoFile));

                mAdapter.addItem(new MediaItem(MediaItem.SDCARD, mPhotoFileName, MediaItem.IMAGE));
            } else
                Toast.makeText(getApplicationContext(), "mPhotoFile is null", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            if (mVideoFileName != null) {
                mAdapter.addItem(new MediaItem(MediaItem.SDCARD, mVideoFileName, MediaItem.VIDEO));
            } else
                Toast.makeText(getApplicationContext(), "!!! null video.", Toast.LENGTH_LONG).show();
        }
    }

    private void saveToExternalFile(Uri imgUri) {
        try {
            Bitmap imgBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);

            mPhotoFileName = "IMG"+currentDateFormat()+".jpg";
            mPhotoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), mPhotoFileName);

            imgBitmap.compress(Bitmap.CompressFormat.JPEG,100,
                    new FileOutputStream(mPhotoFile));
        } catch (IOException e) {}
    }

    /*
        카메라 앱을 통해 이미지를 저장하고 다시 현재 앱으로 돌아오는 경우, 예기치 않게 액티비티가 재시작되는 경우
        기존 상태 (mPhotoFileName)을 저장하는 메소드. 안드로이드 프레임워크에 의해서 자동으로 호출
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("mPhotoFileName", mPhotoFileName);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

}
