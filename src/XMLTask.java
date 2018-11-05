import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class XMLTask {
    private void writeDoc(Document document, String path) throws RemoteException {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(path);
            transformer.transform(domSource, streamResult);
        } catch (TransformerException ex) {
            throw new RemoteException();
        }
    }
    public boolean checkAverage(String path, String outPath){
        Document document;
        boolean isReplaced=false;
        DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder= null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document=documentBuilder.parse(path);
            NodeList studentsList=document.getElementsByTagName("student");
            NodeList average;
            Node averNode;
            Element subject;
            Element student;
            NodeList subjects;
            NamedNodeMap attr;
            double countAver;
            double aver;

            for (int i = 0; i < studentsList.getLength(); i++) {
                countAver = 0;
                student = (Element) studentsList.item(i);
                subjects = student.getElementsByTagName("subject");

                for (int j = 0; j < subjects.getLength(); j++) {
                    subject = (Element)subjects.item(j);
                    countAver += Double.parseDouble(subject.getAttribute("mark"));
                }
                //todo
                countAver/=subjects.getLength();
                average = student.getElementsByTagName("average");
                if (average.getLength()!=1){
                       Element averEl=document.createElement("average");
                       averEl.setTextContent(Double.toString(countAver));
                    student.appendChild(averEl);
                                       isReplaced=true;
                }else {
                    averNode = average.item(0);
                    aver = Double.parseDouble(String.valueOf(averNode.getFirstChild().getNodeValue()));
                    if (aver != countAver) {
                        averNode.getFirstChild().setNodeValue(String.valueOf(countAver));
                        isReplaced = true;
                    }
                }
                writeDoc(document, outPath);
            }

            } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (SAXException e1) {
            e1.printStackTrace();
        }
        return isReplaced;
    }
    public static void main(String[] args) {
        System.out.println("Input name of files. inXml,outXml");
        Scanner scanner=new Scanner(System.in);

        String inFile="src\\group.xml";
        String outFile="src\\group2.xml";
        XMLTask xmlTask=new XMLTask();
        boolean check=xmlTask.checkAverage(inFile,outFile);
        System.out.println(check);
    }
}
/*src\group.xml,src\group2.xml*/