package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.filter.FilterDto;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.omnifaces.util.Faces;

import ci.gouv.dgbf.system.planaction.client.controller.api.ActionPlanActivityController;
import ci.gouv.dgbf.system.planaction.client.controller.api.ImputationController;
import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlanActivity;
import ci.gouv.dgbf.system.planaction.client.controller.entities.CostUnit;
import ci.gouv.dgbf.system.planaction.client.controller.entities.Imputation;
import ci.gouv.dgbf.system.planaction.server.persistence.api.CostUnitPersistence;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ImputationCreatePage extends AbstractPageContainerManagedImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ActionPlanActivity actionPlanActivity;
	//private DualListModel<CostUnit> costUnitsDualListModel;
	private DataTable costUnitsDataTable;
	private Layout layout;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		actionPlanActivity = __inject__(ActionPlanActivityController.class).readBySystemIdentifier(Faces.getRequestParameter("actionplanactivity"));
		/*Collection<CostUnit> selectableCostUnits = __inject__(CostUnitController.class).read(new Properties().setIsPageable(Boolean.FALSE)
				.setQueryIdentifier(CostUnitPersistence.READ_WHERE_IMPUTATION_DO_NOT_EXIST_BY_ACTION_PLAN_CODE_BY_ACTIVITY_CODE)
				.setFilters(new FilterDto()
						.addField(Imputation.FIELD_ACTION_PLAN, actionPlanActivity.getActionPlan().getCode())
						.addField(Imputation.FIELD_ACTIVITY, actionPlanActivity.getActivity().getCode())
						));
		*/
		costUnitsDataTable = DataTable.build(DataTable.FIELD_LAZY,Boolean.TRUE,DataTable.FIELD_ELEMENT_CLASS,CostUnit.class,DataTable.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE
				,DataTable.FIELD_SELECTION_MODE,"multiple");
		costUnitsDataTable.addColumnsAfterRowIndex(Column.build(Column.FIELD_FIELD_NAME,CostUnit.FIELD_CODE,Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE
				,Column.FIELD_WIDTH,"150")
				,Column.build(Column.FIELD_FIELD_NAME,CostUnit.FIELD_NAME,Column.FIELD_HEADER_TEXT,"Libellé",Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE));
		((LazyDataModel<CostUnit>)costUnitsDataTable.getValue()).setReadQueryIdentifier(CostUnitPersistence.READ_WHERE_IMPUTATION_DO_NOT_EXIST_BY_ACTION_PLAN_CODE_BY_ACTIVITY_CODE);
		((LazyDataModel<CostUnit>)costUnitsDataTable.getValue()).setCountQueryIdentifier(CostUnitPersistence.COUNT_WHERE_IMPUTATION_DO_NOT_EXIST_BY_ACTION_PLAN_CODE_BY_ACTIVITY_CODE);
		((LazyDataModel<CostUnit>)costUnitsDataTable.getValue()).setListener(new LazyDataModel.Listener.AbstractImpl() {
			@Override
			public void processFilter(FilterDto filter) {
				super.processFilter(filter);
				filter.addField(Imputation.FIELD_ACTION_PLAN, actionPlanActivity.getActionPlan().getCode())
				.addField(Imputation.FIELD_ACTIVITY, actionPlanActivity.getActivity().getCode());
			}
		});
		/*
		if(selectableCostUnits == null)
			selectableCostUnits = new ArrayList<>();
		costUnitsDualListModel = new DualListModel<CostUnit>((List<CostUnit>) selectableCostUnits, new ArrayList<>());
		*/
		//Layout is cached by action plan code
		layout = Layout.build(Layout.FIELD_IDENTIFIER,"layout_action_plan_activity_create_"+actionPlanActivity.getIdentifier(),Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G
				,Layout.FIELD_NUMBER_OF_COLUMNS,1,Layout.FIELD_ROW_CELL_MODEL,Map.of(0,new Cell().setWidth(12))
				,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
					MapHelper.instantiate(Cell.FIELD_CONTROL,costUnitsDataTable)
						
					,MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_COMMAND_BUTTON_ARGUMENTS,MapHelper.instantiate(CommandButton.ConfiguratorImpl.FIELD_OBJECT,this
							,CommandButton.ConfiguratorImpl.FIELD_METHOD_NAME,"record",CommandButton.FIELD_ICON,"fa fa-floppy-o"))		
					));
	}
	
	@SuppressWarnings("unchecked")
	public void record() {
		Collection<CostUnit> costUnits = (Collection<CostUnit>) costUnitsDataTable.getSelection();
		if(CollectionHelper.isEmpty(costUnits))
			return;
		__inject__(ImputationController.class).createMany(costUnits.stream().map(costUnit -> new Imputation().setActionPlan(actionPlanActivity.getActionPlan())
				.setActivity(actionPlanActivity.getActivity()).setCostUnit(costUnit)).collect(Collectors.toList()));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Ajout d'unité de cout(s) à l'activité "+actionPlanActivity.getActivity();
	}	
}