package my.test.FactoryProject;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
		

		
        assertEquals(true, factoryController.furnishOrderFromRecipe("electric_engine",3),      
        "Factory should be able to build the Pipe Order");          

        /*assertEquals(false, factoryController.furnishOrderFromRecipe("steel_plate",8),      
        "Factory should not be able to finish the steel_plate order"); 
	*/
	}

}
