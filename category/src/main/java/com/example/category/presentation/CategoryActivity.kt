package com.example.category.presentation

import android.content.Intent
import android.os.Bundle
import com.example.category.R
import com.example.category.databinding.ActivityCategoryBinding
import com.example.category.domain.model.CategoryData
import com.example.core.common.extension.startActivityLeftTransition
import com.example.core.common.extension.viewBinding
import com.example.core.presentation.base.BaseActivity
import com.example.newsource.presentation.NewsSourceActivity

class CategoryActivity: BaseActivity() {

    private val binding by viewBinding(ActivityCategoryBinding::inflate)

    private val categoryAdapter by lazy { CategoryAdapter() }

    private val categoriesData = mutableListOf<CategoryData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        showData()
    }

    private fun initView() {
        title = getString(R.string.category)
        binding.rvCategory.adapter = categoryAdapter
        categoryAdapter.setItemClickListener { _, categoryData, _ ->
            val intent = Intent(this, NewsSourceActivity::class.java)
            intent.putExtra("CATEGORY_ID", categoryData?.id)
            startActivityLeftTransition(intent)
        }
    }

    private fun showData() {
        createData()
        categoryAdapter.addAll(categoriesData as ArrayList<CategoryData>)
    }

    private fun createData() {
        categoriesData.add(CategoryData("business", "Business"))
        categoriesData.add(CategoryData("entertainment", "Entertainment"))
        categoriesData.add(CategoryData("health", "Health"))
        categoriesData.add(CategoryData("science", "Science"))
        categoriesData.add(CategoryData("sports", "Sports"))
        categoriesData.add(CategoryData("technology", "Technology"))
    }
}