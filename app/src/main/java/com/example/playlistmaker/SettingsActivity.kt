package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)


        toolbar.setOnClickListener{
            finish()
        }

        val shareApp = findViewById<TextView>(R.id.shareApp)

        shareApp.setOnClickListener{
            val shareAppIntent = Intent(Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT, getString(R.string.prakticum_link))
            startActivity(shareAppIntent)
        }

        val support = findViewById<TextView>(R.id.support)

        support.setOnClickListener{
            val shareIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.myMail)))
                .putExtra(Intent.EXTRA_TEXT, getString(R.string.message))
                .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.letterSubject))
            startActivity(shareIntent)
        }

        val userAgreement = findViewById<TextView>(R.id.userAgreement)

        userAgreement.setOnClickListener{
            val url = Uri.parse(getString(R.string.user_agreement_link))
            val userAgreementIntent = Intent(Intent.ACTION_VIEW, url)
            startActivity(userAgreementIntent)
        }
    }
}