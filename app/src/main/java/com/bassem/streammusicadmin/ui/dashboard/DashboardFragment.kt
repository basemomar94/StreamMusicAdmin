package com.bassem.streammusicadmin.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bassem.streammusicadmin.R
import com.bassem.streammusicadmin.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
    var binding: FragmentDashboardBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            songsCard.setOnClickListener {
                navigate("allsongs")
            }
            singersCard.setOnClickListener {

                navigate("allsingers")
            }
        }
    }

    private fun navigate(key: String) {
        val bundle = Bundle()
        bundle.putString("key",key)
        val nav =
            Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
        nav.navigate(R.id.action_dashboardFragment_to_singersListFragment, bundle)
    }
}