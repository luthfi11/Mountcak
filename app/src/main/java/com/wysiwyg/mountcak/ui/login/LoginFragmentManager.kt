package com.wysiwyg.mountcak.ui.login

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.ui.signup.SignUpFragment
import kotlinx.android.synthetic.main.activity_login_manager.*

class LoginFragmentManager: AppCompatActivity() {

    private val fm = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_manager)

        fm.beginTransaction().replace(R.id.loginContent, LoginFragment()).commit()
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                if (p0?.position == 0) fm.beginTransaction().replace(R.id.loginContent, LoginFragment()).commit()
                else fm.beginTransaction().replace(R.id.loginContent, SignUpFragment()).commit()
            }
        })
    }
}