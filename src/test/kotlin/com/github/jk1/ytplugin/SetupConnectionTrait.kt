package com.github.jk1.ytplugin

import com.github.jk1.ytplugin.tasks.YouTrackServer
import com.intellij.openapi.project.Project
import com.intellij.tasks.TaskManager
import com.intellij.tasks.impl.TaskManagerImpl
import com.intellij.tasks.youtrack.YouTrackRepository
import com.intellij.tasks.youtrack.YouTrackRepositoryType

interface SetupConnectionTrait: IdeaProjectTrait, YouTrackConnectionTrait {

    val project: Project

    fun getTaskManagerComponent() = TaskManager.getManager(project)!! as TaskManagerImpl

    fun createYouTrackRepository(url: String, token: String, shareUrl: Boolean = false,
                                 useProxy: Boolean = false, useHTTP: Boolean = false, loginAnon: Boolean = false): YouTrackServer {
        val repository = YouTrackRepository(YouTrackRepositoryType())
        repository.url = url
        repository.username = username
        repository.password = token
        repository.defaultSearch = ""
        repository.isShared = shareUrl
        repository.isUseProxy = useProxy
        repository.isUseHttpAuthentication = useHTTP
        repository.isLoginAnonymously = loginAnon

        getTaskManagerComponent().setRepositories(listOf(repository))
        //todo: mock YouTrack server here to break dependency from task-core
        return YouTrackServer(repository, project)
    }

    fun cleanUpTaskManager(){
        val taskManager = getTaskManagerComponent()
        readAction {
            taskManager.localTasks.forEach { taskManager.removeTask(it) }
        }
        taskManager.setRepositories(listOf())
    }
}