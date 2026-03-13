package com.example.appstore;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Registrar extends AppCompatActivity {

    int idmarca, stock, garantia;
    String nombre, descripcion;
    double precio;

    EditText edtIdMarca, edtNombre, edtDescripcion, edtPrecio, edtStock, edtGarantia;

    Button btnRegistrarProducto;

    // Enviar / recibir los datos hacia el servicio
    RequestQueue requestQueue;

    // URL — reemplaza la IP por la de tu máquina
    private final String URL = "http://10.0.2.2:3000/productos";

    private void loadUI() {
        edtIdMarca     = findViewById(R.id.edtIdMarca);
        edtNombre      = findViewById(R.id.edtNombre);
        edtDescripcion = findViewById(R.id.edtDescripcion);
        edtPrecio      = findViewById(R.id.edtPrecio);
        edtStock       = findViewById(R.id.edtStock);
        edtGarantia    = findViewById(R.id.edtGarantia);
        btnRegistrarProducto = findViewById(R.id.btnRegistrarProducto);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            return insets;
        });

        // Método con referencias
        loadUI();

        // Evento del botón
        btnRegistrarProducto.setOnClickListener((v) -> { validarRegistro(); });
    }

    private void resetUI() {
        edtIdMarca.setText(null);
        edtNombre.setText(null);
        edtDescripcion.setText(null);
        edtPrecio.setText(null);
        edtStock.setText(null);
        edtGarantia.setText(null);
    }

    private void validarRegistro() {

        if (edtIdMarca.getText().toString().isEmpty()) {
            edtIdMarca.setError("Ingrese el ID de la marca");
            edtIdMarca.requestFocus();
            return;
        }

        if (edtNombre.getText().toString().isEmpty()) {
            edtNombre.setError("Ingrese el nombre del producto");
            edtNombre.requestFocus();
            return;
        }

        if (edtDescripcion.getText().toString().isEmpty()) {
            edtDescripcion.setError("Ingrese una descripción");
            edtDescripcion.requestFocus();
            return;
        }

        if (edtPrecio.getText().toString().isEmpty()) {
            edtPrecio.setError("Ingrese el precio");
            edtPrecio.requestFocus();
            return;
        }

        if (edtStock.getText().toString().isEmpty()) {
            edtStock.setError("Ingrese el stock");
            edtStock.requestFocus();
            return;
        }

        if (edtGarantia.getText().toString().isEmpty()) {
            edtGarantia.setError("Ingrese los meses de garantía");
            edtGarantia.requestFocus();
            return;
        }

        // Asignar valores
        idmarca     = Integer.parseInt(edtIdMarca.getText().toString());
        nombre      = edtNombre.getText().toString().trim();
        descripcion = edtDescripcion.getText().toString().trim();
        precio      = Double.parseDouble(edtPrecio.getText().toString());
        stock       = Integer.parseInt(edtStock.getText().toString());
        garantia    = Integer.parseInt(edtGarantia.getText().toString());

        // Validaciones numéricas
        if (idmarca <= 0) {
            edtIdMarca.setError("El ID de marca debe ser mayor a 0");
            edtIdMarca.requestFocus();
            return;
        }

        if (precio <= 0) {
            edtPrecio.setError("El precio debe ser mayor a 0");
            edtPrecio.requestFocus();
            return;
        }

        if (stock < 0) {
            edtStock.setError("El stock no puede ser negativo");
            edtStock.requestFocus();
            return;
        }

        if (garantia < 0) {
            edtGarantia.setError("La garantía no puede ser negativa");
            edtGarantia.requestFocus();
            return;
        }

        // Solicitar confirmación
        AlertDialog.Builder builder = new AlertDialog.Builder(Registrar.this);
        builder.setTitle("Productos");
        builder.setMessage("¿Seguro de registrar el producto?");

        builder.setPositiveButton("Sí", (a, b) -> {
            registrarProducto();
        });
        builder.setNegativeButton("No", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void registrarProducto() {
        // Comunicación
        requestQueue = Volley.newRequestQueue(this);

        // POST — armar el JSON con los datos del producto
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("idmarca",     idmarca);
            jsonObject.put("nombre",      nombre);
            jsonObject.put("descripcion", descripcion);
            jsonObject.put("precio",      precio);
            jsonObject.put("stock",       stock);
            jsonObject.put("garantia",    garantia);
        } catch (JSONException e) {
            Log.e("Error", e.toString());
        }

        Log.d("ValoresWS", jsonObject.toString());

        // Definir la solicitud
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Éxito
                        Log.d("Resultado", response.toString());
                        Toast.makeText(getApplicationContext(), "Producto registrado correctamente", Toast.LENGTH_SHORT).show();
                        resetUI();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        NetworkResponse response = volleyError.networkResponse;

                        if (response != null && response.data != null) {
                            int statusCode = response.statusCode;
                            String errorJson = new String(response.data);

                            Log.e("VolleyError", "Código: " + statusCode);
                            Log.e("VolleyError", "Cuerpo: " + errorJson);

                            Toast.makeText(getApplicationContext(), "Error " + statusCode + ": no se pudo registrar", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("VolleyError", "Sin respuesta de red");
                            Toast.makeText(getApplicationContext(), "Sin conexión al servidor", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Ejecutar la petición
        requestQueue.add(jsonObjectRequest);
    }
}