// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.github.jk1.ytplugin.setupWindow

import com.intellij.openapi.project.Project
import com.intellij.tasks.TaskManager
import com.intellij.tasks.TaskRepository
import com.intellij.tasks.config.RecentTaskRepositories
import com.intellij.tasks.impl.TaskManagerImpl
import com.intellij.tasks.youtrack.YouTrackRepository
import com.intellij.util.Function
import com.intellij.util.containers.ContainerUtil


/**
 * @author Alina Boshchenko
 */
class SetupWindowManager(val project: Project) {

    fun showIssues(repository: YouTrackRepository) {
        val myManager: TaskManagerImpl = TaskManager.getManager(project) as TaskManagerImpl
        lateinit var myRepositories: List<YouTrackRepository>
        myRepositories = ArrayList()
        myRepositories.add(repository)
        val newRepositories: List<TaskRepository> = ContainerUtil.map<TaskRepository, TaskRepository>(myRepositories, Function { obj: TaskRepository -> obj.clone() })
        myManager.setRepositories(newRepositories)
        myManager.updateIssues(null)
        RecentTaskRepositories.getInstance().addRepositories(myRepositories)
    }

}