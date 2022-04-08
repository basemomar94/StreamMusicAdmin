package com.bassem.streammusicadmin.ui.singers.singerslist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bassem.streammusicadmin.entities.Singer
import com.bassem.streammusicadmin.entities.Song
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore

class SingersListViewModel : ViewModel() {
    val singers = MutableLiveData<List<Singer>>()
    val songs = MutableLiveData<List<Song>>()

    val db = FirebaseFirestore.getInstance()


    fun getSingersList() {
        var list: MutableList<Singer> = mutableListOf()
        db.collection("singers").get().addOnCompleteListener {
            for (dc in it.result.documentChanges) {
                if (dc.type == DocumentChange.Type.ADDED) {
                    list.add(dc.document.toObject(Singer::class.java))
                }

            }
            singers.postValue(list)

        }
    }

    fun getSongsList() {
        var list: MutableList<Song> = mutableListOf()
        db.collection("songs").get().addOnCompleteListener {
            for (dc in it.result.documentChanges) {
                if (dc.type == DocumentChange.Type.ADDED) {
                    list.add(dc.document.toObject(Song::class.java))

                }

            }
            println(list)
            songs.postValue(list)

        }
    }


}