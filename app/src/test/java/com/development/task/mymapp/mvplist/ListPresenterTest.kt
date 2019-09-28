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

    @Test
    fun fetchArticles_Success() {

        `when`(internetConnectivityHelper.isNetworkAvailable(context))
            .thenReturn(true)
        lisPresenter.fetchArticles()
        verify(listView, never()).showErrorMessage()


    }

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

    @Test
    fun onFabIconClicked() {
        lisPresenter.onFabIconClicked()
        verify(listView).showToastCacheCleared()
    }
}