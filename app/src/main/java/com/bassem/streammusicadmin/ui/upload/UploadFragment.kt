package com.bassem.streammusicadmin.ui.upload

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bassem.streammusicadmin.entities.Song
import com.bassem.streammusicadmin.R
import com.bassem.streammusicadmin.databinding.FragmentUploadBinding
import com.google.android.material.chip.Chip
import java.io.File

class UploadFragment : Fragment(R.layout.fragment_upload) {
    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding
    private val AUDIO_CODE = 200
    private val Cover_CODE = 100
    private var audio: Uri? = null
    private var cover: Uri? = null
    private var audioLink: String? = null
    private var category: String? = null
    private var selectedSinger: String? = null

    private var viewmodel: UploadViewModel? = null
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
        viewmodel = ViewModelProvider(this)[UploadViewModel::class.java]
        viewmodel?.getSingersList()

        //Listeners
        binding?.musicFile?.setOnClickListener {
            getMusic()
        }
        binding?.button?.setOnClickListener {
            if (isFieldsFull()) {
                showLoading()
                viewmodel?.uploadAudio(audio!!)
            } else {
                Toast.makeText(requireContext(), "please fill your data", Toast.LENGTH_SHORT).show()
            }

        }
        binding?.coverPhoto?.setOnClickListener {
            getCover()
        }

        binding?.categoryChips?.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId > -1) {
                val selectedChip = view.findViewById<Chip>(checkedId)
                category = selectedChip.text.toString()
            }


        }


        // Observers
        viewmodel?.singersList?.observe(viewLifecycleOwner) {
            val singers = it.map { it.name }
            val spinerAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, singers)
            binding?.spinner?.apply {
                adapter = spinerAdapter

            }
            binding?.spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    selectedSinger = singers[position]
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }

        }
        viewmodel?.audioLink?.observe(viewLifecycleOwner) {
            if (it != null) {
                audioLink = it
                viewmodel?.uploadCover(cover!!)
            }
        }

        viewmodel?.coverLink?.observe(viewLifecycleOwner) {
            if (it != null) {
                viewmodel?.addBookInfo(getData(audioLink!!, it, category!!))
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
                val details = File(audio?.path).name
                println(details)
            }

        }
        if (requestCode == Cover_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                cover = data.data!!
                binding?.coverPhoto?.setImageURI(cover)
                println("cover")
            }

        }


    }


    private fun getData(audioL: String, cover: String, cato: String): Song {
        val audio = Song()
        return audio.apply {
            name = binding?.bookName?.text.toString()
            description = binding?.bookDesc?.text.toString()
            audioLink = audioL
            coverLink = cover
            category = cato
            singer= selectedSinger!!

        }
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

    private fun isFieldsFull() =
     binding?.bookName?.text?.isNotEmpty()!! && audio != null && cover != null && category != null

}