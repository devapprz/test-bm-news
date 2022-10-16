package com.yusuf.bankmandiri.newsapps.utils

import android.content.Context
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.isSuccessful
import com.github.kittinunf.fuel.coroutines.awaitStringResponseResult
import com.google.gson.reflect.TypeToken
import com.yusuf.bankmandiri.newsapps.BuildConfig
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.flow
import org.json.JSONObject

inline fun <reified T> Request.readResult(context: Context, key: String) = flow<T?> {
    parameters = parameters + listOf("apiKey" to BuildConfig.API_KEY)
    val (_, res, data) = awaitStringResponseResult()
    require(res.isSuccessful) { res.responseMessage }
    val fuelError = data.component2()
    require(fuelError == null) { fuelError?.localizedMessage ?: res.responseMessage }
    val payloadJson = JSONObject(data.get())
    val statusMessage = payloadJson.optString("code", "")
    require(statusMessage.isEmpty()) { "Error ${payloadJson.optString("message", statusMessage)}" }
    val typeToken = object : TypeToken<T>() {}.type
    val injector = EntryPointAccessors.fromApplication(context, SingletonInjector::class.java)
    emit(injector.gson.fromJson(payloadJson.optString(key), typeToken))
}