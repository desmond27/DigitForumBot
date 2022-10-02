package com.desmonddavid.bots.digitforumbot

import java.util.*

class AppProperties {

    companion object {
        val DIGIT_BASE_URL: String
        val POSTING_FREQUENCY_SECONDS: Long

        val NEW_POSTS_BOT_TOKEN: String
        val NEW_POSTS_BOT_USERNAME: String
        val NEW_POSTS_PAGE_URI: String

        init {
            ClassLoader.getSystemClassLoader().getResourceAsStream("app.properties").use {
                val props = Properties()
                props.load(it)

                DIGIT_BASE_URL = props["DIGIT_BASE_URL"] as String
                POSTING_FREQUENCY_SECONDS = (props["POSTING_FREQUENCY_SECONDS"] as String).toLong()
                NEW_POSTS_BOT_TOKEN = props["NEW_POSTS_BOT_TOKEN"] as String
                NEW_POSTS_BOT_USERNAME = props["NEW_POSTS_BOT_USERNAME"] as String
                NEW_POSTS_PAGE_URI = props["NEW_POSTS_PAGE_URI"] as String
            }
        }
    }
}