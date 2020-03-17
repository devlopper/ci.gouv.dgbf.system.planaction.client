package ci.gouv.dgbf.system.planaction.client.controller.entities;

import java.io.Serializable;

import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringImpl;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractFunding extends AbstractDataIdentifiableSystemStringImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected Imputation imputation;
	protected Short year;
	protected Integer entryAuthorization;
	protected Integer paymentCredit;
	
	@Override
	public String toString() {
		return imputation+" : "+year+" : "+entryAuthorization+" : "+paymentCredit;
	}
	
	/**/
	
	public static final String FIELD_IMPUTATION = "imputation";
	public static final String FIELD_YEAR = "year";
	public static final String FIELD_ENTRY_AUTHORIZATION = "entryAuthorization";
	public static final String FIELD_PAYMENT_CREDIT = "paymentCredit";
	
	public static final String FIELD_ACTION_PLAN = "actionPlan";
	public static final String FIELD_ACTIVITY = "activity";
}