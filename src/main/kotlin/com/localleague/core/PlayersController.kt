package com.localleague.core

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PlayersController {
    @GetMapping("/api/players")
    fun players() = Player(1, "John Smith", 0, 0)
}