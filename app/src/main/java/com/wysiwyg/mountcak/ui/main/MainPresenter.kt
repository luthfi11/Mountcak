package com.wysiwyg.mountcak.ui.main

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.wysiwyg.mountcak.ui.activities.ActivitiesFragmentManager
import com.wysiwyg.mountcak.ui.explore.ExploreFragment
import com.wysiwyg.mountcak.ui.home.HomeFragment
import com.wysiwyg.mountcak.ui.profile.ProfileFragment
import com.wysiwyg.mountcak.ui.rental.RentalFragment

class MainPresenter(private val view: MainView) {

    private val uid = FirebaseAuth.getInstance().currentUser?.uid
    private val db = FirebaseDatabase.getInstance().reference.child("user")

    fun checkLogin() {
        try {
            if (FirebaseAuth.getInstance().currentUser == null) view.toLogin()
            else {
                FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { p0 ->
                    val token = p0?.token.toString()
                    db.child(uid!!).child("tokenId").setValue(token)
                }
            }
        } catch (ex: Exception) {
            view.toLogin()
            ex.printStackTrace()
        }
    }

}