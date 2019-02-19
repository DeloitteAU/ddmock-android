package com.dd

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://api.github.com"

interface GithubService {
    @GET("/repos/{owner}/{repo}/branches")
    fun getBranches(@Path("owner") owner: String, @Path("repo") repo: String): Call<List<Branch>>
}

data class Branch(val name: String)

class MainActivity : AppCompatActivity() {

    private lateinit var github: GithubService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create OkHttp client with mock interceptor
        val okHttpClient = OkHttpClient().newBuilder().addInterceptor(MockInterceptor()).build()

        // Create simple Retrofit with points to Github API
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        // Create Github API interface
        github = retrofit.create(GithubService::class.java)

        fetchButton.setOnClickListener { fetch() }
    }

    private fun fetch() {
        // Create a call for looking up DDMock branches
        val call = github.getBranches("nf1993", "ddmock")

        // Fetch and print a list of all DDMock branches
        call.enqueue(object : Callback<List<Branch>> {
            override fun onFailure(call: Call<List<Branch>>, t: Throwable) {
                status.setText(R.string.failed)
            }

            override fun onResponse(call: Call<List<Branch>>, response: Response<List<Branch>>) {
                if (response.isSuccessful) {
                    val sb = StringBuilder()
                    response.body()?.let { branches ->
                        for (branch in branches) {
                            sb.append(branch.name).append("\n")
                        }
                    }
                    status.text = sb.toString()
                } else {
                    status.setText(R.string.failed)
                }
            }
        })
    }
}
