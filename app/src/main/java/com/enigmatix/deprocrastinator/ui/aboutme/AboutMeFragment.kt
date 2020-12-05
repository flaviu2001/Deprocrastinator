package com.enigmatix.deprocrastinator.ui.aboutme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.enigmatix.deprocrastinator.ExperienceManipulator
import com.enigmatix.deprocrastinator.R
import com.enigmatix.deprocrastinator.UserInfoManipulator
import com.enigmatix.deprocrastinator.databinding.AboutMeFragmentBinding

class AboutMeFragment : Fragment() {
    private lateinit var binding: AboutMeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AboutMeFragmentBinding.inflate(inflater)
        UserInfoManipulator.nameLiveData.observe(viewLifecycleOwner){
            binding.name.setText(it)
        }
        binding.setName.setOnClickListener {
            UserInfoManipulator.setName(requireActivity(), binding.name.text.toString())
        }
        ExperienceManipulator.getCurrentXP(requireContext()).observe(viewLifecycleOwner){
            var xp = 0L
            if (it != null)
                xp = it.xp
            binding.xpTextView.text = requireContext().getString(R.string.xp_format).format(xp/100, 100-xp%100)
        }
        return binding.root
    }
}