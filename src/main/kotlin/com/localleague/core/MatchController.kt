package com.localleague.core

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MatchController {

    val players: Array<Player> = arrayOf(
            Player(1, "John Smith", 1000, 0),
            Player(2, "Bob Vance", 1000, 0),
            Player(3, "Bernie Sanders", 1000, 0),
            Player(4, "Larry David", 1000, 0)
    )

    data class MatchBody(val players: Array<Int>, val winner: Int)
    data class MatchResponse(val winner: Player, val loser: Player)

    @GetMapping("/api/matches")
    fun getMatches(): Array<Match> {
        return arrayOf(
                Match(1, arrayOf(players[0], players[1]), players[0])
        )
    }

    @PostMapping("/api/matches")
    fun createMatch(@RequestBody requestBody: MatchBody): MatchResponse {
        // record match
        val winner: Player = players[requestBody.winner]
        val loserId = requestBody.players.filter { player -> player != requestBody.winner }[0]
        val loser: Player = players[loserId]

        playedGame(winner, loser)

        val winnerExpectedScore: Double = getExpectedScore(winner.elo, loser.elo)
        val loserExpectedScore: Double = getExpectedScore(loser.elo, winner.elo)

        val winnerScore: Double = 1 - winnerExpectedScore
        val winnerK: Int = getKFactor(winner)
        val winnerDelta: Double = winnerK.toDouble() * winnerScore

        winner.elo = winner.elo + winnerDelta.toLong()

        val loserScore = 0 - loserExpectedScore
        val loserK = getKFactor(loser)
        val loserDelta = loserK * loserScore

        loser.elo = loser.elo + loserDelta.toLong()

        return MatchResponse(winner, loser)
    }

    private fun playedGame(winner: Player, loser: Player) {
        winner.games++
        loser.games++
    }

    private fun getKFactor(player: Player): Int {
        if (getNumberOfGamesPlayed(player) < 30) {
            return 40
        }

        if (player.elo < 2400) {
            return 20;
        }

        return 10
    }

    private fun getNumberOfGamesPlayed(player: Player): Int {
        return player.games
    }

    private fun getExpectedScore(elo: Long, elo1: Long): Double {
        val diff = elo - elo1
        val exponent = diff / 400
        val power = Math.pow(10.toDouble(), exponent.toDouble())
        val denominator = 1 + power

        return 1 / denominator
    }
}