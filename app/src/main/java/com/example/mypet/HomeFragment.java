package com.example.mypet;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.facebook.AccessToken;
//import com.facebook.GraphRequest;
//import com.facebook.GraphResponse;
//import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {
    ImageView ivProfile;
    Button btnSignOut,btnUpdate;
    EditText edtPhone1, edtPhone2,edtEmail,edtName;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    FirebaseAuth firebaseAuth;
    public static final String ip = direccion.DIRECCION_IP;
    public String idUser;
    private static  final  String URL4= "http://"+ip+"/android/saveUsers.php";
    private static  final  String URL6= "http://"+ip+"/android/editarUser.php";
    static    String URL5;
    String proveedor;
    public String id ="123";
    RequestQueue requestQueue;
    String nombre="", correo="", tel1="", tel2 ="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_home, container, false);


        ivProfile = root.findViewById(R.id.ivProfile);
        edtName = root.findViewById(R.id.edtName);
        btnSignOut = root.findViewById(R.id.btnSignOut);
        btnUpdate =root.findViewById(R.id.btnUpdate);
        edtEmail = root.findViewById(R.id.edtEmail);
        firebaseAuth = FirebaseAuth.getInstance();
        edtPhone1 = root.findViewById(R.id.edtPhone1);
        edtPhone2 = root.findViewById(R.id.edtPhone2);
        requestQueue = Volley.newRequestQueue(getContext());
        FirebaseUser userF = firebaseAuth.getCurrentUser();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(getContext(),gso);
        Intent intentIp = new Intent(getContext(), generadorQR.class);
        intentIp.putExtra("dirIp", ip);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if(acct != null){
            edtName.setText(acct.getDisplayName());
            edtName.setEnabled(false);
            edtEmail.setText(acct.getEmail());
            edtEmail.setEnabled(false);
            Picasso.get().load(acct.getPhotoUrl()).into(ivProfile);
            idUser=acct.getId();
            Bundle bundle = new Bundle();
            bundle.putString("id",idUser);
            getParentFragmentManager().setFragmentResult("key",bundle);
            URL5 = "http://"+ip+"/android/consultarUser.php?id="+ idUser;
            //consultar
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

                                edtName.setText(nombreP);
                                edtEmail.setText(correoP);
                                edtPhone1.setText(tel1P);
                                edtPhone2.setText(tel2P);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            StringRequest stringRequest = new StringRequest(
                                    Request.Method.POST,
                                    URL4,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Toast.makeText(getContext(),"Perfil registrado!",Toast.LENGTH_LONG).show();
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
                                    params.put("id",acct.getId() );
                                    params.put("nombre",acct.getDisplayName());
                                    params.put("correo",acct.getEmail());
                                    params.put("tel1",tel1);
                                    params.put("tel2",tel2);
                                    params.put("proveedor","Google");
                                    return params;
                                }
                            };

                            requestQueue.add(stringRequest);



                        }
                    }
            );

            requestQueue.add(jsonObjectRequest);
            //fin consultar

        }
        else if(userF != null){
            Picasso.get().load(firebaseAuth.getCurrentUser().getPhotoUrl()).into(ivProfile);
            edtEmail.setText(firebaseAuth.getCurrentUser().getEmail());
            edtEmail.setEnabled(false);
            idUser=firebaseAuth.getCurrentUser().getUid();
            Bundle bundle = new Bundle();
            bundle.putString("id",idUser);
            getParentFragmentManager().setFragmentResult("key",bundle);
            URL5 = "http://"+ip+"/android/consultarUser.php?id="+ idUser;
            //consultar
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

                                edtName.setText(nombreP);
                                edtEmail.setText(correoP);
                                edtPhone1.setText(tel1P);
                                edtPhone2.setText(tel2P);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            StringRequest stringRequest = new StringRequest(
                                    Request.Method.POST,
                                    URL4,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Toast.makeText(getContext(),"Perfil registrado!",Toast.LENGTH_LONG).show();
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
                                    params.put("id", firebaseAuth.getCurrentUser().getUid());
                                    params.put("nombre",nombre);
                                    params.put("correo",firebaseAuth.getCurrentUser().getEmail());
                                    params.put("tel1",tel1);
                                    params.put("tel2",tel2);
                                    params.put("proveedor","FireBase");
                                    return params;
                                }
                            };

                            requestQueue.add(stringRequest);



                        }
                    }
            );

            requestQueue.add(jsonObjectRequest);
            //fin consultar
        }/*
        else{
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            proveedor="Facebook";
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            // Application code

                            try {
                                edtName.setText(object.getString("name"));
                                edtName.setEnabled(false);

                                idUser = object.getString("id");
                                Picasso.get().load(object.getJSONObject("picture").getJSONObject("data").getString("url")).into(ivProfile);
                                Bundle bundle = new Bundle();
                                bundle.putString("id",idUser);
                                getParentFragmentManager().setFragmentResult("key",bundle);
                                URL5 = "http://"+ip+"/android/consultarUser.php?id="+ idUser;
                                //consultar
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

                                                    edtName.setText(nombreP);
                                                    edtEmail.setText(correoP);
                                                    edtPhone1.setText(tel1P);
                                                    edtPhone2.setText(tel2P);

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                                StringRequest stringRequest = new StringRequest(
                                                        Request.Method.POST,
                                                        URL4,
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                Toast.makeText(getContext(),"Perfil registrado!",Toast.LENGTH_LONG).show();
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
                                                        try {
                                                            params.put("id", object.getString("id"));
                                                            params.put("nombre",object.getString("name"));
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        params.put("correo",correo);
                                                        params.put("tel1",tel1);
                                                        params.put("tel2",tel2);
                                                        params.put("proveedor","Facebook");
                                                        return params;
                                                    }
                                                };

                                                requestQueue.add(stringRequest);



                                            }
                                        }
                                );

                                requestQueue.add(jsonObjectRequest);
                                //fin consultar
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link,picture.type(large)");
            request.setParameters(parameters);
            request.executeAsync();

        }*/
        if  (ivProfile.getDrawable() == null)
        {
            ivProfile.setImageResource(R.drawable.profile);


        }







        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth fire;
                fire = FirebaseAuth.getInstance();
                FirebaseUser userR = fire.getCurrentUser();
                if(acct != null){
                    gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent i = new Intent(getContext(), MainActivity.class);
                            startActivity(i);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        }
                    });
                }
                else if(userR != null){
                    FirebaseAuth.getInstance().signOut();
                    Intent i = new Intent(getContext(), MainActivity.class);
                    startActivity(i);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                else{
                   // LoginMana.getInstance().logOut();
                    Intent i = new Intent(getContext(), MainActivity.class);
                    startActivity(i);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                }

            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        URL6,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getContext(),"Perfil actualizado!", Toast.LENGTH_LONG).show();
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
                        params.put("id",idUser);
                        params.put("nombre",edtName.getText().toString());
                        params.put("correo",edtEmail.getText().toString());
                        params.put("tel1",edtPhone1.getText().toString());
                        params.put("tel2",edtPhone2.getText().toString());

                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            }

        });
        return root;
    }

}