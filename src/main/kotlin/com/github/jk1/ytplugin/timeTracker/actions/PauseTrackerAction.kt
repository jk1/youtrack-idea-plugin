package com.github.jk1.ytplugin.timeTracker.actions

import com.github.jk1.ytplugin.ComponentAware
import com.github.jk1.ytplugin.timeTracker.IconLoader
import com.github.jk1.ytplugin.whenActive
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent


class PauseTrackerAction  : AnAction(
        "Pause work timer",
        "Pause work timer",
        IconLoader.loadIcon("icons/time_tracker_pause_dark.png")){

    override fun actionPerformed(event: AnActionEvent) {
        event.whenActive {

            val project = event.project
            if (project != null) {
                val repo = ComponentAware.of(project).taskManagerComponent.getActiveYouTrackRepository()
                val timer = ComponentAware.of(event.project!!).timeTrackerComponent[repo]
                timer.pause()
            }
        }
    }
}
