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
import ci.gouv.dgbf.system.planaction.client.controller.entities.PaymentCredit;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class PaymentCreditEditPage extends AbstractEntityEditPageContainerManagedImpl<PaymentCredit> implements Serializable {
	private static final long serialVersionUID = 1L;

	private EntryAuthorization entryAuthorization;
	
	@Override
	protected void __listenPostConstruct__() {
		entryAuthorization = WebController.getInstance().getRequestParameterEntityAsParent(EntryAuthorization.class);
		super.__listenPostConstruct__();	
		AutoComplete producerAutoComplete = form.getInput(AutoComplete.class,PaymentCredit.FIELD_PRODUCER).enableAjaxItemSelect();
		
		AutoComplete actionPlanAutoComplete = form.getInput(AutoComplete.class,PaymentCredit.FIELD_ACTION_PLAN).useQueryIdentifiersFiltersLike()
				.enableAjaxItemSelect().listenComplete(producerAutoComplete);
		
		AutoComplete activityAutoComplete = form.getInput(AutoComplete.class,PaymentCredit.FIELD_ACTIVITY).useQueryIdentifiersFiltersLike()
				.enableAjaxItemSelect().listenComplete(actionPlanAutoComplete);
		
		AutoComplete costUnitAutoComplete = form.getInput(AutoComplete.class,PaymentCredit.FIELD_COST_UNIT).useQueryIdentifiersFiltersLike()
				.enableAjaxItemSelect().listenComplete(activityAutoComplete);
		
		AutoComplete entryAuthorizationAutoComplete = form.getInput(AutoComplete.class,PaymentCredit.FIELD_ENTRY_AUTHORIZATION).useQueryIdentifiersFiltersLike()
			.enableAjaxItemSelect().listenComplete(costUnitAutoComplete);
		
		if(entryAuthorization != null) {
			if(entryAuthorization.getImputation()!=null) {
				if(entryAuthorization.getImputation().getActionPlan() != null) {
					producerAutoComplete.setValue(entryAuthorization.getImputation().getActionPlan().getProducer());	
				}
			}			
			actionPlanAutoComplete.setValue(entryAuthorization.getImputation().getActionPlan());
			activityAutoComplete.setValue(entryAuthorization.getImputation().getActivity());
			costUnitAutoComplete.setValue(entryAuthorization.getImputation().getCostUnit());
		}
		entryAuthorizationAutoComplete.setValue(entryAuthorization);
	}
	
	@Override
	protected Collection<String> __getFormArgumentsFieldInputsFieldsNames__(Class<?> klass) {
		return CollectionHelper.listOf(PaymentCredit.FIELD_PRODUCER,PaymentCredit.FIELD_ACTION_PLAN,PaymentCredit.FIELD_ACTIVITY,PaymentCredit.FIELD_COST_UNIT
				,PaymentCredit.FIELD_ENTRY_AUTHORIZATION,PaymentCredit.FIELD_YEAR,PaymentCredit.FIELD_AMOUNT);
	}
}