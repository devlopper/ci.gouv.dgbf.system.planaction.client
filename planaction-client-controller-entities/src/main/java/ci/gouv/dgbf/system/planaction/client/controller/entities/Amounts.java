package ci.gouv.dgbf.system.planaction.client.controller.entities;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class Amounts implements Serializable {

	private Amount year1 = new Amount();
	private Amount year2 = new Amount();
	private Amount year3 = new Amount();
	private Amount total = new Amount();
	
	public static final String FIELD_YEAR_1 = "year1";
	public static final String FIELD_YEAR_2 = "year2";
	public static final String FIELD_YEAR_3 = "year3";
	public static final String FIELD_TOTAL = "total";
}
