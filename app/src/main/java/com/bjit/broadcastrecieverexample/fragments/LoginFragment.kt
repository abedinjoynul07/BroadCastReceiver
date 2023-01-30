package com.bjit.broadcastrecieverexample.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bjit.broadcastrecieverexample.R
import com.bjit.broadcastrecieverexample.databinding.FragmentLoginBinding
import com.bjit.broadcastrecieverexample.utils.Constants
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code: Int = 1
    lateinit var gso: GoogleSignInOptions
    private val REQ_ONE_TAP = 2
    private var showOneTapUI = true
    private lateinit var progressDialog: ProgressDialog
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(requireContext())
        progressDialog

        sharedPreferences =
            activity?.getSharedPreferences(Constants.preferenceId, AppCompatActivity.MODE_PRIVATE)!!

        firebaseAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.googleClientId)).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        if (sharedPreferences.getString("email", null) != null) {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }

        val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)



        binding.apply {
            google.setOnClickListener {
                val signInIntent = googleSignInClient.signInIntent
                startActivityForResult(signInIntent, Req_Code)

            }

            github.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_githubLoginFragment)
            }

            login.setOnClickListener {
                val email = username.text.toString().trim()
                val password = password.text.toString().trim()
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    progressDialog.show()
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                            val editor = sharedPreferences.edit()
                            editor?.putString("email", email)
                            editor?.putString("password", password)
                            editor?.apply()
                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                            progressDialog.dismiss()
                        }.addOnFailureListener {
                            Toast.makeText(requireContext(), "Login Failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                } else {
                    Toast.makeText(requireContext(), "Fill all the fields.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun signInGoogle() {
        progressDialog.show()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.googleClientId)).requestEmail().build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Req_Code) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
                signInGoogle()
            } catch (e: ApiException) {
                Toast.makeText(requireContext(), "Signing Failed", Toast.LENGTH_SHORT).show()
            }
        } else {
            signInGoogle()
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        activity?.let {
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
                if (it.isSuccessful) {
                    val editor = sharedPreferences.edit()
                    editor?.putString("email", it.result.user?.email)
                    editor?.apply()
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    progressDialog.dismiss()
                } else {
                    Toast.makeText(requireContext(), "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}