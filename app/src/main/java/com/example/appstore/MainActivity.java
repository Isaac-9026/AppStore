package com.example.appstore;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button btnActivityRegistrar, btnactivityListar;

    private void loadUI(){
        btnActivityRegistrar = findViewById(R.id.btnActivityRegistrar);
        btnactivityListar = findViewById(R.id.btnactivityListar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadUI();

        btnActivityRegistrar.setOnClickListener(v -> { startActivity(new Intent(getApplicationContext(),Registrar.class));});
        btnactivityListar.setOnClickListener(v -> { startActivity(new Intent(getApplicationContext(),Listar.class));});
    }
}