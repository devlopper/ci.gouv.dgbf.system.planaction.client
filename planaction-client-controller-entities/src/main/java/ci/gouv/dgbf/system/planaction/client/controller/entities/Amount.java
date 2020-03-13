package ci.gouv.dgbf.system.planaction.client.controller.entities;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class Amount implements Serializable {

	private Short year;
	private Integer entryAuthorization;
	private Integer paymentCredit;
	
	/**/
	
	public static final String FIELD_YEAR = "year";
	public static final String FIELD_ENTRY_AUTHORIZATION = "entryAuthorization";
	public static final String FIELD_PAYMENT_CREDIT = "paymentCredit";
	
}
