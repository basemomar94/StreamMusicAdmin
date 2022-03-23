package com.bassem.streammusicadmin.ui.upload

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bassem.streammusicadmin.Audio
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.HashMap

class ViewModelUpload(app: Application) : AndroidViewModel(app) {
    private var db: FirebaseFirestore? = null
    var audioLink = MutableLiveData<String>()
    var coverLink = MutableLiveData<String>()


    fun uploadAudio(uri: Uri) {
        val fileName = UUID.randomUUID().toString() + ".mp3"
        val storage = FirebaseStorage.getInstance().reference.child("audio/$fileName")
        storage.putFile(uri).addOnSuccessListener { it ->
            val reuslt = it.metadata!!.reference!!.downloadUrl
            reuslt.addOnSuccessListener {
                audioLink.postValue(it.toString())
            }

        }
    }

    fun uploadCover(uri: Uri) {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val storage = FirebaseStorage.getInstance().reference.child("cover/$fileName")
        storage.putFile(uri).addOnSuccessListener { it ->
            val reuslt = it.metadata!!.reference!!.downloadUrl
            reuslt.addOnSuccessListener {
                coverLink.postValue(it.toString())
            }

        }

    }

    fun addBookInfo(audio: Audio) {
        val document = UUID.randomUUID().toString()
        db = FirebaseFirestore.getInstance()
        db?.collection("books")?.document(document)?.set(audio)
    }


}