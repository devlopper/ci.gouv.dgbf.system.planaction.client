package ci.gouv.dgbf.system.planaction.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlanActivity;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class ActionPlanActivityControllerImpl extends AbstractControllerEntityImpl<ActionPlanActivity> implements ActionPlanActivityController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
