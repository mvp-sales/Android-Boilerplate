package com.mvpsales.github.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.mvpsales.github.databinding.FragmentMainBinding
import com.usercentrics.sdk.UsercentricsBanner
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import kotlin.math.roundToInt

@AndroidEntryPoint
class MainFragment: Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            showConsentBannerBtn.setOnClickListener {
                viewModel.onShowConsentBanner()
            }

            viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
                when (uiState) {
                    MainViewModel.UiState.Initial -> {}
                    MainViewModel.UiState.ShowConsentBanner -> {
                        val banner = UsercentricsBanner(requireActivity())
                        banner.showFirstLayer { userResponse ->
                            userResponse?.consents?.let { consents ->
                                viewModel.applyConsents(consents)
                            }
                        }
                    }

                    is MainViewModel.UiState.UsercentricsSdkError -> {
                        Snackbar.make(
                            root,
                            uiState.error.message ?: uiState.error.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    is MainViewModel.UiState.ShowConsentScore -> {
                        consentScoreValueTv.text = "${uiState.consentScore.roundToInt()}"
                    }
                }
            }
        }
    }
}