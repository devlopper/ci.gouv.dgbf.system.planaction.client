package ci.gouv.dgbf.system.planaction.client.controller.entities;

import java.io.Serializable;

import org.cyk.utility.client.controller.data.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
public class ActionPlan extends AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private AdministrativeUnit administrativeUnit;
	private Short year;
	private Byte orderNumber;

	public static final String FIELD_ADMINISTRATIVE_UNIT = "administrativeUnit";
	public static final String FIELD_YEAR = "year";
	public static final String FIELD_ORDER_NUMBER = "orderNumber"; 
}