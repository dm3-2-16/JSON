 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.GestionListaEnMemoria;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.converter.IntegerStringConverter;
import javax.xml.parsers.ParserConfigurationException;

import model.Armak;
import org.xml.sax.SAXException;


/**
 *
 * @author idoia
 */
public class MainWindow extends Application {

    private final TableView<Armak> table = new TableView<>();
    ObservableList<Armak> arreglar = FXCollections.observableArrayList();

    final HBox hb = new HBox();
    
    @Override
    public void start(Stage stage) throws ParserConfigurationException, SAXException, IOException {
        ObservableList<Armak> obListDatuak = FXCollections.observableArrayList();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
               new ExtensionFilter("json Files", "*.json"));
        File file = fileChooser.showOpenDialog(stage);
 
        Scene scene = new Scene(new Group());
        
        stage.setTitle("Datuen Taula");
        stage.setWidth(650);
        stage.setHeight(550);
        final Label label = new Label("Armak");
        label.setFont(new Font("Arial", 20));
        
        table.setEditable(true);
        
        TableColumn<Armak, String> nameCol = new TableColumn<>("Izena");
        nameCol.setMinWidth(100);
        nameCol.setCellValueFactory(
            new PropertyValueFactory<>("name"));
        nameCol.setCellFactory(TextFieldTableCell.<Armak>forTableColumn());
        nameCol.setOnEditCommit(
            (TableColumn.CellEditEvent<Armak, String> t) -> {
            ((Armak) t.getTableView().getItems().get(
            t.getTablePosition().getRow())
            ).setName(t.getNewValue());
            });
        
        TableColumn<Armak, String> jatorriCol = new TableColumn<>("Jatorria");
        jatorriCol.setMinWidth(100);
        jatorriCol.setCellValueFactory(
            new PropertyValueFactory<>("jatorria"));
        jatorriCol.setCellFactory(TextFieldTableCell.<Armak>forTableColumn());
        jatorriCol.setOnEditCommit(
            (TableColumn.CellEditEvent<Armak, String> t) -> {
            ((Armak) t.getTableView().getItems().get(
            t.getTablePosition().getRow())
            ).setJatorria(t.getNewValue());
            });
        
        TableColumn<Armak, String> deskribCol = new TableColumn<>("Deskribapena");
        deskribCol.setMinWidth(300);
        deskribCol.setCellValueFactory(
        new PropertyValueFactory<>("desk"));
        deskribCol.setCellFactory(TextFieldTableCell.<Armak>forTableColumn());
        deskribCol.setOnEditCommit(
            (TableColumn.CellEditEvent<Armak, String> t) -> {
                ((Armak) t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setDesk(t.getNewValue());
            });
        
        TableColumn<Armak, Integer> urteaCol = new TableColumn<>("Urtea");
        urteaCol.setMinWidth(100);
        urteaCol.setCellValueFactory(
        new PropertyValueFactory<>("urtea"));
        urteaCol.setCellFactory(TextFieldTableCell.<Armak , Integer>forTableColumn(new IntegerStringConverter()));
        urteaCol.setOnEditCommit(
            (TableColumn.CellEditEvent<Armak, Integer> t) -> {
                ((Armak) t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setUrtea(t.getNewValue());
            });
        
        arreglar = GestionListaEnMemoria.cargaArmas(file);
        table.setItems(arreglar);
        table.getColumns().addAll(nameCol, jatorriCol, deskribCol, urteaCol);
        final TextField addName = new TextField();
        addName.setPromptText("izena");
        addName.setMaxWidth(nameCol.getPrefWidth());
        final TextField addJatorri = new TextField();
        addJatorri.setMaxWidth(jatorriCol.getPrefWidth());
        addJatorri.setPromptText("jatorria");
        final TextField adddesk = new TextField();
        adddesk.setMaxWidth(deskribCol.getPrefWidth());
        adddesk.setPromptText("desk");
        final TextField addurtea = new TextField();
        addurtea.setMaxWidth(deskribCol.getPrefWidth());
        addurtea.setPromptText("urtea");
       
        final Button addButton = new Button("Gehitu");        
        addButton.setOnAction((ActionEvent e) -> {
            Armak p = new Armak(
                addName.getText(),
                addJatorri.getText(),
                adddesk.getText(),
                Integer.parseInt(addurtea.getText()));
            arreglar.add(p);
            table.getItems();
            GestionListaEnMemoria.lista_gorde(arreglar, file);
            
            addName.clear();
            addJatorri.clear();
            adddesk.clear();
            adddesk.clear();
            addurtea.clear();
        });
        
        final Button removeButton = new Button("Ezabatu");        
        removeButton.setOnAction((ActionEvent e) -> {
            Armak person = table.getSelectionModel().getSelectedItem();    
            table.getItems().remove(person);
        });
        
        hb.getChildren().addAll(addName, addJatorri, adddesk, addurtea, addButton, removeButton);
        hb.setSpacing(3);
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, hb);
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        stage.setOnCloseRequest((WindowEvent event)-> {
            try { 
                PrintWriter pw = new PrintWriter("Armak.txt");
                for(int i = 0; i < table.getItems().size(); i++){
                    pw.println(table.getItems().get(i).getName()+","
                            +table.getItems().get(i).getJatorria()+","
                            +table.getItems().get(i).getDesk()+","
                            +table.getItems().get(i).getUrtea());
                }
                pw.close();
            } catch (FileNotFoundException ex) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setContentText("Ez da artxiboa aurkitu datuak gordetzeko.");
                error.showAndWait();
            }
            
        });
        
        /* UPDATE */
        stage.setOnCloseRequest((WindowEvent event) -> {
            GestionListaEnMemoria.lista_gorde(arreglar, file);
        });
        
        stage.setScene(scene);
        stage.show();        
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
