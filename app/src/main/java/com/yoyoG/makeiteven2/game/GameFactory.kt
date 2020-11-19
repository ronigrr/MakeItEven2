package com.yoyoG.makeiteven2.game

import com.yoyoG.makeiteven2.extras.Constants

class GameFactory {
    companion object {
        fun getGame(gameType: String, difficulty: Int): Game? {
            var gameToCreate: Game? = null
            when (gameType) {
                Constants.ARCADE_GAME_TYPE -> {
                    gameToCreate = GameArcade(difficulty)
                }
                Constants.STAGE_GAME_TYPE -> {
                    gameToCreate = GameStage(difficulty)
                }
            }
            return gameToCreate
        }
    }
}