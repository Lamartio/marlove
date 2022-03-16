package io.lamart.mocky.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import io.lamart.mocky.R
import io.lamart.mocky.navigate

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View =
        inflater.inflate(R.layout.main_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view
            .let { it as ComposeView }
            .setContent { MainContent(viewModel, ::showDetails) }
    }

    private fun showDetails(item: Item) {
        viewModel.select(item.id)

        MainFragmentDirections
            .actionMainFragmentToDetailsFragment(id = item.id)
            .let(::navigate)
    }
}

