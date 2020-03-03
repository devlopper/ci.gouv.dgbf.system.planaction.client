package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;
import java.security.Principal;

import org.cyk.utility.__kernel__.icon.Icon;
import org.cyk.utility.client.controller.component.menu.MenuBuilder;
import org.cyk.utility.client.controller.component.menu.MenuItemBuilder;

@ci.gouv.dgbf.system.planaction.server.annotation.System
public class MenuBuilderMapInstantiatorImpl extends org.cyk.utility.client.controller.component.menu.AbstractMenuBuilderMapInstantiatorImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	protected void __instantiateSessionMenuBuilderItems__(Object key, MenuBuilder sessionMenuBuilder, Object request,Principal principal) {		
		
		sessionMenuBuilder.addItems(
			__inject__(MenuItemBuilder.class).setCommandableName("Plan d'action").setCommandableIcon(Icon.BUILDING)
			.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Liste").setCommandableNavigationIdentifier("actionPlanListView").setCommandableIcon(Icon.LIST))
			,__inject__(MenuItemBuilder.class).setCommandableName("Imputation").setCommandableIcon(Icon.LINK)
			.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Liste").setCommandableNavigationIdentifier("imputationListView").setCommandableIcon(Icon.LIST))
		);
	}
	
}
