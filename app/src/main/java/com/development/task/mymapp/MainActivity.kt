package com.development.task.mymapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.development.task.mymapp.model.ResultRespone
import com.development.task.mymapp.mvplist.ListAdapter
import com.development.task.mymapp.mvplist.ListContract
import com.development.task.mymapp.mvplist.listPresenter
import com.development.task.mymapp.providers.ProvideContext
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), ListContract.View, ListAdapter.OnArticleItemClicked {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var listAdapter: ListAdapter
    private val context: Context by lazy { this }

    override fun onCreate(savedInstanceState: Bundle?) {
        ProvideContext.withContext(this)
        listPresenter.setView(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("Respone", "Start1")
        mRecyclerView = findViewById(R.id.rvPosts)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        listAdapter = ListAdapter(this)
        mRecyclerView.adapter = listAdapter
        listPresenter.fetchArticles()
        fabidClearCache.setOnClickListener { _ -> (listPresenter.onFabIconClicked()) }
    }

    override fun showErrorMessage() {
        Toast.makeText(this, R.string.no_internet_connection_msg, Toast.LENGTH_LONG).show()
    }

    override fun showToastCacheCleared() {
        Toast.makeText(this, R.string.cahce_cleared_msg, Toast.LENGTH_LONG).show()
        listPresenter.fetchArticles()
    }

    override fun onClick(results: ResultRespone) {
        listPresenter.onArticleItemCLicked(results)
    }

    override fun showArticleList(results: Array<ResultRespone>) {
        results?.let { listAdapter.setData(it) }
    }

    override fun navigateToDetailsActivity(results: ResultRespone) {
        val intent = Intent(context, DetailsActivity::class.java)
        intent.putExtra(Constants.SELECTED_ITEM, results)
        context.startActivity(intent)
    }
}
