package com.github.jk1.ytplugin

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications

public fun sendNotification(
        title: String = "YouTrack plugin error",
        text: String?,
        type: NotificationType){
    Notifications.Bus.notify(Notification("YouTrack Integration Plugin", title, text ?: "null", type))
}