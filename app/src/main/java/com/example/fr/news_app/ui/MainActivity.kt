package com.example.fr.news_app.ui

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.fr.news_app.R
import com.example.fr.news_app.adapter.NewsListAdapter
import com.example.fr.news_app.base.BaseActivity
import com.example.fr.news_app.databinding.ActivityMainBinding
import com.example.fr.news_app.model.NewsData
import com.example.fr.news_app.viewmodel.NewsViewModel

class MainActivity : BaseActivity<ActivityMainBinding>(), SwipeRefreshLayout.OnRefreshListener {

    override val layoutResourceId: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarText("Actualités")
        hideBack()
        onLoadingSwipeRefresh()
        init()
    }

    private fun onLoadingSwipeRefresh() {
        try {
            binding.swipeRefreshLayout.post { getNewsApi() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun init() {
        binding.swipeRefreshLayout.setOnRefreshListener(this)
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)
    }

    private fun getNewsApi() {
        val newsViewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
        newsViewModel.getNewsList().observe(this, Observer { newsData ->
            binding.swipeRefreshLayout.isRefreshing = false
            hideProgress()
            showData()
            setAdapter(newsData.articles)
        })
    }

    // setting adapter
    private fun setAdapter(newsList: ArrayList<NewsData>) {
        binding.rvNews.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = NewsListAdapter(this@MainActivity, newsList)

            // onclick adapter
            (adapter as NewsListAdapter).onItemClick = { url ->
                val i = Intent(this@MainActivity, DetailActivity::class.java)
                i.putExtra("url", url)
                startActivity(i)
            }
        }
    }

    override fun onRefresh() {
        binding.swipeRefreshLayout.isRefreshing = true
        getNewsApi()
    }

}
