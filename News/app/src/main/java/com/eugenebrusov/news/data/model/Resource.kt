package com.eugenebrusov.news.data.model

/**
 * A generic class that holds a value with its loading status.
 * It follows https://developer.android.com/topic/libraries/architecture/guide.html#addendum
 * @param <T>
 */
@Suppress("DataClassPrivateConstructor")
data class Resource<T> private constructor(
        val status: Status,
        val data: T?,
        val message: String?) {
    companion object {
        fun <T> success(data: T?) = Resource(Status.SUCCESS, data, null)
        fun <T> error(message: String?, data: T?) = Resource(Status.ERROR, data, message)
        fun <T> loading(data: T?) = Resource(Status.LOADING, data, null)
    }
}