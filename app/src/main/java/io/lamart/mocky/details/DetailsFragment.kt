package io.lamart.mocky.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class DetailsFragment : Fragment() {

    // potentially necessary for refreshing the details
    // private val args: DetailsFragmentArgs by navArgs()
    private val viewModel: DetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? =
        container
            ?.context
            ?.let { ComposeView(it) }
            ?.apply {
                setContent {
                    DetailsContent(viewModel)
                }
            }

}

