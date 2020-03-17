package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.constant.ConstantEmpty;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;

import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlan;
import ci.gouv.dgbf.system.planaction.client.controller.entities.EntryAuthorization;
import ci.gouv.dgbf.system.planaction.client.controller.entities.Imputation;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class EntryAuthorizationListPage extends AbstractEntityListPageContainerManagedImpl<EntryAuthorization> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Imputation imputation;
	
	@Override
	protected void __listenPostConstruct__() {
		imputation = WebController.getInstance().getRequestParameterEntityAsParent(Imputation.class);
		super.__listenPostConstruct__();
		dataTable.useQueryIdentifiersFiltersLike();
	}
	
	@Override
	protected Map<Object, Object> __getDataTableArguments__() {
		Map<Object, Object> arguments = super.__getDataTableArguments__();
		if(arguments == null)
			arguments = new HashMap<>();
		arguments.put(DataTable.FIELD___PARENT_ELEMENT__, imputation);
		return arguments;
	}
	
	@Override
	protected Collection<String> __getColumnsFieldsNames__(Class<EntryAuthorization> entityClass) {
		return List.of(
				EntryAuthorization.FIELD_IMPUTATION+"."+Imputation.FIELD_ACTION_PLAN+"."+ActionPlan.FIELD_NAME
				,EntryAuthorization.FIELD_IMPUTATION+"."+EntryAuthorization.FIELD_ACTIVITY
				,EntryAuthorization.FIELD_IMPUTATION+"."+EntryAuthorization.FIELD_COST_UNIT
				,EntryAuthorization.FIELD_YEAR,EntryAuthorization.FIELD_AMOUNT);
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