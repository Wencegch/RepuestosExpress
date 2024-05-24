package com.repuestosexpress.controllers

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.repuestosexpress.R
import com.repuestosexpress.utils.Utils

class LoginActivity : AppCompatActivity() {
    private lateinit var btnLogingGoogle: Button
    private lateinit var btnRegisterWithEmail: Button
    private lateinit var btnLogingWithEmail: Button
    private lateinit var txtEmailLogin: EditText
    private lateinit var txtPass: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogingWithEmail = findViewById(R.id.btn_Login)
        btnLogingGoogle = findViewById(R.id.btn_singGoogle)
        btnRegisterWithEmail = findViewById(R.id.btn_Registrar)
        txtEmailLogin = findViewById(R.id.txtEmailUser)
        txtPass = findViewById(R.id.txtPassword)

        btnLogingWithEmail.setOnClickListener {
            loginWithEmail()
        }

        btnRegisterWithEmail.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        btnLogingGoogle.setOnClickListener { onClickGoogleLogIn() }

    }

    /**
     * Cuando se reinicia la app se comprueba que no haya usuario registrado
     * En caso contrario esta actividad se salta
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
     * Metodo que recibe el token de la cuenta de google seleccionada
     * y la registra en la base de datos de Firebase
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Confirma que la peticion venga del metodo GoogleLogIn
        if (requestCode == Utils.RC_SIGN_IN) {

            //Recoge la cuenta de google asociada a los datos del intent
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                //Se intenta coger la cuenta
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    //Si la cuenta no es nula se recogen sus credenciales
                    val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)

                    //Registramos las credenciales en FirebaseAuth
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                //Crear las preferencias
                                Utils.createUserPrefs(this, it.result.user)
                                //Llevar a main_activity
                                mainIntent()
                            } else {
                                Utils.Toast(this, getString(R.string.error))
                            }
                        }
                }
            } catch (e: ApiException) {
                Utils.Toast(this, "API Error")
            }
        }
    }

    /**
     * Metodo para iniciar sesion con una cuenta de google guardada en el dispositivo
     */
    private fun onClickGoogleLogIn() {
        val googleConf: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build()
        val googleClient: GoogleSignInClient = GoogleSignIn.getClient(this, googleConf)
        Log.d("usuario", "0")
        googleClient.signOut()
        startActivityForResult(googleClient.signInIntent, Utils.RC_SIGN_IN)
    }

    /**
     * Método para iniciar sesión cuando YA TIENES un usuario CREADO en la BD
     */
    private fun loginWithEmail() {
        //Comprueba si el email está vacío
        if (txtEmailLogin.text.trim().isNotEmpty()) {
            //Comprueba si la contraseña está vacía
            if (txtPass.text.trim().isNotEmpty()) {
                //Inicio de sesión con el correo y la contraseña
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    txtEmailLogin.text.toString(),
                    txtPass.text.toString()
                ).addOnSuccessListener {
                    Utils.Toast(this, getString(R.string.ok_credentials))
                    //Utils.createUserPrefs(this, it.user)
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
     * Método para enviar al usuario a la MainActivity
     */
    private fun mainIntent() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}