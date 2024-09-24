package main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Cart;
import model.Product;
import model.ProductDetail;
import model.Transaction;
import model.User;
import util.DatabaseManager;
import validation.CartValidation;
import validation.RegisterValidation;
import validation.TransactionValidation;
import validation.ViewProduct;


public class Main extends Application {

	private Scene sceneLogin, sceneRegister, sceneHome, sceneHomeAdmin, sceneHomeDetail, sceneHomeAdminDetail, sceneCart, sceneCartDetail, sceneHistory, sceneManageProduct, sceneManageProduct1, sceneInputProduct, sceneUpdateProduct;
	private Stage window;
	
//	layout
	private BorderPane rootLogin, rootRegister, rootMenu, rootHome, rootHomeAdmin, rootHomeAdminDetail, rootHomeDetail, rootCart, rootCartDetail, rootHistory, rootManageProduct, rootManageProduct1, rootInputProduct, rootUpdateProduct;
	private FlowPane fp, fp1, fp2, fp3, fp4, fp5, fp6, fp7; //user
	private GridPane gp, gp1;
	
//	login & register
	private User loggedInUser;

	private Label title, title1, username, username1, email, password, password1, passwordConfirm, phoneNumber, address, gender, infoCb, textAccount, textAccount1;
	private TextField usernameTf, usernameTf1, emailTf;
	private PasswordField passwordTf, passwordTf1, passwordConfirmTf;
	private TextField phoneNumberTf;
	private TextArea addressTa;
	private RadioButton male, female;
	private ToggleGroup genderToggleGroup;
	private CheckBox termCb;
	private Hyperlink registerLogin, registerLogin1;
	private Button loginRegister, loginRegister1;
	
//	navigation
	private MenuBar menuBar;
	private Menu home, cart, account, manageProduct;
	private MenuItem homePage, myCart, purchaseHistory, logOut, manageProductItem;
	
//	home & cart 
	private Label productName, productDetail, productPrice, oncePrice, totalInfo, totalPrice, quantity, welcomeMessage, welcomeMessage1, totalAllInfo, totalAllPrice, orderInfo, usernameValue, phoneNumberValue, addressValue, transactionId, transactionValue;
	private ListView<Product> listProduct, listProductDetail;
	private ListView<Cart> listTransactionDetail;
	private ListView<Cart> listCart, listCartDetail;
	private ListView<Transaction> listTransaction;

	private Spinner<Integer> spinQty;
	private SpinnerValueFactory<Integer> valueFactory;
	private Button cartBt, updateCartBt, removeCartBt, makePurchaseBt, makePurchaseBt1;
	
	ObservableList<Product> products = FXCollections.observableArrayList();
	ObservableList<Cart> carts = FXCollections.observableArrayList();

//  homeAdmin 
	private Label titleAdmin, welcomeMessageAdmin, welcomeSelectProduct, titleAdmin1, welcomeMessageAdmin1, welcomeSelectProduct1, productPriceAdmin;
	private ListView<Product> listProductAdmin, listProductAdmin1;
	
//	manageProduct
	private Label titleManage, welcomeMessageManage, welcomeSelectManage;
	private ListView<Product> listManageProduct;
	private Button addProductBt;
	
//	manageProduct2
	private Label titleManage1, welcomeMessageManage1, welcomeSelectManage1, productPriceManage;
	private ListView<Product> listManageProduct1;
	private Button addProductBt1, updateProductBt, removeProductBt;

//	addProduct
	private Label titleAdd, welcomeMessageAdd, welcomeSelectAdd, inputProductName, inputProductPrice, inputProductDescription;
	private TextField productNameTf, productPriceTf;
	private TextArea productDescriptionTa;
	private ListView<Product> listInputProduct;
	private Button addProductBt2, backBt;
	
//	updateProduct
	private Label titleUpdate, productNameUpdate, productDescritionUpdate, productPriceUpdate, updateProduct;
	private TextField updateProductTf;
	private ListView<Product> listUpdateProduct;
	private Button updateProductBt1, backBt1;
	
//  validation
	RegisterValidation registerValidation = new RegisterValidation();
	CartValidation cartValidation = new CartValidation();
	TransactionValidation transactionValidation = new TransactionValidation();
	ViewProduct viewProduct = new ViewProduct();
	
	public void loginInitiate() {

		rootLogin = new BorderPane();
		fp1 = new FlowPane();
		gp1 = new GridPane();
		gp1.setAlignment(Pos.CENTER);
		rootLogin.setCenter(gp1);
		
		title1 = new Label("Login");
		title1.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		username1 = new Label("Username : ");
		password1 = new Label("Password : ");
		usernameTf1 = new TextField();
		passwordTf1 = new PasswordField();
		usernameTf1.setMaxWidth(220);
		passwordTf1.setMaxWidth(220);
		textAccount1 = new Label("Don't have an account yes? ");
		registerLogin1 = new Hyperlink("register here");
		loginRegister1 = new Button("Login");
		loginRegister1.setMaxWidth(100);
		
		fp1.getChildren().setAll(textAccount1, registerLogin1);
		fp1.setMaxWidth(250);
		gp1.add(title1, 1, 0);
		gp1.add(username1, 0, 1);
		gp1.add(usernameTf1, 1, 1);
		gp1.add(password1, 0, 2);
		gp1.add(passwordTf1, 1, 2);
		gp1.add(fp1, 1, 3);
		gp1.add(loginRegister1, 1, 4);
		gp1.setVgap(8);
		
		registerLogin1.setOnAction(e->{
			window.setScene(sceneRegister);
		});
		
		loginRegister1.setOnAction(e -> {
		    String enteredUsername = usernameTf1.getText();
		    String enteredPassword = passwordTf1.getText();

		    if (!enteredUsername.isEmpty() && !enteredPassword.isEmpty()) {
		        String query = "SELECT * FROM user WHERE username = ? AND password = ?";
		        DatabaseManager dbManager = DatabaseManager.getInstance();

		        try {
		            PreparedStatement preparedStatement = dbManager.getConnection().prepareStatement(query);
		            preparedStatement.setString(1, enteredUsername);
		            preparedStatement.setString(2, enteredPassword);

		            ResultSet resultSet = preparedStatement.executeQuery();

		            if (resultSet.next()) {
		                System.out.println("Login successful");
		                String userID = resultSet.getString("userID");
		                String username = resultSet.getString("username");
		                String password = resultSet.getString("password");
		                String role = resultSet.getString("role");
		                String address = resultSet.getString("address");
		                String phoneNum = resultSet.getString("phone_num");
		                String gender = resultSet.getString("gender");

		                loggedInUser = new User(userID, username, password, role, address, phoneNum, gender);

		                if ("Admin".equals(role)) {
		                    System.out.println("admin login");
		                    System.out.println(loggedInUser.getUserID());
		                    System.out.println(loggedInUser.getUsername());
		                    homeAdminInitiate();
		                    window.setScene(sceneHomeAdmin);
		                } else if ("Customer".equals(role)) {
		                    System.out.println("customer login");
		                    System.out.println(loggedInUser.getUserID());
		                    System.out.println(loggedInUser.getUsername());
		                    homeInitiate();
		                    window.setScene(sceneHome);
		                }
		            } else {
		                Alert invalidLogin = new Alert(AlertType.INFORMATION);
		                invalidLogin.setTitle("Fail Login");
		                invalidLogin.setHeaderText("Invalid username or password!");
		                invalidLogin.showAndWait();
		            }
		        } catch (SQLException ex) {
		            ex.printStackTrace();
		        }
		    } else {
		        Alert invalidLogin = new Alert(AlertType.INFORMATION);
		        invalidLogin.setTitle("Fail Login");
		        invalidLogin.setHeaderText("Please fill the username and password!");
		        invalidLogin.showAndWait();
		    }
		});

	}

