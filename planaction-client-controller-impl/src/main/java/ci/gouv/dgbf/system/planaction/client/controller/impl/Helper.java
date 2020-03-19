package ci.gouv.dgbf.system.planaction.client.controller.impl;

import org.cyk.utility.__kernel__.persistence.query.filter.FilterDto;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;

import ci.gouv.dgbf.system.planaction.client.controller.entities.EntryAuthorization;
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
	
}
