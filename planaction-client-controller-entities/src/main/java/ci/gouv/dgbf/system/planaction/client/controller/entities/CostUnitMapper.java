package ci.gouv.dgbf.system.planaction.client.controller.entities;
import ci.gouv.dgbf.system.planaction.server.representation.entities.CostUnitDto;
import org.cyk.utility.__kernel__.mapping.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class CostUnitMapper extends AbstractMapperSourceDestinationImpl<CostUnit, CostUnitDto> {
	private static final long serialVersionUID = 1L;
    	
}