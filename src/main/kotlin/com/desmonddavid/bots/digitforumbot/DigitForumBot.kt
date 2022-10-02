package com.desmonddavid.bots.digitforumbot

import com.desmonddavid.bots.digitforumbot.bots.NewPostsBot
import com.desmonddavid.bots.digitforumbot.bots.NewPostsTimerTask
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*

fun main() {
    try {

        val telegramBotsApi = TelegramBotsApi(DefaultBotSession::class.java)
        telegramBotsApi.registerBot(NewPostsBot)

        val timer = Timer()
        timer.scheduleAtFixedRate(
            NewPostsTimerTask(),
            Date.from(LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS).atZone(ZoneId.systemDefault()).toInstant()),
            AppProperties.POSTING_FREQUENCY_SECONDS * 1000)

        NewPostsBot.sendNewPostsMessage()
    }
    catch(e: TelegramApiException) {
        e.printStackTrace()
    }
}