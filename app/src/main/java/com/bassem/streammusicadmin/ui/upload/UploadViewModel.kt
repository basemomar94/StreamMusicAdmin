package com.bassem.streammusicadmin.ui.upload

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bassem.streammusicadmin.entities.Singer
import com.bassem.streammusicadmin.entities.Song
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class UploadViewModel(app: Application) : AndroidViewModel(app) {
    private var db: FirebaseFirestore? = null
    var audioLink = MutableLiveData<String>()
    var coverLink = MutableLiveData<String>()
    var singersList = MutableLiveData<MutableList<Singer>>()


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

    fun addBookInfo(song: Song) {
        val document = UUID.randomUUID().toString()
        db = FirebaseFirestore.getInstance()
        db?.collection("songs")?.document(document)?.set(song)
    }

    fun getSingersList() {
        val singers: MutableList<Singer> = mutableListOf()
        db = FirebaseFirestore.getInstance()
        db?.collection("singers")?.get()?.addOnCompleteListener {
            if (it.isSuccessful) {
                for (dc: DocumentChange in it.result.documentChanges) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        singers.add(dc.document.toObject(Singer::class.java))
                    }

                }
                singersList.postValue(singers)

            }
        }
    }


}