package ci.gouv.dgbf.system.planaction.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.planaction.client.controller.entities.Lessor;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class LessorControllerImpl extends AbstractControllerEntityImpl<Lessor> implements LessorController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