	public void registerInitiate() {
		rootRegister = new BorderPane();
		fp = new FlowPane();
		fp2 = new FlowPane();
		fp3 = new FlowPane();
		gp = new GridPane();
		gp.setAlignment(Pos.CENTER);
		rootRegister.setCenter(gp);
		
		title = new Label("Register");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		username = new Label("Username : ");
		email = new Label("Email : ");
		password = new Label("Password : ");
		passwordConfirm = new Label("Confirm password : ");
		phoneNumber = new Label("Phone number : ");
		address = new Label("Address : ");
		gender = new Label("Gender : ");
		infoCb = new Label("i agree to all terms and condition");
		textAccount = new Label("Have an account?");
		registerLogin = new Hyperlink("login here");
		usernameTf = new TextField();
		usernameTf.setPromptText("input username..");
		emailTf = new TextField();
		emailTf.setPromptText("input email..");
		passwordTf = new PasswordField();
		passwordTf.setPromptText("input password..");
		passwordConfirmTf = new PasswordField();
		passwordConfirmTf.setPromptText("input confirm password..");
		phoneNumberTf = new TextField();
		phoneNumberTf.setPromptText("input phone number..");
		addressTa = new TextArea();
		addressTa.setPromptText("input address..");
		addressTa.setMaxWidth(250);
		male = new RadioButton("Male");
		female = new RadioButton("Female");
		termCb = new CheckBox();
		loginRegister = new Button("Register");
		
		fp.getChildren().setAll(male, female);
		fp.setMaxWidth(250);
		fp2.getChildren().setAll(termCb, infoCb);
		fp2.setMaxWidth(250);
		fp3.getChildren().setAll(textAccount, registerLogin);
		fp3.setMaxWidth(250);
		gp.add(title, 1, 0);
		gp.add(username, 0, 1);
		gp.add(usernameTf, 1, 1);
		gp.add(email, 0, 2);
		gp.add(emailTf, 1, 2);
		gp.add(password, 0, 3);
		gp.add(passwordTf, 1, 3);
		gp.add(passwordConfirm, 0, 4);
		gp.add(passwordConfirmTf, 1, 4);
		gp.add(phoneNumber, 0, 5);
		gp.add(phoneNumberTf, 1, 5);
		gp.add(address, 0, 6);
		gp.add(addressTa, 1, 6);
		gp.add(gender, 0, 7);
		gp.add(fp, 1, 7);
		gp.add(fp2, 1, 8);
		gp.add(fp3, 1, 9);
		gp.add(loginRegister, 1, 10);
		gp.setVgap(4);
		fp.setHgap(8);
		fp2.setHgap(8);

		
		genderToggleGroup = new ToggleGroup();
		male.setToggleGroup(genderToggleGroup);
		female.setToggleGroup(genderToggleGroup);
		
		loginRegister.setOnAction(e -> {

		    String inputUsername = usernameTf.getText();
		    String inputEmail = emailTf.getText();
		    String inputPassword = passwordTf.getText();
		    String inputPhoneNum = phoneNumberTf.getText();
		    String inputAddress = addressTa.getText();
		    String inputGender = (male.isSelected()) ? "Male" : "Female";

		    String userID = registerValidation.generateUserID();

		    if (!registerValidation.isValidUsername(usernameTf.getText())) {
		        registerValidation.showAlert("Invalid Username", "Username must be 5-20 characters and unique.");
		        return;
		    }

		    if (!registerValidation.isValidEmail(emailTf.getText())) {
		        registerValidation.showAlert("Invalid Email", "Email must end with '@gmail.com'.");
		        return;
		    }

		    if (!registerValidation.isValidPassword(passwordTf.getText())) {
		        registerValidation.showAlert("Invalid Password", "Password must be alphanumeric and at least 5 characters.");
		        return;
		    }

		    if (!passwordTf.getText().equals(passwordConfirmTf.getText())) {
		        registerValidation.showAlert("Invalid Password", "Confirm password must match the password.");
		        return;
		    }

		    if (!registerValidation.isValidPhoneNumber(phoneNumberTf.getText())) {
		        registerValidation.showAlert("Invalid Phone Number", "Phone number must be numeric and start with '+62'.");
		        return;
		    }

		    String insertQuery = String.format("INSERT INTO user VALUES ('%s', '%s', '%s', 'Customer', '%s', '%s', '%s')",
		            userID, inputUsername, inputPassword, inputAddress, inputPhoneNum, inputGender);

		    DatabaseManager.getInstance().execUpdate(insertQuery);

		    Alert registerSuccess = new Alert(AlertType.INFORMATION);
		    registerSuccess.setTitle("Success");
		    registerSuccess.setHeaderText("Register Successfully!");
		    registerSuccess.showAndWait();

		    window.setScene(sceneLogin);
		});
		
		registerLogin.setOnAction(e->{
            window.setScene(sceneLogin);
		});
	}
	
