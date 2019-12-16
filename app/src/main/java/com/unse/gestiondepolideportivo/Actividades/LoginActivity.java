package com.unse.gestiondepolideportivo.Actividades;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.unse.gestiondepolideportivo.Herramientas.PreferenciasManager;
import com.unse.gestiondepolideportivo.Herramientas.Utils;
import com.unse.gestiondepolideportivo.R;

public class LoginActivity extends AppCompatActivity {

    private PreferenciasManager prefManager;
    EditText mDni;
    Button mIngreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        prefManager = new PreferenciasManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }

            mDni = findViewById(R.id.edtDocumento);
            mIngreso = findViewById(R.id.btnIngreso);
            mIngreso.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Utils.validarDNI(mDni.getText().toString())){
                        prefManager.setValue("dni", mDni.getText().toString());
                        launchHomeScreen();
                    }else
                        Toast.makeText(LoginActivity.this, "Ingrese un DNI válido", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

}
