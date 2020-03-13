package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.filter.FilterDto;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.omnifaces.util.Faces;

import ci.gouv.dgbf.system.planaction.client.controller.api.ActionPlanController;
import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlan;
import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlanActivity;
import ci.gouv.dgbf.system.planaction.client.controller.entities.Imputation;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActionPlanFinancialProgramPage extends AbstractPageContainerManagedImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ActionPlan actionPlan;
	private Layout layout;
	private DataTable imputationsDataTable;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		actionPlan = __inject__(ActionPlanController.class).readBySystemIdentifier(Faces.getRequestParameter("actionplan"));
		
		imputationsDataTable = DataTable.build(DataTable.FIELD_LAZY,Boolean.TRUE,DataTable.FIELD_ELEMENT_CLASS,Imputation.class);
		
		((LazyDataModel<?>)imputationsDataTable.getValue()).setListener(new LazyDataModel.Listener.AbstractImpl() {
			@Override
			public void processFilter(FilterDto filter) {
				super.processFilter(filter);
				//filter.addField(Imputation.FIELD_ACTION_PLAN, actionPlan.getCode());
			}
		});
		/*
		imputationsDataTable.addHeaderToolbarLeftCommands(
				CommandButton.build(CommandButton.FIELD_VALUE,"Ajouter",CommandButton.ConfiguratorImpl.FIELD_COLLECTION,imputationsDataTable
						,CommandButton.FIELD_LISTENER,new AbstractCollection.AbstractActionListenerImpl(imputationsDataTable) {
					@Override
					protected void __showDialog__() {
						imputationsDataTable.getDialog().setHeader("Réalisation de l'opération 1");
						super.__showDialog__();
					}
				},CommandButton.ConfiguratorImpl.FIELD_COLLECTION_UPDATABLE,Boolean.FALSE)
			);
		*/
		imputationsDataTable.addColumnsAfterRowIndex(
				Column.build(Column.FIELD_FIELD_NAME,ActionPlanActivity.FIELD_ACTIVITY)
				,Column.build(Column.FIELD_FIELD_NAME,ActionPlanActivity.FIELD_AMOUNT_YEAR1_PAYMENT_CREDIT,Column.FIELD_HEADER_TEXT,(actionPlan.getYear()+0)+"",Column.FIELD_WIDTH,"150")
				,Column.build(Column.FIELD_FIELD_NAME,ActionPlanActivity.FIELD_AMOUNT_YEAR2_PAYMENT_CREDIT,Column.FIELD_HEADER_TEXT,(actionPlan.getYear()+1)+"",Column.FIELD_WIDTH,"150")
				,Column.build(Column.FIELD_FIELD_NAME,ActionPlanActivity.FIELD_AMOUNT_YEAR3_PAYMENT_CREDIT,Column.FIELD_HEADER_TEXT,(actionPlan.getYear()+2)+"",Column.FIELD_WIDTH,"150")
				,Column.build(Column.FIELD_FIELD_NAME,ActionPlanActivity.FIELD_AMOUNT_TOTAL_PAYMENT_CREDIT,Column.FIELD_HEADER_TEXT,"Total",Column.FIELD_WIDTH,"200")
				);
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G
				,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
					MapHelper.instantiate(Cell.FIELD_CONTROL,imputationsDataTable,Cell.FIELD_WIDTH,12)					
					));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Programmation financière de "+actionPlan;
	}
	
}
