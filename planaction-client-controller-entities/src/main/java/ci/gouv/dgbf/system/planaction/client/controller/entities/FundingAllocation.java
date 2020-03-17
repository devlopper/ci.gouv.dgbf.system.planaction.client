package ci.gouv.dgbf.system.planaction.client.controller.entities;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class FundingAllocation extends AbstractFunding implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private FundingSource fundingSource;
	private Lessor lessor;
	
}