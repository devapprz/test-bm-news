package com.yusuf.bankmandiri.newsapps.feature.news

import android.content.Context
import com.github.kittinunf.fuel.httpGet
import com.yusuf.bankmandiri.newsapps.feature.news.data.News
import com.yusuf.bankmandiri.newsapps.utils.readResult
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

@ViewModelScoped
class NewsRemote
@Inject
constructor(
    @ApplicationContext
    private val context: Context
) {

    fun find(search: String?, source: String, page: Int, pageSize: Int) = "v2/top-headlines"
        .httpGet(
            listOfNotNull(
                "sources" to source,
                search?.let { "q" to it },
                "page" to page,
                "pageSize" to pageSize
            )
        )
        .readResult<List<News>>(context = context, key = "articles")

    private val _newsMock = listOf(
        News(title = "a1"),
        News(title = "a2"),
        News(title = "a3"),
        News(title = "a4"),
        News(title = "a5"),
        News(title = "a6"),
        News(title = "a7"),
        News(title = "a8"),
        News(title = "a9"),
        News(title = "a10"),
        News(title = "a11"),
        News(title = "a12"),
        News(title = "a13"),
        News(title = "a14"),
        News(title = "a15"),
        News(title = "a16"),
        News(title = "a17"),
        News(title = "a18"),
        News(title = "a19"),
        News(title = "a20"),
        News(title = "a21"),
        News(title = "a22"),
        News(title = "a23"),
        News(title = "a24"),
        News(title = "a25"),
        News(title = "a26"),
        News(title = "a27"),
        News(title = "a28"),
        News(title = "a29"),
        News(title = "a30"),
        News(title = "a31"),
        News(title = "a32"),
        News(title = "a33"),
        News(title = "a34"),
        News(title = "a35"),
        News(title = "a36"),
        News(title = "a37"),
        News(title = "a38"),
        News(title = "a39"),
        News(title = "a40"),
        News(title = "a41"),
        News(title = "a42"),
        News(title = "a43"),
        News(title = "a44"),
    )

    private var mFirstData = 0
    private var mLastData = 0

    fun findMock(search: String?, source: String, page: Int, pageSize: Int) = flow {
        kotlinx.coroutines.delay(5000)
        /*
        * page = 1
        * pageSize = 10
        *
        * page = 2
        * pageSize 10
        * */

        /*
        * page 1
        * pageSize(last) = 10
        *
        * page 11
        * pageSize(last) = 20
        *
        * page 21
        * pageSize(last) = 30
        * */
        mFirstData += (if (page == 0) 0 else pageSize)
        mLastData += (if (page == 0) (pageSize - 1) else pageSize)
        Timber.tag("RZ_").v("Data from : $mFirstData, to : $mLastData")
        emit(_newsMock.slice(mFirstData..mLastData).map {
            it.copy(urlToImage = "https://cdn.pixabay.com/photo/2012/08/27/14/19/mountains-55067__340.png")
        })
    }

}