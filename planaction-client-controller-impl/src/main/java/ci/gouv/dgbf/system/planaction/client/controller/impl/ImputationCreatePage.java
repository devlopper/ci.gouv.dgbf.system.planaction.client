package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.identifier.resource.PathAsFunctionParameter;
import org.cyk.utility.__kernel__.identifier.resource.QueryAsFunctionParameter;
import org.cyk.utility.__kernel__.identifier.resource.UniformResourceIdentifierAsFunctionParameter;
import org.cyk.utility.__kernel__.identifier.resource.UniformResourceIdentifierHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.filter.FilterDto;
import org.cyk.utility.__kernel__.runnable.Runner;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.omnifaces.util.Faces;

import ci.gouv.dgbf.system.planaction.client.controller.api.ActionPlanController;
import ci.gouv.dgbf.system.planaction.client.controller.api.ActivityController;
import ci.gouv.dgbf.system.planaction.client.controller.api.ImputationController;
import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlan;
import ci.gouv.dgbf.system.planaction.client.controller.entities.Activity;
import ci.gouv.dgbf.system.planaction.client.controller.entities.CostUnit;
import ci.gouv.dgbf.system.planaction.client.controller.entities.Imputation;
import ci.gouv.dgbf.system.planaction.server.persistence.api.ActivityPersistence;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ImputationCreatePage extends AbstractPageContainerManagedImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private AutoComplete actionPlanAutoComplete;
	private AutoComplete activityAutoComplete;
	private AutoComplete costUnitAutoComplete;
	private Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		ActionPlan actionPlan = null;
		if(StringHelper.isNotBlank(Faces.getRequestParameter("actionplan")))
			actionPlan = __inject__(ActionPlanController.class).readBySystemIdentifier(Faces.getRequestParameter("actionplan"));
		
		Activity activity = null;
		if(StringHelper.isNotBlank(Faces.getRequestParameter("activity")))
			activity = __inject__(ActivityController.class).readBySystemIdentifier(Faces.getRequestParameter("activity"));
		
		actionPlanAutoComplete = AutoComplete.build(AutoComplete.FIELD_ENTITY_CLASS,ActionPlan.class,AutoComplete.FIELD_VALUE,actionPlan);
		
		activityAutoComplete = AutoComplete.build(AutoComplete.FIELD_ENTITY_CLASS,Activity.class
				,AutoComplete.FIELD_READ_QUERY_IDENTIFIER,ActivityPersistence.READ_BY_ADMINISTRATIVE_UNIT_CODES
				,AutoComplete.FIELD_COUNT_QUERY_IDENTIFIER,ActivityPersistence.COUNT_BY_ADMINISTRATIVE_UNITS_CODES
				,AutoComplete.FIELD_LISTENER,new AutoComplete.Listener.AbstractImpl() {
					public void listenComplete(AutoComplete autoComplete,Runner.Arguments arguments, FilterDto filter, String queryString) {
						ActionPlan actionPlan = (ActionPlan) actionPlanAutoComplete.getValue();
						if(actionPlan != null) {
							filter.addField("administrativeUnitsCodes", List.of(actionPlan.getAdministrativeUnit().getCode()));	
						}
						super.listenComplete(autoComplete,arguments,filter,queryString);
					};
				} ,AutoComplete.FIELD_VALUE,activity);
				
		costUnitAutoComplete = AutoComplete.build(AutoComplete.FIELD_ENTITY_CLASS,CostUnit.class,AutoComplete.FIELD_MULTIPLE,Boolean.TRUE);
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G
				,Layout.FIELD_NUMBER_OF_COLUMNS,2,Layout.FIELD_ROW_CELL_MODEL,Map.of(0,new Cell().setWidth(3),1,new Cell().setWidth(9))
				,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
					MapHelper.instantiate(Cell.FIELD_CONTROL,actionPlanAutoComplete.getOutputLabel()),MapHelper.instantiate(Cell.FIELD_CONTROL,actionPlanAutoComplete)
					,MapHelper.instantiate(Cell.FIELD_CONTROL,activityAutoComplete.getOutputLabel()),MapHelper.instantiate(Cell.FIELD_CONTROL,activityAutoComplete)
					,MapHelper.instantiate(Cell.FIELD_CONTROL,costUnitAutoComplete.getOutputLabel()),MapHelper.instantiate(Cell.FIELD_CONTROL,costUnitAutoComplete)
					
					,MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_COMMAND_BUTTON_ARGUMENTS,MapHelper.instantiate(CommandButton.ConfiguratorImpl.FIELD_OBJECT,this
							,CommandButton.ConfiguratorImpl.FIELD_METHOD_NAME,"record"))		
					));
	}
	
	public void record() {
		ActionPlan actionPlan = (ActionPlan) actionPlanAutoComplete.getValue();
		if(actionPlan == null)
			return;
		Activity activity = (Activity) activityAutoComplete.getValue();
		if(activity == null)
			return;
		@SuppressWarnings("unchecked")
		Collection<CostUnit> costUnits = (Collection<CostUnit>) costUnitAutoComplete.getValue();
		if(CollectionHelper.isEmpty(costUnits))
			return;
		__inject__(ImputationController.class).createMany(costUnits.stream().map(costUnit -> new Imputation().setActionPlan(actionPlan).setActivity(activity).setCostUnit(costUnit))
				.collect(Collectors.toList()));
		
		UniformResourceIdentifierAsFunctionParameter p = new UniformResourceIdentifierAsFunctionParameter();
		p.setRequest(__getRequest__());
		p.setPath(new PathAsFunctionParameter());
		p.getPath().setIdentifier("actionPlanReadView");
		p.setQuery(new QueryAsFunctionParameter());
		p.getQuery().setValue("entityidentifier="+actionPlan.getIdentifier());
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(UniformResourceIdentifierHelper.build(p));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Création d'imputations";
	}
	
}
