package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.filter.FilterDto;
import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.omnifaces.util.Faces;
import org.primefaces.model.DualListModel;

import ci.gouv.dgbf.system.planaction.client.controller.api.ActionPlanActivityController;
import ci.gouv.dgbf.system.planaction.client.controller.api.ActionPlanController;
import ci.gouv.dgbf.system.planaction.client.controller.api.ActivityController;
import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlan;
import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlanActivity;
import ci.gouv.dgbf.system.planaction.client.controller.entities.Activity;
import ci.gouv.dgbf.system.planaction.server.persistence.api.ActivityPersistence;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActionPlanActivityCreatePage extends AbstractPageContainerManagedImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ActionPlan actionPlan;
	private DualListModel<Activity> activitiesDualListModel;
	private Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		actionPlan = __inject__(ActionPlanController.class).readBySystemIdentifier(Faces.getRequestParameter("actionplan"));
		Collection<Activity> selectableActivities = __inject__(ActivityController.class).read(new Properties().setIsPageable(Boolean.FALSE)
				.setQueryIdentifier(ActivityPersistence.READ_PLANABLE_BY_ADMINISTRATIVE_UNIT_CODE_BY_ACTION_PLAN_CODE)
				.setFilters(new FilterDto()
						.addField("actionPlanCode", actionPlan.getCode())
						.addField("administrativeUnitCode", actionPlan.getProducer().getCode())));
		if(selectableActivities == null)
			selectableActivities = new ArrayList<>();
		activitiesDualListModel = new DualListModel<Activity>((List<Activity>) selectableActivities, new ArrayList<>());
		
		//Layout is cached by action plan code
		layout = Layout.build(Layout.FIELD_IDENTIFIER,"layout_action_plan_activity_create_"+actionPlan.getCode(),Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G
				,Layout.FIELD_NUMBER_OF_COLUMNS,1,Layout.FIELD_ROW_CELL_MODEL,Map.of(0,new Cell().setWidth(12))
				,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
					MapHelper.instantiate(Cell.FIELD_CONTROL,activitiesDualListModel)
						
					,MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_COMMAND_BUTTON_ARGUMENTS,MapHelper.instantiate(CommandButton.ConfiguratorImpl.FIELD_OBJECT,this
							,CommandButton.ConfiguratorImpl.FIELD_METHOD_NAME,"record",CommandButton.FIELD_ICON,"fa fa-floppy-o"))		
					));
	}
	
	public void record() {
		Collection<Activity> activities = (Collection<Activity>) activitiesDualListModel.getTarget();
		if(CollectionHelper.isEmpty(activities))
			return;
		__inject__(ActionPlanActivityController.class).createMany(activities.stream().map(activity -> new ActionPlanActivity().setActionPlan(actionPlan).setActivity(activity))
				.collect(Collectors.toList()));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Ajout d'activité(s) au "+actionPlan.getName();
	}
	
}
