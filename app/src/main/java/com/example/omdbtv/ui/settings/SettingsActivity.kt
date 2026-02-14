package com.example.omdbtv.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.omdbtv.R
import com.example.omdbtv.api.RetrofitClient
import com.example.omdbtv.databinding.ActivitySettingsBinding

class SettingsActivity : FragmentActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        loadCurrentApiKey()
    }

    private fun setupViews() {
        binding.saveButton.setOnClickListener {
            saveApiKey()
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.apiKeyInput.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                v.background = getDrawable(android.R.drawable.editbox_background_normal)
            }
        }
    }

    private fun loadCurrentApiKey() {
        val currentKey = RetrofitClient.API_KEY
        if (currentKey != "YOUR_API_KEY") {
            binding.apiKeyInput.setText(currentKey)
        }
    }

    private fun saveApiKey() {
        val apiKey = binding.apiKeyInput.text.toString().trim()

        if (apiKey.isEmpty()) {
            Toast.makeText(this, "Введите API ключ", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitClient.API_KEY = apiKey
        Toast.makeText(this, getString(R.string.api_key_saved), Toast.LENGTH_SHORT).show()
        finish()
    }
}
