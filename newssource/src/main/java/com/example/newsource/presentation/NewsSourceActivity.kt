package com.example.newsource.presentation

import android.os.Bundle
import android.widget.Toast
import com.example.core.ErrorCode
import com.example.core.common.PaginationListener
import com.example.core.common.extension.gone
import com.example.core.common.extension.setSingleClickListener
import com.example.core.common.extension.viewBinding
import com.example.core.common.extension.visible
import com.example.core.presentation.base.BaseActivity
import com.example.core.presentation.viewstate.PaginationViewState
import com.example.newsource.R
import com.example.newsource.databinding.ActivityNewsSourceBinding
import com.example.newsource.domain.model.NewsSourceData
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsSourceActivity: BaseActivity() {

    private val categoryId: String
        get() = intent?.extras?.getString("CATEGORY_ID", "").toString()
    private val binding by viewBinding(ActivityNewsSourceBinding::inflate)
    private val viewModel by viewModel<NewsSourceViewModel>()
    private val adapter by lazy { NewsSourceAdapter() }
    private val paginationListener by lazy {
        PaginationListener { viewModel.getNewsSourceByCategory(categoryId) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        startObservingData()
        viewModel.getNewsSourceByCategory(categoryId)
    }

    private fun initView() {
        title = getString(R.string.news_source)
        with(binding) {
            rvNewsSource.adapter = adapter
            rvNewsSource.addOnScrollListener(paginationListener)
            srlRefresh.apply {
                setOnRefreshListener {
                    isRefreshing = false
                    adapter.clear()
                    viewModel.resetPage()
                    viewModel.getNewsSourceByCategory(categoryId)
                }
            }
        }
    }

    private fun startObservingData() {
        viewModel.newsSourceState.observe(this) { state ->
            when (state) {
                is PaginationViewState.Loading -> {
                    hideError()
                    showLoading()
                }

                is PaginationViewState.LoadMoreLoading -> showPaginationLoading()

                else -> {
                    hideLoading()
                    hidePaginationLoading()
                    when (state) {
                        is PaginationViewState.Success -> bindView(state.data, state.isLastPage)
                        is PaginationViewState.Error -> showError(state.viewError?.errorCode.toString()) { viewModel.getNewsSourceByCategory(categoryId) }
                        is PaginationViewState.EmptyData -> showEmpty()
                        else -> showErrorToast()
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.srlRefresh.gone()
        binding.viewLoading.clLoading.visible()
    }

    private fun hideLoading() {
        binding.viewLoading.clLoading.gone()
        binding.srlRefresh.visible()
    }

    private fun showPaginationLoading() {
        adapter.showLoadingFooter()
    }

    private fun hidePaginationLoading() {
        adapter.hideLoadingFooter()
    }

    private fun showError(errorCode: String, action: () -> Unit) {
        binding.srlRefresh.gone()
        when(errorCode) {
            ErrorCode.GLOBAL_INTERNET_ERROR -> {
                binding.viewErrorHandling.tvErrorMessage.text = getString(com.example.core.R.string.core_connection_error)
                binding.viewErrorHandling.ivError.setImageResource(com.example.core.R.drawable.ic_no_wifi)
            }

            else -> {
                binding.viewErrorHandling.ivError.setImageResource(com.example.core.R.drawable.icon_error)
                binding.viewErrorHandling.tvErrorMessage.text = getString(com.example.core.R.string.core_generic_error)
            }
        }
        binding.viewErrorHandling.btnRetry.visible()
        binding.viewErrorHandling.btnRetry.setSingleClickListener { action.invoke() }
        binding.viewErrorHandling.llErrorHandling.visible()
    }

    private fun showEmpty() {
        binding.srlRefresh.gone()
        binding.viewErrorHandling.tvErrorMessage.text = getString(com.example.core.R.string.core_search_empty)
        binding.viewErrorHandling.ivError.setImageResource(com.example.core.R.drawable.icon_no_data)
        binding.viewErrorHandling.btnRetry.gone()
        binding.viewErrorHandling.llErrorHandling.visible()
    }

    private fun hideError() {
        binding.srlRefresh.visible()
        binding.viewErrorHandling.llErrorHandling.gone()
    }

    private fun bindView(data: List<NewsSourceData>, isLastPages: Boolean) {
        hideError()
        paginationListener.apply {
            isLastPage = isLastPages
            itemCount = data.size
        }
        adapter.resetData(data)
        if (isLastPages) adapter.hideLoadingFooter()
    }

    private fun showErrorToast() {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
    }
}