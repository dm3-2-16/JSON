/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import model.Armak;

/**
 *
 * @author aitor
 */
public class GestionListaEnMemoria {
    
    public static ObservableList<Armak> cargaArmas(File file) 
    {
        
        long fileSizeInBytes = file.length(); // fitxategiaren tamaina Byte-tan
            long fileSizeInKB = fileSizeInBytes/1024; // fitxategiaren tamaina KB-etan
            if (fileSizeInKB<2){
                return jsonFitxategianKargatu(file); // json OBJECT MODEL erabilita
                }
            else{
                return jsonStreamDatuakKargatu(file); // json STREAM erabilita
            }

    }
    public static ObservableList<Armak> jsonFitxategianKargatu(File file) {
        ObservableList<Armak> obList = FXCollections.observableArrayList();
        try {
            JsonReader reader = Json.createReader(new FileReader(file));
            
            /* Fitxategiko array-a irakurri */
            JsonArray arrArma = reader.readArray();
            
            for (int i = 0; i<arrArma.size(); i++) {
               
                JsonObject cadarma = (JsonObject) arrArma.getJsonObject(i);

                Armak arma = new Armak(
                        cadarma.getString("Izena"),
                        cadarma.getString("Jatorria"), 
                        cadarma.getString("Description"),
                        Integer.parseInt(cadarma.getString("Urtea")));
                
    
                obList.add(arma);
            }
            return obList;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GestionListaEnMemoria.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return null;
    }
    public static ObservableList<Armak> jsonStreamDatuakKargatu(File file) {
        ObservableList<Armak> armaObList = FXCollections.observableArrayList();
        JsonParser parser = null;
        try {
            parser = Json.createParser(new FileReader(file));
            
            String izena = null, jatorria = null, desk = null;
            int urtea = 0;
            String key = "";
            String value = "";
            while (parser.hasNext()) {
                
                JsonParser.Event event = parser.next();
                switch (event) {
                    case START_ARRAY:
                    case END_ARRAY:
                    case START_OBJECT:
                        break;
                    case END_OBJECT:
                        /* Objektu berria sortu eta observableList-ean gehitu */
                        Armak arma = new Armak(izena, jatorria, desk, urtea);
                        armaObList.add(arma);
                        break;
                    case VALUE_FALSE:
                    case VALUE_NULL:
                    case VALUE_TRUE:
                        break;
                    case KEY_NAME:
                        /* Key-aren izena jaso */
                        key = parser.getString();
                        break;
                    case VALUE_STRING:
                    case VALUE_NUMBER:
                        /* Objektuaren balioa hartu, key-aren arabera */
                        value = parser.getString();
                        switch(key) {
                            case "Izena":
                                izena = value;
                                break;
                            case "Jatorria":
                                jatorria = value;
                                break;
                            case "Description":
                                desk = value;
                                break;
                            case "Urtea":
                                urtea = Integer.parseInt(value);
                                break;
                            
                        }
                        break;
                }
            }
            System.out.println("*****************************JSON fitxategia kargatu STREAM erabilita\n*****************************"); // Mezua, stream erabilita datuak kargatzen direla jakiteko
            return armaObList;
        } catch (FileNotFoundException ex) {
            System.err.println("Ez da fitxategia aurkitu.");
        }
        return null;        
    }
    

    public static void lista_gorde(ObservableList<Armak> armak, File file) {
        long fileSizeInBytes = file.length(); // fitxategiaren tamaina Byte-tan
                long fileSizeInKB = fileSizeInBytes/1024; // fitxategiaren tamaina KB-etan
                if (fileSizeInKB<2) 
                    jsonFitxategianGorde(armak, file);
                else
                    jsonStreamFitxategianGorde(armak, file);  //Stream erabilita 

    }
    public static void jsonFitxategianGorde(ObservableList<Armak> armak, File file){
        ObservableList<Armak> obList = FXCollections.observableArrayList();
        JsonWriter jsonWriter = null;
        
        try { 
            JsonArrayBuilder array = Json.createArrayBuilder(); // JsonArray-a sortu
            JsonObjectBuilder object = Json.createObjectBuilder(); // Json Objektua sortu
            
            for (Armak arma : armak) {
                /* Objektuak, elementu bakoitza gehitu */
                object.add("Izena", arma.getName()); 
                object.add("Jatorria", arma.getJatorria());
                object.add("Description", arma.getDesk());
                object.add("Urtea", String.valueOf(arma.getUrtea()));
                
                JsonObject jsonObj = object.build(); 
                array.add(jsonObj); // Objektuak array-era gehitu
            }
            JsonArray jsonArrayArma = array.build();
            jsonWriter = Json.createWriter(new FileOutputStream(file, false)); //JsonWriter-ekin, jsonArray-a fitxategian idazteko
            jsonWriter.write(jsonArrayArma); // datuak fitxategian idatzi
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GestionListaEnMemoria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void jsonStreamFitxategianGorde(ObservableList<Armak> oList, File file) {
        try{
            JsonGenerator jsonGen = Json.createGenerator(new FileWriter(file));
            jsonGen.writeStartArray(); // Array-a idazten hasi
            
            for (Armak arma : oList) {
                /* Objektuak, elementu bakoitza gehitu */
                jsonGen.writeStartObject()
                        .write("Izena", arma.getName())  
                        .write("Jatorria", arma.getJatorria())
                        .write("Description", arma.getDesk())
                        .write("Urtea", String.valueOf(arma.getUrtea()))
                    .writeEnd(); // objektua idazten bukatu
            } 
            
            jsonGen.writeEnd(); // Array-a idazten bukatu
            System.out.println("JSON fitxategian idatzi STREAM erabilita\n"); // Mezua, stream erabilita datuak kargatzen direla jakiteko
            jsonGen.close();
        } 
        catch (IOException ex) {
            System.err.println("Arazoak daude datuekin.");
        }
    }    
}