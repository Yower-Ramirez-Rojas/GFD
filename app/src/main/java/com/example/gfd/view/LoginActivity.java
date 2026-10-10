package com.example.gfd.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gfd.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 1. Inicializamos Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // 2. Configuramos qué datos le pediremos a Google (el correo y el token de identidad)
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // 3. Programamos el clic del botón de Google que creaste en el XML
        findViewById(R.id.btnGoogleSignIn).setOnClickListener(v -> iniciarSesionGoogle());
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Verificamos si ya hay un usuario que inició sesión antes.
        // ¡Así no le pedimos que se loguee cada vez que abre la app!
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            irAlMenuPrincipal();
        }
    }

    private void iniciarSesionGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Atrapamos la respuesta de la ventanita de Google
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // El inicio de sesión en Google fue exitoso, ahora lo registramos en Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                autenticarEnFirebase(account.getIdToken());
            } catch (ApiException e) {
                // Si el usuario cancela o hay error de internet
                Toast.makeText(this, "Error de inicio de sesión: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void autenticarEnFirebase(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // ¡Boom! Usuario logueado correctamente
                        irAlMenuPrincipal();
                    } else {
                        Toast.makeText(LoginActivity.this, "Error de Autenticación en Firebase", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void irAlMenuPrincipal() {
        Intent intent = new Intent(this, MenuPrincipalActivity.class);
        startActivity(intent);
        finish(); // Destruimos la pantalla de Login para que al presionar "Atrás" el usuario no vuelva aquí, sino que salga de la app
    }
}