package com.desmonddavid.bots.digitforumbot.bots

import com.desmonddavid.bots.digitforumbot.AppProperties.Companion.DIGIT_BASE_URL
import com.desmonddavid.bots.digitforumbot.AppProperties.Companion.NEW_POSTS_BOT_TOKEN
import com.desmonddavid.bots.digitforumbot.AppProperties.Companion.NEW_POSTS_BOT_USERNAME
import com.desmonddavid.bots.digitforumbot.parser.NewPostsPage
import org.apache.commons.text.StringEscapeUtils
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.io.File
import java.nio.charset.StandardCharsets

object NewPostsBot : TelegramLongPollingBot() {

    private var activeChatIds = ArrayList<Long>()
    private const val NEW_POSTS_CHAT_ID_FILENAME = "NewPostsChatIds.txt"

    init {

        // Check if chat id file exists, if it does not exist, create it.
        val chatIdsFile = File(NEW_POSTS_CHAT_ID_FILENAME)
        if (!chatIdsFile.exists()) {
            chatIdsFile.createNewFile()
        }
        else {
            // Populate the active chat id list from the file
            val chatIdsStringList = chatIdsFile.readLines()
            chatIdsStringList.forEach {
                if(it.isNotBlank())
                    activeChatIds.add(it.toLong())
            }
        }

        println("Current active chat ids: $activeChatIds")
    }

    private val newPostsPage = NewPostsPage()

    override fun getBotToken(): String {
        return NEW_POSTS_BOT_TOKEN
    }

    override fun getBotUsername(): String {
        return NEW_POSTS_BOT_USERNAME
    }

    override fun onUpdateReceived(update: Update?) {
        //If it's a /start message then persist the chat id
        if (update != null && update.hasMessage() && update.message.hasText()) {
            if (update.message.text.equals("/start") || update.message.text.equals("/start@DigitForumBot")) {
                println("Received start for chat: ${update.message.chatId}")
                if(!activeChatIds.contains(update.message.chatId)) {
                    appendChatIdToFile(update.message.chatId)
                    activeChatIds.add(update.message.chatId)
                    println("Current active chat ids: $activeChatIds")
                    sendTextMessage(update.message.chatId, "Welcome! DigitForumBot has been activated for this chat.")
                }
                else {
                    sendTextMessage(update.message.chatId, "This bot is already active here.")
                }
            }
            if (update.message.text.equals("/stop") || update.message.text.equals("/stop@DigitForumBot")) {
                if(activeChatIds.contains(update.message.chatId)) {
                    println("Received stop for chat: ${update.message.chatId}")
                    activeChatIds.removeIf({ it==update.message.chatId })
                    removeChatIdFromFile(update.message.chatId)
                    println("Current active chat ids: $activeChatIds")
                    sendTextMessage(update.message.chatId, "DigitForumBot has been deactivated for this chat. Goodbye!")
                }
            }
        }
    }

    private fun removeChatIdFromFile(chatId: Long?) {
        val file = File(NEW_POSTS_CHAT_ID_FILENAME) //still assuming the file exists
        val tempfile = File("id.tmp")
        tempfile.createNewFile()
        file.forEachLine{
            if(it != chatId.toString()){
                tempfile.appendText(it+"\n")
            }
        }
        tempfile.renameTo(file)

    }

    private fun appendChatIdToFile(chatId: Long?) {
        val file = File(NEW_POSTS_CHAT_ID_FILENAME)   // File is assumed to exist at this point
        file.appendText(chatId.toString()+"\n", StandardCharsets.UTF_8)
    }

    private fun sendTextMessage(chatId: Long, textMessage: String) {
        val sendMessage = SendMessage()
        sendMessage.chatId = chatId.toString()
        sendMessage.allowSendingWithoutReply = true
        sendMessage.text = textMessage
        try {
            execute(sendMessage)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

    fun sendNewPostsMessage() {

        val sendMessage = SendMessage()
        sendMessage.allowSendingWithoutReply = true
        sendMessage.disableWebPagePreview = true
        sendMessage.parseMode = ParseMode.HTML
        sendMessage.text = makeNewPostsTelegramMessage(10)
        activeChatIds.forEach {
            sendMessage.chatId = it.toString()
            try {
                execute(sendMessage)
            } catch (e: TelegramApiException) {
                e.printStackTrace()
            }
        }
    }

    private fun makeNewPostsTelegramMessage(limit: Int): String {
        var newPostsList = newPostsPage.getContent()
        var message = "<b>Latest posts</b>\n\n"
        newPostsList = newPostsList.subList(0, limit)
        newPostsList.forEach {
            val entry =
                "<a href=\"$DIGIT_BASE_URL${it.link}\">${StringEscapeUtils.escapeHtml4(it.title)}</a>\nby ${
                    StringEscapeUtils.escapeHtml4(it.lastPostAuthor)
                }\n\n"
            message += entry
        }

        return message
    }
}