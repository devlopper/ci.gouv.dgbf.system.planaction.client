package ci.gouv.dgbf.system.planaction.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.planaction.client.controller.entities.PaymentCredit;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class PaymentCreditControllerImpl extends AbstractControllerEntityImpl<PaymentCredit> implements PaymentCreditController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
