package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.identifier.resource.PathAsFunctionParameter;
import org.cyk.utility.__kernel__.identifier.resource.QueryAsFunctionParameter;
import org.cyk.utility.__kernel__.identifier.resource.UniformResourceIdentifierAsFunctionParameter;
import org.cyk.utility.__kernel__.identifier.resource.UniformResourceIdentifierHelper;
import org.cyk.utility.__kernel__.user.interface_.message.RenderType;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.Button;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;

import ci.gouv.dgbf.system.planaction.client.controller.api.ActionPlanController;
import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlan;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActionPlanListPage extends AbstractPageContainerManagedImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	private DataTable dataTable;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		dataTable = DataTable.build(DataTable.ConfiguratorImpl.FIELD_ENTIY_CLASS,ActionPlan.class,DataTable.FIELD_LAZY,Boolean.TRUE);
		
		dataTable.addHeaderToolbarLeftCommands(Button.build(Map.of(Button.FIELD_VALUE,"Créer",Button.FIELD_OUTCOME,"actionPlanEditView",Button.FIELD_PARAMETERS
				,Map.of("actionidentifier",Action.CREATE.name()))));
		
		dataTable.addColumnsAfterRowIndex(
				Column.build(Map.of(Column.FIELD_FIELD_NAME,ActionPlan.FIELD_CODE,Column.FIELD_WIDTH,"200"))
				,Column.build(Map.of(Column.FIELD_FIELD_NAME,ActionPlan.FIELD_ADMINISTRATIVE_UNIT))
				,Column.build(Map.of(Column.FIELD_FIELD_NAME,ActionPlan.FIELD_YEAR,Column.FIELD_WIDTH,"100"))
				,Column.build(Map.of(Column.FIELD_FIELD_NAME,ActionPlan.FIELD_ORDER_NUMBER,Column.FIELD_WIDTH,"100",Column.FIELD_VISIBLE,Boolean.FALSE))
				,Column.build(Map.of(Column.FIELD_FIELD_NAME,ActionPlan.FIELD_NAME))
				);
		
		dataTable.addRecordMenuItemByArguments(MenuItem.FIELD_VALUE,"Modifier",MenuItem.FIELD_ICON,"fa fa-pencil",MenuItem.FIELD_LISTENER,new AbstractAction.Listener() {			
			@Override public void listenAction(Object argument) {
				UniformResourceIdentifierAsFunctionParameter p = new UniformResourceIdentifierAsFunctionParameter();
				p.setRequest(__getRequest__());
				p.setPath(new PathAsFunctionParameter());
				p.getPath().setIdentifier("actionPlanEditView");
				p.setQuery(new QueryAsFunctionParameter());
				p.getQuery().setValue("entityidentifier="+((ActionPlan)argument).getIdentifier()+"&actionidentifier=update");
				try {
					FacesContext.getCurrentInstance().getExternalContext().redirect(UniformResourceIdentifierHelper.build(p));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		},MenuItem.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_NULLABLE,Boolean.TRUE);
		
		dataTable.addRecordMenuItemByArguments(MenuItem.FIELD_VALUE,"Supprimer",MenuItem.FIELD_ICON,"fa fa-remove",MenuItem.FIELD_LISTENER,new AbstractAction.Listener() {			
			@Override public void listenAction(Object argument) {__inject__(ActionPlanController.class).delete((ActionPlan) argument);}
		},MenuItem.ConfiguratorImpl.FIELD_CONFIRMABLE,Boolean.TRUE
						,MenuItem.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES,CollectionHelper.listOf(RenderType.GROWL));
		
		dataTable.addRecordMenuItemByArguments(MenuItem.FIELD_VALUE,"Ouvrir",MenuItem.FIELD_ICON,"fa fa-eye",MenuItem.FIELD_LISTENER,new AbstractAction.Listener() {			
			@Override public void listenAction(Object argument) {
				UniformResourceIdentifierAsFunctionParameter p = new UniformResourceIdentifierAsFunctionParameter();
				p.setRequest(__getRequest__());
				p.setPath(new PathAsFunctionParameter());
				p.getPath().setIdentifier("actionPlanReadView");
				p.setQuery(new QueryAsFunctionParameter());
				p.getQuery().setValue("entityidentifier="+((ActionPlan)argument).getIdentifier());
				try {
					FacesContext.getCurrentInstance().getExternalContext().redirect(UniformResourceIdentifierHelper.build(p));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		},MenuItem.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_NULLABLE,Boolean.TRUE);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des plans d'actions";
	}
	
}
