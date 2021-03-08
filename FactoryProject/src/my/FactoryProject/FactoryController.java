package my.FactoryProject;

import org.json.simple.JSONObject;


import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import my.DataBean.ItemDataBean;
import my.DataBean.RecipeDataBean;

import java.io.FileReader;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

	public class FactoryController 
	{
		private JSONObject objInventory=new JSONObject();
		private Map<String,RecipeDataBean> objRecipeMap=new HashMap<String,RecipeDataBean>();
		private List<ItemDataBean> orderList=new ArrayList<ItemDataBean>();

		/*
		 * This method takes inorder inputs to build the order
		 */
		
		public void buildOrder(String strItem, int qtyNeeded)
		{
			ItemDataBean item=new ItemDataBean();
			item.setItemName(strItem);
			item.setItemQuantity(qtyNeeded);
			orderList.add(item);
		}
		
		/*
		 * 
		 * This method will process the order request
		 */
		public boolean processOrder() 
		{
			printCount();
			printInventoryStatus();
			try {
		          for (int counter = 0; counter < orderList.size(); counter++) { 	
		        	  double timeConsumed=0.0;
		        	  ItemDataBean item=new ItemDataBean();
		        	  item=orderList.get(counter);
		        	  String orderItemName=item.getItemName();
		        	  int orderItemQuantity=item.getItemQuantity();
		        	  
		        	  double recipeTime;
		        	  if((recipeTime=makeRecipe(orderItemName,orderItemQuantity))>0)
		        	  {
		        		  timeConsumed=timeConsumed+recipeTime;
		        	  }
		        	  
		          }
				  System.out.println("order is procssed");
		          return true;				}
				catch(ItemNotMadeException e)
				{
					System.out.println("Exception message->"+e.getMessage());
					return false;
				}
				finally 
				{
		   			  printInventoryStatus();
//		   			  return false;
				}

		}
		
		/*
		 * This method will use recipe to build products, it will call itself recursively if it is comprised of items needing to build
		 */
		
		private double makeRecipe(String strOrderItem, int intOrderItemQuantity) throws ItemNotMadeException
		{
			RecipeDataBean objRecipe=objRecipeMap.get(strOrderItem);
			double totalRecipeTime=0.0;
			if (objRecipe!=null)
			{
				JSONObject itemsConsumed=objRecipe.getObjItemsConsumed();
				//simple or complex items needed
				for (int counter=0;counter<intOrderItemQuantity;counter++) {
					double itemRecipeTime=0.0;
					for (Object item : itemsConsumed.keySet()) {
						double innerRecipeTime=0.0;
				    	String strItem=(String)item;
				    	int qtyNeeded=Integer.parseInt(itemsConsumed.get(strItem).toString());
						if(isEnoughItems(strItem,qtyNeeded))
			        	  {

							getItemFromInventroy(strItem,qtyNeeded);
			        		  
			        	  }
			        	  else if((innerRecipeTime=makeRecipe(strItem,qtyNeeded))>0)
			        	  {
			        		  
			        		  itemRecipeTime=itemRecipeTime+innerRecipeTime;
			        		  getItemFromInventroy(strItem,qtyNeeded);
			        	  }
			        	  else
			        	  {
			        		  throw new ItemNotMadeException("Not enough Items available");
			        	  }
					}
					itemRecipeTime=itemRecipeTime+objRecipe.getFltTime();
					totalRecipeTime=totalRecipeTime+itemRecipeTime;
					System.out.println("Building recipe "+objRecipe.getStrRecipeKey()+" in "+objRecipe.getFltTime()+"s"+ ", ( "+itemRecipeTime+")");
					updateInventory(objRecipe.getStrItemProduced(),objRecipe.getIntItemQuantityProduced());
				}
			}
			return totalRecipeTime;

		}
		
		/*
		 * Utility method to update the inventory
		 */
		public void updateInventory(String itemProduced, int qtyProduced)
		{
			int availableQuantity=0;
			if(objInventory.get(itemProduced)!=null)
			{
				availableQuantity=(int)objInventory.get(itemProduced);
				availableQuantity=availableQuantity+qtyProduced;
				objInventory.put(itemProduced, availableQuantity);
			}
			else
			{
				objInventory.put(itemProduced, qtyProduced);
			}
		}
		
		/*
		 * Utility method to check if enough items are available as requested for the order
		 */
		
		public boolean isEnoughItems(String strItem,int qtyNeeded)
		{
			if (objInventory.get(strItem)!=null && Integer.parseInt(objInventory.get(strItem).toString())>qtyNeeded)
			{
				return true;
			}
			else
			{
				return false;
			}
			
		}


		/*
		 * This method will reduce inventory by items needed
		 */

		public boolean getItemFromInventroy(String strItem,int qtyNeeded)
		{
			int availableQty=-1;
			//System.out.println("came in getItemFromInventory for "+strItem+" : "+objInventory.get(strItem));
			if (objInventory.get(strItem)!=null)
			{
				availableQty=Integer.parseInt(objInventory.get(strItem).toString());
				availableQty=availableQty-qtyNeeded;
				//System.out.println("Available quatntity after giving inventory->"+availableQty);
				if(availableQty<0)
				{
					System.out.println("Not enough items avilable of "+strItem);
					return false;
				}
				objInventory.put(strItem.toString(), availableQty);
				
				return true;
			}
			else
			{
				//System.out.println("Item does not exist");
				return false;
			}
		}
		
		
		/*
		 * Utility method to read inventory JSON file
		 */

		public void readInventoryFile(String filePath)
	    {
	        JSONParser objParser = new JSONParser();
	    	try (Reader objReader = new FileReader(filePath)) {
            objInventory = (JSONObject) objParser.parse(objReader);
            
	    	} catch (IOException e) {
	            e.printStackTrace();
	        } 
	    	catch (ParseException e) {
	            e.printStackTrace();
	        }
	    	
	    }
		
		/*
		 * Utility method to read recipe JSON file
		 */

	    
	    public void readRecipes(String filePath)
	    {
	    	JSONParser objParser = new JSONParser();
	    	RecipeDataBean objRecipe;
	    	try (Reader objReader = new FileReader(filePath)) {
            JSONObject objJson = (JSONObject) objParser.parse(objReader);

            for (Object key : objJson.keySet()) {
            	objRecipe=new RecipeDataBean();
            	  objRecipe.setStrRecipeKey((String)key);
            	  String keyValue=(String)key;
            	  JSONObject recipeValues = (JSONObject) objJson.get(keyValue);
            	  updateRecipeValues(objRecipe,recipeValues);
            	  
            }
            
	    	} catch (IOException e) {
	            e.printStackTrace();
	        } 
	    	catch (ParseException e) {
	            e.printStackTrace();
	        }

	    }
	  
	    /*
		 * This will update RecipeDatBean with specific values
		 */

	    
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

	    /*
		 * This will put the RecipeDataBean in map keyed to recipe Name
		 */
		
		public void updateItemProduced(RecipeDataBean objRecipe, JSONObject itemProduced)
		{
			
			if(itemProduced!=null && itemProduced.size()==1)
			{
	            for (Object key : itemProduced.keySet()) {
	                //based on you key types
	                String keyStr = (String)key;
	                int keyValue = Integer.parseInt(itemProduced.get(keyStr).toString());
	                objRecipe.setStrItemProduced(keyStr.toString());
	                objRecipe.setIntItemQuantityProduced(keyValue);
	                objRecipeMap.put(keyStr.toString(),objRecipe);
	            }
				
			}
		}
		
	    /*
		 * Utility method to print the recipes and inventory count
		 */
		
		public void printCount()
		{
			if(objRecipeMap!=null)
			{
				System.out.println("\n\nRecipes Loaded : "+objRecipeMap.size());
			}
			if(objInventory!=null)
			{
				System.out.println("Inventory Loaded : "+objInventory.size());
			}
		}
		
	    /*
		 * Utility method to print inventory status when needed
		 */
		
		public void printInventoryStatus()
		{
	    	System.out.println("Inventory Status:");
			for (Object inventoryItem : objInventory.keySet()) {
	    	String strItem=(String)inventoryItem;
	    	int qtyStatus=Integer.parseInt(objInventory.get(strItem).toString());
	    	System.out.println(strItem+ " : " +qtyStatus );
		}
			
		}


		
		

	    
}