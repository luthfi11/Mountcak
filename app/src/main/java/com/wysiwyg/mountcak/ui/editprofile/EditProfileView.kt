package com.wysiwyg.mountcak.ui.editprofile

import android.net.Uri
import com.wysiwyg.mountcak.data.model.User

interface EditProfileView {
    fun showData(user: User?)
    fun showLoading(title: String)
    fun hideLoading()
    fun showUpdated(title: String)
    fun showFail(title: String)
    fun showUpdatedPhoto(path: Uri)
    fun showChoosePhoto()
}