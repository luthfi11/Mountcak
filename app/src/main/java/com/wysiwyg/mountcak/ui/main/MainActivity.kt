package com.wysiwyg.mountcak.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.ui.home.HomeFragment
import com.wysiwyg.mountcak.ui.login.LoginFragmentManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity(), MainView {

    private lateinit var presenter: MainPresenter
    private val fm = supportFragmentManager

    override fun toLogin() {
        finish()
        startActivity<LoginFragmentManager>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this)
        presenter.checkLogin()

        navigation.setOnNavigationItemSelectedListener(presenter.selectedView(fm))

        if (savedInstanceState == null) presenter.changeView(fm, HomeFragment())
    }
}
