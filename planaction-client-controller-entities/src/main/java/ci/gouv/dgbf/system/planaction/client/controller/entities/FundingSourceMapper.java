package ci.gouv.dgbf.system.planaction.client.controller.entities;
import ci.gouv.dgbf.system.planaction.server.representation.entities.FundingSourceDto;
import org.cyk.utility.__kernel__.mapping.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class FundingSourceMapper extends AbstractMapperSourceDestinationImpl<FundingSource, FundingSourceDto> {
	private static final long serialVersionUID = 1L;
    	
}