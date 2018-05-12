package com.localleague.core

data class Match(val id: Long, val players: Array<Player>, val winner: Player)