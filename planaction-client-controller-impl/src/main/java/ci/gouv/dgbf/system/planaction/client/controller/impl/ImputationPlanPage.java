package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.planaction.client.controller.entities.Imputation;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ImputationPlanPage extends AbstractPageContainerManagedImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Imputation imputation;
	//private DataTable costUnitsDataTable;
	private Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		imputation = WebController.getInstance().getRequestParameterEntity(Imputation.class);
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G
				,Layout.FIELD_NUMBER_OF_COLUMNS,1,Layout.FIELD_ROW_CELL_MODEL,Map.of(0,new Cell().setWidth(12))
				,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
					MapHelper.instantiate(Cell.FIELD_CONTROL,imputation)
						
					,MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_COMMAND_BUTTON_ARGUMENTS,MapHelper.instantiate(CommandButton.ConfiguratorImpl.FIELD_OBJECT,this
							,CommandButton.ConfiguratorImpl.FIELD_METHOD_NAME,"record",CommandButton.FIELD_ICON,"fa fa-floppy-o"))		
					));
	}
	
	public void record() {
		
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Plannification des autorisations d'engagements de l'imputation "+imputation;
	}	
}