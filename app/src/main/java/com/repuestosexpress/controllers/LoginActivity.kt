package com.repuestosexpress.controllers

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.repuestosexpress.R
import com.repuestosexpress.utils.Firebase
import com.repuestosexpress.utils.Utils

/**
 * LoginActivity maneja el inicio de sesión de los usuarios mediante correo electrónico y Google.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var btnLogingGoogle: Button
    private lateinit var btnRegisterWithEmail: Button
    private lateinit var btnLogingWithEmail: Button
    private lateinit var txtEmailLogin: EditText
    private lateinit var txtPass: EditText
    private lateinit var txtRecuperarPass: TextView

    /**
     * Método llamado cuando se crea la actividad.
     * @param savedInstanceState Estado previamente guardado de la actividad.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogingWithEmail = findViewById(R.id.btn_Login)
        btnLogingGoogle = findViewById(R.id.btn_singGoogle)
        btnRegisterWithEmail = findViewById(R.id.btn_Registrar)
        txtEmailLogin = findViewById(R.id.txtEmail)
        txtPass = findViewById(R.id.txtPassword)
        txtRecuperarPass = findViewById(R.id.txtRecuperarPassword)

        btnLogingWithEmail.setOnClickListener {
            loginWithEmail()
        }

        btnRegisterWithEmail.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        btnLogingGoogle.setOnClickListener { onClickGoogleLogIn() }

        txtRecuperarPass.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Método llamado cuando la actividad se inicia y comprueba si hay un usuario registrado.
     * Si hay un usuario registrado, se redirige a la actividad principal.
     */
    override fun onStart() {
        super.onStart()
        val prefs: SharedPreferences = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val username: String? = prefs.getString("name", "")

        if (!username?.trim().equals("")) {
            mainIntent()
            this.finish()
        }
    }

    /**
     * Método para iniciar sesión con una cuenta de Google.
     */
    private fun onClickGoogleLogIn() {
        val googleConf: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build()
        val googleClient: GoogleSignInClient = GoogleSignIn.getClient(this, googleConf)
        googleClient.signOut()
        startActivityForResult(googleClient.signInIntent, Utils.RC_SIGN_IN)
    }

    /**
     * Método para manejar el resultado de la actividad que devuelve el token de inicio de sesión de Google.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Utils.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)

                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val user = it.result.user
                                val email = user?.email
                                val uid = user?.uid
                                val name = email?.substringBefore("@")

                                if (email != null && name != null && uid != null) {
                                    Firebase().crearUsuario(this, email, name, uid)
                                    Utils.createUserPrefs(this, user)
                                    mainIntent()
                                }
                            } else {
                                Utils.Toast(this, getString(R.string.error))
                            }
                        }
                }
            } catch (exception: ApiException) {
                Log.e("Error", "$exception")
            }
        }
    }

    /**
     * Método para iniciar sesión con correo electrónico y contraseña.
     */
    private fun loginWithEmail() {
        if (txtEmailLogin.text.trim().isNotEmpty()) {
            if (txtPass.text.trim().isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    txtEmailLogin.text.toString(),
                    txtPass.text.toString()
                ).addOnSuccessListener {
                    Utils.Toast(this, getString(R.string.ok_credentials))
                    mainIntent()
                }.addOnFailureListener {
                    Utils.Toast(this, getString(R.string.bad_credentials))
                }
            } else {
                txtPass.error = getString(R.string.required)
            }
        } else {
            txtEmailLogin.error = getString(R.string.required)
        }
    }

    /**
     * Método para iniciar la actividad principal.
     */
    private fun mainIntent() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
