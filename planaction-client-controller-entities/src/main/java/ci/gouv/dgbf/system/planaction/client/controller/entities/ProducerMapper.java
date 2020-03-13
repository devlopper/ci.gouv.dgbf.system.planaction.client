package ci.gouv.dgbf.system.planaction.client.controller.entities;
import ci.gouv.dgbf.system.planaction.server.representation.entities.ProducerDto;
import org.cyk.utility.__kernel__.mapping.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class ProducerMapper extends AbstractMapperSourceDestinationImpl<Producer, ProducerDto> {
	private static final long serialVersionUID = 1L;
    	
}