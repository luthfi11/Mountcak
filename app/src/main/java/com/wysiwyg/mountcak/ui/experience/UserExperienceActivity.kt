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
import com.wysiwyg.mountcak.data.model.Experience
import com.wysiwyg.mountcak.util.FirebaseUtil
import kotlinx.android.synthetic.main.activity_user_experience.*
import kotlinx.android.synthetic.main.layout_add_experience.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.toast

class UserExperienceActivity : AppCompatActivity() {

    private lateinit var adapter: UserExperienceAdapter
    private val experience = mutableListOf<Experience?>()
    private val db = FirebaseDatabase.getInstance().reference.child("experience")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_experience)
        setSupportActionBar(toolbarExperience)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val id = intent.getIntExtra("id",99)
        getUserExperience(id)

        adapter = UserExperienceAdapter(experience)
        rvExperience.layoutManager = LinearLayoutManager(this)
        rvExperience.adapter = adapter

        srlExperience.onRefresh { getUserExperience(id) }
        fabAdd.onClick { showFilterDialog(id.toString()) }
    }

    private fun getUserExperience(id: Int) {
        db.child(id.toString()).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                experience.clear()
                p0.children.forEach {
                    val data = it.getValue(Experience::class.java)
                    experience.add(data)
                }
                adapter.notifyDataSetChanged()
                srlExperience.isRefreshing = false
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun showFilterDialog(id: String) {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.layout_add_experience, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("Add Experience")

        val mAlertDialog = mBuilder.show()
        mAlertDialog.setCancelable(false)
        mDialogView.btnCancel.onClick { mAlertDialog.dismiss() }
        mDialogView.btnShare.onClick {
            mAlertDialog.dismiss()
            val content = mDialogView.etExperience.text.toString()
            val eid = db.child(id).push().key
            db.child(id).child(eid!!).setValue(
                Experience(eid, FirebaseUtil.currentUser(), id, content)
            )
                .addOnSuccessListener {
                    toast(getString(R.string.experience_posted))
                }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}
