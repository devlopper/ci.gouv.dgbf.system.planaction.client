package ci.gouv.dgbf.system.planaction.client.controller.entities;
import ci.gouv.dgbf.system.planaction.server.representation.entities.ActionPlanDto;
import org.cyk.utility.__kernel__.mapping.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class ActionPlanMapper extends AbstractMapperSourceDestinationImpl<ActionPlan, ActionPlanDto> {
	private static final long serialVersionUID = 1L;
    	
}