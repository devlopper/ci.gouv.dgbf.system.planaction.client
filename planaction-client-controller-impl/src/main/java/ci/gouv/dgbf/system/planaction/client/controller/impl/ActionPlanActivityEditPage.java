package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlanActivity;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActionPlanActivityEditPage extends AbstractEntityEditPageContainerManagedImpl<ActionPlanActivity> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected Collection<String> __getEntityFieldsNames__() {
		return List.of(ActionPlanActivity.FIELD_ACTION_PLAN,ActionPlanActivity.FIELD_ACTIVITY);
	}
	
}
