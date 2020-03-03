package ci.gouv.dgbf.system.planaction.client.controller.entities;

import java.io.Serializable;

import org.cyk.utility.client.controller.data.AbstractDataIdentifiableSystemStringImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class AbstractAmounts extends AbstractDataIdentifiableSystemStringImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected Amounts amounts;
	
	public Integer getAmountYear1EntryAuthorization() {
		if(amounts == null || amounts.getYear1() == null)
			return null;
		return amounts.getYear1().getEntryAuthorization();
	}
	
	public Integer getAmountYear1PaymentCredit() {
		if(amounts == null || amounts.getYear1() == null)
			return null;
		return amounts.getYear1().getPaymentCredit();
	}
	
	public Integer getAmountYear2EntryAuthorization() {
		if(amounts == null || amounts.getYear2() == null)
			return null;
		return amounts.getYear2().getEntryAuthorization();
	}
	
	public Integer getAmountYear2PaymentCredit() {
		if(amounts == null || amounts.getYear2() == null)
			return null;
		return amounts.getYear2().getPaymentCredit();
	}
	
	public Integer getAmountYear3EntryAuthorization() {
		if(amounts == null || amounts.getYear3() == null)
			return null;
		return amounts.getYear3().getEntryAuthorization();
	}
	
	public Integer getAmountYear3PaymentCredit() {
		if(amounts == null || amounts.getYear3() == null)
			return null;
		return amounts.getYear3().getPaymentCredit();
	}
	
	public Integer getAmountTotalEntryAuthorization() {
		if(amounts == null || amounts.getTotal() == null)
			return null;
		return amounts.getTotal().getEntryAuthorization();
	}
	
	public Integer getAmountTotalPaymentCredit() {
		if(amounts == null || amounts.getTotal() == null)
			return null;
		return amounts.getTotal().getPaymentCredit();
	}
	
	/**/
	
	public static final String FIELD_AMOUNTS = "amounts";
	
	public static final String FIELD_AMOUNT_YEAR1_ENTRY_AUTHORIZATION = "amountYear1EntryAuthorization";
	public static final String FIELD_AMOUNT_YEAR1_PAYMENT_CREDIT = "amountYear1PaymentCredit";
	public static final String FIELD_AMOUNT_YEAR2_ENTRY_AUTHORIZATION = "amountYear2EntryAuthorization";
	public static final String FIELD_AMOUNT_YEAR2_PAYMENT_CREDIT = "amountYear2PaymentCredit";
	public static final String FIELD_AMOUNT_YEAR3_ENTRY_AUTHORIZATION = "amountYear3EntryAuthorization";
	public static final String FIELD_AMOUNT_YEAR3_PAYMENT_CREDIT = "amountYear3PaymentCredit";
	public static final String FIELD_AMOUNT_TOTAL_ENTRY_AUTHORIZATION = "amountTotalEntryAuthorization";
	public static final String FIELD_AMOUNT_TOTAL_PAYMENT_CREDIT = "amountTotalPaymentCredit";
}