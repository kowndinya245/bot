package com.kv.bot.models

class ChatModel(
    var username: String,
    var message: String,
    var image: Int,
    var isMe: Boolean = false,
    var sentAt: Long = System.currentTimeMillis()
) {

    override fun toString(): String {
        return "ChatModel{" +
                "username='" + username + '\'' +
                ", message='" + message + '\'' +
                ", image=" + image +
                ", isMe=" + isMe +
                ", sentAt=" + sentAt +
                '}'
    }
}
