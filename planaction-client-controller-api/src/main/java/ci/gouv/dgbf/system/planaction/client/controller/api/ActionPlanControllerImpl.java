package ci.gouv.dgbf.system.planaction.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlan;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class ActionPlanControllerImpl extends AbstractControllerEntityImpl<ActionPlan> implements ActionPlanController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
