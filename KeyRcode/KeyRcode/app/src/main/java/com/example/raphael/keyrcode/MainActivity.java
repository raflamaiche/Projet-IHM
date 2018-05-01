package com.example.raphael.keyrcode;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;
import static com.example.raphael.keyrcode.read.read4QRcode;
import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_TAKE_PHOTO = 1;
    private Button buttonCreate;
    private Button buttonRead;
    private Button buttonVall;
    private EditText Key;
    private CheckBox HG, HD, BG, BD;
    private ImageView createResult;
    private Button menu;
    private ImageView Photo;
    private TextView keyResult;
    private String mCurrentPhotoPath;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void galleryAddPic(String path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }

        }
    }


    private View.OnClickListener btnCreateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("debug","button Create Cliqué");
            setContentView(R.layout.create);
            menu = findViewById(R.id.buttonMenu);
            buttonVall = findViewById(R.id.buttonValider);
            Key = findViewById(R.id.newKey);
            HG = findViewById(R.id.checkBoxHG);
            HD = findViewById(R.id.checkBoxHD);
            BG = findViewById(R.id.checkBoxBG);
            BD = findViewById(R.id.checkBoxBD);
            menu.setOnClickListener(btnMenuListener);
            buttonVall.setOnClickListener(btnVallListener);

        }
    };

    private View.OnClickListener btnReadListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("debug","button Read Cliqué");
            dispatchTakePictureIntent();

        }
    };

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "ImagePrise";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            setContentView(R.layout.read);
            Photo = (ImageView) findViewById(R.id.Photo);
            menu = findViewById(R.id.buttonMenu);
            keyResult = findViewById(R.id.keyResult);
            menu.setOnClickListener(btnMenuListener);

            int targetW = 1000;
            int targetH = 1000;
            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;
            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;
            Bitmap imageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

            CharSequence key = read4QRcode(imageBitmap);
            Photo.setImageBitmap(imageBitmap);
            Log.i("debug","key: "+ key);
            keyResult.setText(key);

            File file = new File(mCurrentPhotoPath);
            file.delete();

        }
    }

    private View.OnClickListener btnMenuListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("debug","button Menu Cliqué");
            setContentView(R.layout.activity_main);
            buttonCreate = findViewById(R.id.buttonCreate);
            buttonRead =  findViewById(R.id.buttonRead);
            buttonCreate.setOnClickListener(btnCreateListener);
            buttonRead.setOnClickListener(btnReadListener);
        }
    };

    private View.OnClickListener btnVallListener = new View.OnClickListener()  {
        @Override
        public void onClick(View v) {
            boolean hg = HG.isChecked();
            boolean hd = HD.isChecked();
            boolean bg = BG.isChecked();
            boolean bd = BD.isChecked();
            if(hg || hd || bg || bd) {

                setContentView(R.layout.resultcreate);
                menu = findViewById(R.id.buttonMenu);
                createResult = (ImageView) findViewById(R.id.viewR);
                menu.setOnClickListener(btnMenuListener);

                try {
                    Log.i("debug", "button valider Cliqué");
                    String cle = Key.getText().toString();
                    Log.i("debug", "cle: " + cle + " boox: " + hg + hd + bg + bd);
                    Bitmap result = Create.create4QRcode("code", cle, hg, hd, bg, bd);
                    String path = Create.StoreBitmapImage(result, 100);
                    galleryAddPic(path);
                    createResult.setImageBitmap(result);
                    Log.i("debug", "passage par lflfllf");
                } catch (IOException e) {
                    Log.i("debug", "erreur enregistrement");
                }

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         buttonCreate = findViewById(R.id.buttonCreate);
         buttonRead =  findViewById(R.id.buttonRead);
         buttonCreate.setOnClickListener(btnCreateListener);
         buttonRead.setOnClickListener(btnReadListener);
    }

}

