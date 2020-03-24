package ci.gouv.dgbf.system.planaction.client.controller.entities;

import java.io.Serializable;
import java.util.Collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class EntryAuthorization extends AbstractAmountPlanning implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Imputation imputation;
	private Collection<PaymentCredit> paymentCredits;
	private Long paymentCreditsAmountsCumulation;
	
	@Override
	public String toString() {
		if(imputation == null)
			return super.toString();
		return String.format(TO_STRING_FORMAT, imputation.toString(),year,amount);
	}
	
	private static final String TO_STRING_FORMAT = "%s %s %s";
	
	public static final String FIELD_IMPUTATION = "imputation";
}