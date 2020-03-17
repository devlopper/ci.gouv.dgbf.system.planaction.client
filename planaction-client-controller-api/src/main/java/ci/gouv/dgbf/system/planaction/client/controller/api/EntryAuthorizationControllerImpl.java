package ci.gouv.dgbf.system.planaction.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.planaction.client.controller.entities.EntryAuthorization;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class EntryAuthorizationControllerImpl extends AbstractControllerEntityImpl<EntryAuthorization> implements EntryAuthorizationController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
