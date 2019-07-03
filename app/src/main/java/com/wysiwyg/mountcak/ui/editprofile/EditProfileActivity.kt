package com.wysiwyg.mountcak.ui.editprofile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.User
import com.wysiwyg.mountcak.util.CityUtil
import com.wysiwyg.mountcak.util.LoadingDialog
import com.wysiwyg.mountcak.util.ValidateUtil.emailValidate
import com.wysiwyg.mountcak.util.ValidateUtil.etToString
import com.wysiwyg.mountcak.util.ValidateUtil.passwordValidate
import com.wysiwyg.mountcak.util.ValidateUtil.setError
import com.wysiwyg.mountcak.util.ValidateUtil.validate
import kotlinx.android.synthetic.main.activity_edit_profile.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast

class EditProfileActivity : AppCompatActivity(), EditProfileView {

    private lateinit var presenter: EditProfilePresenter
    private lateinit var email: String

    override fun showData(user: User?) {
        Glide.with(this).load(user?.photo).placeholder(R.color.colorMuted).into(imgUserPhoto)

        etNameUpdate.setText(user?.name)
        etCityUpdate.setText(user?.city)
        etEmailUpdate.setText(user?.email)

        email = user?.email!!

        etCityUpdate.setAdapter(ArrayAdapter(this, R.layout.spinner_item, CityUtil.city))
    }

    override fun showLoading(title: String) {
        LoadingDialog.showLoading(this, title)
    }

    override fun hideLoading() {
        LoadingDialog.hideLoading()
    }

    override fun showUpdated(title: String) {
        toast(title)
        finish()
    }

    override fun showFail(title: String) {
        scrollView.snackbar(title)
    }

    override fun showUpdatedPhoto(path: Uri) {
        Glide.with(this).load(path).placeholder(R.color.colorMuted).into(imgUserPhoto)
    }

    override fun showChoosePhoto() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(Intent.createChooser(intent, "Choose Photo"), 1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        setSupportActionBar(toolbarEditProfile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        val user: User = intent.getParcelableExtra("user")

        presenter = EditProfilePresenter(this)
        presenter.getData(user)

        btnChoose.onClick { presenter.choosePhoto() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((resultCode == Activity.RESULT_OK) && requestCode == 1)
            presenter.updatePhoto(data?.data!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) finish()
        else if (item?.itemId == R.id.nav_done) {

            if (validate(etEmailUpdate, getString(R.string.name_validate)) and
                validate(etCityUpdate, getString(R.string.city_validate))) {

                emailValidate(etEmailUpdate, getString(R.string.email_validate)) {
                    email = etToString(etEmailUpdate)
                }

                if (etNewPassword.text.isNotEmpty()) {
                    passwordValidate(etNewPassword, getString(R.string.password_validate)) {
                        passwordValidate(etRetypePassword, getString(R.string.password_validate)) {
                            if (etToString(etNewPassword) == etToString(etRetypePassword))
                                update(etToString(etNewPassword))
                            else
                                setError(etRetypePassword, "Password didn't match, Try again")
                        }
                    }
                } else update(null)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun update(pass: String?) {
        presenter.updateProfile(
            etToString(etNameUpdate),
            etToString(etCityUpdate),
            email,
            pass
        )
    }
}
