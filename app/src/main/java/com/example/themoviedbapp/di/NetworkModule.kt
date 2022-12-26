package com.example.themoviedbapp.di

import com.example.themoviedbapp.BuildConfig
import com.example.themoviedbapp.data.remote.Api
import com.example.themoviedbapp.data.remote.BASE_POSTER_PATH
import com.example.themoviedbapp.data.remote.THE_MOVIE_DB_URL
import com.example.themoviedbapp.data.remote.TIMEOUT
import com.example.themoviedbapp.data.remote.adapter.ApiResponseAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @BaseUrl
    fun provideBaseUrl(): String {
        return THE_MOVIE_DB_URL
    }

    @Provides
    @BasePosterPath
    fun provideBasePosterPath(): String {
        return BASE_POSTER_PATH
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
            readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
            writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
            addInterceptor { chain ->
                val request = chain.request()
                val newUrl = request.url.newBuilder()
                    .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                    .build()

                val newRequest = request.newBuilder()
                    .url(newUrl)
                    .build()
                chain.proceed(newRequest)
            }
        }.build()
    }

    @Provides
    fun provideConverterFactory(): Converter.Factory {
        val moshi =
            Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()
        return MoshiConverterFactory.create(moshi)
    }

    @Provides
    fun provideCallAdapterFactory(): CallAdapter.Factory = ApiResponseAdapterFactory()

    @Provides
    @Singleton
    fun provideApi(
        @BaseUrl baseUrl: String,
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
        callAdapterFactory: CallAdapter.Factory
    ): Api {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(callAdapterFactory)
            .client(okHttpClient)
            .build()
        return retrofit.create(Api::class.java)
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BaseUrl

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BasePosterPath
}