	public MenuBar customerMenuBar() {
		rootMenu = new BorderPane();
		fp = new FlowPane();
		gp = new GridPane();
		
		menuBar = new MenuBar();
		
		home = new Menu("Home");
		homePage = new MenuItem("HomePage");
		home.getItems().add(homePage);
		homePage.setOnAction(e->{
			window.setScene(sceneHome);
		});
		
		cart = new Menu("Cart");
		myCart = new MenuItem("My Cart");
		cart.getItems().add(myCart);
		myCart.setOnAction(e->{
			cartInitiate(loggedInUser);
			window.setScene(sceneCart);
		});
		
		account = new Menu("Account");
		purchaseHistory = new MenuItem("Purchase History");
		logOut = new MenuItem("Log Out");
		account.getItems().addAll(purchaseHistory, logOut);
		purchaseHistory.setOnAction(e->{
			historyInitiate(loggedInUser);
			window.setScene(sceneHistory);
		});
		logOut.setOnAction(e->{
			usernameTf1.clear();
			passwordTf1.clear();
			products.clear();
			window.setScene(sceneLogin);
		});
		
		rootMenu.setTop(menuBar);
		
		menuBar.getMenus().addAll(home, cart, account);
		return menuBar;
	}
	
	public MenuBar adminMenuBar() {
		rootMenu = new BorderPane();
		menuBar = new MenuBar();

		home = new Menu("Home");
		homePage = new MenuItem("HomePage");
		home.getItems().add(homePage);
		homePage.setOnAction(e -> {
			homeAdminInitiate();
			window.setScene(sceneHomeAdmin);
		});
		
		manageProduct = new Menu("Manage Products");
		manageProductItem = new MenuItem("Manage Products");
		manageProduct.getItems().add(manageProductItem);
		manageProduct.setOnAction(e -> {
			manageProductInitiate();
			window.setScene(sceneManageProduct);
		});
		
		account = new Menu("Account");
		logOut = new MenuItem("Log Out");
		account.getItems().add(logOut);
		logOut.setOnAction(e -> {
			usernameTf1.clear();
			passwordTf1.clear();
			window.setScene(sceneLogin);
		});
		
		rootMenu.setTop(menuBar);

		menuBar.getMenus().addAll(home, manageProduct, account);
		return menuBar;
	}
	
	public void homeInitiate() {
		
		rootHome = new BorderPane();
	    rootHome.setTop(customerMenuBar());
	    gp = new GridPane();

	    title = new Label("SeRuput Teh");
	    title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
	    listProduct = new ListView<>();
	    listProduct.setPrefWidth(200);

	    
	    welcomeMessage1 = new Label("Welcome, " + (loggedInUser != null ? loggedInUser.getUsername() : "Guest"));
	    welcomeMessage1.setFont(Font.font("Arial", FontWeight.BOLD, 18));

	    productDetail = new Label("Select a product to view");
	    productDetail.setWrapText(true);

	    gp.add(title, 1, 0);
	    gp.add(welcomeMessage1, 1, 1);
	    gp.add(productDetail, 1, 2);
	    gp.setHgap(20);
	    gp.setVgap(15);
	    gp.setPadding(new Insets(20, 10, 30, 10));
	    rootHome.setCenter(listProduct);
	    rootHome.setBottom(gp);

	    viewProduct.viewProduct(products, listProduct);

	    listProduct.setOnMouseClicked(e -> {
	        try {
	            Product selectedProduct = listProduct.getSelectionModel().getSelectedItem();
	            homeDetailedInitiate(selectedProduct);
	            window.setScene(sceneHomeDetail);
	            System.out.println(loggedInUser.getUsername());
	        } catch (Exception e2) {
	            e2.printStackTrace();
	        }
	    });
	    
		sceneHome = new Scene(rootHome, 800, 800);
	}
	
