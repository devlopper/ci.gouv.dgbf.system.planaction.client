package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.constant.ConstantEmpty;
import org.cyk.utility.__kernel__.persistence.query.filter.FilterDto;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;

import ci.gouv.dgbf.system.planaction.client.controller.entities.EntryAuthorization;
import ci.gouv.dgbf.system.planaction.client.controller.entities.PaymentCredit;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class PaymentCreditListPage extends AbstractEntityListPageContainerManagedImpl<PaymentCredit> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private EntryAuthorization entryAuthorization;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void __listenPostConstruct__() {
		entryAuthorization = WebController.getInstance().getRequestParameterEntityAsParent(EntryAuthorization.class);
		super.__listenPostConstruct__();
		dataTable.useQueryIdentifiersFiltersLike();
		((LazyDataModel<PaymentCredit>)dataTable.getValue()).setListener(new LazyDataModel.Listener.AbstractImpl() {
			@Override
			public void processFilter(FilterDto filter) {
				super.processFilter(filter);
				if(entryAuthorization != null )
					filter.addField(PaymentCredit.FIELD_ENTRY_AUTHORIZATION, entryAuthorization.getIdentifier());
			}
		});
	}
	
	@Override
	protected Map<Object, Object> __getDataTableArguments__() {
		Map<Object, Object> arguments = super.__getDataTableArguments__();
		if(arguments == null)
			arguments = new HashMap<>();
		arguments.put(DataTable.FIELD___PARENT_ELEMENT__, entryAuthorization);
		return arguments;
	}
	
	@Override
	protected Collection<String> __getColumnsFieldsNames__(Class<PaymentCredit> entityClass) {
		List<String> columnsFieldsNames = CollectionHelper.listOf(PaymentCredit.FIELD_YEAR,PaymentCredit.FIELD_AMOUNT);
		if(entryAuthorization == null) {
			columnsFieldsNames.addAll(0, List.of(PaymentCredit.FIELD_ENTRY_AUTHORIZATION+"."+EntryAuthorization.FIELD_IMPUTATION+"."+PaymentCredit.FIELD_ACTION_PLAN+".name"
					,PaymentCredit.FIELD_ENTRY_AUTHORIZATION+"."+EntryAuthorization.FIELD_IMPUTATION+"."+PaymentCredit.FIELD_ACTIVITY
					,PaymentCredit.FIELD_ENTRY_AUTHORIZATION+"."+EntryAuthorization.FIELD_IMPUTATION+"."+PaymentCredit.FIELD_COST_UNIT));
		}
		return columnsFieldsNames;
	}
	
	@Override
	protected Map<Object, Object> __getColumnArguments__(String fieldName) {
		Map<Object, Object> arguments = super.__getColumnArguments__(fieldName);
		if(arguments == null)
			arguments = new HashMap<>();
		if(fieldName.equals(PaymentCredit.FIELD_ENTRY_AUTHORIZATION+"."+EntryAuthorization.FIELD_IMPUTATION+"."+PaymentCredit.FIELD_ACTION_PLAN+".name"))
			arguments.put(Column.FIELD_HEADER_TEXT, "Plan d'action");
		if(fieldName.equals(PaymentCredit.FIELD_ENTRY_AUTHORIZATION+"."+EntryAuthorization.FIELD_IMPUTATION+"."+PaymentCredit.FIELD_ACTIVITY))
			arguments.put(Column.FIELD_HEADER_TEXT, "Activité");
		if(fieldName.equals(PaymentCredit.FIELD_ENTRY_AUTHORIZATION+"."+EntryAuthorization.FIELD_IMPUTATION+"."+PaymentCredit.FIELD_COST_UNIT))
			arguments.put(Column.FIELD_HEADER_TEXT, "Unité de coût");
		return arguments;
	}
	
	protected void __addDataTableRecordMenuItemByArguments__(DataTable dataTable) {
		//dataTable.addRecordMenuItemByArgumentsNavigateToView(null, "paymentCreditListView",MenuItem.FIELD_VALUE,"Modifier l'échéancier de paiements",MenuItem.FIELD_ICON,"fa fa-money"
		//		,MenuItem.FIELD___ACTION_ARGUMENT_IDENTIFIER_PARAMETER_NAME__,"entryauthorization");
		
		//dataTable.addRecordMenuItemByArgumentsNavigateToViewRead();
		dataTable.addRecordMenuItemByArgumentsOpenViewInDialogUpdate();
		dataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des échéances de paiements"+(entryAuthorization == null ? ConstantEmpty.STRING : " "+entryAuthorization);
	}
}