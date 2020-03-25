package ci.gouv.dgbf.system.planaction.client.controller.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.cyk.utility.__kernel__.collection.CollectionHelper;

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
	
	public Collection<PaymentCredit> getPaymentCredits(Boolean injectIfNull) {
		if(paymentCredits == null && Boolean.TRUE.equals(injectIfNull))
			paymentCredits = new ArrayList<>();
		return paymentCredits;
	}
	
	public PaymentCredit getPaymentCreditByYear(Short year) {
		if(CollectionHelper.isEmpty(paymentCredits) || year == null)
			return null;
		for(PaymentCredit paymentCredit : paymentCredits)
			if(paymentCredit.getYear() != null && paymentCredit.getYear().equals(year))
				return paymentCredit;
		return null;
	}
	
	public PaymentCredit getPaymentCreditByYearIndex(Byte yearIndex) {
		if(yearIndex == null || imputation == null)
			return null;
		return getPaymentCreditByYear((short) (imputation.getActionPlan().getYear()+yearIndex));
	}
	
	@Override
	public String toString() {
		if(imputation == null)
			return super.toString();
		return String.format(TO_STRING_FORMAT, imputation.toString(),year,amount);
	}
	
	private static final String TO_STRING_FORMAT = "%s %s %s";
	
	public static final String FIELD_IMPUTATION = "imputation";
}