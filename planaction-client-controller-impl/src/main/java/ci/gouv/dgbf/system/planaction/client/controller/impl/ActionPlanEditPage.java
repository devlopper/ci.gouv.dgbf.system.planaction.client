package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlan;
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
		layout = Layout.build(Map.of(Layout.FIELD_IDENTIFIER,"layoutTwoColumns",Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.FIELD_NUMBER_OF_COLUMNS,2
				,Layout.FIELD_ROW_CELL_MODEL,Map.of(0,new Cell().setWidth(3),1,new Cell().setWidth(9))
				,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
					MapHelper.instantiate(Cell.FIELD_IDENTIFIER,"code_label"),MapHelper.instantiate(Cell.FIELD_IDENTIFIER,"code_input")
					,MapHelper.instantiate(Cell.FIELD_IDENTIFIER,"administrative_unit_label"),MapHelper.instantiate(Cell.FIELD_IDENTIFIER,"administrative_unit_input")
					,MapHelper.instantiate(Cell.FIELD_IDENTIFIER,"year_label"),MapHelper.instantiate(Cell.FIELD_IDENTIFIER,"year_input")
					,MapHelper.instantiate(Cell.FIELD_IDENTIFIER,"version_label"),MapHelper.instantiate(Cell.FIELD_IDENTIFIER,"version_input")
					,MapHelper.instantiate(Cell.FIELD_IDENTIFIER,"name_label"),MapHelper.instantiate(Cell.FIELD_IDENTIFIER,"name_input")
					)));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		if(administrativeUnit == null)
			return "Création d'un plan d'action";
		return "Création du plan d'action de l'unité administrtative "+administrativeUnit;
	}
	
}
