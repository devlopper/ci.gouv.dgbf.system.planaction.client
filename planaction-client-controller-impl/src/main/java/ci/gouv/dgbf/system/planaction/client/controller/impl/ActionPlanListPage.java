package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;

import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlan;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActionPlanListPage extends AbstractEntityListPageContainerManagedImpl<ActionPlan> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected Collection<String> __getColumnsFieldsNames__(Class<ActionPlan> entityClass) {
		return List.of(ActionPlan.FIELD_CODE,ActionPlan.FIELD_NAME);
	}
	
	protected void __addDataTableHeaderToolbarLeftCommandsByArguments__(DataTable dataTable) {
		dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialogCreate();
	}
	
	protected void __addDataTableRecordMenuItemByArguments__(DataTable dataTable) {
		dataTable.addRecordMenuItemByArgumentsNavigateToViewRead();
		dataTable.addRecordMenuItemByArgumentsOpenViewInDialogUpdate();
		dataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();
	}
}
