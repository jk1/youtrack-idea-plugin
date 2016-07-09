package com.github.jk1.ytplugin.search

import com.intellij.openapi.components.AbstractProjectComponent
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.ActionCallback
import com.intellij.util.containers.SortedList
import java.io.File
import java.io.IOException
import java.util.*

/**
 * Created by elle on 03.04.16.
 */
class Storage(val project: Project) : AbstractProjectComponent(project) {

    private val myIssues = HashMap<String, Issue>()

    private val mySortedIssues = SortedList(Comparator<String> { o1, o2 ->
        o1.compareTo(o2)
    })
    private var myWorking: ActionCallback = ActionCallback.Done()

    fun getInstance(project: Project): Storage {
        return project.getComponent(Storage::class.java!!.getName()) as Storage
    }

    fun getAllIssues(): Collection<Issue> {
        return myIssues.values
    }

    fun update(): ActionCallback {
        if (isUpdating()) {
            return myWorking
        }

        myWorking = ActionCallback()

        refresh(myWorking)

        return myWorking
    }

    private fun refresh(working: ActionCallback) {
        /*val task = object : Task.Backgroundable(myProject, "Updating issues from server", true, PerformInBackgroundOption.ALWAYS_BACKGROUND) {
            override fun run(indicator: ProgressIndicator) {
                val session = YoutrackSession(myProject)
                val modificationStamp = PropertiesComponent.getInstance(myProject).getOrInitLong("MyyLastTimeUpdated", 0)
                try {
                    val started = System.currentTimeMillis()
                    val issues = session.getIssues(indicator, modificationStamp)
                    setIssues(issues)
                    PropertiesComponent.getInstance(myProject).setValue("MyyLastTimeUpdated", started.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            override fun onCancel() {
                working.setDone()
            }

            override fun onSuccess() {
                working.setDone()
                save()
                fireIssuesChanged()
            }
        }
        ProgressManager.getInstance().run(task)*/
    }

    private fun fireIssuesChanged() {
        /*for (listener in myListeners) {
            listener.storageUpdated()
        }*/
    }

    private fun setIssues(issues: List<Issue>) {
        for (issue in issues) {
            myIssues.put(issue.id, issue)
        }
        mySortedIssues.clear()
        mySortedIssues.addAll(myIssues.keys)
    }

    fun isUpdating(): Boolean {
        return !myWorking.isDone
    }



    fun getIssue(id: String): Issue? {
        return myIssues.get(id)
    }

    fun getSortedIssues(): SortedList<String> {
        return mySortedIssues
    }

    fun save() {
        /*if (MyyConnectionUtil.isLogged(myProject)) {
            val path = getIssuesXmlPath()
            val file = File(path)
            ApplicationManager.getApplication().invokeLater {
                ApplicationManager.getApplication().runWriteAction {
                    try {
                        writeTo(file)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }*/
    }

    private fun getIssuesXmlPath(): String {
        /*var url = YoutrackSession(myProject).getUrl()
        if (url.toLowerCase().startsWith("http://")) {
            url = url.substring("http://".length)
        }
        if (url.toLowerCase().startsWith("https://")) {
            url = url.substring("https://".length)
        }
        return PathManager.getSystemPath() + File.separator
        +"myy" + File.separator
        +url.replace("\\", "/").replace("/", File.separator)*/
        return "";
    }

    @Throws(IOException::class)
    fun writeTo(file: File) {
        /*if (!file.exists()) {
            if (!file.mkdirs()) {
                throw IOException("Can't create " + file.path)
            }
        }
        val issues = File(file, "issues.xml")
        if (!issues.exists()) {
            issues.createNewFile()
        }
        val xml = Element("issues")
        for (issue in myIssues.values) {
            val element = issue.toXml()

            xml.addContent(element.clone() as Element)
        }

        JDOMUtil.writeDocument(Document(xml), issues, "\n")*/
    }

    fun load() {
        /*if (MyyConnectionUtil.isLogged(myProject)) {
            val file = File(getIssuesXmlPath() + File.separator + "issues.xml")
            if (file.exists()) {
                ApplicationManager.getApplication().runReadAction {
                    try {
                        val document = JDOMUtil.loadDocument(file)
                        val issues = document.rootElement
                        val storedIssues = ArrayList<Issue>()
                        for (child in issues.children) {
                            if (child is Element) {
                                storedIssues.add(Issue(child))
                            }
                        }
                        setIssues(storedIssues)
                    } catch (e: JDOMException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }*/
    }

    override fun projectOpened() {
        load()
        fireIssuesChanged()
    }

    override fun projectClosed() {
//        if (MyyConnectionUtil.isLogged(myProject)) {
//            val path = getIssuesXmlPath()
//            val file = File(path)
//            try {
//                writeTo(file)
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//
//        }
    }

    /*fun getIssueUrl(issue: Issue): String {
        return YoutrackSession(myProject).getUrl() + "/issue/" + issue.getId()
    }*/

}