package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.filter.FilterDto;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.omnifaces.util.Faces;
import org.primefaces.model.SortOrder;

import ci.gouv.dgbf.system.planaction.client.controller.api.ActionPlanController;
import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlan;
import ci.gouv.dgbf.system.planaction.client.controller.entities.Activity;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActionPlanFinancialProgrammingPage extends AbstractPageContainerManagedImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	private ActionPlan actionPlan;
	private Layout layout;
	private DataTable activitiesDataTable;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		actionPlan = __inject__(ActionPlanController.class).readBySystemIdentifier(Faces.getRequestParameter("entityidentifier"));
		
		activitiesDataTable = DataTable.build(DataTable.FIELD_LAZY,Boolean.TRUE,DataTable.ConfiguratorImpl.FIELD_ENTIY_CLASS,Activity.class
				,DataTable.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE,DataTable.FIELD_SELECTION_MODE,"multiple"
				,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL,new LazyDataModel(actionPlan));
		
		System.out.println("ActionPlanFinancialProgrammingPage.__listenPostConstruct__() : "+activitiesDataTable.getValue());
		
		activitiesDataTable.addColumnsAfterRowIndex(Column.build(Column.FIELD_FIELD_NAME,"codeAndName",Column.FIELD_FILTER_BY,"activity"
				,Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE));
		
		layout = Layout.build(Layout.FIELD_IDENTIFIER,"layout",Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G
				,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
					MapHelper.instantiate(Cell.FIELD_CONTROL,activitiesDataTable,Cell.FIELD_WIDTH,12)
					
					));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Programmation financière : "+actionPlan;
	}
	
	/**/
	
	public static class LazyDataModel extends org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel<Activity> implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private ActionPlan actionPlan;
		
		public LazyDataModel(ActionPlan actionPlan) {
			super(Activity.class);
			this.actionPlan = actionPlan;
			System.out.println("ActionPlanFinancialProgrammingPage.LazyDataModel.LazyDataModel() CONS : "+this);
			System.out.println("ActionPlanFinancialProgrammingPage.LazyDataModel.LazyDataModel() CONS : "+actionPlan.getAdministrativeUnit().getCode());
		}
		
		@Override
		protected FilterDto __instantiateFilter__(int first, int pageSize, String sortField, SortOrder sortOrder,Map<String, Object> filters) {
			FilterDto filter = super.__instantiateFilter__(first, pageSize, sortField, sortOrder, filters);
			if(actionPlan != null && actionPlan.getAdministrativeUnit()!=null)
				filter.addField(Activity.FIELD_ADMINISTRATIVE_UNIT, actionPlan.getAdministrativeUnit().getCode());
			System.out.println("ActionPlanFinancialProgrammingPage.LazyDataModel.__instantiateFilter__() : "+this);
			System.out.println("ActionPlanFinancialProgrammingPage.LazyDataModel.__instantiateFilter__() : "+actionPlan.getAdministrativeUnit().getCode());
			return filter;
		}
		
	}
}
