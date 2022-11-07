package com.udacity.moviestore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.udacity.moviestore.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_detail, container, false
        )

        binding.movie = viewModel
        binding.lifecycleOwner = this

        viewModel.eventDone.observe(viewLifecycleOwner, { event ->
            if (event) {
                viewModel.addMovie()
                view?.findNavController()
                    ?.navigate(
                        DetailFragmentDirections.actionDetailFragmentToListFragment()
                    )
                viewModel.onDoneComplete()
            }
        })

        viewModel.eventCancel.observe(viewLifecycleOwner, { event ->
            if (event) {
                view?.findNavController()
                    ?.navigate(
                        DetailFragmentDirections.actionDetailFragmentToListFragment()
                    )
                viewModel.onCancelComplete()
            }
        })

        return binding.root
    }
}