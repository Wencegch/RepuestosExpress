package com.repuestosexpress.controllers

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.repuestosexpress.R
import com.repuestosexpress.utils.Utils

class SignUpActivity : AppCompatActivity() {
    private lateinit var btnRegistrar: Button
    private lateinit var txtEmail: EditText
    private lateinit var txtNameUser: EditText
    private lateinit var txtPass: EditText
    private lateinit var txtRepeatPass: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.preferences_container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtEmail = findViewById(R.id.txtEmailRegister)
        txtNameUser = findViewById(R.id.txtUserName)
        txtPass = findViewById(R.id.txtPasswordReg)
        txtRepeatPass = findViewById(R.id.txtConfirmPassword)
        btnRegistrar = findViewById(R.id.btn_Register)

        btnRegistrar.setOnClickListener { registerWithEmail() }

    }

    private fun registerWithEmail() {
        // Comprueba si el correo electrónico no está vacío
        if (txtEmail.text.trim().isNotEmpty()) {
            // Comprueba si el nombre de usuario no está vacío
            if (txtNameUser.text.trim().isNotEmpty()) {
                // Comprueba si la contraseña no está vacía
                if (txtPass.text.trim().isNotEmpty()) {
                    // Comprueba si la confirmación de contraseña no está vacía
                    if (txtRepeatPass.text.trim().isNotEmpty()) {
                        // Comprueba si ambas contraseñas son iguales
                        if (txtPass.text.toString() == txtRepeatPass.text.toString()) {
                            // Crea un nuevo usuario utilizando el correo electrónico y la contraseña proporcionados
                            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                                txtEmail.text.toString(),
                                txtPass.text.toString()
                            ).addOnSuccessListener {
                                val user = it.user
                                // Actualiza el perfil del usuario con el nombre proporcionado
                                val profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(txtNameUser.text.toString()).build()
                                user?.updateProfile(profileUpdates)
                                    ?.addOnCompleteListener { profileUpdateTask ->
                                        if (profileUpdateTask.isSuccessful) {
                                            // El perfil del usuario se actualizó correctamente
                                            Utils.Toast(
                                                this,
                                                getString(R.string.user) + " " + user.displayName + getString(
                                                    R.string.created
                                                )
                                            )
                                            Utils.createUserPrefs(this, it.user)
                                            val intent = Intent(this, MainActivity::class.java)
                                            startActivity(intent)
                                            finish() // Finaliza la actividad actual después de iniciar sesión correctamente
                                        } else {
                                            Utils.Toast(this, getString(R.string.error))
                                        }
                                    }
                            }.addOnFailureListener {
                                // Error al crear el usuario
                                Utils.Toast(this, getString(R.string.error))
                            }
                        } else {
                            // Las contraseñas no coinciden
                            txtPass.error = getString(R.string.different_password)
                            txtRepeatPass.error = getString(R.string.different_password)
                            Utils.Toast(this, getString(R.string.different_password))
                        }
                    } else {
                        // La confirmación de la contraseña está vacía
                        txtRepeatPass.error = getString(R.string.required)
                    }
                } else {
                    // La contraseña está vacía
                    txtPass.error = getString(R.string.required)
                }
            } else {
                // El nombre de usuario está vacío
                txtNameUser.error = getString(R.string.required)
            }
        } else {
            // El correo electrónico está vacío
            txtEmail.error = getString(R.string.required)
        }
    }
}