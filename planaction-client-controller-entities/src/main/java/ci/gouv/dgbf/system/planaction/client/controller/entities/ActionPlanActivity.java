package ci.gouv.dgbf.system.planaction.client.controller.entities;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class ActionPlanActivity extends AbstractAmounts implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ActionPlan actionPlan;
	private Activity activity;
	
	@Override
	public String toString() {
		return activity+" du "+actionPlan;
	}
	
	/**/
	
	public static final String FIELD_ACTION_PLAN = "actionPlan";
	public static final String FIELD_ACTIVITY = "activity";
	
}