package com.example.mediastoretest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.RecoverableSecurityException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    TextView mId;
    EditText mName;
    TextView mOwner;
    ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mId = findViewById(R.id._id);
        mName = findViewById(R.id.edit_name);
        mOwner = findViewById(R.id.tv_owner);
        mImage = findViewById(R.id.image);

        Button query = findViewById(R.id.queryall);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!haveStoragePermission()) {
                    requestStoragePermission();
                } else {
                    getImagesFromMediaStore();
                }
            }
        });

        Button copy = findViewById(R.id.copy);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCopiedImageToMediaStore();
                getImagesFromMediaStore();
            }
        });

        Button delete = findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImageFromMediaStore();
                getImagesFromMediaStore();
            }
        });

        Button update = findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateImageFromMediaStore();
                getImagesFromMediaStore();
            }
        });
    }

    private boolean haveStoragePermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE
        )
                == PackageManager.PERMISSION_GRANTED;
    }

    private final int READ_EXTERNAL_STORAGE_REQUEST = 1;
    private void requestStoragePermission() {
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        ActivityCompat.requestPermissions(
                MainActivity.this,
                PERMISSIONS_STORAGE,
                READ_EXTERNAL_STORAGE_REQUEST
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case READ_EXTERNAL_STORAGE_REQUEST:
                    getImagesFromMediaStore();
                    break;
            }
        } else {
            Toast.makeText(getApplicationContext(),"접근 권한이 필요합니다",Toast.LENGTH_SHORT).show();
        }
    }

    private void getImagesFromMediaStore() {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = new String[] {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.OWNER_PACKAGE_NAME
        };

        String selection = MediaStore.Images.Media.MIME_TYPE + " = ?";
        String[] selectionArgs = new String[] {
                "image/jpeg"
        };

        Cursor cursor = getApplicationContext().getContentResolver().query(
                uri,
                projection,
                selection,
                selectionArgs,
                null
        );
//        StringBuffer data = new StringBuffer();
//        int idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
//        int nameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
//        int mineTypeColumn = cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE);
//        while (cursor.moveToNext()) {
//            long id = cursor.getLong(idColumn);
//            String name = cursor.getString(nameColumn);
//            String mineType = cursor.getString(mineTypeColumn);
//            data.append(id +" "+name+" ["+mineType+"]\n");
//        }

        ListView listView = findViewById(R.id.listview);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                                            getApplicationContext(),
                                            R.layout.item,
                                            cursor,
                                            new String[] {
                                                    MediaStore.Images.Media._ID,
                                                    MediaStore.Images.Media.DISPLAY_NAME,
                                                    MediaStore.Images.Media.OWNER_PACKAGE_NAME
                                            },
                                            new int[] {
                                                    R.id.item_id,
                                                    R.id.item_name,
                                                    R.id.item_owner
                                            },
                                            0
                                        );

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Adapter adapter = parent.getAdapter();

                String idStr = ((Cursor)adapter.getItem(position)).getString(0);
                mId.setText(((Cursor)adapter.getItem(position)).getString(0));
                mName.setText(((Cursor)adapter.getItem(position)).getString(1));
                mOwner.setText(((Cursor)adapter.getItem(position)).getString(2));

                Uri imageCollection = MediaStore.Images.Media.getContentUri(
                        MediaStore.VOLUME_EXTERNAL_PRIMARY);
                Uri imageUri = ContentUris.withAppendedId(imageCollection,
                        Long.valueOf(idStr));
                mImage.setImageURI(imageUri);
            }
        });

    }

    public void addCopiedImageToMediaStore() {
        ContentResolver resolver = getApplicationContext()
                .getContentResolver();
        Uri imageCollection = MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL_PRIMARY);

        String imgName = mName.getText().toString();
        String imgOwner = mOwner.getText().toString();

        ContentValues newImageDetails = new ContentValues();
        newImageDetails.put(MediaStore.Images.Media.DISPLAY_NAME, imgName);
        newImageDetails.put(MediaStore.Images.Media.OWNER_PACKAGE_NAME,imgOwner);
        newImageDetails.put(MediaStore.Images.Media.IS_PENDING, 1);

        Uri newImageUri = resolver.insert(imageCollection, newImageDetails);

        try {
            OutputStream out = resolver.openOutputStream(newImageUri);
            InputStream in = resolver.openInputStream(
                    ContentUris.withAppendedId(
                            imageCollection,                        // image collection uri
                            Long.valueOf(mId.getText().toString())) // 복사할 image id
            );

            int size = in.available();
            byte[] bytes = new byte[size];
            in.read(bytes);
            out.write(bytes);

            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        newImageDetails.clear();
        newImageDetails.put(MediaStore.Images.Media.IS_PENDING, 0);
        resolver.update(newImageUri, newImageDetails, null, null);
    }

    public void deleteImageFromMediaStore() {
        ContentResolver resolver = getApplicationContext().getContentResolver();
        Uri imageCollection = MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL_PRIMARY);
        Long imageId = Long.valueOf(mId.getText().toString());
        Uri imageUri = ContentUris.withAppendedId(
                imageCollection,imageId);
        try {
            int numImagesRemoved = resolver.delete(imageUri, null, null);
        }catch(SecurityException securityException) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                RecoverableSecurityException recoverableSecurityException;
                if (securityException instanceof RecoverableSecurityException) {
                    recoverableSecurityException =
                            (RecoverableSecurityException) securityException;
                } else {
                    throw new RuntimeException(
                            securityException.getMessage(), securityException);
                }
                IntentSender intentSender = recoverableSecurityException.getUserAction()
                        .getActionIntent().getIntentSender();
                try {
                    startIntentSenderForResult(intentSender, 1,
                            null, 0, 0, 0, null);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            } else {
                throw new RuntimeException(
                        securityException.getMessage(), securityException);
            }
        }
    }



    public void updateImageFromMediaStore() {
        ContentResolver resolver = getApplicationContext()
                .getContentResolver();

        Uri imageCollection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Long imageId = Long.valueOf(mId.getText().toString());
        Uri imageUri = ContentUris.withAppendedId(
                imageCollection,imageId);

        ContentValues updatedImgDetails = new ContentValues();

        updatedImgDetails.put(MediaStore.Images.Media.DISPLAY_NAME,
                mName.getText().toString());
        int numImagesUpdated=0;
        try {
            numImagesUpdated = resolver.update(
                    imageUri,
                    updatedImgDetails,
                    null,
                    null);
        } catch(SecurityException securityException) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                RecoverableSecurityException recoverableSecurityException;
                if (securityException instanceof RecoverableSecurityException) {
                    recoverableSecurityException =
                            (RecoverableSecurityException)securityException;
                } else {
                    throw new RuntimeException(
                            securityException.getMessage(), securityException);
                }
                IntentSender intentSender =recoverableSecurityException.getUserAction()
                        .getActionIntent().getIntentSender();
                try {
                    startIntentSenderForResult(intentSender, 1,
                            null, 0, 0, 0, null);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            } else {
                throw new RuntimeException(
                        securityException.getMessage(), securityException);
            }
        }
    }

}