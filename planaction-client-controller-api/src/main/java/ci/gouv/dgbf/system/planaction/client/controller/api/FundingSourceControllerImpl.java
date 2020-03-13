package ci.gouv.dgbf.system.planaction.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.planaction.client.controller.entities.FundingSource;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class FundingSourceControllerImpl extends AbstractControllerEntityImpl<FundingSource> implements FundingSourceController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
