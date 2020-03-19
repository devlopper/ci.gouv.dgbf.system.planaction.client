package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlan;
import ci.gouv.dgbf.system.planaction.client.controller.entities.EntryAuthorization;
import ci.gouv.dgbf.system.planaction.client.controller.entities.Imputation;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ImputationPlanPage extends AbstractPageContainerManagedImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Imputation imputation;
	private DataTable entryAuthorizationsDataTable;
	private DataTable paymentCreditsDataTable;
	private Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		imputation = WebController.getInstance().getRequestParameterEntity(Imputation.class);
		super.__listenPostConstruct__();
		entryAuthorizationsDataTable = DataTable.build(DataTable.FIELD_ELEMENT_CLASS,EntryAuthorization.class);
		entryAuthorizationsDataTable.useQueryIdentifiersFiltersLike();
		entryAuthorizationsDataTable.addColumnsAfterRowIndex(Column.build(Column.FIELD_FIELD_NAME
				,EntryAuthorization.FIELD_IMPUTATION+"."+Imputation.FIELD_ACTION_PLAN+"."+ActionPlan.FIELD_NAME,Column.FIELD_HEADER_TEXT,"Plan d'action")
				,Column.build(Column.FIELD_FIELD_NAME
						,EntryAuthorization.FIELD_IMPUTATION+"."+EntryAuthorization.FIELD_ACTIVITY,Column.FIELD_HEADER_TEXT,"Activité")
				,Column.build(Column.FIELD_FIELD_NAME
						,EntryAuthorization.FIELD_IMPUTATION+"."+Imputation.FIELD_COST_UNIT,Column.FIELD_HEADER_TEXT,"Unité de coût")
				,Column.build(Column.FIELD_FIELD_NAME,EntryAuthorization.FIELD_YEAR)
				,Column.build(Column.FIELD_FIELD_NAME,EntryAuthorization.FIELD_AMOUNT)
				);
		entryAuthorizationsDataTable.addRecordMenuItemByArgumentsOpenViewInDialogUpdate();
		entryAuthorizationsDataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();
		
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G
				,Layout.FIELD_NUMBER_OF_COLUMNS,1,Layout.FIELD_ROW_CELL_MODEL,Map.of(0,new Cell().setWidth(12))
				,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
					MapHelper.instantiate(Cell.FIELD_CONTROL,entryAuthorizationsDataTable)
					,MapHelper.instantiate(Cell.FIELD_CONTROL,paymentCreditsDataTable)							
					));
	}
		
	@Override
	protected String __getWindowTitleValue__() {
		return "Plannification des autorisations d'engagements et des échéances de paiements de l'imputation "+imputation;
	}	
}