package com.example.hasan.chat;


import android.app.Fragment;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentChat extends Fragment {

    String url ;
    int mycolor;
    SharedPreferences sharedPreferences2;
    RequestQueue requestQueue;
    RecyclerView recyclerView;
    ImageButton btn_send_chat;
    EditText txt_send_chat;
    String name1,number1;

    private final static String TAG = "Fragment Chats";
    public FragmentChat() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.recylcerView_chats);
        btn_send_chat = view.findViewById(R.id.btn_send_chat);
        txt_send_chat = view.findViewById(R.id.txt_sent_chat);
        recyclerView.setHasFixedSize(true);
         url = "http://track-kids.com/Chat.php";
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        SharedPreferences sharedPreferences =getActivity().getSharedPreferences("user_info",Context.MODE_PRIVATE);
          name1=sharedPreferences.getString("name",null);
          number1 =sharedPreferences.getString("number",null);


        loadChats();
        //thread.start ();
        btn_send_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Send_chats();
                txt_send_chat.setText("");
                loadChats();
            }

        });

        return view;

    }


    private void Send_chats() {
        if (txt_send_chat.equals("")){
            return;
        }else {
            final String chat = txt_send_chat.getText().toString();

            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i(TAG, "onResponse: " + response);
  }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
                }
            }) {
                /// send data (name , chat ) to server for save in data bace
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("name", name1);
                    params.put("chat", chat);
                    params.put("number", number1);

                    Log.i(TAG, "getParams: " + params);
                    return params;
                }
            };
            requestQueue.add(request);


        }
        //progressBar.setVisibility(View.VISIBLE);
    }

    private void loadChats() {

        final List<Chats> chatsArrayList = new ArrayList<>();
        StringRequest stringRequest  = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                          try {

                            Log.i(TAG, "onResponse: " + response);

                            JSONArray array = new JSONArray(response);
                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {
                                //getting product object from json array
                                JSONObject jsonObject = array.getJSONObject(i);
                                //adding the product to product list
                                Chats chats = new Chats();

                                chats.setName(jsonObject.getString("name"));
                                chats.setChat_text(jsonObject.getString("text_chat"));
                                chats.setNumber(jsonObject.getString("id_sender"));
                                chats.setDate(jsonObject.getString("date"));

                                chatsArrayList.add(chats);

                            }

                            //creating adapter object and setting it to recyclerview
                            AdapterChat adapter = new AdapterChat(getActivity(), chatsArrayList);
                            recyclerView.setAdapter(adapter);
                           recyclerView.scrollToPosition(chatsArrayList.size()-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "onErrorResponse: " + error);
                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                Log.i(TAG, "getParams: " + params);
                return params;
            }
        };

        //adding our stringrequest to queue
        MySingleton.getmInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    Thread thread= new Thread (  ){
        @Override
        public void run(){
            try {

                 for (int i =1 ;i>0 ; i++){
                     sleep(2000);
                     loadChats();
                 }
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }
        }
    };
}
