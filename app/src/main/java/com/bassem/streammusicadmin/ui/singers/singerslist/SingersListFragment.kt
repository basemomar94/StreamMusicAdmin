package com.bassem.streammusicadmin.ui.singers.singerslist

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bassem.streammusicadmin.R
import com.bassem.streammusicadmin.adapters.SingersAdapters
import com.bassem.streammusicadmin.adapters.SongssAdapters
import com.bassem.streammusicadmin.databinding.SingerslistFragmentBinding
import com.bassem.streammusicadmin.entities.Singer
import com.bassem.streammusicadmin.entities.Song

class SingersListFragment : Fragment(R.layout.singerslist_fragment) {
    var binding: SingerslistFragmentBinding? = null
    var viewModel: SingersListViewModel? = null
    var singersAdapters: SingersAdapters? = null
    var songssAdapters: SongssAdapters? = null
    var key: String? = null
    var songsList: List<Song>? = null
    var singersList: List<Singer>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = this.arguments
        key = args?.getString("key")
        println(key)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SingerslistFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SingersListViewModel::class.java]
        when (key) {
            "allsingers" -> viewModel?.getSingersList()
            "allsongs" -> viewModel?.getSongsList()

        }
        viewModel?.singers?.observe(viewLifecycleOwner) {
            if (it != null) {
                initSingerRv(it)
                endLoading()
                singersList = it
            }
        }
        viewModel?.songs?.observe(viewLifecycleOwner) {
            if (it != null) {
                initSongsRv(it)
                endLoading()
                songsList = it
            }
        }

        binding?.searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                when (key) {
                    "allsingers" -> singersSearch(p0!!)
                    "allsongs" -> songsSearch(p0!!)
                }
                return true
            }
        })
    }


    private fun initSingerRv(list: List<Singer>) {
        singersAdapters = SingersAdapters(list, requireContext())
        binding?.singersRv?.apply {
            adapter = singersAdapters
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initSongsRv(list: List<Song>) {
        songssAdapters = SongssAdapters(list, requireContext())
        binding?.singersRv?.apply {
            adapter = songssAdapters
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun endLoading() {
        binding?.apply {
            progressBar.visibility = View.GONE
            singersRv.visibility = View.VISIBLE
        }
    }


    private fun songsSearch(q: String) {
        val searchList: MutableList<Song> = mutableListOf()
        songsList?.forEach {
            if (it.name.lowercase().contains(q.lowercase())) {
                searchList.add(it)
            }

        }
        songssAdapters?.fileter(searchList)

    }

    private fun singersSearch(q: String) {
        val searchList: MutableList<Singer> = mutableListOf()
        singersList?.forEach {
            if (it.name.lowercase().contains(q.lowercase())) {
                searchList.add(it)
            }

        }
        singersAdapters?.fileter(searchList)

    }

}