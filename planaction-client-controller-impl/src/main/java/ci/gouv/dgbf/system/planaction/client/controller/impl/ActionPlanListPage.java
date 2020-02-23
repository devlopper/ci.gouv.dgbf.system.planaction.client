package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.object.Builder;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.Button;

import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlan;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActionPlanListPage extends AbstractPageContainerManagedImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	private DataTable dataTable;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		dataTable = Builder.build(DataTable.class,Map.of(DataTable.ConfiguratorImpl.FIELD_ENTIY_CLASS,ActionPlan.class,DataTable.FIELD_LAZY,Boolean.TRUE));
		
		dataTable.addColumnsAfterRowIndex(
				Column.build(Map.of(Column.FIELD_FIELD_NAME,ActionPlan.FIELD_CODE,Column.FIELD_WIDTH,"200"))
				,Column.build(Map.of(Column.FIELD_FIELD_NAME,ActionPlan.FIELD_ADMINISTRATIVE_UNIT))
				,Column.build(Map.of(Column.FIELD_FIELD_NAME,ActionPlan.FIELD_YEAR,Column.FIELD_WIDTH,"100"))
				,Column.build(Map.of(Column.FIELD_FIELD_NAME,ActionPlan.FIELD_VERSION,Column.FIELD_WIDTH,"100",Column.FIELD_VISIBLE,Boolean.FALSE))
				,Column.build(Map.of(Column.FIELD_FIELD_NAME,ActionPlan.FIELD_NAME))
				);
		
		Map<Object,Object> createOutcomeParameters = new LinkedHashMap<>();
		if(administrativeUnit != null)
			createOutcomeParameters.put("administrativeUnit",administrativeUnit.getIdentifier());
		if(year != null)
			createOutcomeParameters.put("year",year);
		dataTable.addHeaderToolbarLeftCommands(
				Button.build(Map.of(Button.FIELD_VALUE,"Créer",Button.FIELD_OUTCOME,"actionPlanEditView",Button.FIELD_PARAMETERS,createOutcomeParameters))
			);
		
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des plans d'actions";
	}
	
}
