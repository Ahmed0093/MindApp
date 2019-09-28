package com.development.task.mymapp.mvplist

import com.development.task.mymapp.MainActivity
import com.development.task.mymapp.model.ResultRespone

/**
 * Created by Ahmed Abdullah on 9/28/2019.
 */
val listContractView by lazy { MainActivity() }

interface ListContract{
    interface View {
        fun showArticleList(results: Array<ResultRespone>)
        fun navigateToDetailsActivity(results: ResultRespone)
        fun showToastCacheCleared()
        fun showErrorMessage()
    }

    interface Presenter {
        fun fetchArticles()
        fun setView(mainActivity: MainActivity)
        fun onArticleItemCLicked(results: ResultRespone)
        fun onFabIconClicked()

    }
}