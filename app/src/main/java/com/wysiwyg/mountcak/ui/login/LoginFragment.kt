package com.wysiwyg.mountcak.ui.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.startActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login)

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        login()
    }

    private fun login() {
        btnLogin.setOnClickListener { viewModel.login(etEmail.toString(), etPassword.toString()) }

        viewModel.ifSuccess.observe(this, Observer {
            if (it!!) startActivity<MainActivity>()
            else tvWrong.visibility = View.VISIBLE
        })
    }
}
