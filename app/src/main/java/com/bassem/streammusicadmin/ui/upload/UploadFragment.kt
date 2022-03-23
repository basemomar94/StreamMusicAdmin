package com.bassem.streammusicadmin.ui.upload

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bassem.streammusicadmin.Audio
import com.bassem.streammusicadmin.R
import com.bassem.streammusicadmin.databinding.FragmentUploadBinding
import java.net.URI

class UploadFragment : Fragment(R.layout.fragment_upload) {
    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding
    private val AUDIO_CODE = 100
    private val Cover_CODE = 100
    private lateinit var audio: Uri
    private lateinit var cover: Uri
    private var audioLink: String? = null

    private var viewmodel: ViewModelUpload? = null
    var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUploadBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel = ViewModelProvider(this)[ViewModelUpload::class.java]

        //Listeners
        binding?.musicFile?.setOnClickListener {
            getMusic()
        }
        binding?.button?.setOnClickListener {
            showLoading()
            viewmodel?.uploadAudio(audio)
        }
        binding?.coverPhoto?.setOnClickListener {
            getCover()
        }


        // Observers
        viewmodel?.audioLink?.observe(viewLifecycleOwner) {
            if (it != null) {
                audioLink = it
                viewmodel?.uploadCover(cover)
            }
        }

        viewmodel?.coverLink?.observe(viewLifecycleOwner) {
            if (it != null) {
                viewmodel?.addBookInfo(getData(audioLink!!, it))
                dialog?.dismiss()
                clearFields()


            }


        }
    }


    private fun getMusic() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "audio/*"
        startActivityForResult(intent, AUDIO_CODE)
    }

    private fun getCover() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, Cover_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AUDIO_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                audio = data.data!!
            }

        }
        if (requestCode == Cover_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                cover = data.data!!
                binding?.coverPhoto?.setImageURI(cover)
            }

        }


    }

    private fun getData(audioLink: String, coverLink: String): Audio {
        val audio = Audio()
        audio.name = binding?.bookName?.text.toString()
        audio.description = binding?.bookDesc?.text.toString()
        audio.audioLink = audioLink
        audio.coverLink = coverLink
        return audio
    }

    private fun clearFields() {
        binding?.apply {
            bookDesc.text?.clear()
            bookName.text?.clear()
        }

    }

    private fun showLoading() {
        dialog = ProgressDialog(requireContext())
        dialog?.setTitle("uploading audio")
        dialog?.setMessage("please wait...")
        dialog?.show()
    }

}