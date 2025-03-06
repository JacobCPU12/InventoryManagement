/**
 * 
 */
/**
 * 
 */
module ITP_Assignment {
	// Below needed for SQL controls
	requires java.sql;
	requires javafx.graphics;
	requires javafx.fxml;
	// Below needed for JavaFX
	requires javafx.controls;
	requires javafx.base;
	
	opens inventory_management to javafx.graphics, javafx.fxml;
}