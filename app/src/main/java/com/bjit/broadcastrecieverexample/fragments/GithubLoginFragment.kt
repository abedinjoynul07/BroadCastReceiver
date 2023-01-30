package com.bjit.broadcastrecieverexample.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bjit.broadcastrecieverexample.R
import com.bjit.broadcastrecieverexample.databinding.FragmentGithubLoginBinding
import com.bjit.broadcastrecieverexample.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider

class GithubLoginFragment : Fragment() {
    private var _binding: FragmentGithubLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGithubLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val provider = OAuthProvider.newBuilder("github.com")
        val sharedPreferences: SharedPreferences? =
            activity?.getSharedPreferences(Constants.preferenceId, AppCompatActivity.MODE_PRIVATE)
        binding.apply {
            githubLoginButton.setOnClickListener {
                val email = githubAddress.text.toString().trim()
                firebaseAuth = FirebaseAuth.getInstance()
                // Target specific email with login hint.
                provider.addCustomParameter("login", email)
                provider.scopes = listOf("user:email")
                val pendingResultTask = firebaseAuth.pendingAuthResult
                if (pendingResultTask != null) {
                    pendingResultTask.addOnSuccessListener {

                        }.addOnFailureListener {
                            // Handle failure.
                        }
                } else {
                    firebaseAuth.startActivityForSignInWithProvider(
                            requireActivity(),
                            provider.build()
                        ).addOnSuccessListener {
                            val editor = sharedPreferences?.edit()
                            editor?.putString("email", it.user?.email)
                            editor?.apply()
                            findNavController().navigate(R.id.action_githubLoginFragment_to_homeFragment)
                        }.addOnFailureListener {
                            findNavController().navigate(R.id.action_githubLoginFragment_to_loginFragment)
                        }
                }
            }
        }
    }
}