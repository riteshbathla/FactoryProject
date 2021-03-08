package my.DataBean;
import java.io.Serializable;

public class ItemDataBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String strItemName;
	private int intItemQuantity;
	
	public String getStrItemName() {
		return strItemName;
	}
	public void setStrItemName(String strItemName) {
		this.strItemName = strItemName;
	}
	
	public int getIntItemQuantity() {
		return intItemQuantity;
	}
	public void setIntItemQuantity(int intItemQuantity) {
		this.intItemQuantity = intItemQuantity;
	}

	
}
