package com.example.mypet;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
public class CargarImagen extends AppCompatActivity {
    private ImageView ivPet;
    Uri uri;
    String id,foto="";
EditText e;
Uri path;
    Bitmap bitmapFinal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargar_imagen);
        ivPet = (ImageView) findViewById(R.id.ivPet);
        ivPet.setImageResource(R.drawable.signo);
        id = getIntent().getStringExtra("id");

    }

    public void imagen(View view) {
        cargarImagen();
    }

    public void cargarImagen() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/");
        startActivityForResult(i.createChooser(i, "Seleccione la aplicacion"), 10);
    }

    private void setToImageView(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,baos);
        bitmapFinal = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()));
        ivPet.setImageBitmap(bitmapFinal);
    }
    private Bitmap getResizedBitmap(Bitmap bitmap, int maxSize){
        int width= bitmap.getWidth();
        int height=bitmap.getHeight();
        if(width <= maxSize && height <=maxSize){
            return bitmap;
        }

        float bitmapRatio = (float) width / (float) height;
        if(bitmapRatio > 1){
            width = maxSize;
            height = (int)(width/bitmapRatio);
        }else{
            height=maxSize;
            width=(int)(height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(bitmap,width,height,true);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            path = data.getData();
            uri = path;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), path);
                //setToImageView(getResizedBitmap(bitmap, 480));
                bitmap = getResizedBitmap(bitmap, 480);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100,baos);
                byte[] imageBytes = baos.toByteArray();
                foto = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                ivPet.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //ivPet.setImageURI(path);
        }
    }
    public void cfefeambio(View view) {


        e.setText(foto);
    }
    public void cambio(View view) {

        if(foto.equals("")){
            path = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.drawable.signo);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), path);
                //setToImageView(getResizedBitmap(bitmap, 480));
                bitmap = getResizedBitmap(bitmap, 480);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100,baos);
                byte[] imageBytes = baos.toByteArray();
                foto = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Intent i = new Intent(this, generadorQR.class);
        i.putExtra("uri",foto);
        i.putExtra("id",id);
        startActivity(i);


    }
}