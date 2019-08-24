package com.wysiwyg.mountcak.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.ui.main.MainActivity
import com.wysiwyg.mountcak.util.CityUtil
import com.wysiwyg.mountcak.util.LoadingDialog
import com.wysiwyg.mountcak.util.ValidateUtil.emailValidate
import com.wysiwyg.mountcak.util.ValidateUtil.etToString
import com.wysiwyg.mountcak.util.ValidateUtil.etValidate
import com.wysiwyg.mountcak.util.ValidateUtil.passwordValidate
import kotlinx.android.synthetic.main.fragment_signup.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast

class SignUpFragment : Fragment(), SignUpView {

    private lateinit var presenter: SignUpPresenter

    override fun showCity() {
        val adapter = ArrayAdapter(context!!, R.layout.spinner_item, CityUtil.city)
        etCity.setAdapter(adapter)
    }

    override fun showLoading() {
        LoadingDialog.showLoading(activity!!, R.string.signup)
    }

    override fun hideLoading() {
        LoadingDialog.hideLoading()
    }

    override fun signUpSuccess() {
        activity?.finish()
        startActivity<MainActivity>()
    }

    override fun signUpFail() {
        toast(R.string.signup_failed)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        presenter = SignUpPresenter(this)
        presenter.getCity()

        btnSignUp.onClick { signUp() }
    }

    private fun signUp() {
        etValidate(etName, getString(R.string.name_validate)) {
            etValidate(etCity, getString(R.string.city_validate)) {
                emailValidate(etEmail, getString(R.string.email_validate)) {
                    etValidate(etPhone, (getString(R.string.phone_validate))) {
                        passwordValidate(etPassword, getString(R.string.password_validate)) {
                            presenter.signUp(
                                etToString(etEmail),
                                etToString(etPhone),
                                etToString(etPassword),
                                etToString(etName),
                                etToString(etCity)
                            )
                        }
                    }
                }
            }
        }
    }
}