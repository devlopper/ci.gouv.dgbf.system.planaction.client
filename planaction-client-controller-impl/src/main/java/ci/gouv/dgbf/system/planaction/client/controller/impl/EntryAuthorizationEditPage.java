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
		if(imputation == null) {
			AutoComplete producerAutoComplete = form.getInput(AutoComplete.class,EntryAuthorization.FIELD_PRODUCER).enableAjaxItemSelect();
			
			AutoComplete actionPlanAutoComplete = form.getInput(AutoComplete.class,EntryAuthorization.FIELD_ACTION_PLAN).useQueryIdentifiersFiltersLike()
					.enableAjaxItemSelect().listenComplete(producerAutoComplete);
			
			AutoComplete activityAutoComplete = form.getInput(AutoComplete.class,EntryAuthorization.FIELD_ACTIVITY).useQueryIdentifiersFiltersLike()
					.enableAjaxItemSelect().listenComplete(actionPlanAutoComplete);
			
			AutoComplete costUnitAutoComplete = form.getInput(AutoComplete.class,EntryAuthorization.FIELD_COST_UNIT);
			costUnitAutoComplete.useQueryIdentifiersFiltersLike().enableAjaxItemSelect().listenComplete(activityAutoComplete);
		}else {
			if(Action.CREATE.equals(action)) {
				EntryAuthorization entryAuthorization = (EntryAuthorization) form.getEntity();
				entryAuthorization.setImputation(imputation);
			}
		}
	}
	
	@Override
	protected Collection<String> __getFormArgumentsFieldInputsFieldsNames__() {
		List<String> list = CollectionHelper.listOf(EntryAuthorization.FIELD_YEAR,EntryAuthorization.FIELD_AMOUNT);
		if(imputation == null)
			list.addAll(0, CollectionHelper.listOf(EntryAuthorization.FIELD_PRODUCER,EntryAuthorization.FIELD_ACTION_PLAN,EntryAuthorization.FIELD_ACTIVITY
					,EntryAuthorization.FIELD_COST_UNIT));
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
				if(EntryAuthorization.FIELD_PRODUCER.equals(fieldName))
					return ArrayUtils.addAll(super.getInputArguments(form, fieldName, field, annotation, fieldType)
							, AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Réalisateur");
				return super.getInputArguments(form, fieldName, field, annotation, fieldType);
			}
		});
		return arguments;
	}
}