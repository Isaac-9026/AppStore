package com.example.appstore;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Listar extends AppCompatActivity {

    ListView lstProductos;
    RequestQueue requestQueue;

    private final String URL = "http://10.0.2.2:3000/productos";

    private void loadUI() {
        lstProductos = findViewById(R.id.lstProductos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            return insets;
        });

        loadUI();
        obtenerDatos();
    }

    // Importar objetos JSON desde el WS
    private void obtenerDatos() {
        requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        renderizarListView(jsonArray);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("ErrorWS", volleyError.toString());
                        Toast.makeText(getApplicationContext(), "No se obtuvieron los datos", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    // Poblar el ListView con los datos JSON
    private void renderizarListView(JSONArray jsonProductos) {
        try {
            ArrayAdapter adapter;
            ArrayList<String> listaProductos = new ArrayList<>();

            for (int i = 0; i < jsonProductos.length(); i++) {
                JSONObject obj = jsonProductos.getJSONObject(i);

                String item = obj.getString("marca") + " | " +
                        obj.getString("nombre") + " | " +
                        "S/ " + obj.getString("precio") + " | " +
                        "Stock: " + obj.getString("stock") + " | " +
                        obj.getString("garantia") + " meses";

                listaProductos.add(item);
            }

            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaProductos);
            lstProductos.setAdapter(adapter);

        } catch (Exception e) {
            Log.e("Error JSON", e.toString());
        }
    }


}