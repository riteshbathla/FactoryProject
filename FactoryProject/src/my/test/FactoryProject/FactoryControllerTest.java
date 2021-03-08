package my.test.FactoryProject;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import my.DataBean.ItemDataBean;
import my.FactoryProject.FactoryController;

class FactoryControllerTest {
	
	private FactoryController factoryController;

	@BeforeEach
	void setUp() throws Exception {
		factoryController=new FactoryController();
		factoryController.readInventoryFile(".\\data\\inventory.json");
		factoryController.readRecipes(".\\data\\recipes.json");

	}

	@Test
	@DisplayName("Factory should not complete this order")
	void testPrcoessOrderMultipleItems() {
		
		factoryController.buildOrder("electric_engine",3);
		factoryController.buildOrder("electric_circuit",5);
		factoryController.buildOrder("electric_engine",3);

		factoryController.buildOrder("engine_block",3);
        assertEquals(false, factoryController.processOrder(),      
        "Factory should not be able to complete the Order");          

        
        

	}

	
	@Test
	@DisplayName("Factory should complete this order")
	void testPrcoessOrderSingleItem() {
		
		factoryController.buildOrder("electric_engine",3);
	        assertEquals(true, factoryController.processOrder(),      
        "Factory should be able to complete the Order");          

        
        

	}

}
