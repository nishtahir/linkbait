package de.langerhans.linkbait.tenor

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.langerhans.linkbait.tenor.model.TenorBaseResponse
import de.langerhans.linkbait.tenor.model.TenorTagsResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by maxke on 24.08.2016.
 * Retrofit interface for the API
 */
interface TenorService {

    companion object{
        private val BASE_URL = "https://api.tenor.co/v1/"
        private val API_KEY = "LIVDSRZULELA" // TODO Put our own API key somewhere

        fun build(): TenorService {
            val builder = OkHttpClient.Builder()
            builder.interceptors().add(Interceptor { chain ->
                var request = chain.request()
                val url = request.url().newBuilder().addQueryParameter("key", API_KEY).build()
                request = request.newBuilder().url(url).build()
                return@Interceptor chain.proceed(request)
            })

            val mapper = jacksonObjectMapper()
            val module = SimpleModule()
            module.addDeserializer(TenorBaseResponse::class.java, TenorDeserializer())
            mapper.registerModule(module)
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(builder.build())
                    .addConverterFactory(JacksonConverterFactory.create(mapper))
                    .build()
                    .create(TenorService::class.java)
        }
    }

    @GET("search")
    fun search(
            @Query("tag") tag: String,
            @Query("country") country: String = "",
            @Query("limit") limit: Int = 10,
            @Query("locale") locale: String = "en_US",
            @Query("pos") pos: String = "",
            @Query("safesearch") safesearch: String = "moderate"
    ): Call<TenorBaseResponse>

    @GET("tags")
    fun tags(@Query("type") tag: String = "featured"): Call<TenorTagsResponse>

    @GET("trending")
    fun trending(@Query("limit") limit: Int = 10, @Query("pos") pos: String = ""): Call<TenorBaseResponse>

    @GET("music")
    fun clips(@Query("limit") limit: Int = 10, @Query("pos") pos: String = ""): Call<TenorBaseResponse>

    @GET("gifs")
    fun gifs(@Query("ids") ids: String): Call<TenorBaseResponse>

}