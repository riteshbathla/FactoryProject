package my.test.FactoryProject;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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
	void testFurnishOrderFromRecipe() {
		
		factoryController.buildOrder("electric_engine",3);
		factoryController.buildOrder("electric_circuit",5);
		factoryController.buildOrder("electric_engine",3);
		
        assertEquals(false, factoryController.processOrder(),      
        "Factory should not be able to build the Order");          

        
        

        /*assertEquals(false, factoryController.furnishOrderFromRecipe("steel_plate",8),      
        "Factory should not be able to finish the steel_plate order"); 
	*/
	}

}
