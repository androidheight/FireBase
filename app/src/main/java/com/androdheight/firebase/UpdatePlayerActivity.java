package com.androdheight.firebase;

import android.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androdheight.firebase.model.PlayerModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UpdatePlayerActivity extends AppCompatActivity {
    protected static final int CAMERA_REQUESTID = 1788;
    protected static final int GALLERY_PICTUREID = 1789;
    PackageManager packageManager;
    Context mContext;
    String imageEncoded="";
    private ImageView playerImage;
    private EditText etplayername,etplayermatch,etplayerinning,etplayerrun,
            etplayerwicket,playerBestRun,etplayerbestbowling,etplayerAvg;
    private Button btnUpdate;
    PlayerModel playerinfo;
    String id;

    DatabaseReference databasePlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_player);
        mContext = UpdatePlayerActivity.this;
        packageManager = mContext.getPackageManager();

        databasePlayers = FirebaseDatabase.getInstance().getReference("players");

        initilize();
        Bundle bundle =getIntent().getExtras();

        playerinfo = (PlayerModel) bundle.getSerializable("player");

        if(playerinfo != null){
            setValue();
        }

        playerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    startDialog(CAMERA_REQUESTID, GALLERY_PICTUREID);
                } else {
                    Toast.makeText(UpdatePlayerActivity.this, "You Dont Have Camera in Your Device", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFirebaseDB();
            }
        });
    }

    private  void initilize() {
        playerImage = (ImageView)findViewById(R.id.playerImage);
        etplayername = (EditText) findViewById(R.id.etplayername);
        etplayermatch = (EditText) findViewById(R.id.etplayermatch);
        etplayerinning = (EditText) findViewById(R.id.etplayerinning);
        etplayerrun = (EditText) findViewById(R.id.etplayerrun);
        etplayerwicket = (EditText) findViewById(R.id.etplayerwicket);
        playerBestRun = (EditText) findViewById(R.id.etplayerBestRun);
        etplayerbestbowling = (EditText) findViewById(R.id.etplayerbestbowling);
        etplayerAvg = (EditText) findViewById(R.id.etplayerAvg);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
    }

    private void setValue(){

        byte[] dec = Base64.decode(playerinfo.getPlayerImage(),Base64.DEFAULT);
        if(dec!= null ) {
            Bitmap decodeByte = BitmapFactory.decodeByteArray(dec,
                    0, dec.length);
            if (decodeByte!= null)
            playerImage.setImageBitmap(decodeByte);

        }
        etplayername.setText(playerinfo.getPlayer_name());
        etplayermatch.setText(playerinfo.getPlayer_match());
        etplayerinning.setText(playerinfo.getPlayer_inning());
        etplayerrun.setText(playerinfo.getPlayer_run());
        etplayerwicket.setText(playerinfo.getPlayer_wicket());
        playerBestRun.setText(playerinfo.getPlayer_bestRun());
        etplayerbestbowling.setText(playerinfo.getPlayer_bestBowling());
        etplayerAvg.setText(playerinfo.getAverage());

    }

    private void updateFirebaseDB(){
        int pid = playerinfo.getP_id();
        String name = etplayername.getText().toString();
        String match = etplayermatch.getText().toString();
        String inning = etplayerinning.getText().toString();
        String run = etplayerrun.getText().toString();
        String wicket = etplayerwicket.getText().toString();
        String bestRun = playerBestRun.getText().toString();
        String bestBowling = etplayerbestbowling.getText().toString();
        String avg = etplayerAvg.getText().toString();


        id =playerinfo.getParentid();

        PlayerModel model = new PlayerModel();
        model.setPlayerImage(imageEncoded);
        model.setPlayer_name(name);
        model.setPlayer_match(match);
        model.setPlayer_inning(inning);
        model.setPlayer_run(run);
        model.setPlayer_wicket(wicket);
        model.setPlayer_bestRun(bestRun);
        model.setPlayer_bestBowling(bestBowling);
        model.setAverage(avg);
        model.setParentid(id);

        if (!TextUtils.isEmpty(id)) {
            

            databasePlayers.child(id).setValue(model);

            Toast.makeText(UpdatePlayerActivity.this,"Value updated successfully",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bmp;
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == CAMERA_REQUESTID && resultCode == Activity.RESULT_OK && data != null) {
                bmp = (Bitmap) data.getExtras().get("data");
                String encodedBase64String= encodeTobase64(bmp);

            }
            if (requestCode == GALLERY_PICTUREID && resultCode == Activity.RESULT_OK && data != null) {
                Uri uri = data.getData();
                try {
                    bmp = MediaStore.Images.Media.getBitmap(UpdatePlayerActivity.this.getContentResolver(), uri);
                    String encodedBase64String = encodeTobase64(bmp);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }


    public String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] b = baos.toByteArray();
        imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        playerImage.setImageBitmap(image);

        return imageEncoded;
    }



    //////////////////Dialog for choose type///////////

    private void startDialog(final int cameraRequestaddress, final int galleryPictureaddress) {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(UpdatePlayerActivity.this);
        myAlertDialog.setTitle("Upload Image");
        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent pictureActionIntent = null;

                        pictureActionIntent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        pictureActionIntent.setType("image/jpeg");
                        startActivityForResult(pictureActionIntent, galleryPictureaddress);

                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                        if (currentapiVersion >= Build.VERSION_CODES.M){

                            getPermissionForCamera();
                            // Toast.makeText(RegisterPhoto.this,"Marshmellow",Toast.LENGTH_LONG).show();

                        }else {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            //cameraIntent.setType("image/jpeg");
                            startActivityForResult(cameraIntent, cameraRequestaddress);
                        }

                    }
                });
        myAlertDialog.show();
    }



    @SuppressLint("NewApi")
    public void getPermissionForCamera() {

        if (ContextCompat.checkSelfPermission(UpdatePlayerActivity.this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {


            if (shouldShowRequestPermissionRationale(
                    android.Manifest.permission.CAMERA)) {
                // Show our own UI to explain to the user why we need to read the contacts
                // before actually requesting the permission and showing the default UI
            }

            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                    CAMERA_REQUESTID);
        }else{
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUESTID);

        }
    }

    // Callback with the request from calling requestPermissions(...)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == CAMERA_REQUESTID) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //cameraIntent.setType("image/jpeg");
                startActivityForResult(cameraIntent, CAMERA_REQUESTID);
                // Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UpdatePlayerActivity.this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
