package ci.gouv.dgbf.system.planaction.client.controller.entities;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class Imputation extends AbstractAmounts implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ActionPlan actionPlan;
	private Activity activity;
	private CostUnit costUnit;
	private List<ImputationFunding> fundings;
	
	private ActionPlanActivity actionPlanActivity;
	
	@Override
	public String toString() {
		return getActivity()+" "+getCostUnit();
	}
	
	/**/
	
	public static final String FIELD_ACTION_PLAN = "actionPlan";
	public static final String FIELD_ACTIVITY = "activity";
	public static final String FIELD_COST_UNIT = "costUnit";
	public static final String FIELD_FUNDINGS = "fundings";
	public static final String FIELD_ACTION_PLAN_ACTIVITY = "actionPlanActivity";
}