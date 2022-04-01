package com.bassem.streammusicadmin.ui.addsinger

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.bassem.streammusicadmin.R
import com.bassem.streammusicadmin.databinding.FragmentAddsingerBinding
import com.bassem.streammusicadmin.entities.Singer

class AddsingerFragment : Fragment(R.layout.fragment_addsinger) {
    private var binding: FragmentAddsingerBinding? = null
    private var viewModel: AddsingerViewModel? = null
    val REQUEST_CODE = 11
    var photo: Uri? = null
    var dialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddsingerBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[AddsingerViewModel::class.java]

        //Observers
        viewModel?.photoLink?.observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel?.addSinger(prepareSingerDetails(it))
            }
        }
        viewModel?.isSuccessful?.observe(viewLifecycleOwner) {
            if (it) {
                dialog?.dismiss()
                clearFields()
            } else {
                dialog?.dismiss()
            }
        }

        //Listeners
        binding?.singerPhoto?.setOnClickListener {

            getPhoto()
        }

        binding?.button?.setOnClickListener {
            showLoading()
            photo?.let { it1 -> viewModel?.addSingerPhoto(it1) }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            photo = data?.data
            binding?.singerPhoto?.setImageURI(photo)
        }
    }

    private fun getPhoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun prepareSingerDetails(link: String): Singer {
        val singer = Singer()
        return singer.apply {
            name = binding?.singerName?.text.toString()
            bio = binding?.singerbio?.text.toString()
            photo = link
        }

    }

    private fun showLoading() {
        dialog = ProgressDialog(requireContext())
        dialog?.setTitle("uploading audio")
        dialog?.setMessage("please wait...")
        dialog?.show()
    }

    private fun clearFields() {
        binding?.apply {
            singerName.text?.clear()
            singerbio.text?.clear()
        }

    }
}