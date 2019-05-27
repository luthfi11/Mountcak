package com.wysiwyg.mountcak.ui.resetpassword

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.util.LoadingDialog
import com.wysiwyg.mountcak.util.ValidateUtil.emailValidate
import com.wysiwyg.mountcak.util.ValidateUtil.etToString
import com.wysiwyg.temanolga.utilities.gone
import com.wysiwyg.temanolga.utilities.visible
import kotlinx.android.synthetic.main.activity_reset_password.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

class ResetPasswordActivity : AppCompatActivity(), ResetPasswordView {

    private lateinit var presenter: ResetPasswordPresenter

    override fun showLoading() {
        LoadingDialog.showLoading(this, R.string.reset_password)
    }

    override fun hideLoading() {
        LoadingDialog.hideLoading()
    }

    override fun showEmailSent() {
        alert(getString(R.string.email_sent)) {
            yesButton { finish() }
            isCancelable = false
        }.show()
    }

    override fun emailNotFound() {
        alert(getString(R.string.email_not_found)) {
            yesButton { it.dismiss() }
            isCancelable = false
        }.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        presenter = ResetPasswordPresenter(this)
    }

    fun resetPassword(view: View) {
        emailValidate(etEmail, getString(R.string.email_validate)) {
            presenter.resetPassword(etToString(etEmail))
        }
    }
}