	public void homeDetailedInitiate(Product selectedProduct) {
		rootHomeDetail = new BorderPane();
		rootHomeDetail.setTop(customerMenuBar());
		fp = new FlowPane();
		fp2 = new FlowPane();
		fp3 = new FlowPane();
		gp = new GridPane();
		
		title = new Label("SeRuput Teh");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

		listProductDetail = new ListView<>();
		listProduct.setPrefWidth(200);
		
		ObservableList<Product> selectedProductList = FXCollections.observableArrayList();
	    selectedProductList.add(selectedProduct);
	    listProductDetail.setItems(selectedProductList);
	    
		productName = new Label(selectedProduct.getName());
		productName.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		
		productDetail = new Label(selectedProduct.getProductDescription());
		productDetail.setWrapText(true);
		productPrice = new Label("Price: Rp.");
		oncePrice = new Label(String.valueOf(selectedProduct.getPrice()));
		quantity = new Label("Quantity : ");
		spinQty = new Spinner<>();
		valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
		
		
		totalInfo = new Label("Total: ");
		totalInfo.setPadding(new Insets(0,0,0,10));
		totalPrice = new Label(String.valueOf(selectedProduct.getPrice()));
		totalPrice.setPadding(new Insets(0,10,0,0));
		cartBt = new Button("Add to Cart");
		
		fp.getChildren().addAll(productPrice, oncePrice);
		fp2.getChildren().addAll(quantity, spinQty, totalInfo, totalPrice);
		
		gp.add(title, 1, 0);
		gp.add(productName, 1, 1);
		gp.add(productDetail, 1, 2);
		gp.add(fp, 1, 3);
		gp.add(fp2, 1, 4);
		gp.add(cartBt, 1, 5);
		gp.setHgap(20);
		gp.setVgap(15);
		gp.setPadding(new Insets(20,10,30,10));
		rootHomeDetail.setCenter(listProductDetail);
		rootHomeDetail.setBottom(gp);
		
	    spinQty.setValueFactory(valueFactory);
	    
	    spinQty.setOnMouseClicked(event -> {
	        if (event.getButton() == MouseButton.PRIMARY) {
	            valueFactory.increment(0);
	        } else if (event.getButton() == MouseButton.SECONDARY) {
	            valueFactory.decrement(0);
	        }
	    });
	    
	    spinQty.valueProperty().addListener((observable, oldValue, newValue) -> {
	        int quantity = newValue.intValue();
	        int unitPrice = selectedProduct.getPrice();
	        int totalPriceValue = quantity * unitPrice;
	        totalPrice.setText("Rp. " + totalPriceValue);
	    });
		
	    cartBt.setOnMouseClicked(e -> {
	        int quantity = spinQty.getValue();
	        int unitPrice = selectedProduct.getPrice();
	        String userID = loggedInUser.getUserID();
	        String productID = selectedProduct.getProductID();

	        boolean productExistsInCart = false;
	        for (Cart cartItem : carts) {
	            if (cartItem.getProductID().equals(productID)) {
	                int newQuantity = cartItem.getQuantity() + quantity;
	                cartItem.setQuantity(newQuantity);

	                String updateQuery = "UPDATE cart SET quantity = ? WHERE productID = ? AND userID = ?";
	                try {
	                    DatabaseManager dbManager = DatabaseManager.getInstance();
	                    PreparedStatement updateStatement = dbManager.getConnection().prepareStatement(updateQuery);
	                    updateStatement.setInt(1, newQuantity);
	                    updateStatement.setString(2, productID);
	                    updateStatement.setString(3, userID);
	                    updateStatement.executeUpdate();
	                } catch (SQLException ex) {
	                    ex.printStackTrace();
	                }

	                productExistsInCart = true;
	                break;
	            }
	        }

	        if (!productExistsInCart) {
	            String insertQuery = String.format("INSERT INTO cart (productID, userID, quantity) VALUES ('%s', '%s', %d)",
	                    productID, userID, quantity);

	            DatabaseManager.getInstance().execUpdate(insertQuery);

	            Cart cartItem = new Cart(productID,
	                    selectedProduct.getName(),
	                    selectedProduct.getProductDescription(),
	                    unitPrice,
	                    quantity);
	            carts.add(cartItem);
	        }

	        Alert addCard = new Alert(AlertType.INFORMATION);
	        addCard.setTitle("Message");
	        addCard.setHeaderText("Added to Cart");
	        addCard.showAndWait();
	        window.setScene(sceneHome);
	    });

		sceneHomeDetail = new Scene(rootHomeDetail, 800, 800);
	}
	
