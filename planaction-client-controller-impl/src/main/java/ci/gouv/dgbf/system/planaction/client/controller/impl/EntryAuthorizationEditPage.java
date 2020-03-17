package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.planaction.client.controller.entities.EntryAuthorization;
import ci.gouv.dgbf.system.planaction.client.controller.entities.Imputation;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class EntryAuthorizationEditPage extends AbstractEntityEditPageContainerManagedImpl<EntryAuthorization> implements Serializable {
	private static final long serialVersionUID = 1L;

	private Imputation imputation;
	
	@Override
	protected void __listenPostConstruct__() {
		imputation = WebController.getInstance().getRequestParameterEntityAsParent(Imputation.class);
		super.__listenPostConstruct__();
		AutoComplete producerAutoComplete = form.getInput(AutoComplete.class,EntryAuthorization.FIELD_PRODUCER).enableAjaxItemSelect();
		
		AutoComplete actionPlanAutoComplete = form.getInput(AutoComplete.class,EntryAuthorization.FIELD_ACTION_PLAN).useQueryIdentifiersFiltersLike()
				.enableAjaxItemSelect().listenComplete(producerAutoComplete);
		
		AutoComplete activityAutoComplete = form.getInput(AutoComplete.class,EntryAuthorization.FIELD_ACTIVITY).useQueryIdentifiersFiltersLike()
				.enableAjaxItemSelect().listenComplete(actionPlanAutoComplete);
		
		form.getInput(AutoComplete.class,EntryAuthorization.FIELD_COST_UNIT).useQueryIdentifiersFiltersLike()
				.enableAjaxItemSelect().listenComplete(activityAutoComplete);
	}
	
	@Override
	protected Collection<String> __getFormArgumentsFieldInputsFieldsNames__(Class<?> klass) {
		return CollectionHelper.listOf(EntryAuthorization.FIELD_PRODUCER,EntryAuthorization.FIELD_ACTION_PLAN,EntryAuthorization.FIELD_ACTIVITY
				,EntryAuthorization.FIELD_COST_UNIT,EntryAuthorization.FIELD_YEAR,EntryAuthorization.FIELD_AMOUNT);
	}
	
}