package com.localleague.core

data class Player(
        val id: Long,
        val name: String,
        var elo: Long,
        var games: Int
)

