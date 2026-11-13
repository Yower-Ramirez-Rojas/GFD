package com.example.gfd.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.gfd.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001; // Código para identificar la respuesta de Google
    private GoogleSignInClient mGoogleSignInClient;

    private TextView tvCuentaRecordada, tvSeparador;
    private MaterialButton btnHuella, btnGoogleClasico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 1. Conectar las vistas
        tvCuentaRecordada = findViewById(R.id.tvCuentaRecordada);
        tvSeparador = findViewById(R.id.tvSeparador);
        btnHuella = findViewById(R.id.btnHuella);
        btnGoogleClasico = findViewById(R.id.btnGoogleClasico);

        // 2. Configurar la petición a Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail() // Solo queremos el correo para recordarlo
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // 3. Revisar si ya hay una sesión guardada (El "Recuerdo")
        verificarSesionActiva();

        // 4. Asignar los clics
        btnHuella.setOnClickListener(v -> mostrarLectorHuella());

        btnGoogleClasico.setOnClickListener(v -> {
            // Lanzamos la ventana de selección de cuenta de Google
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    private void verificarSesionActiva() {
        SharedPreferences prefs = getSharedPreferences("MisDatosFintech", Context.MODE_PRIVATE);
        String correoGuardado = prefs.getString("correoSesion", null);

        if (correoGuardado != null) {
            // ¡Ya tiene sesión! Ocultamos el botón de Google y actualizamos el texto
            tvCuentaRecordada.setText("Hola, " + correoGuardado);
            btnGoogleClasico.setVisibility(View.GONE);
            tvSeparador.setVisibility(View.GONE);

            // Habilitamos directamente el botón de huella
            btnHuella.setVisibility(View.VISIBLE);
        } else {
            // Es la primera vez: Ocultamos la huella hasta que se vincule con Google
            tvCuentaRecordada.setText("Vincula tu cuenta para comenzar");
            btnHuella.setVisibility(View.GONE);
            btnGoogleClasico.setVisibility(View.VISIBLE);
            tvSeparador.setVisibility(View.GONE);
        }
    }

    // Recibimos la respuesta de la ventanita de Google
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // El inicio de sesión fue exitoso
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String correo = account.getEmail();

                // Guardamos el "recuerdo" en la memoria del teléfono
                SharedPreferences prefs = getSharedPreferences("MisDatosFintech", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("correoSesion", correo);
                editor.apply();

                // Actualizamos la pantalla para mostrar el botón de la huella
                verificarSesionActiva();
                Toast.makeText(this, "Cuenta vinculada. Ahora usa tu huella.", Toast.LENGTH_LONG).show();

            } catch (ApiException e) {
                // Error al conectar con Google (Falta Internet o falla el certificado SHA-1)
                Toast.makeText(this, "Error con Google. Intenta más tarde.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void mostrarLectorHuella() {
        Executor executor = ContextCompat.getMainExecutor(this);

        BiometricPrompt biometricPrompt = new BiometricPrompt(LoginActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "Error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "¡Desbloqueado!", Toast.LENGTH_SHORT).show();

                // ¡Éxito total! Google y Huella aprobados. Entramos a la app.
                Intent intent = new Intent(LoginActivity.this, MenuPrincipalActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Huella no reconocida", Toast.LENGTH_SHORT).show();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Bóveda GFD")
                .setSubtitle("Confirma tu identidad para entrar")
                .setNegativeButtonText("Cancelar")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }
}