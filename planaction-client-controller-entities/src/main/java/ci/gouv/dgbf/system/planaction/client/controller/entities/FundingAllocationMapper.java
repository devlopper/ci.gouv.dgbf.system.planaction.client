package ci.gouv.dgbf.system.planaction.client.controller.entities;
import ci.gouv.dgbf.system.planaction.server.representation.entities.FundingAllocationDto;
import org.cyk.utility.__kernel__.mapping.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class FundingAllocationMapper extends AbstractMapperSourceDestinationImpl<FundingAllocation, FundingAllocationDto> {
	private static final long serialVersionUID = 1L;
    	
}