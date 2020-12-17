package com.liferay.samples.fbo.gogo;

import com.liferay.portal.instances.service.PortalInstancesLocalService;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.search.IndexWriterHelper;
import com.liferay.portal.kernel.search.SearchException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		property = {
				"osgi.command.function=spellcheckReindex",
				"osgi.command.scope=lrext"
		},
		service = SpellCheckReindexCommand.class
		)
public class SpellCheckReindexCommand {

	public void spellcheckReindex() {
		System.out.println("Reindexing spell checker dictionnaries...");
		
		reindexDictionaries(CompanyConstants.SYSTEM);

		long[] companyIds = _portalInstancesLocalService.getCompanyIds();

		for (long companyId : companyIds) {
			reindexDictionaries(companyId);
		}
	}
	
	private void reindexDictionaries(long companyId) {

		System.out.println(" - Reindexing spell checker dictionnary for company " + companyId);

		try {
			_indexWriterHelper.indexQuerySuggestionDictionaries(companyId);
			_indexWriterHelper.indexSpellCheckerDictionaries(companyId);
		} catch (SearchException e) {
			System.out.println("   /!\\ Failed to reindex spell checker dictionnary for company " + companyId);
		}
	}

	@Reference
	private IndexWriterHelper _indexWriterHelper;
	
	@Reference
	private PortalInstancesLocalService _portalInstancesLocalService;
	
}
