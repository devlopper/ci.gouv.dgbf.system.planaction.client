package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.user.interface_.message.RenderType;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.Button;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;

import ci.gouv.dgbf.system.planaction.client.controller.api.ActionPlanController;
import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlan;
import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlanActivity;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActionPlanActivityListPage extends AbstractPageContainerManagedImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	private DataTable dataTable;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		dataTable = DataTable.build(DataTable.FIELD_ELEMENT_CLASS,ActionPlanActivityListPage.class,DataTable.FIELD_LAZY,Boolean.TRUE);
		
		dataTable.addHeaderToolbarLeftCommands(Button.build(Map.of(Button.FIELD_VALUE,"Créer",Button.FIELD_OUTCOME,"actionPlanActivityCreateView",Button.FIELD_PARAMETERS
				,Map.of("actionidentifier",Action.CREATE.name()))));
		
		dataTable.addColumnsAfterRowIndex(
				Column.build(Column.FIELD_FIELD_NAME,ActionPlanActivity.FIELD_ACTION_PLAN)
				,Column.build(Column.FIELD_FIELD_NAME,ActionPlanActivity.FIELD_ACTIVITY)
				);
		
		dataTable.addRecordMenuItemByArguments(MenuItem.FIELD_VALUE,"Supprimer",MenuItem.FIELD_ICON,"fa fa-remove",MenuItem.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {			
			@Override public void listenAction(Object argument) {__inject__(ActionPlanController.class).delete((ActionPlan) argument);}
		},MenuItem.ConfiguratorImpl.FIELD_CONFIRMABLE,Boolean.TRUE
						,MenuItem.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES,CollectionHelper.listOf(RenderType.GROWL));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des plans d'actions et activités";
	}
	
}
