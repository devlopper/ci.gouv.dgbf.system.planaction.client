package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.filter.FilterDto;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.omnifaces.util.Faces;

import ci.gouv.dgbf.system.planaction.client.controller.api.ActionPlanController;
import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlan;
import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlanActivity;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActionPlanReadPage extends AbstractPageContainerManagedImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	private ActionPlan actionPlan;
	private Layout layout;
	private DataTable activitiesDataTable;
	
	private String[] years;
	private String[] entryAuthorizationsFieldsNames;
	private String[] paymentCreditsFieldsNames;
	
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
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G
				,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
					MapHelper.instantiate(Cell.FIELD_CONTROL,activitiesDataTable,Cell.FIELD_WIDTH,12)
					));
	}
	
	private void __buildActivitiesDataTable__() {
		activitiesDataTable = DataTable.build(DataTable.FIELD_LAZY,Boolean.TRUE,DataTable.FIELD_ELEMENT_CLASS,ActionPlanActivity.class,DataTable.FIELD___PARENT_ELEMENT__,actionPlan);		
		activitiesDataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialogCreate();		
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
		activitiesDataTable.addRecordMenuItemByArgumentsNavigateToView(null, "actionPlanActivityFinanceView",MenuItem.FIELD_VALUE,"Financer",MenuItem.FIELD_ICON,"fa fa-money");
		activitiesDataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return actionPlan.getName();
	}
}