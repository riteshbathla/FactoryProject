package my.testProject;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import my.DataBean.ItemListDataBean;
import my.DataBean.RecipeDataBean;

import java.io.FileReader;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

	public class FactoryController 
	{
		public JSONObject objInventory=new JSONObject();
		public Map<String,RecipeDataBean> objRecipeMap=new HashMap<String,RecipeDataBean>();
		public ItemListDataBean objOrder=new ItemListDataBean();
	    public static void main(String[] args) {
	    	
	    	System.out.println("Hello world");
	    	FactoryController fc=new FactoryController();
	        fc.readInventoryFile();
	        fc.readRecipes();
	        //jse.furnishOrderFromRecipe("electric_engine",3);
	        fc.furnishOrderFromRecipe("pipe",3);
	        fc.furnishOrderFromRecipe("steel_plate",8);
	      //  jse.furnishOrder("electric_circuit",5);
	      //  jse.furnishOrder("electric_engine",3);
}
	    public void readInventoryFile()
	    {
	        JSONParser objParser = new JSONParser();
	    	try (Reader objReader = new FileReader(".\\data\\inventory.json")) {
            objInventory = (JSONObject) objParser.parse(objReader);
            
	    	} catch (IOException e) {
	            e.printStackTrace();
	        } 
	    	catch (ParseException e) {
	            e.printStackTrace();
	        }
	    	
	    }
	    
	    public void readRecipes()
	    {
	    	JSONParser objParser = new JSONParser();
	    	RecipeDataBean objRecipe;
	    	try (Reader objReader = new FileReader(".\\data\\recipes.json")) {
            JSONObject objJson = (JSONObject) objParser.parse(objReader);

            for (Object key : objJson.keySet()) {
            	objRecipe=new RecipeDataBean();
            	  objRecipe.setStrRecipeKey((String)key);
            	  String keyValue=(String)key;
            	  JSONObject recipeValues = (JSONObject) objJson.get(keyValue);
            	  updateRecipeValues(objRecipe,recipeValues);
            	  
            }
            
/*            for (Map.Entry<String,RecipeDataBean> entry : objRecipeMap.entrySet())  
                System.out.println("Recipe Key = " + entry.getKey() + 
                                 ", Value = " + entry.getValue()); 
  */          
	    	} catch (IOException e) {
	            e.printStackTrace();
	        } 
	    	catch (ParseException e) {
	            e.printStackTrace();
	        }

	    }
	  
	
		public void updateRecipeValues(RecipeDataBean objRecipe, JSONObject recipeValues)
		{
			if(recipeValues!=null)
			{
				
				objRecipe.setStrTitle(recipeValues.get("title").toString());
				objRecipe.setFltTime(Float.parseFloat(recipeValues.get("time").toString()));
				objRecipe.setObjItemsConsumed((JSONObject)recipeValues.get("consumes"));
				updateItemProduced(objRecipe, (JSONObject)recipeValues.get("produces"));
			}
		}
		
		public void updateItemProduced(RecipeDataBean objRecipe, JSONObject itemProduced)
		{
			
			if(itemProduced!=null && itemProduced.size()==1)
			{
	            for (Object key : itemProduced.keySet()) {
	                //based on you key types
	                String keyStr = (String)key;
	                int keyValue = Integer.parseInt(itemProduced.get(keyStr).toString());
	                objRecipe.setStrItemProduced(keyStr.toString());
	                objRecipe.setIntItemQuantity(keyValue);
	                objRecipeMap.put(keyStr.toString(),objRecipe);
	            }
				
			}
		}
		
		public boolean furnishOrderFromRecipe(String strOrderItem, int intOrderItemQuantity)
		{
			System.out.println("Make "+strOrderItem+" with qty "+intOrderItemQuantity+" from recipe.");

			RecipeDataBean objRecipe=objRecipeMap.get(strOrderItem);
			System.out.println("Trying to make recipe from "+objRecipe);
			float timeNeeded=(float) 0.0;
			//System.out.println("Time needed in beginning ->"+timeNeeded);
			boolean isFurnishingPossible=false;
			if (objRecipe!=null)
			{
				JSONObject itemsConsumed=objRecipe.getObjItemsConsumed();
					//System.out.println("time needed for Recipe"+objRecipe.getFltTime());
				//simple or complex items needed
				for (int counter=0;counter<intOrderItemQuantity;counter++) {
					for (Object item : itemsConsumed.keySet()) {
				    	String strItem=(String)item;
				    	int qtyNeeded=Integer.parseInt(itemsConsumed.get(strItem).toString());
				    	System.out.println("For "+strOrderItem+", item needed->"+strItem+", qty needed->"+qtyNeeded);
				    	if(getOrMakeItem(strItem,qtyNeeded))
				    	{
				    		System.out.println("Item "+strItem+" with qty "+qtyNeeded+" made.");
				    	}
				    	else
				    	{
				    		System.out.println("Order "+strOrderItem+" can't be done");
				    		return false;
				    	}
					}
				}
				
				System.out.println("Order "+strOrderItem+"with qty "+ intOrderItemQuantity +" is done");
				return true;
			}
			else
			{
				System.out.println("This project can't be done");
				return false;
			}
			//return true;

		}
		
		public boolean getOrMakeItem(String strItem, int qtyNeeded)
		{
			boolean isItemAccomplished=false;
	    	if (getItemFromInventroy(strItem,qtyNeeded) )
			{
	    		isItemAccomplished=true;
			}
	    	else if(furnishOrderFromRecipe(strItem,qtyNeeded))
	    	{
	    		System.out.println("Try to make item");
	    		objInventory.put(strItem, qtyNeeded);
	    		return furnishOrderFromRecipe(strItem,qtyNeeded);
	    	}
	    	return isItemAccomplished;
		}
		
		public boolean getItemFromInventroy(String strItem,int qtyNeeded)
		{
			System.out.println("Item name->"+strItem);
			System.out.println("item quantity in JSON-->"+objInventory.get(strItem));
			int availableQty=-1;
			if (objInventory.get(strItem)!=null)
			{
				availableQty=Integer.parseInt(objInventory.get(strItem).toString());
				availableQty=availableQty-qtyNeeded;
				objInventory.put(strItem.toString(), availableQty);
				System.out.println("Available quatntity->"+objInventory.get(strItem).toString());
				if(availableQty<=0)
				{
					System.out.println("Not enough items avilable of "+strItem);
					return false;
				}
				
				
				return true;
			}
			else
			{
				System.out.println("Item does not exist");
				return false;
			}
		}
		
		

	    
}