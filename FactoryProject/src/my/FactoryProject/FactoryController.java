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

		
		public void buildOrder(String strItem, int qtyNeeded)
		{
			ItemDataBean item=new ItemDataBean();
			item.setItemName(strItem);
			item.setItemQuantity(qtyNeeded);
			orderList.add(item);
		}
		
		public boolean processOrder()
		{
			printCount();
			//RecipeLoaded();
			printInventoryStatus();
			boolean isProcessed=false;
	          for (int counter = 0; counter < orderList.size(); counter++) { 	
	        	  ItemDataBean item=new ItemDataBean();
	        	  item=orderList.get(counter);
	        	  System.out.println("Processing order item : "+item.getItemName()+" for quantity: "+item.getItemQuantity());
	        	  if(!getOrMakeItem(item.getItemName(),item.getItemQuantity()))
	        	  {
	        		  printInventoryStatus();
	        		  return false;
	        	  }
	          }
   			  System.out.println("order is procssed");
   			  printInventoryStatus();
	          return true;
		}
		
		public void printCount()
		{
			if(objRecipeMap!=null)
			{
				System.out.println("Recipes Loaded : "+objRecipeMap.size());
			}
			if(objInventory!=null)
			{
				System.out.println("Inventory Loaded : "+objInventory.size());
			}
		}
		
		public void printInventoryStatus()
		{
			for (Object inventoryItem : objInventory.keySet()) {
	    	String strItem=(String)inventoryItem;
	    	int qtyStatus=Integer.parseInt(objInventory.get(strItem).toString());
	    	System.out.println("Inventory Status:");
	    	System.out.println("Item: "+strItem+ " , qty" +qtyStatus );
		}
			
		}
		
		
		public boolean furnishOrderFromRecipe(String strOrderItem, int intOrderItemQuantity)
		{
			boolean isFurnished;
			RecipeDataBean objRecipe=objRecipeMap.get(strOrderItem);
			float timeNeeded=(float) 0.0;
			if (objRecipe!=null)
			{
				JSONObject itemsConsumed=objRecipe.getObjItemsConsumed();
					//System.out.println("time needed for Recipe"+objRecipe.getFltTime());
				//simple or complex items needed
				for (int counter=0;counter<intOrderItemQuantity;counter++) {
					float timeConsumed=(float)0.0;
					
					for (Object item : itemsConsumed.keySet()) {
				    	String strItem=(String)item;
				    	int qtyNeeded=Integer.parseInt(itemsConsumed.get(strItem).toString());
				    	if(getOrMakeItem(strItem,qtyNeeded))
				    	{
				    	//	System.out.println("Item "+strItem+" with qty "+qtyNeeded+" made.");
				    		
				    	}	
				    	else
				    	{
				    		System.out.println("Order "+strOrderItem+" can't be done");
				    		return false;
				    	}
					}
					System.out.println("Building recipe "+objRecipe.getStrRecipeKey()+" in "+objRecipe.getFltTime()+"s");
					updateInventory(objRecipe.getStrItemProduced(),objRecipe.getIntItemQuantityProduced());
					//System.out.println("Item added to inventory ->"+objRecipe.getStrItemProduced());
				}
				//objInventory.put(objRecipe.getStrItemProduced(),objRecipe.getIntItemQuantityProduced());
				isFurnished=true;
			}
			else
			{
				//System.out.println("This project can't be done");
				isFurnished=false;
			}
			//return true;
			
			return isFurnished;

		}
		
		public void updateInventory(String itemProduced, int qtyProduced)
		{
			int availableQuantity=0;
			if(objInventory.get(itemProduced)!=null)
			{
				availableQuantity=(int)objInventory.get(itemProduced);
				availableQuantity=availableQuantity+qtyProduced;
				//System.out.println("updating inventory for "+itemProduced+" to "+availableQuantity);
				objInventory.put(itemProduced, availableQuantity);
			}
			else
			{
				//System.out.println("creating inventory for "+itemProduced+" to "+qtyProduced);
				objInventory.put(itemProduced, qtyProduced);
			}
		}
		
		public boolean getOrMakeItem(String strItem, int qtyNeeded)
		{
			boolean isItemAccomplished=false;
			
			if (!isEnoughItems(strItem,qtyNeeded))
			{
				if(!furnishOrderFromRecipe(strItem,qtyNeeded))
				{
					System.out.println("not enough inventory available to do this work"+strItem);
					return false;
				}
			}
			return getItemFromInventroy(strItem,qtyNeeded);
		}
		
		public boolean isEnoughItems(String strItem,int qtyNeeded)
		{
			int availableQty=-1;
			if (objInventory.get(strItem)!=null && Integer.parseInt(objInventory.get(strItem).toString())>qtyNeeded)
			{
				return true;
			}
			else
			{
				System.out.println();
				return false;
			}
			
		}

		
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
	                objRecipe.setIntItemQuantityProduced(keyValue);
	                objRecipeMap.put(keyStr.toString(),objRecipe);
	            }
				
			}
		}

		
		

	    
}