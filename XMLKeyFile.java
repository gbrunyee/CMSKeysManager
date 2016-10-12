package CMSKeysManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

public class XMLKeyFile
{
	String fileNamePath;

	Document doc;

	public CMSKey findKey(String findKeySection, String findKeyName)
	{
		CMSKey key1 = new CMSKey();
		boolean found = false;
		NodeList sectionList = doc.getElementsByTagName("wcm:list");
		for (int i = 0; i < sectionList.getLength(); i++)
		{
			String sectionName = sectionList.item(i).getAttributes().getNamedItem("name").getNodeValue();
			if (sectionName.equals(findKeySection))
			{
				List<CMSKey> sectionKeysArray = this.getSectionKeys(sectionName, sectionList.item(i));

				for (int j = 0; j < sectionKeysArray.size(); j++)
				{
					if (sectionKeysArray.get(j).getName().equals(findKeyName))
					{
						key1.keyCopy(sectionKeysArray.get(j));
						found = true;
					}
				}
			}
		}
		if (!found)
		{
			key1 = null;
		}

		return key1;
	}

	protected List<CMSKey> getSectionKeys(String sectionName, Node sectionNode)
	{
		List<CMSKey> sectionKeys = new ArrayList<CMSKey>();
		NodeList keyNodeList = sectionNode.getChildNodes();
		//		System.out.println("Key Count: " + (keyNodeList.getLength() - 1) / 2);
		//		System.out.println("--------------------------------------------------------");

		for (int j = 0; j < keyNodeList.getLength(); j++)
		{
			Node innerNode = keyNodeList.item(j);
			if (innerNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element eElement = (Element) innerNode;
				int k = 0;
				String keyName = eElement.getElementsByTagName("wcm:element").item(k++).getTextContent();
				String keyTitle = null;
				if (sectionName.equals("FieldLabelList") || sectionName.equals("PageSectionContentList"))
				{
					keyTitle = eElement.getElementsByTagName("wcm:element").item(k++).getTextContent();
				}
				String keyDescription = eElement.getElementsByTagName("wcm:element").item(k++).getTextContent();
				String keyComments = eElement.getElementsByTagName("wcm:element").item(k).getTextContent();

				CMSKey key1 = new CMSKey();
				key1.setKey(keyName, keyTitle, keyDescription, keyComments);
				sectionKeys.add(key1);
			}
		}

		return sectionKeys;
	}

	public void Merge(XMLKeyFile sourceFile)
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		NodeList sectionList = doc.getElementsByTagName("wcm:list");
		for (int i = 0; i < sectionList.getLength(); i++)
		{
			int mismatchCount = 0;
			String sectionName = sectionList.item(i).getAttributes().getNamedItem("name").getNodeValue();
			System.out.println("MERGE SECTION NAME: |" + sectionName + "| ");
			List<CMSKey> sectionKeysArray = this.getSectionKeys(sectionName, sectionList.item(i));
			System.out.println("Key Count: " + sectionKeysArray.size());
			System.out.println("--------------------------------------------------------");

			for (int j = 0; j < sectionKeysArray.size(); j++)
			{
				CMSKey iterationDestKey = sectionKeysArray.get(j);
				String iterationDestKeyName = iterationDestKey.getName();
				CMSKey sourceKey = sourceFile.findKey(sectionName, iterationDestKeyName);
				if (sourceKey == null)
				{
					System.out.println("WARNING: deleted key " + sectionName + "." + iterationDestKeyName);
				}
				else
				{
					if (!iterationDestKey.equals(sourceKey))
					{
						mismatchCount++;
						boolean mergeChoiceUser = iterationDestKey.mergeDecision(sectionName, sourceKey, br,
								"C:\\Users\\gbrunyee\\Desktop\\MergeKeys.log");
						if (mergeChoiceUser)
						{
							System.out.println("Merge choice was YES");
						}
						else
						{
							System.out.println("Merge choice was NO");
						}

					}
				}

			}
			if (mismatchCount > 0)
			{
				System.out.println("========================================================");
			}
			System.out.println("Mismatch count is " + mismatchCount);
			System.out.println("========================================================");
		}
		try
		{
			br.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public void initXMLKeyFile(String initFileNamePath)
	{
		fileNamePath = initFileNamePath;

		try
		{
			File fXmlFile = new File(initFileNamePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
			// doc.getDocumentElement().normalize();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public Document getDoc()
	{
		return this.doc;
	}

	public void copyXMLKeyFile(XMLKeyFile srcFile)
	{
		this.doc = srcFile.getDoc();
	}

	public void displayDoc()
	{
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

					System.out.println(
							(j + 1) / 2 + " Field Description: " + eElement.getElementsByTagName("wcm:element").item(k++).getTextContent());
					System.out.println((j + 1) / 2 + " Comments: " + eElement.getElementsByTagName("wcm:element").item(k).getTextContent());
				}
			}
			System.out.println("========================================================");
		}
	}

	public void writeFile(String fName)
	{
		try
		{
			File f = new File(fName);

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
	}

	public void insertKey()
	{
		System.out.println("----------------------------------");
		System.out.println("insertKey still to be implemented!");
		System.out.println("----------------------------------");
	}
}
