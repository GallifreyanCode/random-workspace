package be.gallifreyan.eclipse.archetype.plugin.mvn.creation;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.maven.cli.MavenCli;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import be.gallifreyan.eclipse.archetype.plugin.mvn.model.Archetype;

public class CustomArchetypes implements IRunnableWithProgress {
	public static String LOCATION = "D:\\catalog";

	/**
	 * Installs the archetypes defined within the catalog.
	 */
	public void run() {
		/* TODO: Uses list() method twice */
		for(Archetype archetype : list()){
			installArtifact(archetype);
		}
//		
//		File file = new File(LOCATION);
//		String[] args = new String[] {"install"};
//
//		MavenCli cli = new MavenCli();
//		cli.doMain(args, file.getAbsolutePath(), System.out, System.err);
	}
	
	public void installArtifact(Archetype archetype) {
		File file = new File(LOCATION);
		if(!isArchetypeInstalled(archetype)){
			MavenCli cli = new MavenCli();
			String[] args = new String[] {"org.apache.maven.plugins:maven-dependency-plugin:2.1:get",
					"-DrepoUrl=" + archetype.getRepository(),
					"-Dartifact=" + archetype.toString()
					};
			
			cli.doMain(args, file.getAbsolutePath(), System.out, System.err);
		}
	}
	
	private boolean isArchetypeInstalled(Archetype archetype) {
		boolean b = false;
		String location = composeLocation(archetype);
		System.out.println(location);
		if(new File(location).exists()){
			b = true;
		}
		return b;
	}
	
	private String composeLocation(Archetype archetype) {
		String mavenHome = System.getProperty("user.home") + "\\.m2\\repository";
		String localPath = mavenHome + "\\"
				+ archetype.getArchetypeGroupId().replace(".", "\\") + "\\"
				+ archetype.getArchetypeArtifactId().replace(".", "\\") + "\\"
				+ archetype.getArchetypeVersion();
		String localJar = archetype.getArchetypeArtifactId() + "-" + archetype.getArchetypeVersion() + ".jar";
		return localPath + "\\" + localJar;
	}

	/**
	 * Creates a list of all archetypes defined within the catalog.
	 * @return
	 */
	public static List<Archetype> list() {
		File file = new File(LOCATION);
		List<Archetype> archetypeList = new ArrayList<Archetype>();
		if (file.exists()) {
			File catalog = new File(LOCATION + "\\archetype-catalog.xml");
			if (catalog.exists()) {
				archetypeList = readXML(catalog);
			}
		}
		return archetypeList;
	}

	/**
	 * Parses the XML for MavenArchetypes.
	 * 
	 * @param catalog
	 * @return
	 */
	private static List<Archetype> readXML(File catalog) {
		List<Archetype> archetypeList = new ArrayList<Archetype>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			Document document = db.parse(catalog);
			document.getDocumentElement().normalize();

			if (document.getDocumentElement().getNodeName()
					.equals("archetype-catalog")) {
				NodeList rootArchetypeNode = document
						.getElementsByTagName("archetypes");
				Element rootArchetypeElement = (Element) rootArchetypeNode
						.item(0);
				NodeList archetypeNodes = rootArchetypeElement
						.getElementsByTagName("archetype");
				
				for (int i = 0; i < archetypeNodes.getLength(); i++) {
					Element element = (Element) archetypeNodes.item(i);
					Archetype archetype = createArchetypeFromNode(element);
					if (archetype != null) {
						archetypeList.add(archetype);
					}
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return archetypeList;
	}
	
	/**
	 * Creates a MavenArchetype object from an XML element.
	 * @param archetypeElement
	 * @return
	 */
	private static Archetype createArchetypeFromNode(Element archetypeElement) {
		Archetype archetype = null;
		String groupId = null, artifactId = null, version = null, repository = null, description = null;
		groupId = getNodeValue(archetypeElement, "groupId");
		artifactId = getNodeValue(archetypeElement, "artifactId");
		version = getNodeValue(archetypeElement, "version");
		repository = getNodeValue(archetypeElement, "repository");
		description = getNodeValue(archetypeElement, "description");
		archetype = new Archetype(groupId, artifactId, version, repository, description);
		return archetype;
	}

	/**
	 * Gets the value of a specified value within the XML element.
	 * @param archetypeElement
	 * @param tagName
	 * @return
	 */
	private static String getNodeValue(Element archetypeElement, String tagName) {
		NodeList nodeList = archetypeElement.getElementsByTagName(tagName);
		Element nodeElement = (Element) nodeList.item(0);
		NodeList valueNodeList = nodeElement.getChildNodes();
		Node valueNode = (Node) valueNodeList.item(0);
		String value = valueNode.getNodeValue().trim();
		return value;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		run();
	}
}