package application;
	


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import java.util.Optional;


/**
 * 
 * @author Olgierdas Balcevicius
 *
 */
public class Main extends Application {
	
	//Controls
	private Dialog<Pair<String, String>> mainDialog;
	private ButtonType loginButtonType;
	private ChoiceBox<String> envChoiceBox;
	private ComboBox<String> userComboBox;;
	private GridPane mainGrid; 
	private Label envLabel;
	private Label userLabel;
	private Label passLabel;
	private PasswordField passField;
	private Node loginButton;
	
	//Environment types
	private static String[] envList = new String[] {"Production", "Testing", "Development"};
	
	//User Data
	private static String[][] prodUserList = new String[][] {{"Jan.Kowalski", "Robert.Jordan", "Maynard.Keenan", "Henry.Chinaski", "Greg.House"},
													 {"password123",  "qwerty", 	   "1111qwerty",    "pass1234", 	   "prozpassword"}};
	
	private static String[][] devUserList = new String[][] {{"Ricky.Gervais", "Holden.Ford", "Paul.Smith"},
													 {"helloworld",     "haslo098",    "mypass123"}};
	
	private static String[][] testUserList = new String[][] {{"Landsbergis", "Donskis.Leonidas", "Vinz", "Will.Emerson"},
													  {"slaptazodis", "mYpAsSWord", 	   "123456", "zxcasdqwe"}};
	
	
	/**
	 * Main method
	 */	
	@Override
	public void start(Stage primaryStage) {
		try {
			mainDialog = new Dialog<>();
			loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
			envChoiceBox = new ChoiceBox<>();
			userComboBox = new ComboBox<>();
			mainGrid = new GridPane();
			envLabel = new Label("Environment: ");
			userLabel = new Label("Username: ");
			passLabel = new Label("Password: ");
			passField = new PasswordField();
			
			envChoiceBox.setItems(FXCollections.observableArrayList(envList));
			envChoiceBox.setValue(envList[0]);
			
			
			userComboBox.setItems(FXCollections.observableArrayList(prodUserList[0]));
			userComboBox.setValue(prodUserList[0][0]);
			userComboBox.setEditable(true);
			
			mainDialog.setTitle("Login Window");
			mainDialog.setHeaderText("Logowanie do systemu STYLEman");
			mainDialog.getDialogPane().getButtonTypes().add(loginButtonType);
			mainDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
			
			loginButton = mainDialog.getDialogPane().lookupButton(loginButtonType);
			loginButton.setDisable(true);
		 
			envChoiceBox.valueProperty().addListener(
					(observable, oldVal, newVal) -> {						
						switch(newVal) {
						case "Development":	userComboBox.setItems(FXCollections.observableArrayList(devUserList[0])); 
											userComboBox.setValue(devUserList[0][0]); break;
						case "Testing": userComboBox.setItems(FXCollections.observableArrayList(testUserList[0]));
											userComboBox.setValue(testUserList[0][0]);break;
						case "Production": userComboBox.setItems(FXCollections.observableArrayList(prodUserList[0]));
											userComboBox.setValue(prodUserList[0][0]);}
						passField.clear();});
			userComboBox.valueProperty().addListener(
					(observable, oldVal, newVal) -> passField.clear());
			
			userComboBox.getEditor().textProperty().addListener(
					(observable, oldVal, newVal) -> { loginButton.setDisable(passField.getText().trim().isEmpty() ||
																			newVal.trim().isEmpty() ||
																			envChoiceBox.getValue() == null);
														if(newVal.trim().isEmpty()) passField.clear(); });
			
			passField.textProperty().addListener(
					(observable, oldVal, newval) -> {loginButton.setDisable( passField.getText().trim().isEmpty() ||
																			 userComboBox.getValue() == null || //.toString().trim().isEmpty() ||
																			 envChoiceBox.getValue() == null);} );
			
			
			mainGrid.add(envLabel, 0, 0); 
			mainGrid.add(envChoiceBox, 1, 0);
			mainGrid.add(userLabel, 0, 1); 
			mainGrid.add(userComboBox, 1, 1);
			mainGrid.add(passLabel, 0, 2); 
			mainGrid.add(passField, 1, 2);
			mainGrid.setHgap(15);
			mainGrid.setVgap(15);
			mainDialog.getDialogPane().setContent(mainGrid);
			
			
			mainDialog.setResultConverter(buttonPressed -> {
					if(buttonPressed == loginButtonType && checkPassword(envChoiceBox.getValue().toString(), 
																	 userComboBox.getEditor().textProperty().getValue().trim(), 
																	 passField.getText().trim()))
						return new Pair<>(envChoiceBox.getValue().toString(), userComboBox.getEditor().textProperty().getValue().trim());
					else
						return null; }			
			);
			
			Optional<Pair<String, String>> result = showAndWait();
			
			if(result.isPresent())
			{
				System.out.println("Welcome " + result.get().getValue());
			}
			else
			{
				System.out.println("LOGIN FAILED");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Checks whether user has an access to current environment and whether password is correct.
	 * @param environment
	 * @param userName
	 * @param passWord
	 * @return True if environment and password are correct.
	 */
	private boolean checkPassword(String environment, String userName, String passWord) {
		
		if(environment == "Production")
		{
			for(int i = 0; i < prodUserList[0].length; i++)
			{
				if(prodUserList[0][i].equals(userName))
					if(prodUserList[1][i].equals(passWord))
						return true;
			}
			
		}
		else if(environment == "Testing")
		{
			for(int i = 0; i < testUserList[0].length; i++)
			{
				if(testUserList[0][i].equals(userName))
					if(testUserList[1][i].equals(passWord))
						return true;
			}			
		}
		else if(environment == "Development")
		{
			for(int i = 0; i < devUserList[0].length; i++)
			{
				if(devUserList[0][i].equals(userName))
					if(devUserList[1][i].equals(passWord))
						return true;
			}			
		}
		return false;
	}
	
	public Optional<Pair<String, String>> showAndWait() {
		return mainDialog.showAndWait();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
