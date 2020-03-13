package ci.gouv.dgbf.system.planaction.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.planaction.client.controller.entities.FundingAllocation;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class FundingAllocationControllerImpl extends AbstractControllerEntityImpl<FundingAllocation> implements FundingAllocationController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
