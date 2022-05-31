package boundary.fxmlControllers;

import java.net.URL;
import java.util.ResourceBundle;

import entity.BuildItem;
import entity.Item;
import entity.Item.OrderItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class orderItemController implements Initializable {
	
	   @FXML
	    private Label amountLbl;

	    @FXML
	    private Label nameOrderLbl;

	    @FXML
	    private Label priceLbl;

	    @FXML
	    private Button viewBtn;
	    

	    int sale=40;
	    double price=200*sale/100;
	   
	    
	     
	    
	    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	Item orderItem;
	BuildItem buildItem;
	CustomerOrdersController customerOrdersController;

	public void setData(OrderItem orderItem, BuildItem buildItem, CustomerOrdersController customerOrdersController) {
		 this.orderItem=orderItem;
		 this.buildItem=buildItem;
		 this.customerOrdersController=customerOrdersController;
		 
		 if(orderItem!=null) {
		  nameOrderLbl.setText("x"+orderItem.getName());
		  amountLbl.setText("x"+orderItem.getAmount());
			 priceLbl.setText(orderItem.getPrice()+"");
		 }
		 else {
		  nameOrderLbl.setText("Custom Item");
		  amountLbl.setText("x"+buildItem.getAmount());
		  priceLbl.setText(buildItem.getPrice()+"");
		  viewBtn.setVisible(true);
		 }
		 
		 
			
		 
		 
		 
		 
		
		
		
	}

}
