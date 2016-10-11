import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReadXMLFile
{
	public static void main(String argv[])
	{
		try
		{
			File fXmlFile = new File("C:\\Users\\gbrunyee\\Desktop\\CMS_Test\\ES_GENERAL.devt.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			//optional, but recommended - read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			NodeList sectionList = doc.getElementsByTagName("wcm:list");
			for (int i = 0; i < sectionList.getLength(); i++)
			{
				String sectionName = sectionList.item(i).getAttributes().getNamedItem("name").getNodeValue();
				NodeList keyList = sectionList.item(i).getChildNodes();
				System.out.println("SECTION NAME: |" + sectionName + "| " + "Key Count: " + (keyList.getLength() - 1) / 2);
				System.out.println("--------------------------------------------------------");

				for (int j = 0; j < keyList.getLength(); j++)
				{
					Node innerNode = keyList.item(j);
					if (innerNode.getNodeType() == Node.ELEMENT_NODE && j <= 2)
					{
						Element eElement = (Element) innerNode;
						int k = 0;
						System.out.println(
								(j + 1) / 2 + " Key: " + eElement.getElementsByTagName("wcm:element").item(k++).getTextContent());
						if (sectionName.equals("FieldLabelList") || sectionName.equals("PageSectionContentList"))
						{
							System.out.println((j + 1) / 2 + " Title: " + eElement.getElementsByTagName("wcm:element").item(k++).getTextContent());
						}

						// 2. CREATE A NEW KEY
						if (sectionName.equals("FieldLabelList"))
						{
							Node sectionNode = sectionList.item(i);
							Element newKey = doc.createElement("wcm:row");
							Element newElement1 = doc.createElement("wcm:element");
							newElement1.setAttribute("name", "field_name");
							newElement1.appendChild(doc.createTextNode("GENERAL_MY_NEW_KEY"));
							newKey.appendChild(newElement1);
							Element newElement2 = doc.createElement("wcm:element");
							newElement2.setAttribute("name", "title");
							newElement2.appendChild(doc.createTextNode("Yes Please"));
							newKey.appendChild(newElement2);
							sectionNode.appendChild(newKey);
						}

						System.out.println(
								(j + 1) / 2 + " Field Description: " + eElement.getElementsByTagName("wcm:element").item(k++).getTextContent());
						System.out.println((j + 1) / 2 + " Comments: " + eElement.getElementsByTagName("wcm:element").item(k).getTextContent());
					}
				}
				System.out.println("========================================================");
			}

			// 3. WRITE OUT TO FILE
			File f = new File("C:\\Users\\gbrunyee\\Desktop\\CMS_Test\\ES_GENERAL.new.xml");

			// Use a Transformer for output
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(f);
			transformer.transform(source, result);
		}
		catch (TransformerConfigurationException tce)
		{
			System.out.println("* Transformer Factory error");
			System.out.println(" " + tce.getMessage());

			Throwable x = tce;
			if (tce.getException() != null)
				x = tce.getException();
			x.printStackTrace();
		}
		catch (TransformerException te)
		{
			System.out.println("* Transformation error");
			System.out.println(" " + te.getMessage());

			Throwable x = te;
			if (te.getException() != null)
				x = te.getException();
			x.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