	public void cartInitiate(User loggedInUser) {

		rootCart = new BorderPane();
		rootCart.setTop(customerMenuBar());
		fp4 = new FlowPane();
		fp5 = new FlowPane();
		fp6 = new FlowPane();
		fp7 = new FlowPane();
		gp = new GridPane();
		
		title = new Label(loggedInUser.getUsername() + " Carts");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

		listCart = new ListView<>();
	    listCart.setItems(carts);

		welcomeMessage = new Label("Welcome, " + loggedInUser != null ? loggedInUser.getUsername() : "Guest");
		welcomeMessage.setFont(Font.font("Arial", FontWeight.BOLD, 18));

		productDetail = new Label("Select a product to add and remove");
		productDetail.setWrapText(true);
		orderInfo = new Label("Order Information");
		username = new Label("Username : ");
		usernameValue = new Label(loggedInUser.getUsername());
		phoneNumber = new Label("Phone Number : ");
		phoneNumberValue = new Label(loggedInUser.getPhoneNum());
		address = new Label("Address : ");
		addressValue = new Label(loggedInUser.getAddress());
		makePurchaseBt = new Button("Make Purchase");
		
		
		fp5.getChildren().addAll(username, usernameValue);
		fp6.getChildren().addAll(phoneNumber, phoneNumberValue);
		fp7.getChildren().addAll(address, addressValue);
		
		gp.add(title, 0, 0);
		gp.add(welcomeMessage, 0, 1);
		gp.add(productDetail, 0, 2);
		gp.add(orderInfo, 0, 7);
		gp.add(fp5, 0, 8);
		gp.add(fp6, 0, 9);
		gp.add(fp7, 0, 10);
		gp.add(makePurchaseBt, 0, 11);

		title.setPadding(new Insets(0, 0, 20, 0));
		gp.setPadding(new Insets(20, 20, 20, 20));
		gp.setVgap(8);
		
		DatabaseManager dbManager = DatabaseManager.getInstance();

		carts.clear();
		cartValidation.showCart(dbManager, loggedInUser, carts);
		 
		 makePurchaseBt.setOnMouseClicked(e -> {
			    if (carts.isEmpty()) {
			        cartValidation.showEmpty();
			    } else {
			        String transactionId = transactionValidation.generateTransactionId();
			        cartValidation.insertTransactionHeader(transactionId, loggedInUser.getUserID());
			        for (Cart cartItem : carts) {
			            cartValidation.insertTransactionDetail(transactionId, cartItem.getProductID(), cartItem.getQuantity());
			        }
			        cartValidation.clearUserCart(loggedInUser.getUserID());
			        carts.clear();
			        cartValidation.showSuccess();
			    }
			});
	    
	    listCart.setOnMouseClicked(e -> {
			try {
				Cart selectedCart = listCart.getSelectionModel().getSelectedItem();
		        cartDetailInitiate(selectedCart);
				window.setScene(sceneCartDetail);	
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		});
		rootCart.setCenter(listCart);
		rootCart.setBottom(gp);
		sceneCart = new Scene(rootCart, 800, 800);

	}
	
	public void cartDetailInitiate(Cart selectedCart) {
		rootCartDetail = new BorderPane();
		rootCartDetail.setTop(customerMenuBar());
		fp = new FlowPane();
		fp2 = new FlowPane();
		fp3 = new FlowPane();
		fp4 = new FlowPane();
		fp5 = new FlowPane();
		fp6 = new FlowPane();
		fp7 = new FlowPane();
		gp = new GridPane();
		title = new Label(loggedInUser.getUsername() + " Cart");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		listCartDetail = new ListView<>();
	    listCartDetail.setItems(carts);
		productName = new Label(selectedCart.getName());
		productDetail= new Label(selectedCart.getProductDescription());
		productDetail.setWrapText(true);
		productPrice = new Label("Price: Rp.");
		oncePrice = new Label(String.valueOf(selectedCart.getPrice()));
		quantity = new Label("Quantity : ");
		spinQty = new Spinner<>();
		valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, selectedCart.getQuantity(), 1);
		totalInfo = new Label("Total : ");
		totalPrice = new Label(String.valueOf(selectedCart.getPrice()));
		updateCartBt = new Button("Update Cart");
		removeCartBt = new Button("Remove From Cart");
		totalAllInfo = new Label("Total : ");
		totalAllPrice = new Label(String.valueOf(selectedCart.getQuantity() * selectedCart.getPrice()));
		orderInfo = new Label("Order Information");
		username = new Label("Username : ");
		usernameValue = new Label(loggedInUser.getUsername());
		phoneNumber = new Label("Phone Number : ");
		phoneNumberValue = new Label(loggedInUser.getPhoneNum());
		address = new Label("Address : ");
		addressValue = new Label(loggedInUser.getAddress());
		makePurchaseBt1 = new Button("Make Purchase");
		fp.getChildren().addAll(productPrice, oncePrice);
		fp2.getChildren().addAll(quantity, spinQty, totalInfo, totalPrice);
		fp3.getChildren().addAll(updateCartBt, removeCartBt);
		fp4.getChildren().addAll(totalAllInfo, totalAllPrice);
		fp5.getChildren().addAll(username, usernameValue);
		fp6.getChildren().addAll(phoneNumber, phoneNumberValue);
		fp7.getChildren().addAll(address, addressValue);
		gp.add(title, 0, 0);
		gp.add(productName, 0, 1);
		gp.add(productDetail, 0, 2);
		gp.add(fp, 0, 3);
		gp.add(fp2, 0, 4);
		gp.add(fp3, 0, 5);
		gp.add(fp4, 0, 6);
		gp.add(orderInfo, 0, 7);
		gp.add(fp5, 0, 8);
		gp.add(fp6, 0, 9);
		gp.add(fp7, 0, 10);
		gp.add(makePurchaseBt1, 0, 11);
		title.setPadding(new Insets(0, 0, 20, 0));
		gp.setPadding(new Insets(20, 20, 20, 20));
		gp.setVgap(8);
		fp2.setHgap(8);
		fp3.setHgap(8);
		
		spinQty.setValueFactory(valueFactory);
	    
	    spinQty.setOnMouseClicked(event -> {
	        if (event.getButton() == MouseButton.PRIMARY) {
	            valueFactory.increment(0);
	        } else if (event.getButton() == MouseButton.SECONDARY) {
	            valueFactory.decrement(0);
	        }
	    });
	    
	    spinQty.valueProperty().addListener((observable, oldValue, newValue) -> {
	        int newQuantity = newValue.intValue();
	        int unitPrice = selectedCart.getPrice();
	        int newTotalPrice = newQuantity * unitPrice;
	        totalPrice.setText("Rp. " + newTotalPrice);
	        updateTotalAllPrice();
	    });
	    
	    updateCartBt.setOnMouseClicked(e->{	        
	    	Cart selectionCart = listCartDetail.getSelectionModel().getSelectedItem();
	        if (selectionCart != null && loggedInUser != null) {
	            int newQuantity = spinQty.getValue();
	            String updateQuery = "UPDATE cart SET quantity = ? WHERE productID = ? AND userID = ?";
	            try {
	                cartValidation.updateCart(newQuantity, selectionCart, loggedInUser, valueFactory, updateQuery);
	                updateCartDetailUI(selectionCart);
	                listCart.refresh();
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	            cartValidation.showUpdated();
	        } else {
	        	int newQuantity = spinQty.getValue();
	            String updateQuery = "UPDATE cart SET quantity = ? WHERE productID = ? AND userID = ?";
	            try {
	                cartValidation.updateCart(newQuantity, selectedCart, loggedInUser, valueFactory, updateQuery);
	                updateCartDetailUI(selectedCart);
	                listCart.refresh();
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	            cartValidation.showUpdated();
	        }
	        });
	    
	    removeCartBt.setOnMouseClicked(e -> {
	        Cart selectionCart = listCartDetail.getSelectionModel().getSelectedItem();

	        if (selectionCart != null && loggedInUser != null) {
	            try {
	                cartValidation.deleteCart(selectionCart, loggedInUser);
	                carts.remove(selectionCart);
	                Platform.runLater(() -> {
	                    listCartDetail.refresh();
	                    updateTotalAllPrice();
	                });
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }

	            cartValidation.showRemoved();
	        } else {
	        	try {
	                cartValidation.deleteCart(selectedCart, loggedInUser);
	                carts.remove(selectedCart);
	                Platform.runLater(() -> {
	                    listCartDetail.refresh();
	                    updateTotalAllPrice();
	                });
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	            cartValidation.showRemoved();
	        }
	    });

	    makePurchaseBt1.setOnAction(e -> {
	    	if (carts.isEmpty()) {
		        cartValidation.showEmpty();
		    } else {
		        Alert makePurchase = new Alert(AlertType.CONFIRMATION);
		        makePurchase.setTitle("Message");
		        makePurchase.setHeaderText("Are you sure you want to make a purchase?");
		        ButtonType buttonTypeYes = new ButtonType("Yes");
		        ButtonType buttonTypeNo = new ButtonType("No");
		        makePurchase.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
		        makePurchase.showAndWait().ifPresent(buttonType -> {
		            if (buttonType == buttonTypeYes) {
		            	if (!carts.isEmpty() && loggedInUser != null) {
		    	            try {
		    	                String transactionId = transactionValidation.generateTransactionId();
		    	                transactionValidation.insertTransaction(transactionId, loggedInUser, carts, listCartDetail);
		    	                updateTotalAllPrice();
		    	                cartValidation.showSuccess();
		    	            } catch (SQLException ex) {
		    	                ex.printStackTrace();
		    	            }
		            	}
		            }
		        });
		    }
	    });

	    listCartDetail.setOnMouseClicked(e -> {
	        Cart selectionCart = listCartDetail.getSelectionModel().getSelectedItem();
	        if (selectionCart != null) {
	            updateCartDetailUI(selectionCart);
	        }
	    });

		rootCartDetail.setCenter(listCartDetail);
		rootCartDetail.setBottom(gp);
		sceneCartDetail = new Scene(rootCartDetail, 800, 800);
	}
	
	public void historyInitiate(User loggedInUser) {
		rootHistory = new BorderPane();
		rootHistory.setTop(customerMenuBar());
		fp = new FlowPane();
		fp2 = new FlowPane();
		fp3 = new FlowPane();
		fp4 = new FlowPane();
		fp5 = new FlowPane();
		fp6 = new FlowPane();
		gp = new GridPane();
		listTransaction = new ListView<>();
		title = new Label(loggedInUser.getUsername() + " Purchase History");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		transactionId = new Label("Transaction ID : ");
		transactionValue = new Label();
		username = new Label("Username : ");
		usernameValue = new Label(loggedInUser.getUsername());
		phoneNumber = new Label("Phone Number : ");
		phoneNumberValue = new Label(loggedInUser.getPhoneNum());
		address = new Label("Address : ");
		addressValue = new Label(loggedInUser.getAddress());
		totalAllInfo = new Label("Total : Rp.");
		totalAllPrice = new Label();
		listTransactionDetail = new ListView<>();
		fp.getChildren().addAll(transactionId, transactionValue);
		fp2.getChildren().addAll(username, usernameValue);
		fp3.getChildren().addAll(phoneNumber, phoneNumberValue);
		fp4.getChildren().addAll(address, addressValue);
		fp5.getChildren().addAll(totalAllInfo, totalAllPrice);
		gp.add(fp, 0, 1);
		gp.add(fp2, 0, 2);
		gp.add(fp3, 0, 3);
		gp.add(fp4, 0, 4);
		gp.add(fp5, 0, 5);
		gp.add(listTransactionDetail, 0, 6);
		fp6.getChildren().addAll(listTransaction, gp);
		fp6.setAlignment(Pos.CENTER);
		title.setPadding(new Insets(50,0,0,0));
		gp.setVgap(10);
		fp6.setHgap(10);
		fp6.setPadding(new Insets(50,50,150,50));
		rootHistory.setCenter(title);
		rootHistory.setBottom(fp6);
		sceneHistory = new Scene(rootHistory, 800, 800);
		listTransaction.setItems(transactionValidation.fetchUserTransactions(loggedInUser));
	    transactionValidation.setupTransactionDetailListView(listTransaction, transactionValue, totalAllPrice, listTransactionDetail);
	}
	
	private void updateCartDetailUI(Cart selectedCart) {
	    productName.setText(selectedCart.getName());
	    productDetail.setText(selectedCart.getProductDescription());
	    oncePrice.setText(String.valueOf(selectedCart.getPrice()));
	    int maxQuantity = selectedCart.getQuantity();
	    ((IntegerSpinnerValueFactory) valueFactory).setMax(maxQuantity);
	    spinQty.getValueFactory().setValue(selectedCart.getQuantity());
		DatabaseManager dbManager = DatabaseManager.getInstance();
		String productQuery = "SELECT * FROM product WHERE productID = ?";
	    try {
	        PreparedStatement productStatement = dbManager.getConnection().prepareStatement(productQuery);
	        productStatement.setString(1, selectedCart.getProductID());
	        ResultSet productResult = productStatement.executeQuery();

	        if (productResult.next()) {
	            int unitPrice = productResult.getInt("product_price");

	            selectedCart.setPrice(unitPrice);
	            int totalPriceValue = selectedCart.getQuantity() * unitPrice;
	            totalPrice.setText("Rp. " + totalPriceValue);
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    updateTotalAllPrice();
	}

	private void updateTotalAllPrice() {
	    int totalAllPriceValue = 0;
	    for (Cart cartItem : carts) {
	        totalAllPriceValue += cartItem.getQuantity() * cartItem.getPrice();
	    }
	    totalAllPrice.setText("Rp. " + totalAllPriceValue);
	}
	
	private void homeAdminInitiate() {
		rootHomeAdmin = new BorderPane();
		rootHomeAdmin.setTop(adminMenuBar());
		gp = new GridPane();
		
		titleAdmin = new Label("SeRuput Teh");
		titleAdmin.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		titleAdmin.setAlignment(Pos.BASELINE_LEFT);
		welcomeMessageAdmin = new Label("Welcome, " + loggedInUser.getUsername());
		welcomeSelectProduct = new Label("Select a product to view");
		
		listProductAdmin = new ListView<>();
		
		gp.add(titleAdmin, 0, 0);
		gp.add(welcomeMessageAdmin, 0, 1);
		gp.add(welcomeSelectProduct, 0, 2);
		gp.setVgap(10);
		gp.setPadding(new Insets(20,40,40,40));

		rootHomeAdmin.setCenter(listProductAdmin);
		rootHomeAdmin.setBottom(gp);
		
		viewProduct.viewProduct(products, listProductAdmin);
	    
	    listProductAdmin.setOnMouseClicked(e -> {
	        try {
	            Product selectedProduct = listProductAdmin.getSelectionModel().getSelectedItem();
	            homeAdminDetailedInitiate(selectedProduct);
	            window.setScene(sceneHomeAdminDetail);
	            System.out.println(loggedInUser.getUsername());
	        } catch (Exception e2) {
	            e2.printStackTrace();
	        }
	    });
		
		sceneHomeAdmin = new Scene(rootHomeAdmin, 800, 800);
	}
	
	private void homeAdminDetailedInitiate(Product selectedProduct) {
		rootHomeAdminDetail = new BorderPane();
		rootHomeAdminDetail.setTop(adminMenuBar());
		gp = new GridPane();
		
		titleAdmin1 = new Label("SeRuput Teh");
		titleAdmin1.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		titleAdmin1.setAlignment(Pos.BASELINE_LEFT);
		welcomeMessageAdmin1 = new Label(selectedProduct.getName());
		welcomeSelectProduct1 = new Label(selectedProduct.getProductDescription());
	    welcomeSelectProduct1.setWrapText(true);
	    productPriceAdmin= new Label("Price: Rp." + String.valueOf(selectedProduct.getPrice()));

		listProductAdmin1 = new ListView<>();
		
		gp.add(titleAdmin1, 0, 0);
		gp.add(welcomeMessageAdmin1, 0, 1);
		gp.add(welcomeSelectProduct1, 0, 2);
		gp.add(productPriceAdmin, 0, 3);
		gp.setVgap(10);
		gp.setPadding(new Insets(20,40,40,40));
		
		rootHomeAdminDetail.setCenter(listProductAdmin1);
		rootHomeAdminDetail.setBottom(gp);
		
		viewProduct.viewProduct(products, listProductAdmin1);
	    
	    listProductAdmin1.setOnMouseClicked(e -> {
	        try {
	            Product selectedProduct1 = listProductAdmin1.getSelectionModel().getSelectedItem();
	            homeAdminDetailedInitiate(selectedProduct1);
	            window.setScene(sceneHomeAdminDetail);
	            System.out.println(loggedInUser.getUsername());
	        } catch (Exception e2) {
	            e2.printStackTrace();
	        }
	    });
	    
	    sceneHomeAdminDetail = new Scene(rootHomeAdminDetail, 800, 800);
	}
	
	private void manageProductInitiate() {
		rootManageProduct = new BorderPane();
		fp = new FlowPane();
		gp = new GridPane();
		
		titleManage = new Label("Manage Products");
		titleManage.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		welcomeMessageManage = new Label("Welcome, " + loggedInUser.getUsername());
		welcomeMessageManage.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		welcomeSelectManage = new Label("Select a Product to Update");
		addProductBt = new Button("Add Product");
		listManageProduct = new ListView<>();
		
		gp.add(titleManage, 0, 0);
		gp.add(welcomeMessageManage, 0, 1);
		gp.add(welcomeSelectManage, 0, 2);
		gp.add(addProductBt, 0, 3);
		gp.setPadding(new Insets(30, 50, 50, 50));
		gp.setVgap(10);
		
		viewProduct.viewProduct(products, listManageProduct);
	    
		rootManageProduct.setTop(adminMenuBar());
		rootManageProduct.setCenter(listManageProduct);
		rootManageProduct.setBottom(gp);
		
		listManageProduct.setOnMouseClicked(e -> {
	        try {
	            Product selectedProduct = listManageProduct.getSelectionModel().getSelectedItem();
	            manageProductInitiate2(selectedProduct);
	            window.setScene(sceneManageProduct1);
	            System.out.println(loggedInUser.getUsername());
	        } catch (Exception e2) {
	            e2.printStackTrace();
	        }
	    });
		
		addProductBt.setOnAction(e -> {
			inputProductInitiate();
			window.setScene(sceneInputProduct);
		});
		
		sceneManageProduct = new Scene(rootManageProduct, 800, 800);
	}
	
	private void manageProductInitiate2(Product selectedProduct) {
		rootManageProduct1 = new BorderPane();
		fp = new FlowPane();
		gp = new GridPane();
		
		titleManage1 = new Label("Manage Products");
		titleManage1.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		welcomeMessageManage1 = new Label(selectedProduct.getName());
		welcomeMessageManage1.setFont(Font.font("Arial", FontWeight.BOLD, 14));

		welcomeSelectManage1 = new Label(selectedProduct.getProductDescription());
		welcomeSelectManage1.setWrapText(true);
		productPriceManage = new Label("Price: Rp." + selectedProduct.getPrice());
		addProductBt1 = new Button("Add Product");
		updateProductBt = new Button("Update Product");
		removeProductBt = new Button("Remove Product");
		listManageProduct1 = new ListView<>();
		
		fp.getChildren().setAll(updateProductBt, removeProductBt);
		gp.add(titleManage1, 0, 0);
		gp.add(welcomeMessageManage1, 0, 1);
		gp.add(welcomeSelectManage1, 0, 2);
		gp.add(productPriceManage, 0, 3);
		gp.add(addProductBt1, 0, 4);
		gp.add(fp, 0, 5);
		gp.setPadding(new Insets(30, 50, 50, 50));

		gp.setVgap(10);
		fp.setHgap(10);
		
		rootManageProduct1.setTop(adminMenuBar());
		rootManageProduct1.setCenter(listManageProduct1);
		rootManageProduct1.setBottom(gp);
		
		viewProduct.viewProduct(products, listManageProduct1);
	    
	    listManageProduct1.setOnMouseClicked(e -> {
	        try {
	            Product selectedProduct1 = listManageProduct1.getSelectionModel().getSelectedItem();
	            manageProductInitiate2(selectedProduct1);
	            window.setScene(sceneManageProduct1);
	            System.out.println(loggedInUser.getUsername());
	        } catch (Exception e2) {
	            e2.printStackTrace();
	        }
	    });
		
	    addProductBt1.setOnAction(e -> {
	    	inputProductInitiate();
	    	window.setScene(sceneInputProduct);
	    });
	    
	    updateProductBt.setOnAction(e -> {
	    	try {
	            updateManageProductInitiate(selectedProduct);
	            window.setScene(sceneUpdateProduct);
	            System.out.println(loggedInUser.getUsername());
	        } catch (Exception e2) {
	            e2.printStackTrace();
	        }
	    });
	    
	    removeProductBt.setOnAction(e -> {
	        // Get the selected product
	        if (selectedProduct != null) {
	            products.remove(selectedProduct);
	            viewProduct.removeProduct(selectedProduct.getProductID());
	            viewProduct.viewProduct(products, listManageProduct1);
	            viewProduct.removeProduct();
	        }else {
	            viewProduct.showError();
	        }
	    });
	    
		sceneManageProduct1 = new Scene(rootManageProduct1, 800, 800);
	}
	
	private void inputProductInitiate() {
		rootInputProduct = new BorderPane();
		fp = new FlowPane();
		gp = new GridPane();
		
		titleAdd = new Label("Manage Products");
		titleAdd.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		welcomeMessageAdd = new Label("Welcome, " + loggedInUser.getUsername());
		welcomeSelectAdd = new Label("Select a Poduct to Update");
		inputProductName = new Label("Input Product name");
		inputProductPrice = new Label("Input Product Price");
		inputProductDescription = new Label("Input product description");
		listInputProduct = new ListView<>();
		
		addProductBt2 = new Button("Add Product");
		backBt = new Button("Back");
		
		productNameTf = new TextField();
		productNameTf.setPromptText("input product name..");
		productPriceTf = new TextField();
		productPriceTf.setPromptText("input product price..");
		productDescriptionTa = new TextArea();
		productDescriptionTa.setPromptText("input product description..");
		
		fp.getChildren().addAll(addProductBt2, backBt);
		fp.setHgap(20);
		gp.add(titleAdd, 0, 0);
		gp.add(welcomeMessageAdd, 0, 1);
		gp.add(welcomeSelectAdd, 0, 2);
		gp.add(inputProductName, 0, 3);
		gp.add(productNameTf, 0, 4);
		gp.add(inputProductPrice, 0, 5);
		gp.add(productPriceTf, 0, 6);
		gp.add(inputProductDescription, 0, 7);
		gp.add(productDescriptionTa, 0, 8);
		gp.add(fp, 0, 9);
		gp.setVgap(10);
		gp.setAlignment(Pos.CENTER);
		gp.setPadding(new Insets(20,20,20,20));
		rootInputProduct.setTop(adminMenuBar());
		rootInputProduct.setCenter(listInputProduct);
		rootInputProduct.setBottom(gp);
		
		viewProduct.viewProduct(products, listInputProduct);
		
		 addProductBt2.setOnAction(e -> {
		        String productName = productNameTf.getText().trim();
		        String productPriceText = productPriceTf.getText().trim();
		        String productDescription = productDescriptionTa.getText().trim();
		        viewProduct.insertedValidation(productName, productPriceText, productDescription, products);
		        String productID = viewProduct.generateProductID(products);
		        String insertQuery = String.format("INSERT INTO product VALUES ('%s', '%s', %s, '%s')",
		                productID, productName, productPriceText, productDescription);
		        DatabaseManager.getInstance().execUpdate(insertQuery);
		        viewProduct.viewProduct(products, listInputProduct);
		        productNameTf.clear();
		        productPriceTf.clear();
		        productDescriptionTa.clear();
		    });
		
		 backBt.setOnAction(e -> {
			 manageProductInitiate();
			 window.setScene(sceneManageProduct);
		 });
		sceneInputProduct = new Scene(rootInputProduct, 800, 800);
	}
	
	private void updateManageProductInitiate(Product selectedProduct) {
		rootUpdateProduct = new BorderPane();
		fp = new FlowPane();
		gp = new GridPane();
		
		titleUpdate=new Label("Manage Products");
		titleUpdate.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		productNameUpdate = new Label(selectedProduct.getName());
		productNameUpdate.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		productDescritionUpdate = new Label(selectedProduct.getProductDescription());
		productDescritionUpdate.setWrapText(true);
		productPriceUpdate = new Label("Price: Rp. " + String.valueOf(selectedProduct.getPrice()));
		updateProduct = new Label("Update Product");
		updateProductTf = new TextField();
		updateProductTf.setPromptText("Input new price");
		listUpdateProduct = new ListView<>();
		updateProductBt1 = new Button("Update Product");
		backBt1 = new Button("Back");
		fp.getChildren().addAll(updateProductBt1, backBt1);
		gp.add(titleUpdate, 0, 0);
		gp.add(productNameUpdate, 0, 1);
		gp.add(productDescritionUpdate, 0, 2);
		gp.add(productPriceUpdate, 0, 3);
		gp.add(updateProduct, 0, 4);
		gp.add(updateProductTf, 0, 5);
		gp.add(fp, 0, 6);
		fp.setHgap(10);
		gp.setVgap(20);		
		gp.setPadding(new Insets(20,20,20,20));
		viewProduct.viewProduct(products, listUpdateProduct);
		rootUpdateProduct.setTop(adminMenuBar());
		rootUpdateProduct.setCenter(listUpdateProduct);
		rootUpdateProduct.setBottom(gp);
		sceneUpdateProduct = new Scene(rootUpdateProduct, 800, 800);
		
		listUpdateProduct.setOnMouseClicked(e -> {
	        try {
	            Product selectedProduct1 = listUpdateProduct.getSelectionModel().getSelectedItem();
	            updateManageProductInitiate(selectedProduct1);
	            window.setScene(sceneUpdateProduct);
	            System.out.println(loggedInUser.getUsername());
	        } catch (Exception e2) {
	            e2.printStackTrace();
	        }
	    });
		
		updateProductBt1.setOnAction(e -> {
	        if (viewProduct.isValidNumber(updateProductTf.getText())) {
	            int newPrice = Integer.parseInt(updateProductTf.getText());
	            viewProduct.updateProductPrice(selectedProduct.getProductID(), newPrice, productPriceUpdate);
	            viewProduct.viewProduct(products, listUpdateProduct);
	            viewProduct.showUpdate();
	        } else {
	            viewProduct.showError();
	        }
	    });
		backBt1.setOnAction(e -> {
			manageProductInitiate();
			window.setScene(sceneManageProduct);
		});

	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		window = stage;
		
		loginInitiate();
		registerInitiate();
		
		sceneLogin = new Scene(rootLogin, 800, 800);
		sceneRegister = new Scene(rootRegister, 800, 800);
		
		stage.setScene(sceneLogin);
		stage.show();
	}
	
}
