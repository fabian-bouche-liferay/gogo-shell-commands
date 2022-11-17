package com.liferay.samples.fbo.gogo;

import com.liferay.portal.instances.service.PortalInstancesLocalService;
import com.liferay.portal.kernel.backgroundtask.BackgroundTask;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.search.IndexWriterHelper;
import com.liferay.portal.kernel.search.IndexerRegistry;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.Portal;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		property = {
				"osgi.command.function=reindex",
				"osgi.command.scope=lrext"
		},
		service = ReindexCommand.class
		)
public class ReindexCommand {

	public void reindex() {
		
		System.out.println("Reindexing entities...");

		long[] companyIds = _portalInstancesLocalService.getCompanyIds();

		for (long companyId : companyIds) {

			User admin = getAdmin(companyId);

			if(admin == null) {
				return;
			}

			long userId = admin.getUserId();

			reindexEntities(companyId, userId);
		}
		
	}
	
	private void reindexEntities(long companyId, long userId) {

		System.out.println(" - Reindexing entities for company " + companyId);

		_indexerRegistry.getIndexers().forEach(indexer -> {
			
			reindexEntity(companyId, indexer.getClassName(), userId);
			
		});
		
	}
	
	private void reindexEntity(long companyId, String className, long userId) {

		System.out.println("   + Reindexing " + className + " for company " + companyId);
		try {
			long[] companyIds = new long[1];
			companyIds[0] = companyId;
			String jobName = "reindex." + companyId + "." + className;
			Map<String, Serializable> taskContextMap = new HashMap<>();
			BackgroundTask task = _indexWriterHelper.reindex(userId, jobName, companyIds, className, taskContextMap);
			System.out.println("     => Creating task " + task.getName());
		} catch (SearchException e) {
			System.out.println("   /!\\ Failed to reindex " + className + " for company " + companyId);
		}
		
	}

	@Reference
	private UserLocalService _userLocalService;

	@Reference
	private RoleLocalService _roleLocalService;

	@Reference
	private IndexWriterHelper _indexWriterHelper;
	
	@Reference
	private PortalInstancesLocalService _portalInstancesLocalService;

	@Reference
	private IndexerRegistry _indexerRegistry;

	@Reference
	private Portal _portal;

    private User getAdmin(long companyId) {
    	System.out.println("getAdmin " + companyId);
    	
        Role role = null;
        try {
            role = getRoleById(companyId, RoleConstants.ADMINISTRATOR);
            
            if(role == null) {
    			System.out.println("/!\\ Failed to find admin for company " + companyId);
            	return null;
            }
            
            for (final User admin : _userLocalService.getRoleUsers(role.getRoleId())) {
                return admin;
            }
        } catch (final Exception e) {
			System.out.println("/!\\ Failed to find admin for company " + companyId);
        }
        return null;
    }
	
    private Role getRoleById(final long companyId, final String roleStrId) {
    	System.out.println("getRoleById " + companyId + " " + roleStrId);
        try {
            return _roleLocalService.getRole(companyId, roleStrId);
        } catch (final Exception e) {
			System.out.println("/!\\ Failed to find admin role for company " + companyId);
        }
        return null;
    }
}
