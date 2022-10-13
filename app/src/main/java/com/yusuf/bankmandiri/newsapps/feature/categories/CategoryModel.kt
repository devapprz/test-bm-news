package com.yusuf.bankmandiri.newsapps.feature.categories

import com.yusuf.bankmandiri.newsapps.feature.categories.data.Category
import com.yusuf.bankmandiri.newsapps.utils.ResponseWrapper
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class CategoryModel
@Inject
constructor() {

    /*
    * The category you want to get articles for. Possible options:
    * - business
    * - entertainment
    * - general
    * - health
    * - science
    * - sports
    * - technology
    * Note: you can't mix this param with the sources param.
    * */
    fun findAll() = flow {
        kotlinx.coroutines.delay(3000)
        val categories = listOf(
            Category(1, "Business"),
            Category(2, "Entertainment"),
            Category(3, "General"),
            Category(4, "Health"),
            Category(5, "Science"),
            Category(6, "Sports"),
            Category(7, "Technology"),
        )
        emit(ResponseWrapper(status = "ok", message = null, categories))
    }

}