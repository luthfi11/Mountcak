package com.wysiwyg.mountcak.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.ui.main.MainActivity
import com.wysiwyg.mountcak.ui.resetpassword.ResetPasswordActivity
import com.wysiwyg.mountcak.util.LoadingDialog
import com.wysiwyg.mountcak.util.ValidateUtil.emailValidate
import com.wysiwyg.mountcak.util.ValidateUtil.etToString
import com.wysiwyg.mountcak.util.ValidateUtil.passwordValidate
import com.wysiwyg.mountcak.util.visible
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.startActivity

class LoginFragment : Fragment(), LoginView {

    private lateinit var presenter: LoginPresenter

    override fun showLoading() {
        LoadingDialog.showLoading(activity!!, R.string.login)
    }

    override fun hideLoading() {
        LoadingDialog.hideLoading()
    }

    override fun loginSuccess() {
        startActivity<MainActivity>()
        activity?.finish()
    }

    override fun loginFail() {
        tvWrong.visible()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        presenter = LoginPresenter(this)

        btnLogin.onClick { login() }
        btnForgot.onClick { startActivity<ResetPasswordActivity>() }
    }

    private fun login() {
        emailValidate(etEmail, getString(R.string.email_validate)) {
            passwordValidate(etPassword, getString(R.string.password_validate)) {
                presenter.login(etToString(etEmail), etToString(etPassword))
            }
        }
    }
}
