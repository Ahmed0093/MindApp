package com.development.task.mymapp.mvplist

import android.content.Context
import android.net.ConnectivityManager
import com.development.task.mymapp.model.*
import com.development.task.mymapp.providers.DownloadProvider
import com.mindvalley.appcontentloader.utilities.ServiceContentDownloadManager
import org.junit.Test

import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Created by Ahmed Abdullah on 9/28/2019.
 */
class ListPresenterTest {
    private lateinit var lisPresenter: ListPresenter

    @Mock
    lateinit var listView: ListContract.View
    @Mock
    lateinit var downloadProvider: ServiceContentDownloadManager
    @Mock
    lateinit var context: Context

    @Mock
    lateinit var internetConnectivityHelper: DownloadProvider

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        lisPresenter = ListPresenter(
            listView,
            downloadProvider,
            context,
            internetConnectivityHelper
        )
    }
    /**
     * Verify [ListPresenter.fetchArticles] triggers when internet connecttion available no interaction with [listView.showErrorMessage] &
     * */
    @Test
    fun fetchArticles_Success() {

        `when`(internetConnectivityHelper.isNetworkAvailable(context))
            .thenReturn(true)
        lisPresenter.fetchArticles()
        verify(listView, never()).showErrorMessage()


    }
    /**
     * Verify [ListPresenter.fetchArticles] triggers when no internet connections interaction with [listView.showErrorMessage] &
     * */
    @Test
    fun fetchArticles() {
        val connectivityManager = mock(ConnectivityManager::class.java)

        `when`(context.getSystemService(Context.CONNECTIVITY_SERVICE))
            .thenReturn(connectivityManager)
        `when`(internetConnectivityHelper.isNetworkAvailable(context))
            .thenReturn(false)

        lisPresenter.fetchArticles()
        verify(listView).showErrorMessage()


    }
    /**
     * Verify [ListPresenter.onArticleItemCLicked] triggers no interaction with [listView.navigateToDetailsActivity] &
     * */
    @Test
    fun onArticleItemCLicked() {
        lisPresenter.onArticleItemCLicked(getDummyResultList().get(0))
        verify(listView).navigateToDetailsActivity(getDummyResultList().get(0))
    }

    fun getDummyResultList(): List<ResultRespone> {
        val results: ArrayList<ResultRespone> = ArrayList()

        val result2: ResultRespone = ResultRespone(
            "",
            "",
            2,
            3,
            "",
            4,
            true,
            User("", "", "", Profile_image("", "", ""), Links("", "", "")),
            listOf("", ""),
            Urls("", "", "", "", ""),
            listOf(),
            Links("", "", "")
        )

        results.add(result2)
        return results
    }
    /**
     * Verify [ListPresenter.onFabIconClicked] triggers no interaction with [listView.showToastCacheCleared] &
     * */
    @Test
    fun onFabIconClicked() {
        lisPresenter.onFabIconClicked()
        verify(listView).showToastCacheCleared()
    }
}