package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;
import java.util.Map;

import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.persistence.query.filter.FilterDto;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.user.interface_.message.MessageRenderer;
import org.cyk.utility.__kernel__.user.interface_.message.RenderType;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.ajax.Ajax;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.Button;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.omnifaces.util.Faces;

import ci.gouv.dgbf.system.planaction.client.controller.api.ActionPlanActivityController;
import ci.gouv.dgbf.system.planaction.client.controller.api.ActionPlanController;
import ci.gouv.dgbf.system.planaction.client.controller.api.ImputationController;
import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlan;
import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlanActivity;
import ci.gouv.dgbf.system.planaction.client.controller.entities.Imputation;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActionPlanReadPageOLD extends AbstractPageContainerManagedImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	private ActionPlan actionPlan;
	private Layout layout;
	private DataTable activitiesDataTable;
	private DataTable imputationsPaymentCreditsDataTable;
	private DataTable imputationsEntryAuthorizationsDataTable;
	
	private String[] years;
	private String[] entryAuthorizationsFieldsNames;
	private String[] paymentCreditsFieldsNames;
	
	private Ajax amountEditAjax;
	private Integer amount;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		actionPlan = __inject__(ActionPlanController.class).readBySystemIdentifier(Faces.getRequestParameter("entityidentifier"));
		Integer numberOfYears = (int)actionPlan.getNumberOfYears();
		years = new String[numberOfYears];
		paymentCreditsFieldsNames = new String[numberOfYears+1];
		entryAuthorizationsFieldsNames = new String[numberOfYears+1];
		for(Integer index = 0; index < numberOfYears; index = index + 1) {
			years[index] = StringHelper.get(actionPlan.getYear()+index);
			entryAuthorizationsFieldsNames[index] = String.format("amountYear%sEntryAuthorization",index+1);
			paymentCreditsFieldsNames[index] = String.format("amountYear%sPaymentCredit",index+1);
		}
		entryAuthorizationsFieldsNames[numberOfYears] = "amountTotalEntryAuthorization";
		paymentCreditsFieldsNames[numberOfYears] = "amountTotalPaymentCredit";
		__buildActivitiesDataTable__();
		__buildImputationsPaymentCreditsDataTable__();
		__buildImputationsEntryAuthorizationsDataTable__();
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G
				,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
					MapHelper.instantiate(Cell.FIELD_CONTROL,activitiesDataTable,Cell.FIELD_WIDTH,12)
					,MapHelper.instantiate(Cell.FIELD_CONTROL,imputationsPaymentCreditsDataTable,Cell.FIELD_WIDTH,12)
					,MapHelper.instantiate(Cell.FIELD_CONTROL,imputationsEntryAuthorizationsDataTable,Cell.FIELD_WIDTH,12)
					));
		
		amountEditAjax = Ajax.build(Ajax.FIELD_EVENT,"blur",Ajax.FIELD_LISTENER,new Ajax.Listener.AbstractImpl() {
			@Override
			public void listenAction(Object argument) {
				AjaxBehaviorEvent event = (AjaxBehaviorEvent) argument;
				Imputation imputation = (Imputation) event.getComponent().getAttributes().get("imputation");
				Integer yearIndex = NumberHelper.getInteger(event.getComponent().getAttributes().get("yearIndex"));
				MessageRenderer.getInstance().render(imputation+" of year "+yearIndex+" has been updated with amount "+amount, RenderType.GROWL);
			}
		});
		amountEditAjax.getRunnerArguments().setSuccessMessageArguments(null);
		amountEditAjax.setDisabled(Boolean.FALSE);
	}
	
	private void __buildActivitiesDataTable__() {
		activitiesDataTable = DataTable.build(DataTable.FIELD_LAZY,Boolean.TRUE,DataTable.FIELD_ELEMENT_CLASS,ActionPlanActivity.class
				,DataTable.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
		
		activitiesDataTable.addHeaderToolbarLeftCommands(
				Button.build(Button.FIELD_VALUE,"Ajouter",Button.FIELD_OUTCOME,"actionPlanActivityCreateView",Button.FIELD_PARAMETERS
				,Map.of("actionplan",actionPlan.getIdentifier()))
				/*,Button.build(Button.FIELD_VALUE,"Imputations",Button.FIELD_OUTCOME,"actionPlanImputationsReadView",Button.FIELD_PARAMETERS
						,Map.of("actionplan",actionPlan.getIdentifier()))
				/*,Button.build(Button.FIELD_VALUE,"Planification des autorisations d'engagements",Button.FIELD_OUTCOME,"actionPlanEntryAuthorizationPlanView",Button.FIELD_PARAMETERS
						,Map.of("actionplan",actionPlan.getIdentifier()))
				*/
				);
		
		((LazyDataModel<?>)activitiesDataTable.getValue()).setListener(new LazyDataModel.Listener.AbstractImpl() {
			@Override
			public void processFilter(FilterDto filter) {
				super.processFilter(filter);
				filter.addField(ActionPlanActivity.FIELD_ACTION_PLAN, actionPlan.getCode());
			}
		});
		
		activitiesDataTable.addColumnsAfterRowIndex(
				Column.build(Column.FIELD_FIELD_NAME,ActionPlanActivity.FIELD_ACTIVITY)
				,Column.build(Column.FIELD_FIELD_NAME,entryAuthorizationsFieldsNames[0],Column.FIELD_HEADER_TEXT,years[0]+".AE",Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,paymentCreditsFieldsNames[0],Column.FIELD_HEADER_TEXT,years[0]+".CP",Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,entryAuthorizationsFieldsNames[1],Column.FIELD_HEADER_TEXT,years[1]+".AE",Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,paymentCreditsFieldsNames[1],Column.FIELD_HEADER_TEXT,years[1]+".CP",Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,entryAuthorizationsFieldsNames[2],Column.FIELD_HEADER_TEXT,years[2]+".AE",Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,paymentCreditsFieldsNames[2],Column.FIELD_HEADER_TEXT,years[2]+".CP",Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,entryAuthorizationsFieldsNames[3],Column.FIELD_HEADER_TEXT,"AE",Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,paymentCreditsFieldsNames[3],Column.FIELD_HEADER_TEXT,"CP",Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				);
		
		
		activitiesDataTable.addRecordMenuItemByArguments(MenuItem.FIELD_VALUE,"Retirer",MenuItem.FIELD_ICON,"fa fa-minus",MenuItem.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {			
			@Override public void listenAction(Object argument) {__inject__(ActionPlanActivityController.class).delete((ActionPlanActivity) argument);}
		},MenuItem.ConfiguratorImpl.FIELD_CONFIRMABLE,Boolean.TRUE
						,MenuItem.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES,CollectionHelper.listOf(RenderType.GROWL));
	}
	
	private void __buildImputationsPaymentCreditsDataTable__() {
		imputationsPaymentCreditsDataTable = DataTable.build(DataTable.FIELD_LAZY,Boolean.TRUE,DataTable.FIELD_ELEMENT_CLASS,Imputation.class
				,DataTable.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
		
		imputationsPaymentCreditsDataTable.addHeaderToolbarLeftCommands(
				Button.build(Button.FIELD_VALUE,"Créer",Button.FIELD_OUTCOME,"imputationCreateView",Button.FIELD_PARAMETERS
				,Map.of("actionplan",actionPlan.getIdentifier()))
				/*,Button.build(Button.FIELD_VALUE,"Imputations",Button.FIELD_OUTCOME,"actionPlanImputationsReadView",Button.FIELD_PARAMETERS
						,Map.of("actionplan",actionPlan.getIdentifier()))
				/*,Button.build(Button.FIELD_VALUE,"Planification des autorisations d'engagements",Button.FIELD_OUTCOME,"actionPlanEntryAuthorizationPlanView",Button.FIELD_PARAMETERS
						,Map.of("actionplan",actionPlan.getIdentifier()))
				*/
				);
		
		((LazyDataModel<?>)imputationsPaymentCreditsDataTable.getValue()).setListener(new LazyDataModel.Listener.AbstractImpl() {
			@Override
			public void processFilter(FilterDto filter) {
				super.processFilter(filter);
				filter.addField(Imputation.FIELD_ACTION_PLAN, actionPlan.getCode());
			}
		});
		
		imputationsPaymentCreditsDataTable.addColumnsAfterRowIndex(
				Column.build(Column.FIELD_FIELD_NAME,Imputation.FIELD_ACTIVITY)
				,Column.build(Column.FIELD_FIELD_NAME,Imputation.FIELD_COST_UNIT)
				,Column.build(Column.FIELD_FIELD_NAME,paymentCreditsFieldsNames[0],Column.FIELD_HEADER_TEXT,years[0],Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,paymentCreditsFieldsNames[1],Column.FIELD_HEADER_TEXT,years[1],Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,paymentCreditsFieldsNames[2],Column.FIELD_HEADER_TEXT,years[2],Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,paymentCreditsFieldsNames[3],Column.FIELD_HEADER_TEXT,"Total",Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				);
		
		imputationsPaymentCreditsDataTable.addRecordMenuItemByArguments(MenuItem.FIELD_VALUE,"Supprimer",MenuItem.FIELD_ICON,"fa fa-remove",MenuItem.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {			
			@Override public void listenAction(Object argument) {__inject__(ImputationController.class).delete((Imputation) argument);}
		},MenuItem.ConfiguratorImpl.FIELD_CONFIRMABLE,Boolean.TRUE
						,MenuItem.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES,CollectionHelper.listOf(RenderType.GROWL));
	}
	
	private void __buildImputationsEntryAuthorizationsDataTable__() {
		imputationsEntryAuthorizationsDataTable = DataTable.build(DataTable.FIELD_LAZY,Boolean.TRUE,DataTable.FIELD_ELEMENT_CLASS,Imputation.class
				,DataTable.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
		
		imputationsEntryAuthorizationsDataTable.addHeaderToolbarLeftCommands(
				Button.build(Button.FIELD_VALUE,"Créer",Button.FIELD_OUTCOME,"imputationCreateView",Button.FIELD_PARAMETERS
				,Map.of("actionplan",actionPlan.getIdentifier()))
				/*,Button.build(Button.FIELD_VALUE,"Imputations",Button.FIELD_OUTCOME,"actionPlanImputationsReadView",Button.FIELD_PARAMETERS
						,Map.of("actionplan",actionPlan.getIdentifier()))
				/*,Button.build(Button.FIELD_VALUE,"Planification des autorisations d'engagements",Button.FIELD_OUTCOME,"actionPlanEntryAuthorizationPlanView",Button.FIELD_PARAMETERS
						,Map.of("actionplan",actionPlan.getIdentifier()))
				*/
				);
		
		((LazyDataModel<?>)imputationsEntryAuthorizationsDataTable.getValue()).setListener(new LazyDataModel.Listener.AbstractImpl() {
			@Override
			public void processFilter(FilterDto filter) {
				super.processFilter(filter);
				filter.addField(Imputation.FIELD_ACTION_PLAN, actionPlan.getCode());
			}
		});
		
		imputationsEntryAuthorizationsDataTable.addColumnsAfterRowIndex(
				Column.build(Column.FIELD_FIELD_NAME,Imputation.FIELD_ACTIVITY)
				,Column.build(Column.FIELD_FIELD_NAME,Imputation.FIELD_COST_UNIT)
				,Column.build(Column.FIELD_FIELD_NAME,entryAuthorizationsFieldsNames[0],Column.FIELD_HEADER_TEXT,years[0],Column.FIELD_WIDTH,"100",Column.FIELD_HEADER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,entryAuthorizationsFieldsNames[1],Column.FIELD_HEADER_TEXT,years[1],Column.FIELD_WIDTH,"100",Column.FIELD_HEADER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,entryAuthorizationsFieldsNames[2],Column.FIELD_HEADER_TEXT,years[2],Column.FIELD_WIDTH,"100",Column.FIELD_HEADER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,entryAuthorizationsFieldsNames[3],Column.FIELD_HEADER_TEXT,"Total",Column.FIELD_WIDTH,"100",Column.FIELD_HEADER_TEXT,"TOTAL")
				);
		
		imputationsEntryAuthorizationsDataTable.addRecordMenuItemByArguments(MenuItem.FIELD_VALUE,"Supprimer",MenuItem.FIELD_ICON,"fa fa-remove",MenuItem.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {			
			@Override public void listenAction(Object argument) {__inject__(ImputationController.class).delete((Imputation) argument);}
		},MenuItem.ConfiguratorImpl.FIELD_CONFIRMABLE,Boolean.TRUE
						,MenuItem.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES,CollectionHelper.listOf(RenderType.GROWL));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		if(actionPlan == null)
			return super.__getWindowTitleValue__();
		return actionPlan.toString();
	}
	
	public void updateAmount(AjaxBehaviorEvent event) {
		
	}
}
