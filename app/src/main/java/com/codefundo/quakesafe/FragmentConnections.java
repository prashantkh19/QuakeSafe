package com.codefundo.quakesafe;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentConnections extends Fragment {
    public static String user_id="1";

    public FragmentConnections() {
        // Required empty public constructor
    }

    AdapterConnections adapterConnections;
    RecyclerView recyclerView;
    ArrayList<ItemConnection> itemConnectionArrayList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_fragment_connections, container, false);

        adapterConnections = new AdapterConnections(getContext(), itemConnectionArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterConnections);
        getList();

        return rootView;
    }

    private void getList() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait!");
        progressDialog.setCancelable(false);
        class GetData extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                progressDialog.dismiss();
                super.onPostExecute(s);
                parseJSON(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    URL url = new URL("https://earthquakerapp.azurewebsites.net/get_connections/");
                    String urlParams = "user_id="+user_id;

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoOutput(true);
                    StringBuilder sb = new StringBuilder();

                    OutputStream os = con.getOutputStream();
                    os.write(urlParams.getBytes());
                    os.flush();
                    os.close();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    String s = sb.toString().trim();
                    return s;

                } catch (IOException e) {
                    // Toast.makeText(getActivity(), "Please Check your Connection", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    return "error";
                }
            }

        }
        GetData gd = new GetData();
        gd.execute();
    }

    private void parseJSON(String json) {
        boolean error = true;
        String message = "";
        Log.i("Connections", json);
        try {
            JSONObject root = new JSONObject(json);
            error = root.getBoolean("error");
            if (error) {
                JSONArray jsonArray = root.getJSONArray("response");
                if (jsonArray.length() > 0)
                    itemConnectionArrayList.clear();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject o = jsonArray.getJSONObject(i);
                    ItemConnection itemConnection = new ItemConnection(
                         o.getString("name"),
                            o.getInt("mobile"),
                            o.getBoolean("status"),
                            o.getBoolean("is_fav")
                    );
                    itemConnectionArrayList.add(itemConnection);
                }
               adapterConnections.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Connection error!", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
