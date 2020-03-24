package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.constant.ConstantEmpty;
import org.cyk.utility.__kernel__.data.structure.grid.Grid;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.__kernel__.user.interface_.message.MessageRenderer;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractCollection;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.planaction.client.controller.entities.EntryAuthorization;
import ci.gouv.dgbf.system.planaction.client.controller.entities.Imputation;
import ci.gouv.dgbf.system.planaction.client.controller.entities.PaymentCredit;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ImputationFinancialProgrammingPage extends AbstractPageContainerManagedImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Imputation imputation;
	private DataTable dataTable;
	private Layout layout;
	private CommandButton s;
	
	@Override
	protected void __listenPostConstruct__() {
		imputation = WebController.getInstance().getRequestParameterEntityBySystemIdentifier(Imputation.class,ParameterName.ENTITY_IDENTIFIER.getValue()
				,new Properties().setFields(Imputation.FIELD_IDENTIFIER+","+Imputation.FIELD_ACTION_PLAN+","+Imputation.FIELD_ACTIVITY
						+","+Imputation.FIELD_COST_UNIT+","+Imputation.FIELD_ENTRY_AUTHORIZATIONS));
		super.__listenPostConstruct__();
		if(imputation == null) {
			
		}else {
			Set<Object> columnsKeys = CollectionHelper.setOf(EntryAuthorization.FIELD_YEAR,EntryAuthorization.FIELD_AMOUNT);
			for(Integer index = 0 ; index < imputation.getActionPlan().getNumberOfYears(); index = index + 1)
				columnsKeys.add("paymentCredit"+index);		
			Grid grid = new Grid().setListener(new Grid.Listener.AbstractImpl() {
				@Override
				public Object listenGetColumnValueFromRowObject(Grid grid, Object rowObject, Object columnKey) {
					if(columnKey!=null && StringUtils.startsWith(columnKey.toString(), "paymentCredit")) {
						Integer index = NumberHelper.getInteger(StringUtils.substringAfter(columnKey.toString(), "paymentCredit"));
						PaymentCredit paymentCredit = CollectionHelper.getElementAt(((EntryAuthorization)rowObject).getPaymentCredits(), index);
						if(paymentCredit == null)
							return null;
						return paymentCredit.getAmount();
					}
					return super.listenGetColumnValueFromRowObject(grid, rowObject, columnKey);
				}
				
				@Override
				public Object formatNextColumnKey(Grid grid) {
					return "paymentCredit"+(grid.getNumberOfColumns() - 2);
				}
			}).addColumnsKeys(columnsKeys).setColumnKeyFormat("paymentCredit%s").addRows(imputation.getEntryAuthorizations());
			
			dataTable = DataTable.build(DataTable.FIELD_ELEMENT_CLASS,Grid.Row.class,DataTable.FIELD_DATA_GRID,grid,DataTable.FIELD_EDITABLE,Boolean.TRUE
					,DataTable.FIELD_LISTENER,new AbstractDataTable.Listener.AbstractImpl() {
						
				@Override
				public Map<Object, Object> listenAddColumnGetArguments(AbstractDataTable dataTable,String fieldName) {
					Map<Object,Object> map = super.listenAddColumnGetArguments(dataTable, fieldName);
					if(map == null)
						map = new HashMap<>();
					if(EntryAuthorization.FIELD_YEAR.equals(fieldName)) {
						map.put(Column.FIELD_HEADER_TEXT, "Année");
						map.remove(Column.FIELD_REMOVE_COMMAND_BUTTON);
					}else if(EntryAuthorization.FIELD_AMOUNT.equals(fieldName)) {
						map.put(Column.FIELD_HEADER_TEXT, "Montant");
						map.remove(Column.FIELD_REMOVE_COMMAND_BUTTON);
					}else if(StringUtils.startsWith(fieldName, "paymentCredit")) {
						Integer index = NumberHelper.getInteger(StringUtils.substringAfter(fieldName, "paymentCredit"));
						map.put(Column.FIELD_HEADER_TEXT, "C.P. "+(imputation.getActionPlan().getYear()+index));
						if(index <= 2)
							map.remove(Column.FIELD_REMOVE_COMMAND_BUTTON);
					}
					return map;
				}
				
				@Override
				public Object listenSave(AbstractCollection collection) {
					AbstractDataTable dataTable = (AbstractDataTable) collection;
					MessageRenderer.getInstance().render(dataTable.getDataGrid().toString());
					dataTable.getDataGrid().writeValuesToObjects();
					MessageRenderer.getInstance().render(dataTable.getDataGrid().getRows().stream().map(row -> row.getObject()).collect(Collectors.toList()).toString());
					return null;
				}
			
			});
			
			layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G
					,Layout.FIELD_NUMBER_OF_COLUMNS,1,Layout.FIELD_ROW_CELL_MODEL,Map.of(0,new Cell().setWidth(12))
					,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable)));
		}	
	}
		
	@Override
	protected String __getWindowTitleValue__() {
		return "Programmation financière"+(imputation == null ? ConstantEmpty.STRING : " de l'imputation "+imputation);
	}
}