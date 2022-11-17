# LREXT Gogo Shell commands

`lrext:reindex` Reindex all entities indexes

`lrext:spellcheckReindex` Reindex all dictionnaries

`lrext:backgroundTasksList` Examine all registered background tasks

`lrext:backgroundTasksListByGroup` Examine all registered background tasks, grouped by GroupId and Status

## Some groovy scripts to monitor background tasks

```
def backgroundTasks = com.liferay.portal.background.task.service.BackgroundTaskLocalServiceUtil.getBackgroundTasks(-1, -1);
backgroundTasks.each { task ->
    if("com.liferay.portal.search.internal.background.task.ReindexPortalBackgroundTaskExecutor".equals(task.taskExecutorClassName)) {
        out.println("########");
        out.println("Background Task Id: " + task.backgroundTaskId);
        out.println("Background Task Status: " + task.statusLabel);
        out.println("In progress?  " + task.inProgress);
        out.println("Task executor class name: " + task.taskExecutorClassName);
    }
}

backgroundTasks.each { task ->
    if("com.liferay.portal.search.internal.background.task.ReindexSingleIndexerBackgroundTaskExecutor".equals(task.taskExecutorClassName)) {
        out.println("########");
        out.println("Background Task Id: " + task.backgroundTaskId);
        out.println("Background Task Status: " + task.statusLabel);
        out.println("In progress?  " + task.inProgress);
        out.println("Task executor class name: " + task.taskExecutorClassName);
        out.println("Reindexing entity: " + task.taskContextMap.className);
    }
}
```
