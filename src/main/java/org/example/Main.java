package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) throws ParserConfigurationException, TransformerException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element staff = document.createElement("staff");
        document.appendChild(staff);
        Element employee = document.createElement("employee");
        staff.appendChild(employee);
        Element id = document.createElement("id");
        id.appendChild(document.createTextNode("1"));
        employee.appendChild(id);
        Element firstName = document.createElement("firstName");
        firstName.appendChild(document.createTextNode("John"));
        employee.appendChild(firstName);
        Element lastName = document.createElement("lastName");
        lastName.appendChild(document.createTextNode("Smith"));
        employee.appendChild(lastName);
        Element country = document.createElement("country");
        country.appendChild(document.createTextNode("USA"));
        employee.appendChild(country);
        Element age = document.createElement("age");
        age.appendChild(document.createTextNode("25"));
        employee.appendChild(age);

        Element employee2 = document.createElement("employee");
        staff.appendChild(employee2);
        id = document.createElement("id");
        id.appendChild(document.createTextNode("2"));
        employee2.appendChild(id);
        firstName = document.createElement("firstName");
        firstName.appendChild(document.createTextNode("Inav"));
        employee2.appendChild(firstName);
        lastName = document.createElement("lastName");
        lastName.appendChild(document.createTextNode("Petrov"));
        employee2.appendChild(lastName);
        country = document.createElement("country");
        country.appendChild(document.createTextNode("RU"));
        employee2.appendChild(country);
        age = document.createElement("age");
        age.appendChild(document.createTextNode("23"));
        employee2.appendChild(age);

        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File("data.xml"));
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(domSource, streamResult);

        List<Employee> list = parseXML("data.xml");
        String json = listToJson(list);
        writeString(json, "data.json");


    }

    public static List<Employee> parseXML(String filename) throws ParserConfigurationException, SAXException, IOException {
            List<Employee> list = new ArrayList<>();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(filename));
            document.getDocumentElement().normalize();
            NodeList nodeList = document.getElementsByTagName("employee");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    long id = Long.parseLong(getTagValue("id", element));
                    String firstName = getTagValue("firstName", element);
                    String lastName = getTagValue("lastName", element);
                    String country = getTagValue("country", element);
                    int age = Integer.parseInt(getTagValue("age", element));

                    Employee employee = new Employee(id, firstName, lastName, country, age);
                    list.add(employee);
                }
            }
            return list;
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(list);
        return json;
    }

    private static void writeString(String json, String filePath) {
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }
}