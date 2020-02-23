package ci.gouv.dgbf.system.planaction.client.deployment;

import java.io.Serializable;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;

import org.cyk.user.interface_.theme.web.jsf.primefaces.atlantis.dgbf.DesktopDefault;
import org.cyk.utility.client.deployment.AbstractServletContextListener;

import ci.gouv.dgbf.system.planaction.client.controller.impl.ApplicationScopeLifeCycleListener;

@WebListener
public class ServletContextListener extends AbstractServletContextListener implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public void __initialize__(ServletContext context) {
		DesktopDefault.DYNAMIC_MENU = Boolean.FALSE;
		DesktopDefault.IS_SHOW_USER_MENU = Boolean.FALSE;
		DesktopDefault.SYSTEM_LINK = "#";
		super.__initialize__(context);
		__inject__(ApplicationScopeLifeCycleListener.class).initialize(null);
	}
	
}
