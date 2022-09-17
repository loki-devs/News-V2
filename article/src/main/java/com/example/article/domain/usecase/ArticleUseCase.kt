package com.example.article.domain.usecase

import com.example.article.domain.model.ArticleData
import com.example.article.domain.model.ArticleParam
import com.example.core.domain.UseCase

interface ArticleUseCase: UseCase<ArticleParam, List<ArticleData>>