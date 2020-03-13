package ci.gouv.dgbf.system.planaction.client.controller.entities;

import java.io.Serializable;

import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
public class ActionPlan extends AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Input private Producer producer;
	@Input private Short year;
	private Byte numberOfYears;
	private Byte orderNumber;
	private Amounts amounts;

	@Override
	public String toString() {
		return getCode()+" "+getName();
	}
	
	public static final String FIELD_PRODUCER = "producer";
	public static final String FIELD_YEAR = "year";
	public static final String FIELD_ORDER_NUMBER = "orderNumber"; 
}