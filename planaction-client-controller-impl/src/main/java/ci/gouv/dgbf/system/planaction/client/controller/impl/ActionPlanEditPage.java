package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.InputText;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.planaction.client.controller.api.ActionPlanController;
import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlan;
import ci.gouv.dgbf.system.planaction.client.controller.entities.AdministrativeUnit;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActionPlanEditPage extends AbstractPageContainerManagedImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	private ActionPlan actionPlan;
	private Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		ActionPlan actionPlan = new ActionPlan();
		
		InputText code = InputText.build(InputText.ConfiguratorImpl.FIELD_OBJECT,actionPlan,InputText.ConfiguratorImpl.FIELD_FIELD_NAME,ActionPlan.FIELD_CODE);
		InputText name = InputText.build(InputText.ConfiguratorImpl.FIELD_OBJECT,actionPlan,InputText.ConfiguratorImpl.FIELD_FIELD_NAME,ActionPlan.FIELD_NAME);
		InputText year = InputText.build(InputText.ConfiguratorImpl.FIELD_OBJECT,actionPlan,InputText.ConfiguratorImpl.FIELD_FIELD_NAME,ActionPlan.FIELD_YEAR);
		AutoComplete administrativeUnit = AutoComplete.build(AutoComplete.ConfiguratorImpl.FIELD_OBJECT,actionPlan,AutoComplete.ConfiguratorImpl.FIELD_FIELD_NAME,ActionPlan.FIELD_ADMINISTRATIVE_UNIT
				,AutoComplete.FIELD_ENTITY_CLASS,AdministrativeUnit.class);
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.FIELD_NUMBER_OF_COLUMNS,2
				,Layout.FIELD_ROW_CELL_MODEL,Map.of(0,new Cell().setWidth(3),1,new Cell().setWidth(9))
				,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
					MapHelper.instantiate(Cell.FIELD_CONTROL,code.getOutputLabel()),MapHelper.instantiate(Cell.FIELD_CONTROL,code)
					,MapHelper.instantiate(Cell.FIELD_CONTROL,name.getOutputLabel()),MapHelper.instantiate(Cell.FIELD_CONTROL,name)
					,MapHelper.instantiate(Cell.FIELD_CONTROL,year.getOutputLabel()),MapHelper.instantiate(Cell.FIELD_CONTROL,year)
					,MapHelper.instantiate(Cell.FIELD_CONTROL,administrativeUnit.getOutputLabel()),MapHelper.instantiate(Cell.FIELD_CONTROL,administrativeUnit)
					
					,MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_COMMAND_BUTTON_ARGUMENTS,MapHelper.instantiate(CommandButton.ConfiguratorImpl.FIELD_OBJECT,this
							,CommandButton.ConfiguratorImpl.FIELD_METHOD_NAME,"save"))
				));
	}
	
	public void save() {
		__inject__(ActionPlanController.class).create(actionPlan);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		if(administrativeUnit == null)
			return "Création d'un plan d'action";
		return "Création du plan d'action de l'unité administrtative "+administrativeUnit;
	}
	
}
