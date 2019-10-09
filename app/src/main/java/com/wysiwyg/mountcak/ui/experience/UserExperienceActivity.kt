package com.wysiwyg.mountcak.ui.experience

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Review
import com.wysiwyg.mountcak.util.FirebaseUtil
import kotlinx.android.synthetic.main.activity_user_experience.*
import kotlinx.android.synthetic.main.layout_add_experience.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.toast

class UserExperienceActivity : AppCompatActivity() {

    private lateinit var adapter: UserExperienceAdapter
    private val experience = mutableListOf<Review?>()
    private val db = FirebaseDatabase.getInstance().reference.child("experience")
    private var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_experience)
        setSupportActionBar(toolbarExperience)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        id = intent.getIntExtra("id",99)
        getUserExperience(id)

        adapter = UserExperienceAdapter(experience)
        rvExperience.layoutManager = LinearLayoutManager(this)
        rvExperience.adapter = adapter

        srlExperience.onRefresh { getUserExperience(id) }
        fabAdd.onClick { showFilterDialog(id.toString()) }
    }

    private val listener = object : ValueEventListener{
        override fun onDataChange(p0: DataSnapshot) {
            experience.clear()
            p0.children.forEach {
                val data = it.getValue(Review::class.java)
                experience.add(data)
            }
            adapter.notifyDataSetChanged()
            srlExperience.isRefreshing = false
        }

        override fun onCancelled(p0: DatabaseError) {

        }
    }

    private fun getUserExperience(id: Int?) {
        db.child(id.toString()).addValueEventListener(listener)
    }

    private fun showFilterDialog(id: String) {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.layout_add_experience, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("Add Review")

        val mAlertDialog = mBuilder.show()
        mAlertDialog.setCancelable(false)
        mDialogView.btnCancel.onClick { mAlertDialog.dismiss() }
        mDialogView.btnShare.onClick {
            if (mDialogView.etExperience.text.isNotEmpty()) {
                mAlertDialog.dismiss()
                val content = mDialogView.etExperience.text.toString()
                val eid = db.child(id).push().key
                db.child(id).child(eid!!).setValue(
                    Review(eid, FirebaseUtil.currentUser(), id, content)
                )
                    .addOnSuccessListener {
                        toast(getString(R.string.experience_posted))
                    }
            } else {
                mDialogView.etExperience.requestFocus()
                mDialogView.etExperience.error = getString(R.string.general_validate)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        db.child(id.toString()).removeEventListener(listener)
    }
}
