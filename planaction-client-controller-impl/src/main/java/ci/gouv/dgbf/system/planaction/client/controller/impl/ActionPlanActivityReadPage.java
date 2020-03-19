package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.filter.FilterDto;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;

import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlanActivity;
import ci.gouv.dgbf.system.planaction.client.controller.entities.Imputation;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActionPlanActivityReadPage extends AbstractPageContainerManagedImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	private ActionPlanActivity actionPlanActivity;
	private Layout layout;
	private DataTable imputationsDataTable;
	
	private String[] years;
	private String[] entryAuthorizationsFieldsNames;
	private String[] paymentCreditsFieldsNames;
	
	private Integer amount;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		actionPlanActivity = WebController.getInstance().getRequestParameterEntity(ActionPlanActivity.class);
		Integer numberOfYears = (int)actionPlanActivity.getActionPlan().getNumberOfYears();
		years = new String[numberOfYears];
		paymentCreditsFieldsNames = new String[numberOfYears+1];
		entryAuthorizationsFieldsNames = new String[numberOfYears+1];
		for(Integer index = 0; index < numberOfYears; index = index + 1) {
			years[index] = StringHelper.get(actionPlanActivity.getActionPlan().getYear()+index);
			entryAuthorizationsFieldsNames[index] = String.format("amounts.year%s.entryAuthorization",index+1);
			paymentCreditsFieldsNames[index] = String.format("amounts.year%s.paymentCredit",index+1);
		}
		entryAuthorizationsFieldsNames[numberOfYears] = "amounts.total.entryAuthorization";
		paymentCreditsFieldsNames[numberOfYears] = "amounts.total.paymentCredit";
		__buildImputationsDataTable__();
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G
				,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
					MapHelper.instantiate(Cell.FIELD_CONTROL,imputationsDataTable,Cell.FIELD_WIDTH,12)
					));
	}
	
	private void __buildImputationsDataTable__() {
		imputationsDataTable = DataTable.build(DataTable.FIELD_LAZY,Boolean.TRUE,DataTable.FIELD_ELEMENT_CLASS,Imputation.class,DataTable.FIELD___PARENT_ELEMENT__
				,actionPlanActivity,DataTable.FIELD_EDITABLE, Boolean.TRUE,DataTable.ConfiguratorImpl.FIELD_EDITABLE_CELL, Boolean.TRUE);		
		//imputationsDataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialogCreate(CommandButton.FIELD_VALUE,"Ajouter",CommandButton.FIELD_ICON,"fa fa-plus");
		imputationsDataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialogCreate(CommandButton.FIELD_VALUE,"Ajouter",CommandButton.FIELD_ICON,"fa fa-plus");
		((LazyDataModel<?>)imputationsDataTable.getValue()).setListener(new LazyDataModel.Listener.AbstractImpl() {
			@Override
			public void processFilter(FilterDto filter) {
				super.processFilter(filter);
				filter.addField(Imputation.FIELD_ACTION_PLAN, actionPlanActivity.getActionPlan().getCode());
				filter.addField(Imputation.FIELD_ACTIVITY, actionPlanActivity.getActivity().getCode());
			}
		});		
		imputationsDataTable.addColumnsAfterRowIndex(
				Column.build(Column.FIELD_FIELD_NAME,Imputation.FIELD_COST_UNIT)
				,Column.build(Column.FIELD_FIELD_NAME,entryAuthorizationsFieldsNames[0],Column.FIELD_HEADER_TEXT,years[0]+".AE",Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,paymentCreditsFieldsNames[0],Column.FIELD_HEADER_TEXT,years[0]+".CP",Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,entryAuthorizationsFieldsNames[1],Column.FIELD_HEADER_TEXT,years[1]+".AE",Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,paymentCreditsFieldsNames[1],Column.FIELD_HEADER_TEXT,years[1]+".CP",Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,entryAuthorizationsFieldsNames[2],Column.FIELD_HEADER_TEXT,years[2]+".AE",Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,paymentCreditsFieldsNames[2],Column.FIELD_HEADER_TEXT,years[2]+".CP",Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,entryAuthorizationsFieldsNames[3],Column.FIELD_HEADER_TEXT,"AE",Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,paymentCreditsFieldsNames[3],Column.FIELD_HEADER_TEXT,"CP",Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				);		
		imputationsDataTable.addRecordMenuItemByArgumentsNavigateToView(null, "imputationPlanView",MenuItem.FIELD_VALUE,"Planifier A.E.",MenuItem.FIELD_ICON,"fa fa-calendar");
		//imputationsDataTable.addRecordMenuItemByArgumentsNavigateToViewRead();
		//imputationsDataTable.addRecordMenuItemByArgumentsNavigateToView(null, "entryAuthorizationListView",MenuItem.FIELD_VALUE,"Planifier A.E."
		//		,MenuItem.FIELD_ICON,"fa fa-file-text",MenuItem.FIELD___ACTION_ARGUMENT_IDENTIFIER_PARAMETER_NAME__,"imputation");
		imputationsDataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();
		
		imputationsDataTable.enableAjaxCellEdit();
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return actionPlanActivity.toString();
	}
}