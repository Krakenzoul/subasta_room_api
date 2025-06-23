package com.example.subasta.data.remote

import com.example.subasta.data.model.Auction
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("auctions")
    suspend fun getAuctions(): List<Auction>

    @GET("auctions/{id}")
    suspend fun getAuctionDetail(@Path("id") id: String): Auction

    @POST("auctions")
    suspend fun createAuction(@Body auction: Auction): Response<Auction>
    @GET("auctions/{id}")
    suspend fun getAuctionById(@Path("id") id: String): Auction

}