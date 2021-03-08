package my.DataBean;

import java.io.Serializable;
import java.util.List;

import org.json.simple.JSONObject;

public class RecipeDataBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private String strRecipeKey;
	private String strTitle;
	private float fltTime;
	private JSONObject objItemsConsumed;
	private String strItemProduced;
	private int intItemQuantityProduced;
	public String getStrTitle() {
		return strTitle;
	}
	public void setStrTitle(String strTitle) {
		this.strTitle = strTitle;
	}
	public float getFltTime() {
		return fltTime;
	}
	public void setFltTime(float fltTime) {
		this.fltTime = fltTime;
	}

	public String getStrRecipeKey() {
		return strRecipeKey;
	}
	public void setStrRecipeKey(String strRecipeKey) {
		this.strRecipeKey = strRecipeKey;
	}

	public String getStrItemProduced() {
		return strItemProduced;
	}
	public void setStrItemProduced(String strItemProduced) {
		this.strItemProduced = strItemProduced;
	}

	public JSONObject getObjItemsConsumed() {
		return objItemsConsumed;
	}
	public void setObjItemsConsumed(JSONObject objItemsConsumed) {
		this.objItemsConsumed = objItemsConsumed;
	}
	public int getIntItemQuantityProduced() {
		return intItemQuantityProduced;
	}
	public void setIntItemQuantityProduced(int intItemQuantityProduced) {
		this.intItemQuantityProduced = intItemQuantityProduced;
	}
}
