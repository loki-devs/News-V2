package com.example.article.presentation.listarticle

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.article.common.FilterListener
import com.example.article.databinding.ActivityListArticlesBinding
import com.example.article.domain.model.ArticleData
import com.example.article.presentation.detailarticle.DetailArticleActivity
import com.example.core.ErrorCode
import com.example.core.common.PaginationListener
import com.example.core.common.VerticalItemSpaceDecoration
import com.example.core.common.extension.*
import com.example.core.presentation.base.BaseActivity
import com.example.core.presentation.viewstate.PaginationViewState
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListArticleActivity: BaseActivity(), FilterListener {

    private val sourceId: String
        get() = intent?.extras?.getString("SOURCE_ID", "").toString()
    private val binding by viewBinding(ActivityListArticlesBinding::inflate)
    private val viewModel by viewModel<ListArticleViewModel>()
    private val adapter by lazy { ListArticleAdapter() }
    private val paginationListener by lazy {
        PaginationListener { viewModel.getArticleBySource(sourceId) }
    }

    private var keyword: String = ""
    private var originalArticles = mutableListOf<ArticleData>()
    private var filterArticles = mutableListOf<ArticleData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        initListener()
        startObservingData()
        viewModel.getArticleBySource(sourceId)
    }

    private fun initView() {
        title = getString(com.example.article.R.string.article)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        with(binding) {
            rvArticles.adapter = adapter
            rvArticles.addOnScrollListener(paginationListener)
            rvArticles.addItemDecoration(VerticalItemSpaceDecoration(resources.getDimensionPixelSize(
                com.example.core.R.dimen.spacing_x)))
            srlRefresh.apply {
                setOnRefreshListener {
                    isRefreshing = false
                    adapter.clear()
                    viewModel.resetPage()
                    viewModel.getArticleBySource(sourceId)
                }
            }
        }
    }

    private fun initListener() {
        adapter.setItemClickListener { _, articleData, _ ->
            val intent = Intent(this, DetailArticleActivity::class.java)
            intent.putExtra("EXTRA_URL", articleData?.url)
            startActivityLeftTransition(intent)
        }
        with(binding) {
            etSearch.afterTextChanged {
                keyword = it
                binding.ibClear.visibility = when (keyword.isNotBlank()) {
                    true -> View.VISIBLE
                    else -> View.GONE
                }
                if (keyword.isNotBlank()) {
                    filterArticles = originalArticles.filter { filter -> filter.title.contains(keyword, true) }.toMutableList()
                    adapter.resetData(filterArticles)
                } else {
                    adapter.resetData(originalArticles)
                }
            }
            ibClear.setOnClickListener {
                clearKeyword()
            }
        }
    }

    private fun startObservingData() {
        viewModel.articleState.observe(this) { state ->
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
                        is PaginationViewState.Error -> showError(state.viewError?.errorCode.toString()) { viewModel.getArticleBySource(sourceId) }
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

    private fun bindView(data: List<ArticleData>, isLastPages: Boolean) {
        hideError()
        originalArticles.clear()
        originalArticles.addAll(data)
        paginationListener.apply {
            isLastPage = isLastPages
            itemCount = data.size
        }
        if (keyword.isNotBlank()) {
            adapter.resetData(filterArticles)
        } else {
            adapter.resetData(data)
        }
        if (isLastPages) adapter.hideLoadingFooter()
    }

    private fun showErrorToast() {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
    }

    private fun clearKeyword() {
        binding.etSearch.setText("")
        adapter.resetData(originalArticles)
    }

    override fun filteringFinished(filteredItemsCount: Int) {
        if (filteredItemsCount == 0) showEmpty() else hideError()
    }
}