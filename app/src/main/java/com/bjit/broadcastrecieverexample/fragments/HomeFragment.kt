package com.bjit.broadcastrecieverexample.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bjit.broadcastrecieverexample.R
import com.bjit.broadcastrecieverexample.databinding.FragmentHomeBinding
import com.bjit.broadcastrecieverexample.utils.Constants
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val sharedPrefKey = "isDarkMode"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences: SharedPreferences? =
            activity?.getSharedPreferences(Constants.preferenceId, AppCompatActivity.MODE_PRIVATE)
        val myEdit: SharedPreferences.Editor = sharedPreferences!!.edit()
        binding.apply {
            button.setOnClickListener {
                val firebaseAuth = FirebaseAuth.getInstance()
                firebaseAuth.signOut()
                myEdit.clear()
                myEdit.apply()
                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            }

            textView.text = "Welcome Back \n${sharedPreferences.getString("email", null)}"

            mood.isChecked = sharedPreferences.getBoolean(sharedPrefKey, false)
            mood.setOnCheckedChangeListener { _, isChecked ->
                sharedPreferences.edit().putBoolean(sharedPrefKey, isChecked).apply()
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }
}