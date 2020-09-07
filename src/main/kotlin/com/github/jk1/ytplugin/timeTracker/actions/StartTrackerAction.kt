package com.github.jk1.ytplugin.timeTracker.actions

import com.github.jk1.ytplugin.ComponentAware
import com.github.jk1.ytplugin.rest.IssuesRestClient
import com.github.jk1.ytplugin.timeTracker.*
import com.github.jk1.ytplugin.whenActive
import com.intellij.icons.AllIcons
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.WindowManager
import com.intellij.tasks.TaskManager

class StartTrackerAction : AnAction(
        "Start work timer",
        "Start work timer",
        AllIcons.Actions.Profile) {

    fun startAutomatedTracking(project: Project) {

        val repo = ComponentAware.of(project).taskManagerComponent.getActiveYouTrackRepository()
        val myTimer = ComponentAware.of(project).timeTrackerComponent[repo]

        val taskManager = project.let { it1 -> TaskManager.getManager(it1) }
        val activeTask = taskManager.activeTask

        if (!myTimer.isRunning || myTimer.isPaused) {
            myTimer.issueId = IssuesRestClient(repo).getEntityIdByIssueId(activeTask.id)
            if (myTimer.issueId == "0") {
                val trackerNote = TrackerNotification()
                trackerNote.notify("Could not post time: not a YouTrack issue", NotificationType.ERROR)
            } else {
                val bar = WindowManager.getInstance().getStatusBar(project)
                if (bar?.getWidget("Time Tracking Clock") == null) {
                    bar?.addWidget(ClockWidget(myTimer))
                }
                myTimer.start(activeTask.id)
                val application = ApplicationManager.getApplication()
                myTimer.activityTracker = ActivityTracker(
                        parentDisposable = application,
                        logTrackerCallDuration = false,
                        timer = myTimer,
                        inactivityPeriod = myTimer.inactivityPeriodInMills,
                        repo = repo,
                        project = project,
                        taskManager = taskManager
                )

                myTimer.activityTracker!!.startTracking()
                myTimer.isAutoTrackingEnable = true
            }
        } else {
            val trackerNote = TrackerNotification()
            trackerNote.notify("Work timer is already running", NotificationType.ERROR)
        }
    }


    override fun actionPerformed(event: AnActionEvent) {
        event.whenActive {
            val project = event.project

            if (project != null) {
                val repo = ComponentAware.of(project).taskManagerComponent.getActiveYouTrackRepository()
                val myTimer = ComponentAware.of(project).timeTrackerComponent[repo]
                val taskManager = project.let { it1 -> TaskManager.getManager(it1) }
                val activeTask = taskManager?.activeTask

                if (!myTimer.isRunning || myTimer.isPaused) {
                    if (activeTask != null) {
                        myTimer.issueId = IssuesRestClient(repo).getEntityIdByIssueId(activeTask.id)
                        if (myTimer.issueId == "0") {
                            val trackerNote = TrackerNotification()
                            trackerNote.notify("Could not post time: not a YouTrack issue", NotificationType.ERROR)
                        } else {
                            val bar = project.let { it1 -> WindowManager.getInstance().getStatusBar(it1) }
                            if (bar?.getWidget("Time Tracking Clock") == null) {
                                bar?.addWidget(ClockWidget(myTimer))
                            }
                            myTimer.start(activeTask.id)

                        }
                    }
                } else {
                    val trackerNote = TrackerNotification()
                    trackerNote.notify("Work timer is already running", NotificationType.ERROR)
                }
            }
        }
    }
}