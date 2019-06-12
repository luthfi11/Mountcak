package com.wysiwyg.mountcak.util

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.mountcak.R

fun showMountList(ctx: Context, spn: Spinner, tvLoc: TextView, sel: Int) {
    val db = FirebaseDatabase.getInstance().reference.child("mount")
    db.addValueEventListener(object : ValueEventListener{
        override fun onDataChange(p0: DataSnapshot) {
            val name: MutableList<String?> = mutableListOf()
            val loc: MutableList<String?> = mutableListOf()
            for (data: DataSnapshot in p0.children) {
                val n = data.child("mountName").getValue(String::class.java)
                val l = data.child("location").getValue(String::class.java)
                name.add(n)
                loc.add(l)
            }
            spn.adapter = ArrayAdapter(ctx, R.layout.spinner_item, name)
            spn.setSelection(sel)
            spn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    tvLoc.text = loc[position]
                }
            }
        }

        override fun onCancelled(p0: DatabaseError) {

        }
    })
}