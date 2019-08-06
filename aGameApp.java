import com.sun.javafx.binding.StringFormatter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class aGameApp extends Application {
    public Button[][] buttons;
    public Image[][] images;
    public int [][] points;

    public int Brl=-1,Bcl=-1;
    public Button mButton;
    public Image Blank,tempBlank;
    public boolean Playing=false;

    public int timer=0,MatchingCount=0;
    public Timeline updateTimer;
    public String currentS="Lego";

    public void start(Stage primaryStage) {
        GridPane aPane = new GridPane();

        buttons = new Button[4][4];
        images = new Image[4][4];
        Blank = new Image(getClass().getResourceAsStream("BLANK.png"));
        tempBlank = new Image(getClass().getResourceAsStream("BLANK.png"));
        points = new int[4][4];

        Label label_view = new Label();
        Label label_Blank = new Label();
        ListView<String> imageList = new ListView<String>();
        TextField timerField = new TextField();

        //Create Game Pane
        GridPane GamePane = new GridPane();
        GamePane.setPadding(new Insets(10, 0, 10, 10));
        GamePane.setHgap(1);
        GamePane.setVgap(1);

        Label label_Complete = new Label();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j< 4; j++) {
                buttons[i][j] = new Button();
                buttons[i][j].setGraphic(new ImageView(Blank));
                buttons[i][j].setPadding(new Insets(0));
                buttons[i][j].setPrefSize(187, 187);
                GamePane.add(buttons[i][j], 0+i, 0+j);

                int col = j;
                int row = i;
                buttons[i][j].setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (Playing) {
                            if(!(row==Brl&&col==Bcl)) {
                                swap(row, col);
                                if (isComplete()) {
                                    label_Complete.setText("You Complete!");
                                    GameWin();
                                    label_view.setDisable(false);
                                    imageList.setDisable(false);
                                }
                            }
                            else System.out.println("Wrong Press");
                        }
                        else System.out.println("Stopped....");
                    }
                });
            }
        }

        GamePane.setPrefSize(771, 771);
        aPane.add(GamePane, 1, 0);

        //Add image view label
        GridPane viewPane = new GridPane();
        viewPane.setVgap(10);
        viewPane.setPadding(new Insets(0, 10, 0, 0));
        viewPane.setPrefWidth(187);

        label_view.setPadding(new Insets(0));
        label_view.setPrefSize(187, 187);
        label_view.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("Lego_Thumbnail.png"))));

        //Create List Pane
        imageList.setPrefHeight(142);
        String[] iNames = {"Lego","Pets", "Scenery", "Numbers"};
        imageList.setItems(FXCollections.observableArrayList(iNames));
        imageList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                label_Complete.setText("");
                System.out.println("You clicked: " + iNames[imageList.getSelectionModel().getSelectedIndex()]);
                switch (iNames[imageList.getSelectionModel().getSelectedIndex()]) {
                    case "Pets": {
                        currentS="Pets";
                        updateBlankImage();
                        label_view.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("Pets_Thumbnail.png"))));
                        break;
                    }
                    case "Lego": {
                        currentS="Lego";
                        updateBlankImage();
                        label_view.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("Lego_Thumbnail.png"))));
                        break;
                    }
                    case "Scenery": {
                        currentS="Scenery";
                        updateBlankImage();
                        label_view.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("Scenery_Thumbnail.png"))));
                        break;
                    }
                    case "Numbers": {
                        currentS="Numbers";
                        updateBlankImage();
                        label_view.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("Numbers_Thumbnail.png"))));
                        break;
                    }
                }
            }
        });

        //Create a Start/Stop Button
        mButton = new Button();
        mButton.setPrefSize(187, 25);
        mButton.setText("Start");
        mButton.setStyle("-fx-text-fill: rgb(255,255,255); -fx-background-color: DarkGreen;");
        mButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (Playing) {
                    GameStop();
                    label_view.setDisable(false);
                    imageList.setDisable(false);
                    update();
                } else {
                    label_Complete.setText("");
                    timerField.setText("0:00");
                    GameStart();
                    label_view.setDisable(true);
                    imageList.setDisable(true);
                    updateTimer.play();
                    label_Blank.setGraphic(new ImageView(tempBlank));
                }
            }
        });

        //Add time Pane
        GridPane timePane = new GridPane();
        timePane.setHgap(10);
        Label text_time = new Label("Time:");
        text_time.setMinWidth(40);
        timerField.setText("0:00");

        //Create time
        timer = 0;
        updateTimer = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                timer++;
                timerField.setText(timer/60 + ":" + String.format("%02d", timer%60));
            }
        }));
        updateTimer.setCycleCount(Timeline.INDEFINITE);
        timePane.add(text_time, 1, 0);
        timePane.add(timerField, 2, 0);


        label_Complete.setPrefSize(187,187);
        label_Complete.setStyle("-fx-font:20 arial;-fx-text-fill:DarkRed;");
        label_Complete.setAlignment(Pos.CENTER);


        //Add items into view Pane
        viewPane.add(label_view, 0, 1);
        viewPane.add(mButton, 0, 3);
        viewPane.add(imageList, 0, 2);
        viewPane.add(timePane, 0, 4);
        viewPane.add(label_Complete,0,5);


        aPane.add(viewPane, 2, 0);

        //set primaryStage
        primaryStage.setTitle("Simple GridPane Example");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(aPane, 968, 771));
        primaryStage.show();
    }

    public void update(){
        for (int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                buttons[i][j].setGraphic(new ImageView(images[i][j]));
            }
        }
    }

    public void updateBlankImage(){
        for (int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                images[i][j] = images[i][j] = new Image(getClass().getResourceAsStream("Blank.png"));
            }
        }
        update();
    }

    public void GameStop(){
        updateBlankImage();
        System.out.println("Game Stop");
        Playing = false;
        mButton.setText("Start");
        mButton.setStyle("-fx-text-fill: rgb(255,255,255);  -fx-background-color: DarkGreen;");
        updateTimer.stop();
        MatchingCount=0;
        update();
    }

    public void GameStart(){
        System.out.println("Start");
        Playing = true;
        timer=0;
        MatchingCount=0;
        this.mButton.setText("Stop");
        mButton.setStyle("-fx-text-fill: rgb(255,255,255); -fx-background-color: DarkRed;");
        for(int i=0;i<4;i++) {
            for (int j = 0; j < 4; j++) {
                images[i][j] = new Image(getClass().getResourceAsStream(currentS+"_" + j + i + ".png"));
                points[i][j] = i * 10 + j;
            }
        }
        Brl = (int) (Math.random() * 4);
        Bcl = (int) (Math.random() * 4);
        tempBlank = images[Brl][Bcl];

        for(int i=0;i<5000;i++){
            int Srl,Scl;
            Srl = (int)(Math.random()*4);
            Scl = (int)(Math.random()*4);
            swap(Srl,Scl);
        }
    }
    public void swap(int rl,int cl) {
        boolean Swapped=false;
        if(Bcl>=0&&Brl>=0) {
            if ((rl == Brl && cl == Bcl + 1) || (rl == Brl && cl == Bcl - 1) || (rl == Brl + 1 && cl == Bcl) || (rl == Brl - 1 && cl == Bcl)) {
                Swapped = true;
            }
        }
        if(Swapped){
            Image tempImage = images[rl][cl];
            images[rl][cl] = Blank;
            images[Brl][Bcl] = tempImage;

            int tempPoint = points[rl][cl];
            points[rl][cl]=points[Brl][Bcl];
            points[Brl][Bcl]=tempPoint;

            Brl = rl;
            Bcl = cl;
        }
        update();
    }
    public boolean isComplete(){
        MatchingCount=0;
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                if (points[i][j]==i*10+j){
                   MatchingCount++;
                }
            }
        }
        if(MatchingCount==16) {

            System.out.println("Game Completed!");
            return true;
        }else {
            return false;
        }
    }
    public void GameWin(){
        images[Brl][Bcl]=tempBlank;
        Playing = false;
        mButton.setText("Start");
        mButton.setStyle("-fx-text-fill: rgb(255,255,255);  -fx-background-color: DarkGreen;");
        updateTimer.stop();
        MatchingCount=0;
        update();
    }
    public static void main(String[] args) {
        launch(args);
    }
}