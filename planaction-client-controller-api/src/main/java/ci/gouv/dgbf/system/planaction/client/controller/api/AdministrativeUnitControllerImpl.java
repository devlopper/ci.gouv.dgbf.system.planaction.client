package ci.gouv.dgbf.system.planaction.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.planaction.client.controller.entities.AdministrativeUnit;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class AdministrativeUnitControllerImpl extends AbstractControllerEntityImpl<AdministrativeUnit> implements AdministrativeUnitController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
