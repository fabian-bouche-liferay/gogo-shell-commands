package com.liferay.samples.fbo.gogo;

import com.liferay.portal.kernel.backgroundtask.BackgroundTaskConstants;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskManager;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.LocaleUtil;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		property = {
				"osgi.command.function=backgroundTasksListByGroup",
				"osgi.command.scope=lrext"
		},
		service = BackgroundTaksListByGroupCommand.class
		)
public class BackgroundTaksListByGroupCommand {

	public void backgroundTasksListByGroup() {

		Locale locale = LocaleUtil.getDefault();

		System.out.println("----------------");
		displayTasksByGroup(GroupConstants.DEFAULT_PARENT_GROUP_ID, "Default");
		System.out.println("----------------");
		System.out.println("");

		int groupsCount = _groupLocalService.getGroupsCount();
		_groupLocalService.getGroups(0, groupsCount).forEach(group -> {
			
			System.out.println("----------------");
			displayTasksByGroup(group.getGroupId(), group.getName(locale));
			System.out.println("----------------");
			System.out.println("");

		});
		
	}

	private void displayTasksByGroup(long groupId, String groupName) {
		System.out.println("Group [" + groupId + "]: " + groupName);

		System.out.println("++ STATUS_NEW ++");
		displayTasksByGroupAndStatus(groupId, BackgroundTaskConstants.STATUS_NEW);

		System.out.println("++ STATUS_QUEUED ++");
		displayTasksByGroupAndStatus(groupId, BackgroundTaskConstants.STATUS_QUEUED);

		System.out.println("++ STATUS_IN_PROGRESS ++");
		displayTasksByGroupAndStatus(groupId, BackgroundTaskConstants.STATUS_IN_PROGRESS);

		System.out.println("++ STATUS_SUCCESSFUL ++");
		displayTasksByGroupAndStatus(groupId, BackgroundTaskConstants.STATUS_SUCCESSFUL);

		System.out.println("++ STATUS_FAILED ++");
		displayTasksByGroupAndStatus(groupId, BackgroundTaskConstants.STATUS_FAILED);

		System.out.println("++ STATUS_CANCELLED ++");
		displayTasksByGroupAndStatus(groupId, BackgroundTaskConstants.STATUS_CANCELLED);
	}

	private void displayTasksByGroupAndStatus(long groupId, int status) {
		_backgroundTaskManager.getBackgroundTasks(groupId, status).forEach(task -> {

			long id = task.getBackgroundTaskId();
			String name = task.getName();
			System.out.println("[" + id + "] " + name);
			
		});
	}

	@Reference
	private BackgroundTaskManager _backgroundTaskManager;

	@Reference
	private GroupLocalService _groupLocalService;

}
