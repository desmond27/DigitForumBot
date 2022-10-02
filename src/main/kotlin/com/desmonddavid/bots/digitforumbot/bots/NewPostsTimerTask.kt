package com.desmonddavid.bots.digitforumbot.bots

import java.time.LocalDateTime
import java.util.TimerTask

class NewPostsTimerTask : TimerTask() {
    override fun run() {
        println("Sending latest posts message now: "+ LocalDateTime.now().toString())
        NewPostsBot.sendNewPostsMessage()
    }
}