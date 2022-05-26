package com.example.mypet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SeePet extends AppCompatActivity {
    private ImageView ivPet;
    public static final String ip = direccion.DIRECCION_IP;
    private EditText  edtNombre, edtRaza, edtTel1,edtTel2,edtCorreo,edtDescripcion,edtVacunas,edtDueño;
    Uri uri;
    String petId, url;
    public String foto="";
    RequestQueue requestQueue;
    private Spinner spKind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_pet);
        ivPet = (ImageView) findViewById(R.id.ivPet);

        edtNombre=(EditText)findViewById(R.id.edtNombre);
        edtRaza=(EditText)findViewById(R.id.edtRaza);
        edtDueño = (EditText)findViewById(R.id.edtDueño);
        edtTel1 =(EditText)findViewById(R.id.edtTel1);
        edtTel2=(EditText)findViewById(R.id.edtTel2);
        edtCorreo=(EditText)findViewById(R.id.edtCorreo);
        edtDescripcion=(EditText)findViewById(R.id.edtDescripcion);
        edtVacunas=(EditText)findViewById(R.id.edtVacunas);
        spKind = (Spinner) findViewById(R.id.spKind);
        String [] options = {"Perro","Gato","Caballo","Otro"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,options);
        spKind.setAdapter(adapter);
        requestQueue = Volley.newRequestQueue(this);

        //ID de la mascota consultarMascotaId.php
        petId = getIntent().getStringExtra("petId");
        //Foto de la mascota
        uri=Uri.parse(getIntent().getStringExtra("petUri"));
        Picasso.get().load(uri).into(ivPet);
        String URL7= "http://"+ip+"/android/consultarMascotaId.php?id="+petId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL7,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String petNameP = response.getString("petName");
                            String petKindP = response.getString("petKind");
                            String petBreedp = response.getString("petBreed");
                            String petOwnerP = response.getString("petOwner");
                            String phone1P = response.getString("phone1");
                            String phone2P = response.getString("phone2");
                            String emailP = response.getString("email");
                            String descriptionP = response.getString("description");
                            String vaccinesP = response.getString("vaccines");
                            url= response.getString("foto");


                            edtNombre.setText(petNameP);
                            if(petKindP.equals("Perro")){
                                spKind.setSelection(0);
                            }else if(petKindP.equals("Gato")){
                                spKind.setSelection(1);
                            }else if(petKindP.equals("Caballo")){
                                spKind.setSelection(2);
                            }else{
                                spKind.setSelection(3);
                            }
                            edtRaza.setText(petBreedp);
                            edtDueño.setText(petOwnerP);
                            edtTel1.setText(phone1P);
                            edtTel2.setText(phone2P);
                            edtCorreo.setText(emailP);
                            edtDescripcion .setText(descriptionP);
                            edtVacunas.setText(vaccinesP);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
        //fin consultar

    }
    public void informacion(View view){

        if(edtNombre.getText().toString().length()!=0){
            if(edtDueño.getText().toString().length()!=0){
                if(edtTel1.getText().toString().length()!=0 || edtTel2.getText().toString().length()!=0 ){


                    String raza = "",tel1="",tel2="",email="",descripcion="",vacunas="";

                    if(edtRaza.getText().toString().length()!=0){
                        raza = "\nRaza: " + edtRaza.getText().toString();
                    }
                    if(edtTel1.getText().toString().length()!=0){
                        tel1 = "\nTelefono 1: " + edtTel1.getText().toString();
                    }if(edtTel2.getText().toString().length()!=0){
                        tel2 = "\nTelefono 2: " + edtTel2.getText().toString();
                    }if(edtCorreo.getText().toString().length()!=0){
                        email = "\nCorreo: " + edtCorreo.getText().toString();
                    }if(edtDescripcion.getText().toString().length()!=0){
                        descripcion = "\nDescripcion de la mascota: " + edtDescripcion.getText().toString();
                    }if(edtVacunas.getText().toString().length()!=0){
                        vacunas = "\nVacunas: " + edtVacunas.getText().toString();
                    }
                    // use la varaible uri que tiene la direccion de la imagen
                    String selected = spKind.getSelectedItem().toString();
                    if(url.equals("")){
                        String inf = "INFORMACION DE LA MASCOTA\n" + "Nombre de la mascota: " + edtNombre.getText().toString() + "\nTipo: " + selected + raza + "\nDueño: " + edtDueño.getText().toString() + tel1 + tel2 + email + descripcion + vacunas;
                        Intent i = new Intent(this, VisualizarQR.class);
                        i.putExtra("dato", inf);
                        startActivity(i);
                    }else{
                        String inf = "INFORMACION DE LA MASCOTA\n"  + "http://"+ip+url + "\n" + "Nombre de la mascota: " + edtNombre.getText().toString() + "\nTipo: " + selected + raza + "\nDueño: " + edtDueño.getText().toString() + tel1 + tel2 + email + descripcion + vacunas;
                        Intent i = new Intent(this, VisualizarQR.class);
                        i.putExtra("dato", inf);
                        startActivity(i);
                    }


                }



                else{
                    Toast.makeText(this, "Ingrese almenos un telefono de contacto", Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(this, "Ingrese el nombre del dueño de la mascota", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(this, "Ingrese el nombre de la mascota", Toast.LENGTH_LONG).show();
        }

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









    public void imagen(View view) {
        cargarImagen();
    }

    public void cargarImagen() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/");
        startActivityForResult(i.createChooser(i, "Seleccione la aplicacion"), 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri path = data.getData();
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


        }
    }



    public void changes(View view){
        String URL6 = "http://"+ip+"/android/editarMascota.php?id="+petId;
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL6,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(SeePet.this,"Mascota actualizada!", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",petId);
                params.put("petName",edtNombre.getText().toString());
                params.put("petKind",spKind.getSelectedItem().toString());
                params.put("petBreed",edtRaza.getText().toString());
                params.put("petOwner",edtDueño.getText().toString());
                params.put("phone1",edtTel1.getText().toString());
                params.put("phone2",edtTel2.getText().toString());
                params.put("email",edtCorreo.getText().toString());
                params.put("description",edtDescripcion.getText().toString());
                params.put("vaccines",edtVacunas.getText().toString());

                    params.put("foto",foto);


                return params;
            }
        };
        requestQueue.add(stringRequest);

        Intent i = new Intent(this,SecondActivity.class);
        startActivity(i);
    }

    public  void  delete(View view){
        String URL6 = "http://"+ip+"/android/deleteMascota.php?id="+petId;
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL6,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(SeePet.this,"Mascota eliminada!", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",petId);
                return params;
            }
        };
        requestQueue.add(stringRequest);

        Intent i = new Intent(this,SecondActivity.class);
        startActivity(i);
    }

}