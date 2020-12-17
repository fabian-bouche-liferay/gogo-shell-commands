package com.liferay.samples.fbo.gogo;

import com.liferay.portal.background.task.model.BackgroundTask;
import com.liferay.portal.background.task.service.BackgroundTaskLocalService;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskConstants;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		property = {
				"osgi.command.function=backgroundTasksList",
				"osgi.command.scope=lrext"
		},
		service = BackgroundTaskListCommand.class
		)
public class BackgroundTaskListCommand {

	public void backgroundTasksList() {
		System.out.println("Listing background tasks...");
		
		int nb = _backgroundTaskLocalService.getBackgroundTasksCount();
		System.out.println(" => Found " + nb + " background tasks.");
		
		List<BackgroundTask> backgroundTasks = _backgroundTaskLocalService.getBackgroundTasks(0, nb);
		backgroundTasks.forEach(backgroundTask -> {

			long id = backgroundTask.getBackgroundTaskId();
			int status = backgroundTask.getStatus();
			String name = backgroundTask.getName();
			System.out.println("[" + id + "] " + name + " - Status: " + getStatusLabel(status));
			
		});
		
	}

	private String getStatusLabel(int status) {
		
		switch(status) {
		
		case BackgroundTaskConstants.STATUS_NEW:
			return "STATUS_NEW";

		case BackgroundTaskConstants.STATUS_CANCELLED:
			return "STATUS_CANCELLED";
			
		case BackgroundTaskConstants.STATUS_FAILED:
			return "STATUS_FAILED";
			
		case BackgroundTaskConstants.STATUS_IN_PROGRESS:
			return "STATUS_IN_PROGRESS";

		case BackgroundTaskConstants.STATUS_QUEUED:
			return "STATUS_QUEUED";

		case BackgroundTaskConstants.STATUS_SUCCESSFUL:
			return "STATUS_SUCCESSFUL";

		default:
			return "UNKNOWN";
		
		}
		
	}

	@Reference
	private BackgroundTaskLocalService _backgroundTaskLocalService;
	
}
