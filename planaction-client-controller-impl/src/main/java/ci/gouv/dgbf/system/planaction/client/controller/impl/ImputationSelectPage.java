package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.constant.ConstantEmpty;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.identifier.resource.PathAsFunctionParameter;
import org.cyk.utility.__kernel__.identifier.resource.QueryAsFunctionParameter;
import org.cyk.utility.__kernel__.identifier.resource.UniformResourceIdentifierAsFunctionParameter;
import org.cyk.utility.__kernel__.identifier.resource.UniformResourceIdentifierHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.QueryHelper;
import org.cyk.utility.__kernel__.persistence.query.filter.FilterDto;
import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.planaction.client.controller.api.ImputationController;
import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlan;
import ci.gouv.dgbf.system.planaction.client.controller.entities.Activity;
import ci.gouv.dgbf.system.planaction.client.controller.entities.CostUnit;
import ci.gouv.dgbf.system.planaction.client.controller.entities.Imputation;
import ci.gouv.dgbf.system.planaction.client.controller.entities.Producer;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ImputationSelectPage extends AbstractPageContainerManagedImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String actionIdentifier;
	private AutoComplete producerAutoComplete;
	private AutoComplete actionPlanAutoComplete;
	private AutoComplete activityAutoComplete;
	private AutoComplete costUnitAutoComplete;
	private Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		actionIdentifier = WebController.getInstance().getRequestParameter(ParameterName.ACTION_IDENTIFIER.getValue());
		super.__listenPostConstruct__();
		producerAutoComplete = AutoComplete.build(AutoComplete.FIELD_ENTITY_CLASS,Producer.class).enableAjaxItemSelect();
		actionPlanAutoComplete = AutoComplete.build(AutoComplete.FIELD_ENTITY_CLASS,ActionPlan.class).useQueryIdentifiersFiltersLike().enableAjaxItemSelect().listenComplete(producerAutoComplete);
		activityAutoComplete = AutoComplete.build(AutoComplete.FIELD_ENTITY_CLASS,Activity.class).useQueryIdentifiersFiltersLike().enableAjaxItemSelect().listenComplete(actionPlanAutoComplete);
		costUnitAutoComplete = AutoComplete.build(AutoComplete.FIELD_ENTITY_CLASS,CostUnit.class).useQueryIdentifiersFiltersLike().enableAjaxItemSelect().listenComplete(activityAutoComplete);
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G
				,Layout.FIELD_NUMBER_OF_COLUMNS,2,Layout.FIELD_ROW_CELL_MODEL,Map.of(0,new Cell().setWidth(2),1,new Cell().setWidth(10))
				,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
					MapHelper.instantiate(Cell.FIELD_CONTROL,producerAutoComplete.getOutputLabel()),MapHelper.instantiate(Cell.FIELD_CONTROL,producerAutoComplete)
					,MapHelper.instantiate(Cell.FIELD_CONTROL,actionPlanAutoComplete.getOutputLabel()),MapHelper.instantiate(Cell.FIELD_CONTROL,actionPlanAutoComplete)
					,MapHelper.instantiate(Cell.FIELD_CONTROL,activityAutoComplete.getOutputLabel()),MapHelper.instantiate(Cell.FIELD_CONTROL,activityAutoComplete)
					,MapHelper.instantiate(Cell.FIELD_CONTROL,costUnitAutoComplete.getOutputLabel()),MapHelper.instantiate(Cell.FIELD_CONTROL,costUnitAutoComplete)
					
					,MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_COMMAND_BUTTON_ARGUMENTS,MapHelper.instantiate(CommandButton.ConfiguratorImpl.FIELD_OBJECT,this
							,CommandButton.ConfiguratorImpl.FIELD_METHOD_NAME,"select"))
				));
	}
	
	public void select() {
		FilterDto filter = new FilterDto();
		if(actionPlanAutoComplete.getValue() != null)
			filter.addField(Imputation.FIELD_ACTION_PLAN, ((ActionPlan)actionPlanAutoComplete.getValue()).getCode());
		if(activityAutoComplete.getValue() != null)
			filter.addField(Imputation.FIELD_ACTIVITY, ((Activity)activityAutoComplete.getValue()).getCode());
		if(costUnitAutoComplete.getValue() != null)
			filter.addField(Imputation.FIELD_COST_UNIT, ((CostUnit)costUnitAutoComplete.getValue()).getCode());
		Imputation imputation = CollectionHelper.getFirst(__inject__(ImputationController.class).read(new Properties()
				.setQueryIdentifier(QueryHelper.getIdentifierReadByFiltersLike(Imputation.class)).setFilters(filter)));
		if(imputation == null) {
			throw new RuntimeException("Aucune imputation trouvée");
		}else {
			UniformResourceIdentifierAsFunctionParameter p = new UniformResourceIdentifierAsFunctionParameter();
			p.setRequest(__getRequest__());
			p.setPath(new PathAsFunctionParameter());
			p.getPath().setIdentifier("imputationFinancialProgrammingView");
			p.setQuery(new QueryAsFunctionParameter());
			p.getQuery().setValue("entityidentifier="+imputation.getIdentifier());
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect(UniformResourceIdentifierHelper.build(p));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
		
	@Override
	protected String __getWindowTitleValue__() {
		String suffix = ConstantEmpty.STRING;
		if(FINANCIAL_PROGRAMMING.equals(actionIdentifier))
			suffix = " afin d'élaborer la programmation financière";
		return "Choisir une imputation"+suffix;
	}
	
	/**/
	
	public static final String FINANCIAL_PROGRAMMING = "financialprogramming";
}