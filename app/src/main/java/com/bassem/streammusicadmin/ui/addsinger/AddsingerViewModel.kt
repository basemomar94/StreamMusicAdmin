package com.bassem.streammusicadmin.ui.addsinger

import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bassem.streammusicadmin.entities.Singer
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AddsingerViewModel(app: Application) : AndroidViewModel(app) {
    var db: FirebaseFirestore? = null
    var photoLink = MutableLiveData<String>()
    val context = app.applicationContext
    var isSuccessful = MutableLiveData<Boolean>()

    fun addSingerPhoto(uri: Uri) {
        val filename = UUID.randomUUID().toString() + "jpg"
        val storage = FirebaseStorage.getInstance().reference.child("singers/$filename")
        storage.putFile(uri).addOnSuccessListener { it ->
            val reuslt = it.metadata!!.reference!!.downloadUrl
            reuslt.addOnSuccessListener {
                photoLink.postValue(it.toString())

            }
        }
    }

    fun addSinger(singer: Singer) {
        db = FirebaseFirestore.getInstance()
        db?.collection("singers")?.add(singer)?.addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "singer has been added", Toast.LENGTH_SHORT).show()
                isSuccessful.postValue(true)
            } else {
                Toast.makeText(context, it.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                isSuccessful.postValue(false)


            }
        }
    }


}