package com.example.mypet;

//import static com.facebook.FacebookSdk.getApplicationContext;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.android.volley.toolbox.Volley;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.util.List;



public class PetsFragment extends Fragment {

    List<PetProfile> petList ;
    Button btnAddPet;
    String s;
    public static final String ip = direccion.DIRECCION_IP;

    RequestQueue requestQueue;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_pets, container, false);

        petList = new ArrayList<>();
        btnAddPet = root.findViewById(R.id.btnAddPet);

        petList = new ArrayList<>();

        getParentFragmentManager().setFragmentResultListener("key", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                String id = bundle.getString("id");
                s=id;
                requestQueue = Volley.newRequestQueue(getContext());
                String URL7= "http://"+ip+"/android/consultarMascotas.php?idOwner="+s;
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        URL7,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray jsonArray = new JSONArray();
                                    jsonArray=response.getJSONArray("data");
                                        for(int i=0;i<=jsonArray.length();i++){
                                            new JsonObject();
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String idP = jsonObject.getString("id");
                                            String petNameP = jsonObject.getString("petName");
                                            String foto = jsonObject.getString("foto");

                                            String img = "http://"+ip+foto;

                                            //Simulacion de imagen
                                            Uri path = Uri.parse(img);
                                            //fin
                                            if (foto.length() == 0) {
                                                path = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.drawable.signo);
                                            }
                                            petList.add(new PetProfile(petNameP, idP, path));
                                            ListAdapter listAdapter = new ListAdapter(petList, getContext(), new ListAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(PetProfile pet) {
                                                    Intent intent = new Intent(getContext(), SeePet.class);
                                                    intent.putExtra("petId", String.valueOf(pet.getIdPet()));
                                                    intent.putExtra("petUri", String.valueOf(pet.getUriPet()));
                                                    startActivity(intent);

                                                }
                                            });
                                            RecyclerView recyclerView = root.findViewById(R.id.recyclerId);
                                            recyclerView.setHasFixedSize(true);
                                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                            recyclerView.setAdapter(listAdapter);
                                        }

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

            }
        });


        btnAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),CargarImagen.class);
                i.putExtra("id",s);
                startActivity(i);


            }
        });


        return  root;
    }


}