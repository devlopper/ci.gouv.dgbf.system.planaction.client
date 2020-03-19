package ci.gouv.dgbf.system.planaction.client.controller.impl;

import org.cyk.utility.__kernel__.persistence.query.filter.FilterDto;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;

import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlan;
import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlanActivity;
import ci.gouv.dgbf.system.planaction.client.controller.entities.EntryAuthorization;
import ci.gouv.dgbf.system.planaction.client.controller.entities.Imputation;
import ci.gouv.dgbf.system.planaction.client.controller.entities.PaymentCredit;

public interface Helper {

	@SuppressWarnings("unchecked")
	static DataTable buildDataTablePaymentCredits(EntryAuthorization entryAuthorization) {
		DataTable dataTable = DataTable.build(DataTable.FIELD_LAZY,Boolean.TRUE,DataTable.FIELD_ELEMENT_CLASS,PaymentCredit.class);
		((LazyDataModel<PaymentCredit>)dataTable.getValue()).setListener(new LazyDataModel.Listener.AbstractImpl() {
			@Override
			public void processFilter(FilterDto filter) {
				super.processFilter(filter);
				if(entryAuthorization != null )
					filter.addField(PaymentCredit.FIELD_ENTRY_AUTHORIZATION, entryAuthorization.getIdentifier());
			}
		});
		dataTable.useQueryIdentifiersFiltersLike();
		dataTable.addColumnsAfterRowIndex(Column.build(Column.FIELD_FIELD_NAME,PaymentCredit.FIELD_YEAR)
				,Column.build(Column.FIELD_FIELD_NAME,PaymentCredit.FIELD_AMOUNT)
				);
		dataTable.addRecordMenuItemByArgumentsOpenViewInDialogUpdate();
		dataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();
		return dataTable;
	}
	
	static DataTable addDataTableColumnsAfterRowIndexEntryAuthorizationAndPaymentCredit(DataTable dataTable,Short year,Byte numberOfYears) {
		if(numberOfYears == null || numberOfYears < -1)
			return null;
		String[] years = new String[numberOfYears];
		String[] entryAuthorizationsFieldsNames = new String[numberOfYears+1];
		String[] paymentCreditsFieldsNames = new String[numberOfYears+1];		
		for(Integer index = 0; index < numberOfYears; index = index + 1) {
			years[index] = StringHelper.get(year+index);
			entryAuthorizationsFieldsNames[index] = String.format("amounts.year%s.entryAuthorization",index+1);
			paymentCreditsFieldsNames[index] = String.format("amounts.year%s.paymentCredit",index+1);
		}
		entryAuthorizationsFieldsNames[numberOfYears] = "amounts.total.entryAuthorization";
		paymentCreditsFieldsNames[numberOfYears] = "amounts.total.paymentCredit";
		
		dataTable.addColumnsAfterRowIndex(
				Column.build(Column.FIELD_FIELD_NAME,entryAuthorizationsFieldsNames[0],Column.FIELD_HEADER_TEXT,years[0]+".AE",Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,paymentCreditsFieldsNames[0],Column.FIELD_HEADER_TEXT,years[0]+".CP",Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,entryAuthorizationsFieldsNames[1],Column.FIELD_HEADER_TEXT,years[1]+".AE",Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,paymentCreditsFieldsNames[1],Column.FIELD_HEADER_TEXT,years[1]+".CP",Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,entryAuthorizationsFieldsNames[2],Column.FIELD_HEADER_TEXT,years[2]+".AE",Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,paymentCreditsFieldsNames[2],Column.FIELD_HEADER_TEXT,years[2]+".CP",Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,entryAuthorizationsFieldsNames[3],Column.FIELD_HEADER_TEXT,"AE",Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,paymentCreditsFieldsNames[3],Column.FIELD_HEADER_TEXT,"CP",Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				);		
		return dataTable;
	}
	
	static DataTable buildDataTableActionPlanActivities(ActionPlan actionPlan) {
		DataTable dataTable = DataTable.build(DataTable.FIELD_LAZY,Boolean.TRUE,DataTable.FIELD_ELEMENT_CLASS,ActionPlanActivity.class,DataTable.FIELD___PARENT_ELEMENT__,actionPlan);		
		dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialogCreate(CommandButton.FIELD_VALUE,"Ajouter",CommandButton.FIELD_ICON,"fa fa-plus");		
		((LazyDataModel<?>)dataTable.getValue()).setListener(new LazyDataModel.Listener.AbstractImpl() {
			@Override
			public void processFilter(FilterDto filter) {
				super.processFilter(filter);
				filter.addField(ActionPlanActivity.FIELD_ACTION_PLAN, actionPlan.getCode());
			}
		});
		dataTable.addColumnsAfterRowIndex(Column.build(Column.FIELD_FIELD_NAME,ActionPlanActivity.FIELD_ACTIVITY));
		addDataTableColumnsAfterRowIndexEntryAuthorizationAndPaymentCredit(dataTable, actionPlan.getYear(), actionPlan.getNumberOfYears());
		//dataTable.addRecordMenuItemByArgumentsNavigateToView(null, "actionPlanActivityPlanView",MenuItem.FIELD_VALUE,"Planifier A.E.",MenuItem.FIELD_ICON,"fa fa-calendar");
		dataTable.addRecordMenuItemByArgumentsNavigateToViewRead();
		//activitiesDataTable.addRecordMenuItemByArgumentsNavigateToView(null, "entryAuthorizationListView",MenuItem.FIELD_VALUE,"Planifier A.E."
		//		,MenuItem.FIELD_ICON,"fa fa-file-text",MenuItem.FIELD___ACTION_ARGUMENT_IDENTIFIER_PARAMETER_NAME__,"imputation");
		dataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();
		return dataTable;
	}
	
	static DataTable buildDataTableImputations(ActionPlanActivity actionPlanActivity) {
		DataTable dataTable = DataTable.build(DataTable.FIELD_LAZY,Boolean.TRUE,DataTable.FIELD_ELEMENT_CLASS,Imputation.class,DataTable.FIELD___PARENT_ELEMENT__,actionPlanActivity);		
		dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialogCreate(CommandButton.FIELD_VALUE,"Ajouter",CommandButton.FIELD_ICON,"fa fa-plus");		
		((LazyDataModel<?>)dataTable.getValue()).setListener(new LazyDataModel.Listener.AbstractImpl() {
			@Override
			public void processFilter(FilterDto filter) {
				super.processFilter(filter);
				filter.addField(Imputation.FIELD_ACTION_PLAN, actionPlanActivity.getActionPlan().getCode());
				filter.addField(Imputation.FIELD_ACTIVITY, actionPlanActivity.getActivity().getCode());
			}
		});		
		dataTable.addColumnsAfterRowIndex(Column.build(Column.FIELD_FIELD_NAME,Imputation.FIELD_COST_UNIT));
		addDataTableColumnsAfterRowIndexEntryAuthorizationAndPaymentCredit(dataTable, actionPlanActivity.getActionPlan().getYear(), actionPlanActivity.getActionPlan().getNumberOfYears());
		dataTable.addRecordMenuItemByArgumentsOpenViewInDialog("entryAuthorizationListView",MenuItem.FIELD_VALUE,"A.E.",MenuItem.FIELD_ICON,"fa fa-paint-brush",
				MenuItem.FIELD___ACTION_ARGUMENT_IDENTIFIER_PARAMETER_NAME__,"imputation");
		dataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();
		return dataTable;
	}
}
