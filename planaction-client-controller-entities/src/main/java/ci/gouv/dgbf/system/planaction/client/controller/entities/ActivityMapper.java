package ci.gouv.dgbf.system.planaction.client.controller.entities;
import ci.gouv.dgbf.system.planaction.server.representation.entities.ActivityDto;
import org.cyk.utility.__kernel__.mapping.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class ActivityMapper extends AbstractMapperSourceDestinationImpl<Activity, ActivityDto> {
	private static final long serialVersionUID = 1L;
    	
}