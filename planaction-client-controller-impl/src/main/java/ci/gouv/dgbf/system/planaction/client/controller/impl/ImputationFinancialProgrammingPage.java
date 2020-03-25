package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.constant.ConstantEmpty;
import org.cyk.utility.__kernel__.data.structure.grid.Grid;
import org.cyk.utility.__kernel__.data.structure.grid.Grid.Row;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.__kernel__.throwable.RuntimeException;
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
				public Object getColumnValueFromRowObject(Grid grid, Object rowObject, Object columnKey) {
					if(grid == null || rowObject == null || columnKey == null)
						return null;
					EntryAuthorization entryAuthorization = (EntryAuthorization)rowObject;
					if(columnKey.toString().startsWith(PAYMENT_CREDIT)) {
						Byte paymentCreditYearIndex = getPaymentCreditColumnIndex(columnKey);
						PaymentCredit paymentCredit = ((EntryAuthorization)rowObject).getPaymentCreditByYearIndex(paymentCreditYearIndex);
						if(paymentCredit == null) {
							paymentCredit = new PaymentCredit();
							paymentCredit.setEntryAuthorization(entryAuthorization);
							paymentCredit.setYear((short) (entryAuthorization.getImputation().getActionPlan().getYear()+paymentCreditYearIndex));
							paymentCredit.setAmount(0l);
							entryAuthorization.getPaymentCredits(Boolean.TRUE).add(paymentCredit);
						}
						return paymentCredit.getAmount();
					}
					return super.getColumnValueFromRowObject(grid, rowObject, columnKey);
				}
				
				@Override
				public void setColumnValueToRowObject(Grid grid, Row row, Object columnKey, Object value) {
					if(grid == null || row == null || columnKey == null)
						return;
					if(EntryAuthorization.FIELD_YEAR.equals(columnKey) || EntryAuthorization.FIELD_AMOUNT.equals(columnKey))
						super.setColumnValueToRowObject(grid, row, columnKey, value);
					else {
						EntryAuthorization entryAuthorization = (EntryAuthorization)row.getObject();
						Byte paymentCreditYearIndex = getPaymentCreditColumnIndex(columnKey);
						PaymentCredit paymentCredit = entryAuthorization.getPaymentCreditByYearIndex(paymentCreditYearIndex);
						if(paymentCredit == null)
							throw new RuntimeException("No payment credit found at "+columnKey+" for "+entryAuthorization);						
						paymentCredit.setAmount((Long) value);												
					}					
				}
				
				@Override
				public Object formatNextColumnKey(Grid grid) {
					return PAYMENT_CREDIT+(grid.getNumberOfColumns() - 2);
				}
				
				@Override
				protected Object formatValue(Grid grid, Row row, Object columnKey, Object value) {
					if(value == null)
						return null;
					if(EntryAuthorization.FIELD_YEAR.equals(columnKey))
						return value.toString();
					return NumberFormat.getInstance(Locale.FRENCH).format(value);
				}
				
			}).addColumnsKeys(columnsKeys).setColumnKeyFormat(PAYMENT_CREDIT_FORMAT).addRows(imputation.getEntryAuthorizations());
			
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
					}else if(StringUtils.startsWith(fieldName, PAYMENT_CREDIT)) {
						Byte index = getPaymentCreditColumnIndex(fieldName);
						map.put(Column.FIELD_HEADER_TEXT, "C.P. "+(imputation.getActionPlan().getYear()+index));
						if(index <= 2)
							map.remove(Column.FIELD_REMOVE_COMMAND_BUTTON);
					}
					return map;
				}
				
				@Override
				public String listenGetStyleClassByRecordByColumn(Object record, Integer recordIndex,Column column, Integer columnIndex) {
					if(record == null || recordIndex == null || column == null || columnIndex == null)
						return null;
					if(EntryAuthorization.FIELD_YEAR.equals(column.getFieldName()))
						return "cyk-font-weight-bold";
					return "cyk-text-align-right";					
				}
				
				@Override
				public Object listenSave(AbstractCollection collection) {
					AbstractDataTable dataTable = (AbstractDataTable) collection;
					MessageRenderer.getInstance().render(dataTable.getDataGrid().toString());
					dataTable.getDataGrid().writeValuesToObjects();
					MessageRenderer.getInstance().render(dataTable.getDataGrid().getRows().stream()
							.map(row -> ((EntryAuthorization)row.getObject()).getPaymentCredits() ).collect(Collectors.toList()).toString());
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
		return "Programmation financière"+(imputation == null ? ConstantEmpty.STRING : " : "+imputation);
	}
	
	private static Byte getPaymentCreditColumnIndex(Object columnKey) {
		return NumberHelper.get(Byte.class,StringUtils.substringAfter(columnKey.toString(), PAYMENT_CREDIT));
	}
	
	/**/
	
	private static final String PAYMENT_CREDIT = "paymentCredit";
	private static final String PAYMENT_CREDIT_FORMAT = PAYMENT_CREDIT+"%s";
}