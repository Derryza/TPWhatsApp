package com.example.user.derryza;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_GALERI = 2;
    CircleImageView civ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton camera = (ImageButton)findViewById(R.id.cam);
        civ = (CircleImageView) findViewById(R.id.profile_image);
    }

    public void dialogCall(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Ganti Foto");
        builder.setMessage("Ambil foto profil baru dari mana?");
        builder.setPositiveButton("Galeri", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fotoGaleri();
            }
        }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNeutralButton("Kamera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dispatchTakePictureIntent();
            }
        });
        builder.show();
    }

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            civ.setImageBitmap(imageBitmap);
            civ.setMinimumWidth(200);
            civ.setMinimumHeight(200);
            View view = findViewById(R.id.root_view);
            Snackbar snackbar = Snackbar.make(view, "Foto anda telah diganti", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(), "Fitur undo belum jadi",Toast.LENGTH_SHORT).show();
                        }
                    });
            snackbar.show();
        }else if(requestCode==REQUEST_IMAGE_GALERI && resultCode==RESULT_OK){
            Bitmap bitmap = null;
            if (data != null){
                try{
                    bitmap = MediaStore.Images.Media.getBitmap(
                            getApplicationContext().getContentResolver(),
                            data.getData());
                }catch (IOException e){
                    e.printStackTrace();
                }
            }if (civ!=null){
                civ.setImageBitmap(bitmap);
                View view = findViewById(R.id.root_view);
                Snackbar snackbar = Snackbar.make(view, "Foto anda telah diganti", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(), "Fitur undo nelum jadi",Toast.LENGTH_SHORT).show();
                            }
                        });
                        snackbar.show();
            }
        }
    }

    public void fotoGaleri(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Foto"),2);
    }
}
