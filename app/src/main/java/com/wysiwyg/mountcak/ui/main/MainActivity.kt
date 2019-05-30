package com.wysiwyg.mountcak.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.ui.explore.ExploreFragment
import com.wysiwyg.mountcak.ui.login.LoginFragmentManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity(), MainView {

    private lateinit var presenter: MainPresenter
    private val fm = supportFragmentManager
    private var active: Fragment = ExploreFragment()

    override fun toLogin() {
        finish()
        startActivity<LoginFragmentManager>()
    }

    override fun addView(fragment: Array<Fragment>) {
        for (fr : Fragment in fragment) {
            fm.beginTransaction().add(R.id.content, fr).hide(fr).commit()
        }
    }

    override fun changeView(fragment: Fragment) {
        fm.beginTransaction().hide(active).show(fragment).commit()
        active = fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this)
        presenter.checkLogin()
        presenter.addView()
        navigation.setOnNavigationItemSelectedListener(presenter.selectedView())
        presenter.initialView()
    }
}
