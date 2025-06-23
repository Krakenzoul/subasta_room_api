package com.example.subasta.navigation

sealed class Screen(val route: String) {
    object AuctionList : Screen("auction_list")
    object AddAuction : Screen("add_auction")
    object AuctionDetail : Screen("auction_detail/{auctionId}") {
        fun createRoute(auctionId: String) = "auction_detail/$auctionId"
    }
}