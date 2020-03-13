package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;

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
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.ajax.Ajax;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;

import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlanActivity;
import ci.gouv.dgbf.system.planaction.client.controller.entities.Imputation;
import ci.gouv.dgbf.system.planaction.server.persistence.api.ImputationPersistence;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActionPlanActivityFinancePage extends AbstractPageContainerManagedImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ActionPlanActivity actionPlanActivity;
	private DataTable imputationsDataTable;
	private String[] years;
	private String[] paymentCreditsFieldsNames;
	private Layout layout;
	
	private Ajax amountEditAjax;
	private Integer amount;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		actionPlanActivity = WebController.getInstance().getRequestParameterEntity(ActionPlanActivity.class);
		Integer numberOfYears = (int)actionPlanActivity.getActionPlan().getNumberOfYears();
		years = new String[numberOfYears];
		paymentCreditsFieldsNames = new String[numberOfYears+1];
		for(Integer index = 0; index < numberOfYears; index = index + 1) {
			years[index] = StringHelper.get(actionPlanActivity.getActionPlan().getYear()+index);
			paymentCreditsFieldsNames[index] = String.format("amountYear%sPaymentCredit",index+1);
		}
		paymentCreditsFieldsNames[numberOfYears] = "amountTotalPaymentCredit";
		__buildImputationsDataTable__();
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G
				,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
					MapHelper.instantiate(Cell.FIELD_CONTROL,imputationsDataTable,Cell.FIELD_WIDTH,12)
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
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Programmation financière de l'activité "+actionPlanActivity.getActivity();
	}
	
	private void __buildImputationsDataTable__() {
		imputationsDataTable = DataTable.build(DataTable.FIELD_LAZY,Boolean.TRUE,DataTable.FIELD_ELEMENT_CLASS,Imputation.class,DataTable.FIELD___PARENT_ELEMENT__,actionPlanActivity);
		imputationsDataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialogCreate();	
		((LazyDataModel<?>)imputationsDataTable.getValue()).setReadQueryIdentifier(ImputationPersistence.READ_BY_ACTION_PLAN_CODE_BY_ACTIVITY_CODE);
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
				,Column.build(Column.FIELD_FIELD_NAME,paymentCreditsFieldsNames[0],Column.FIELD_HEADER_TEXT,years[0],Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,paymentCreditsFieldsNames[1],Column.FIELD_HEADER_TEXT,years[1],Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,paymentCreditsFieldsNames[2],Column.FIELD_HEADER_TEXT,years[2],Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				,Column.build(Column.FIELD_FIELD_NAME,paymentCreditsFieldsNames[3],Column.FIELD_HEADER_TEXT,"Total",Column.FIELD_WIDTH,"100",Column.FIELD_FOOTER_TEXT,"TOTAL")
				);
		
		imputationsDataTable.addRecordMenuItemByArgumentsOpenViewInDialog("imputationDistributeView",MenuItem.FIELD_VALUE,"Répartir",MenuItem.FIELD_ICON,"fa fa-paint-brush");
		imputationsDataTable.addRecordMenuItemByArgumentsOpenViewInDialog("imputationPlanView",MenuItem.FIELD_VALUE,"Planifier A.E.",MenuItem.FIELD_ICON,"fa fa-envelope");
		imputationsDataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();
	}
}