package com.desmonddavid.bots.digitforumbot.parser

import com.desmonddavid.bots.digitforumbot.AppProperties.Companion.DIGIT_BASE_URL
import com.desmonddavid.bots.digitforumbot.AppProperties.Companion.NEW_POSTS_PAGE_URI
import org.jsoup.Jsoup

class NewPostsPage {

    fun getContent(): List<NewPost> {
        val document = Jsoup.connect("${DIGIT_BASE_URL}${NEW_POSTS_PAGE_URI}").get()
        val newPostsListElements = document.select("div.structItem")
        val dataList = ArrayList<NewPost>()

        newPostsListElements.forEach {
            val title = it.select("div.structItem-title").text()
            val link = it.select("div.structItem-cell--latest > a").attr("href")
            val postAuthor = it.select("div.structItem-cell--latest > div.structItem-minor").text()
            dataList.add(NewPost(title, link, postAuthor))
        }

        return dataList
    }
}

data class NewPost(val title: String, val link: String, val lastPostAuthor: String)