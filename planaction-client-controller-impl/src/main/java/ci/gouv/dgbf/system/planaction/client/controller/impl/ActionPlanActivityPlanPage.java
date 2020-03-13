package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.persistence.query.filter.FilterDto;
import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.ajax.Ajax;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.planaction.client.controller.api.FundingController;
import ci.gouv.dgbf.system.planaction.client.controller.api.ImputationController;
import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlanActivity;
import ci.gouv.dgbf.system.planaction.client.controller.entities.Funding;
import ci.gouv.dgbf.system.planaction.client.controller.entities.Imputation;
import ci.gouv.dgbf.system.planaction.server.persistence.api.FundingPersistence;
import ci.gouv.dgbf.system.planaction.server.persistence.api.ImputationPersistence;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActionPlanActivityPlanPage extends AbstractPageContainerManagedImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ActionPlanActivity actionPlanActivity;
	private DataTable imputationsDataTable;
	private String[] years;
	private String[] amountsFieldsNames;
	private Layout layout;
	private Collection<Funding> fundings;
	
	private Ajax amountEditAjax;
	private Integer amount;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		actionPlanActivity = WebController.getInstance().getRequestParameterEntity(ActionPlanActivity.class);
		Integer numberOfYears = (int)actionPlanActivity.getActionPlan().getNumberOfYears();
		years = new String[numberOfYears];
		amountsFieldsNames = new String[numberOfYears+1];
		for(Integer index = 0; index < numberOfYears; index = index + 1) {
			years[index] = StringHelper.get(actionPlanActivity.getActionPlan().getYear()+index);
			amountsFieldsNames[index] = String.format("amountYear%sPaymentCredit",index+1);
		}
		amountsFieldsNames[numberOfYears] = "amountTotalPaymentCredit";
		__buildImputationsDataTable__();
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G
				,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
					MapHelper.instantiate(Cell.FIELD_CONTROL,imputationsDataTable,Cell.FIELD_WIDTH,12)
					));
		
		amountEditAjax = Ajax.build(Ajax.FIELD_EVENT,"blur",Ajax.ConfiguratorImpl.FIELD_LISTENER_ACTION,Ajax.Listener.Action.EXECUTE_FUNCTION,Ajax.FIELD_LISTENER,new Ajax.Listener.AbstractImpl() {
			@Override
			protected Object __executeFunction__(Object argument) {
				AjaxBehaviorEvent event = (AjaxBehaviorEvent) argument;
				Imputation imputation = (Imputation) event.getComponent().getAttributes().get("imputation");
				Integer yearIndex = NumberHelper.getInteger(event.getComponent().getAttributes().get("yearIndex"));
				String amountFieldName = (String) event.getComponent().getAttributes().get("amountFieldName");
				return amountFieldName+" of "+imputation+" of year "+yearIndex+" has been updated with amount "+amount;
			}
		});
		amountEditAjax.getRunnerArguments().setSuccessMessageArguments(null);
		amountEditAjax.setDisabled(Boolean.FALSE);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Planification des autorisations d'engagements(A.E.) et des crédits de paiements(C.P.) de l'activité "+actionPlanActivity.getActivity();
	}
	
	private void __buildImputationsDataTable__() {
		Collection<Imputation> imputations = __inject__(ImputationController.class).read(new Properties()
				.setQueryIdentifier(ImputationPersistence.READ_BY_ACTION_PLAN_CODE_BY_ACTIVITY_CODE)
				.setFilters(new FilterDto().addField(Funding.FIELD_ACTION_PLAN, actionPlanActivity.getActionPlan().getCode())
						.addField(Funding.FIELD_ACTIVITY, actionPlanActivity.getActivity().getCode())));
		
		fundings = __inject__(FundingController.class).read(new Properties().setQueryIdentifier(FundingPersistence.READ_BY_IMPUTATIONS_IDENTIFIERS)
				.setFilters(new FilterDto().addField(Funding.FIELD_IMPUTATION, imputations.stream().map(Imputation::getIdentifier).collect(Collectors.toList()))));
		
		for(Imputation imputation : imputations) {
			imputation.setFundings(fundings.stream().filter(funding -> funding.getImputation().equals(imputation)).collect(Collectors.toList()));
		}
		
		imputationsDataTable = DataTable.build(DataTable.FIELD_VALUE,imputations,DataTable.FIELD_ELEMENT_CLASS,Imputation.class,DataTable.FIELD___PARENT_ELEMENT__
				,actionPlanActivity);
		imputationsDataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialogCreate(CommandButton.FIELD_VALUE,"Ajouter",CommandButton.FIELD_ICON,"fa fa-plus");	
		
		String amountColumnWidth = "130";
		imputationsDataTable.addColumnsAfterRowIndex(
				Column.build(Column.FIELD_FIELD_NAME,Imputation.FIELD_COST_UNIT)
				,Column.build(Column.FIELD_FIELD_NAME,amountsFieldsNames[0],Column.FIELD_HEADER_TEXT,years[0],Column.FIELD_WIDTH,amountColumnWidth,Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,amountsFieldsNames[1],Column.FIELD_HEADER_TEXT,years[1],Column.FIELD_WIDTH,amountColumnWidth,Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,amountsFieldsNames[2],Column.FIELD_HEADER_TEXT,years[2],Column.FIELD_WIDTH,amountColumnWidth,Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,amountsFieldsNames[3],Column.FIELD_HEADER_TEXT,"Total",Column.FIELD_WIDTH,amountColumnWidth,Column.FIELD_FOOTER_TEXT,"TOTAL")
				);
		
		//imputationsDataTable.addRecordMenuItemByArgumentsOpenViewInDialog("imputationDistributeView",MenuItem.FIELD_VALUE,"Répartir",MenuItem.FIELD_ICON,"fa fa-paint-brush");
		//imputationsDataTable.addRecordMenuItemByArgumentsOpenViewInDialog("imputationPlanView",MenuItem.FIELD_VALUE,"Planifier A.E.",MenuItem.FIELD_ICON,"fa fa-envelope");
		imputationsDataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();
	}
}