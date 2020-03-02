package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;

import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.omnifaces.util.Faces;

import ci.gouv.dgbf.system.planaction.client.controller.api.AdministrativeUnitController;
import ci.gouv.dgbf.system.planaction.client.controller.entities.AdministrativeUnit;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractPageContainerManagedImpl extends org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	protected AdministrativeUnit administrativeUnit;
	protected Integer year;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		if(StringHelper.isNotBlank(Faces.getRequestParameter("administrativeunit")))
			administrativeUnit = __inject__(AdministrativeUnitController.class).readBySystemIdentifier(Faces.getRequestParameter(ParameterName.ENTITY_IDENTIFIER.getValue()));
		year = NumberHelper.getInteger(Faces.getRequestParameter("year"));
	}
	
}
