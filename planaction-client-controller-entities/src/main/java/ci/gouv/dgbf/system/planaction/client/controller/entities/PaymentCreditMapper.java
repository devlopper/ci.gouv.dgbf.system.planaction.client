package ci.gouv.dgbf.system.planaction.client.controller.entities;
import ci.gouv.dgbf.system.planaction.server.representation.entities.PaymentCreditDto;
import org.cyk.utility.__kernel__.mapping.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class PaymentCreditMapper extends AbstractMapperSourceDestinationImpl<PaymentCredit, PaymentCreditDto> {
	private static final long serialVersionUID = 1L;
    	
}