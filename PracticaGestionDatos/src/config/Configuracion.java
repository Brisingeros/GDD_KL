package config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.InputSource;

public class Configuracion {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static info parse() throws JAXBException, ParserConfigurationException, IOException, SAXException{ //Para leer el fichero de config

        JAXBContext jc = JAXBContext.newInstance(info.class);

        Unmarshaller unmar = jc.createUnmarshaller();

        info jugador = null;
        if(validate("resources/puzzle.xml")) //validamos el xml. Si es valido, cogemos la informacion
            
            jugador = (info) unmar.unmarshal(new File("resources/puzzle.xml"));

        return jugador;

    }
    
    public static PartidaXML parseXML(String obj){
        try {
            JAXBContext jc = JAXBContext.newInstance(PartidaXML.class);
            
            Unmarshaller unmar = jc.createUnmarshaller();
            
            StringReader reader = new StringReader(obj);
            
            PartidaXML party = (PartidaXML) unmar.unmarshal(reader);

            return party;
            
        } catch (JAXBException ex) {
            Logger.getLogger(Configuracion.class.getName()).log(Level.SEVERE, null, ex);
            
            return null;
        }
    }

    public static boolean validate(String xml)throws ParserConfigurationException, IOException, org.xml.sax.SAXException{
        
        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(true);
            factory.setNamespaceAware(true);

            SAXParser parser = factory.newSAXParser();

            XMLReader reader = parser.getXMLReader();
            reader.setErrorHandler(
                new ErrorHandler() {
                    public void warning(SAXParseException e) throws SAXException {
                      System.out.println("WARNING : " + e.getMessage()); // do nothing
                    }

                    public void error(SAXParseException e) throws SAXException {
                      System.out.println("ERROR : " + e.getMessage());
                      throw e;
                    }

                    public void fatalError(SAXParseException e) throws SAXException {
                      System.out.println("FATAL : " + e.getMessage());
                      throw e;
                    }
                }
            );
          
            reader.parse(new InputSource( xml ));
          
            return true;
            
        }
        catch (ParserConfigurationException pce) {
            
            throw pce;
          
        } 
        catch (IOException io) {
            
            throw io;
          
        }
        catch (SAXException se){
            
            return false;
          
        }
        
    }

}