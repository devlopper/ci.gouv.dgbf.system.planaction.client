package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.computation.ComparisonOperator;
import org.cyk.utility.__kernel__.constant.ConstantEmpty;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.persistence.query.filter.FilterDto;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;

import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlan;
import ci.gouv.dgbf.system.planaction.client.controller.entities.EntryAuthorization;
import ci.gouv.dgbf.system.planaction.client.controller.entities.Imputation;
import ci.gouv.dgbf.system.planaction.client.controller.entities.PaymentCredit;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class EntryAuthorizationListPage extends AbstractEntityListPageContainerManagedImpl<EntryAuthorization> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Imputation imputation;
	//private DataTable paymentCreditsDataTable;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void __listenPostConstruct__() {
		imputation = WebController.getInstance().getRequestParameterEntityAsParent(Imputation.class);
		super.__listenPostConstruct__();
		dataTable.useQueryIdentifiersFiltersLike();
		((LazyDataModel<PaymentCredit>)dataTable.getValue()).setListener(new LazyDataModel.Listener.AbstractImpl() {
			@Override
			public void processFilter(FilterDto filter) {
				super.processFilter(filter);
				if(imputation != null )
					filter.addField(EntryAuthorization.FIELD_IMPUTATION, imputation.getIdentifier());
			}
		});
		dataTable.setListener(new AbstractDataTable.Listener.AbstractImpl() {
			@Override
			public String listenGetStyleClassByRecordByColumn(Object record,Integer recordIndex,Column column,Integer columnIndex) {
				if(imputation!=null && NumberHelper.compare(columnIndex, 1, ComparisonOperator.EQ))
					return "cyk-text-align-right";
				return null;
			}
		});
		if(imputation == null) {
			
		}else {
			dataTable.setIsExportable(Boolean.FALSE);
			dataTable.setAreColumnsChoosable(Boolean.FALSE);
		}
		//paymentCreditsDataTable = Helper.buildDataTablePaymentCredits(null);
	}
	
	@Override
	protected Map<Object, Object> __getDataTableArguments__() {
		Map<Object, Object> arguments = super.__getDataTableArguments__();
		if(arguments == null)
			arguments = new HashMap<>();
		if(imputation == null) {
			
		}else {
			arguments.put(DataTable.FIELD___PARENT_ELEMENT__, imputation);
			arguments.put(DataTable.FIELD_EDITABLE, Boolean.TRUE);
			arguments.put(DataTable.ConfiguratorImpl.FIELD_EDITABLE_CELL, Boolean.TRUE);
		}		
		return arguments;
	}
	
	@Override
	protected Collection<String> __getColumnsFieldsNames__(Class<EntryAuthorization> entityClass) {
		List<String> list = CollectionHelper.listOf(EntryAuthorization.FIELD_YEAR,EntryAuthorization.FIELD_AMOUNT);
		if(imputation == null)
			list.addAll(0, List.of(
					EntryAuthorization.FIELD_IMPUTATION+"."+Imputation.FIELD_ACTION_PLAN+"."+ActionPlan.FIELD_NAME
					,EntryAuthorization.FIELD_IMPUTATION+"."+EntryAuthorization.FIELD_ACTIVITY
					,EntryAuthorization.FIELD_IMPUTATION+"."+EntryAuthorization.FIELD_COST_UNIT));
		return list;
	}
	
	@Override
	protected Map<Object, Object> __getColumnArguments__(String fieldName) {
		Map<Object, Object> arguments = super.__getColumnArguments__(fieldName);
		if(arguments == null)
			arguments = new HashMap<>();
		if(fieldName.equals(EntryAuthorization.FIELD_IMPUTATION+"."+Imputation.FIELD_ACTION_PLAN+"."+ActionPlan.FIELD_NAME))
			arguments.put(Column.FIELD_HEADER_TEXT, "Plan d'action");
		if(fieldName.equals(EntryAuthorization.FIELD_IMPUTATION+"."+EntryAuthorization.FIELD_ACTIVITY))
			arguments.put(Column.FIELD_HEADER_TEXT, "Activité");
		if(fieldName.equals(EntryAuthorization.FIELD_IMPUTATION+"."+EntryAuthorization.FIELD_COST_UNIT))
			arguments.put(Column.FIELD_HEADER_TEXT, "Unité de coût");
		return arguments;
	}
	
	protected void __addDataTableRecordMenuItemByArguments__(DataTable dataTable) {
		dataTable.addRecordMenuItemByArgumentsOpenViewInDialog("paymentCreditListView",MenuItem.FIELD_VALUE,"Échéances de paiements",MenuItem.FIELD_ICON,"fa fa-money"
				,MenuItem.FIELD___ACTION_ARGUMENT_IDENTIFIER_PARAMETER_NAME__,ParameterName.stringify(EntryAuthorization.class));
		/*
		dataTable.addRecordMenuItemByArgumentsNavigateToViewRead(MenuItem.FIELD_VALUE,"Échéances de paiements",MenuItem.FIELD_ICON,"fa fa-money"
				,MenuItem.FIELD___ACTION_ARGUMENT_IDENTIFIER_PARAMETER_NAME__,ParameterName.stringify(EntryAuthorization.class));
		*/
		dataTable.addRecordMenuItemByArgumentsOpenViewInDialogUpdate();
		dataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des autorisations d'engagements"+(imputation == null ? ConstantEmpty.STRING : " : "+imputation.toString());
	}
}