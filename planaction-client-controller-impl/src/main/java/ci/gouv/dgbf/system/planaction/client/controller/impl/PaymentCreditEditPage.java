package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Input;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
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
		if(entryAuthorization == null) {
			AutoComplete producerAutoComplete = form.getInput(AutoComplete.class,PaymentCredit.FIELD_PRODUCER).enableAjaxItemSelect();
			
			AutoComplete actionPlanAutoComplete = form.getInput(AutoComplete.class,PaymentCredit.FIELD_ACTION_PLAN).useQueryIdentifiersFiltersLike()
					.enableAjaxItemSelect().listenComplete(producerAutoComplete);
			
			AutoComplete activityAutoComplete = form.getInput(AutoComplete.class,PaymentCredit.FIELD_ACTIVITY).useQueryIdentifiersFiltersLike()
					.enableAjaxItemSelect().listenComplete(actionPlanAutoComplete);
			
			AutoComplete costUnitAutoComplete = form.getInput(AutoComplete.class,PaymentCredit.FIELD_COST_UNIT).useQueryIdentifiersFiltersLike()
					.enableAjaxItemSelect().listenComplete(activityAutoComplete);
			
			AutoComplete entryAuthorizationAutoComplete = form.getInput(AutoComplete.class,PaymentCredit.FIELD_ENTRY_AUTHORIZATION);
			entryAuthorizationAutoComplete.useQueryIdentifiersFiltersLike().enableAjaxItemSelect().listenComplete(costUnitAutoComplete);
		}else {
			if(Action.CREATE.equals(action)) {
				PaymentCredit paymentCredit = (PaymentCredit) form.getEntity();
				paymentCredit.setEntryAuthorization(entryAuthorization);	
			}			
		}
		/*
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
		*/
	}
	
	@Override
	protected Collection<String> __getFormArgumentsFieldInputsFieldsNames__() {
		List<String> list = CollectionHelper.listOf(PaymentCredit.FIELD_YEAR,PaymentCredit.FIELD_AMOUNT);
		if(entryAuthorization == null)
			list.addAll(0, List.of(PaymentCredit.FIELD_PRODUCER,PaymentCredit.FIELD_ACTION_PLAN,PaymentCredit.FIELD_ACTIVITY,PaymentCredit.FIELD_COST_UNIT
					,PaymentCredit.FIELD_ENTRY_AUTHORIZATION));
		return list;
	}
	
	@Override
	protected Map<Object,Object> __getFormArguments__() {
		Map<Object,Object> arguments = super.__getFormArguments__();
		if(arguments == null)
			arguments = new HashMap<>();
		arguments.put(Form.FIELD_LISTENER, new Form.Listener.AbstractImpl() {
			@Override
			public Object[] getInputArguments(Form form, String fieldName, Field field, Input annotation,Class<?> fieldType) {
				if(PaymentCredit.FIELD_PRODUCER.equals(fieldName))
					return ArrayUtils.addAll(super.getInputArguments(form, fieldName, field, annotation, fieldType)
							, AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Réalisateur");
				return super.getInputArguments(form, fieldName, field, annotation, fieldType);
			}
		});
		return arguments;
	}
}