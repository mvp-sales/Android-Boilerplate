package com.mvpsales.github.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.mvpsales.github.databinding.FragmentMainBinding
import com.usercentrics.sdk.Usercentrics
import com.usercentrics.sdk.UsercentricsBanner
import com.usercentrics.sdk.UsercentricsServiceConsent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment: Fragment() {

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
                Usercentrics.isReady({ status ->
                    if (status.shouldCollectConsent) {
                        // Show banner to collect consent
                        showConsentBanner()
                    } else {
                        // Apply consent with status.consents
                    }
                }, { error ->
                    // Handle non-localized error
                    Snackbar.make(root, error.message ?: error.toString(), Snackbar.LENGTH_LONG).show()
                })
            }
        }
    }

    private fun showConsentBanner() {
        val banner = UsercentricsBanner(requireActivity())
        banner.showFirstLayer() { userResponse ->
            userResponse?.consents?.let { consents ->
                applyConsents(consents)
            }
        }
    }

    private fun applyConsents(consents: List<UsercentricsServiceConsent>) {

    }
}