package com.onandoff.onandoff_android.presentation.look

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.onandoff.onandoff_android.databinding.ActivityReportFeedBinding
import com.onandoff.onandoff_android.databinding.BottomSheetFeedListOptionMenuBinding
import com.onandoff.onandoff_android.presentation.look.viewmodel.ReportFeedViewModel
import kotlinx.coroutines.launch

class ReportFeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportFeedBinding

    private val viewModel by viewModels<ReportFeedViewModel>(factoryProducer = {
        ReportFeedViewModel.Factory
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupViewModel()
        setupListeners()
    }

    private fun setupViewModel() {
        with(viewModel) {
            lifecycleScope.launch {
                state.collect { state ->
                    when (state) {
                        is ReportFeedViewModel.State.ReportFeedFailed -> {
                            when (state.reason) {
                                ReportFeedViewModel.State.ReportFeedFailed.Reason.PARAMETER_ERROR -> {
                                    Toast.makeText(
                                        this@ReportFeedActivity,
                                        "parameter error",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                ReportFeedViewModel.State.ReportFeedFailed.Reason.JWT_ERROR -> {
                                    Toast.makeText(
                                        this@ReportFeedActivity,
                                        "jwt error",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                ReportFeedViewModel.State.ReportFeedFailed.Reason.DB_ERROR -> {
                                    Toast.makeText(
                                        this@ReportFeedActivity,
                                        "db error",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                ReportFeedViewModel.State.ReportFeedFailed.Reason.NO_FEED -> {
                                    Toast.makeText(
                                        this@ReportFeedActivity,
                                        "해당 게시물이 존재하지 않습니다.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                ReportFeedViewModel.State.ReportFeedFailed.Reason.ALREADY_REPORTED -> {
                                    Toast.makeText(
                                        this@ReportFeedActivity,
                                        "이미 신고된 게시글입니다.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                ReportFeedViewModel.State.ReportFeedFailed.Reason.INVALID_REPORT_CATEGORY_ID -> {
                                    Toast.makeText(
                                        this@ReportFeedActivity,
                                        "올바르지 않은 신고 분류입니다.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                ReportFeedViewModel.State.ReportFeedFailed.Reason.NO_REPORT_ETC_REASON -> {
                                    Toast.makeText(
                                        this@ReportFeedActivity,
                                        "기타 신고 사유를 입력하지 않았습니다.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                        ReportFeedViewModel.State.Idle -> {}
                        ReportFeedViewModel.State.ReportFeedSuccess -> {
                            Toast.makeText(
                                this@ReportFeedActivity,
                                "신고가 성공적으로 접수되었습니다!",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.ivBackArrow.setOnClickListener {
            finish()
        }

        binding.btnAgree.setOnClickListener {
            viewModel.reportFeed()
        }

        binding.checkboxSpamPromotion.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                binding.checkboxPorn.isChecked = false
                binding.checkboxDespise.isChecked = false
                binding.checkboxDobae.isChecked = false
                binding.checkboxPrivateInfoIllegalInfo.isChecked = false
                binding.checkboxEtc.isChecked = false
                binding.editEtc.isEnabled = false
            }
        }

        binding.checkboxPorn.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                binding.checkboxSpamPromotion.isChecked = false
                binding.checkboxDespise.isChecked = false
                binding.checkboxDobae.isChecked = false
                binding.checkboxPrivateInfoIllegalInfo.isChecked = false
                binding.checkboxEtc.isChecked = false
                binding.editEtc.isEnabled = false
            }
        }

        binding.checkboxDespise.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                binding.checkboxPorn.isChecked = false
                binding.checkboxSpamPromotion.isChecked = false
                binding.checkboxDobae.isChecked = false
                binding.checkboxPrivateInfoIllegalInfo.isChecked = false
                binding.checkboxEtc.isChecked = false
                binding.editEtc.isEnabled = false
            }
        }

        binding.checkboxDobae.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                binding.checkboxPorn.isChecked = false
                binding.checkboxDespise.isChecked = false
                binding.checkboxSpamPromotion.isChecked = false
                binding.checkboxPrivateInfoIllegalInfo.isChecked = false
                binding.checkboxEtc.isChecked = false
                binding.editEtc.isEnabled = false
            }
        }

        binding.checkboxPrivateInfoIllegalInfo.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                binding.checkboxPorn.isChecked = false
                binding.checkboxDespise.isChecked = false
                binding.checkboxDobae.isChecked = false
                binding.checkboxSpamPromotion.isChecked = false
                binding.checkboxEtc.isChecked = false
                binding.editEtc.isEnabled = false
            }
        }

        binding.checkboxEtc.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                binding.checkboxPorn.isChecked = false
                binding.checkboxDespise.isChecked = false
                binding.checkboxDobae.isChecked = false
                binding.checkboxPrivateInfoIllegalInfo.isChecked = false
                binding.checkboxSpamPromotion.isChecked = false
                binding.editEtc.isEnabled = true

                if (binding.editEtc.text.length > 20) {
                    Toast.makeText(
                        this@ReportFeedActivity,
                        "글자 수는 최대 20자까지 가능합니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                binding.editEtc.text.clear()
                binding.editEtc.isEnabled = false
            }
        }
    }

    private fun onCheckBoxChecked(checked: Boolean) {
        when(checked) {
            (binding.checkboxSpamPromotion.isChecked) -> {
                binding.checkboxPorn.isChecked = false
                binding.checkboxDespise.isChecked = false
                binding.checkboxDobae.isChecked = false
                binding.checkboxPrivateInfoIllegalInfo.isChecked = false
                binding.checkboxEtc.isChecked = false
                binding.editEtc.isEnabled = false
            }
            (binding.checkboxPorn.isChecked) -> {
                binding.checkboxSpamPromotion.isChecked = false
                binding.checkboxDespise.isChecked = false
                binding.checkboxDobae.isChecked = false
                binding.checkboxPrivateInfoIllegalInfo.isChecked = false
                binding.checkboxEtc.isChecked = false
                binding.editEtc.isEnabled = false
            }
            (binding.checkboxDespise.isChecked) -> {
                binding.checkboxSpamPromotion.isChecked = false
                binding.checkboxDobae.isChecked = false
                binding.checkboxPorn.isChecked = false
                binding.checkboxPrivateInfoIllegalInfo.isChecked = false
                binding.checkboxEtc.isChecked = false
                binding.editEtc.isEnabled = false
            }
            (binding.checkboxDobae.isChecked) -> {
                binding.checkboxSpamPromotion.isChecked = false
                binding.checkboxPorn.isChecked = false
                binding.checkboxDespise.isChecked = false
                binding.checkboxPrivateInfoIllegalInfo.isChecked = false
                binding.checkboxEtc.isChecked = false
                binding.editEtc.isEnabled = false
            }
            (binding.checkboxPrivateInfoIllegalInfo.isChecked) -> {
                binding.checkboxSpamPromotion.isChecked = false
                binding.checkboxPorn.isChecked = false
                binding.checkboxDespise.isChecked = false
                binding.checkboxDobae.isChecked = false
                binding.checkboxEtc.isChecked = false
                binding.editEtc.isEnabled = false
            }
            (binding.checkboxEtc.isChecked) -> {
                binding.checkboxSpamPromotion.isChecked = false
                binding.checkboxPorn.isChecked = false
                binding.checkboxDespise.isChecked = false
                binding.checkboxDobae.isChecked = false
                binding.checkboxPrivateInfoIllegalInfo.isChecked = false
                binding.editEtc.isEnabled = true
            }
            else -> {}
        }
    }


    companion object {
        fun getIntent(context: Context, feedId: Int) =
            Intent(context, ReportFeedActivity::class.java)
                .putExtra(ReportFeedViewModel.FEED_ID, feedId)
    }
}