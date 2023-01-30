package com.bjit.broadcastrecieverexample.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bjit.broadcastrecieverexample.R
import com.bjit.broadcastrecieverexample.databinding.FragmentGoogleLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class GoogleLoginFragment : Fragment() {
    private var _binding: FragmentGoogleLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val RC_SIGN_IN: Int = 1
    lateinit var gso: GoogleSignInOptions

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoogleLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createRequest()
        binding.apply {
            signIn()
        }
    }

    private fun createRequest() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.googleClientId)).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception

            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(requireContext(), "Login Failed", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                findNavController().navigate(R.id.action_googleLoginFragment_to_homeFragment)
            } else {
                Toast.makeText(requireContext(), "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}