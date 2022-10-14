package com.yusuf.bankmandiri.newsapps.feature.sources

import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SourceRepository
@Inject
constructor(
    private val sourceRemote: SourceRemote
) {

    fun findAll(category: String?) =
        sourceRemote.findAll(category)

}