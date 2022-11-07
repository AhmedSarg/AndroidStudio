package com.udacity.moviestore

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.udacity.moviestore.databinding.FragmentListBinding
import android.view.View
import androidx.fragment.app.activityViewModels
import com.udacity.moviestore.databinding.MovieItemBinding


class ListFragment : Fragment() {

    private val viewModel: MovieViewModel by activityViewModels()
    private lateinit var binding: FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list, container, false
        )

        setHasOptionsMenu(true)

        binding.addMovieBtn.setOnClickListener { view: View ->
            view.findNavController().navigate(ListFragmentDirections.actionListFragmentToDetailFragment())
        }

        viewModel.movies.observe(viewLifecycleOwner, { movies ->

            movies.forEach {
                val itemBinding = MovieItemBinding.inflate(LayoutInflater.from(context))
                itemBinding.movie = it
                itemBinding.executePendingBindings()
                binding.listMenu.addView(itemBinding.root)
                val param = itemBinding.menuItem.layoutParams as ViewGroup.MarginLayoutParams
                param.setMargins(0,16,0,16)
                itemBinding.menuItem.layoutParams = param
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.list_overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }
}