package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlanActivity;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActionPlanActivityEditPage extends AbstractPageContainerManagedImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ActionPlanActivity actionPlanActivity;
	
	/*@Override
	protected Collection<String> __getEntityFieldsNames__() {
		return List.of(ActionPlanActivity.FIELD_ACTION_PLAN,ActionPlanActivity.FIELD_ACTIVITY);
	}*/
	
}
