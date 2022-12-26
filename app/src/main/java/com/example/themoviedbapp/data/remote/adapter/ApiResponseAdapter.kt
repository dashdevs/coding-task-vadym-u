package com.example.themoviedbapp.data.remote.adapter

import com.example.themoviedbapp.data.remote.ApiResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import java.lang.reflect.Type

class ApiResponseAdapter<T : Any, E : Any>(
    private val successType: Type,
    private val errorBodyConverter: Converter<ResponseBody, E>
) : CallAdapter<T, Call<ApiResponse<T, E>>> {

    override fun responseType(): Type = successType

    override fun adapt(call: Call<T>): Call<ApiResponse<T, E>> {
        return ApiResponseCall(call, errorBodyConverter)
    }
}
