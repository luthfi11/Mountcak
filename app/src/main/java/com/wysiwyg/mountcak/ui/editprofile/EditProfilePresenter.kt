package com.wysiwyg.mountcak.ui.editprofile

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.wysiwyg.mountcak.data.model.User
import java.lang.Exception

class EditProfilePresenter(private val view: EditProfileView) {

    private val current = FirebaseAuth.getInstance().currentUser
    private val db = FirebaseDatabase.getInstance().reference.child("user").child(current!!.uid)
    private val storage = FirebaseStorage.getInstance().reference.child(current!!.uid + ".png")

    fun getData(user: User?) {
        view.showData(user)
    }

    fun choosePhoto() {
        view.showChoosePhoto()
    }

    fun updatePhoto(filePath: Uri) {
        try {
            view.showLoading("Uploading")
            storage.putFile(filePath)
                .continueWithTask { uploadTask ->
                    if (!uploadTask.isSuccessful) {
                        throw uploadTask.exception!!
                    }
                    return@continueWithTask storage.downloadUrl
                }
                .addOnSuccessListener { uri ->
                    view.hideLoading()
                    view.showUpdatedPhoto(uri)
                    db.child("photo").setValue(uri.toString())
                }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun updateProfile(newName: String, newCity: String, newMail: String, newPhone: String, newPass: String?) {
        try {
            view.showLoading("Updating Profile")

            if (newMail != current?.email) current?.updateEmail(newMail)
            if (newPass != null) current?.updatePassword(newPass)

            db.let {
                it.child("name").setValue(newName)
                it.child("city").setValue(newCity)
                it.child("email").setValue(newMail)
                it.child("phone").setValue(newPhone)
            }
                .addOnSuccessListener {
                    view.hideLoading()
                    view.showUpdated("Profile Updated")
                }
                .addOnFailureListener {
                    view.hideLoading()
                    view.showFail("Failed to update your profile")
                }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

}