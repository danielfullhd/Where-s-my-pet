package com.example.mypet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


import java.util.Map;

public class generadorQR extends AppCompatActivity {
    private EditText edtNombre, edtRaza, edtTel1,edtTel2,edtCorreo,edtDescripcion,edtVacunas,edtDueño;
    private Spinner spKind;
    public static final String ip = direccion.DIRECCION_IP;
    private static  final  String URL1= "http://"+ip+"/android/prueba.php";
    RequestQueue requestQueue;
    String foto;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generador_qr);
        edtNombre=(EditText)findViewById(R.id.edtNombre);
        edtRaza=(EditText)findViewById(R.id.edtRaza);
        edtTel1 =(EditText)findViewById(R.id.edtTel1);
        edtTel2=(EditText)findViewById(R.id.edtTel2);
        edtCorreo=(EditText)findViewById(R.id.edtCorreo);
        edtDescripcion=(EditText)findViewById(R.id.edtDescripcion);
        edtVacunas=(EditText)findViewById(R.id.edtVacunas);
        spKind = (Spinner) findViewById(R.id.spKind);
        edtDueño = (EditText)findViewById(R.id.edtDueño);
        requestQueue = Volley.newRequestQueue(this);
        String [] options = {"Dog","Cat","Horse","Other"};
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,options);
        spKind.setAdapter(adapter);
        foto = getIntent().getStringExtra("uri");

        id = getIntent().getStringExtra("id");

        //consultar
        String URL5 = "http://"+ip+"/android/consultarUser.php?id="+ id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL5,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String nombreP = response.getString("nombre");
                            String correoP = response.getString("correo");
                            String tel1P = response.getString("tel1");
                            String tel2P = response.getString("tel2");

                            edtDueño.setText(nombreP);
                            edtCorreo.setText(correoP);
                            edtTel1.setText(tel1P);
                            edtTel2.setText(tel2P);

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
                    String selected = spKind.getSelectedItem().toString();
                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            URL1,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(generadorQR.this,"Perfil de la mascota guardado!",Toast.LENGTH_LONG).show();
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
                            params.put("petName", edtNombre.getText().toString());
                            params.put("petKind",selected);
                            params.put("petBreed",edtRaza.getText().toString());
                            params.put("petOwner",edtDueño.getText().toString());
                            params.put("phone1",edtTel1.getText().toString());
                            params.put("phone2",edtTel2.getText().toString());
                            params.put("email",edtCorreo.getText().toString());
                            params.put("description",edtDescripcion.getText().toString());
                            params.put("vaccines",edtVacunas.getText().toString());
                            params.put("idOwner",id);
                            params.put("foto",foto);
                            return params;
                        }
                    };

                    requestQueue.add(stringRequest);
                    String inf = "INFORMACION DE LA MASCOTA\n" + "Nombre de la mascota: " + edtNombre.getText().toString() + "\nTipo: " + selected + raza + "\nDueño: " + edtDueño.getText().toString() + tel1 + tel2 + email + descripcion + vacunas;
                    Intent i = new Intent(this, VisualizarQR.class);
                    i.putExtra("dato", inf);
                    startActivity(i);
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
}