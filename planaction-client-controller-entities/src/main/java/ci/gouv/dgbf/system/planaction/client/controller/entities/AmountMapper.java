package ci.gouv.dgbf.system.planaction.client.controller.entities;
import org.cyk.utility.__kernel__.mapping.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

import ci.gouv.dgbf.system.planaction.server.representation.entities.AmountDto;

@Mapper
public abstract class AmountMapper extends AbstractMapperSourceDestinationImpl<Amount, AmountDto> {
	private static final long serialVersionUID = 1L;
    	
}