package ci.gouv.dgbf.system.planaction.client.controller.entities;

import java.io.Serializable;

import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringImpl;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Input;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractAmountPlanning extends AbstractDataIdentifiableSystemStringImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Input protected Short year;
	@Input protected Long amount;
	
	protected Producer producer;
	protected ActionPlan actionPlan;
	protected Activity activity;
	protected ActionPlanActivity actionPlanActivity;
	protected CostUnit costUnit;
	
	@Override
	public String toString() {
		return getYear()+":"+getAmount();
	}
	
	/**/
	
	public static final String FIELD_YEAR = "year";
	public static final String FIELD_AMOUNT = "amount";
	
	public static final String FIELD_PRODUCER = "producer";
	public static final String FIELD_ACTION_PLAN = "actionPlan";
	public static final String FIELD_ACTIVITY = "activity";
	public static final String FIELD_ACTION_PLAN_ACTIVITY = "actionPlanActivity";
	public static final String FIELD_COST_UNIT = "costUnit";
}