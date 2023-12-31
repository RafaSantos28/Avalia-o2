package io.sim;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Itinerary {

	private boolean on;
	private String uriItineraryXML;
	private String[] itinerary;
	private String idItinerary;
	private ArrayList<Route> rotas;

	public Itinerary(String _uriRoutesXML) {
		this.uriItineraryXML = _uriRoutesXML;
		this.idItinerary = "Rota";
		rotas = new ArrayList<Route>();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(this.uriItineraryXML);
			NodeList nList = doc.getElementsByTagName("vehicle");
			
			Node nNode = nList.item(0);
			Element elem = (Element) nNode;
			
			
			for (int i = 0; i < 100; i++) {
				String idRouteAux = idItinerary+(i+1);
				Node node = elem.getElementsByTagName("route").item(0);
				Element edges = (Element) node;
				this.itinerary = new String[] { idRouteAux, edges.getAttribute("edges") };
			
				rotas.add(new Route(idRouteAux, edges.getAttribute("edges")));
			}

			Thread.sleep(500); ///////////////////////////////////////////
			this.on = true;

		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Route> getRotas() {
		return rotas;
	}

	public String getUriItineraryXML() {
		return this.uriItineraryXML;
	}

	public String[] getItinerary() {
		return this.itinerary;
	}

	public String getIdItinerary() {
		return this.idItinerary;
	}

	public boolean isOn() {
		return this.on;
	}
}