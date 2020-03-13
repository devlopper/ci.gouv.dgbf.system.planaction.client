package ci.gouv.dgbf.system.planaction.client.controller.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Input;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.planaction.client.controller.entities.ActionPlan;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActionPlanEditPage extends AbstractEntityEditPageContainerManagedImpl<ActionPlan> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	protected Collection<String> __getFormArgumentsFieldInputsFieldsNames__(Class<?> klass) {
		return CollectionHelper.listOf(ActionPlan.FIELD_PRODUCER,ActionPlan.FIELD_YEAR);
	}
	
	@Override
	protected Object[] __getFormArguments__() {
		return ArrayUtils.addAll(super.__getFormArguments__(),new Object[] {Form.FIELD_LISTENER,new Form.Listener.AbstractImpl() {
			@Override
			public Object[] getInputArguments(Form form, String fieldName, Field field, Input annotation,Class<?> fieldType) {
				if(ActionPlan.FIELD_PRODUCER.equals(fieldName))
					return ArrayUtils.addAll(super.getInputArguments(form, fieldName, field, annotation, fieldType)
							, AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Réalisateur");
				else if(ActionPlan.FIELD_YEAR.equals(fieldName))
					return super.getInputArguments(form, fieldName, field, annotation, fieldType);
				return null;
			}
		}});
	}
}