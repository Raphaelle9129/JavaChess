import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class main extends Application{

	@Override
	public void start(Stage stage) throws Exception {
		
		stage.setMaxHeight(900);
		stage.setMaxWidth(900);
		stage.setTitle("Echec 1.0");
		
		VBox vbnet=new VBox();
		
		Scene scene1=new Scene(vbnet);
		stage.setScene(scene1);
		
		Canvas mainCan=new Canvas(840, 720);
		
		vbnet.getChildren().add(mainCan);
		
		stage.show();
		
		Board mainBoard=new Board(mainCan);
		mainBoard.draw(false, 60);
		
		mainCan.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				
				mainBoard.click((int)e.getX(), (int)e.getY(), 60);
				
				Platform.runLater(()->mainBoard.draw(false, 60));
			}
		});
		
	}
	
	public static void main(String[] args) {
		
		main.launch();
	}

}
