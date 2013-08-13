package com.des.hidrosity.levels;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.des.hidrosity.constants.GameConstants;
import com.des.hidrosity.debug.Logger;

public class LevelCreator {

	private final String LEVEL_DATA_DIR = "res/levels/data/";
	private final String DATA_PREFIX = "level";

	private World gameWorld;
	
	private Body currentBody;

	public LevelCreator(World gameWorld) {
		this.gameWorld = gameWorld;
	}

	public void createLevelFromXMLData(int levelNumber) {
		destroyAllExistingBodies();

		File levelXMLFile = new File(LEVEL_DATA_DIR + DATA_PREFIX + levelNumber + ".xml");
		Array<LevelBody> levelBodies = loadBodiesFromFile(levelXMLFile);

		for (LevelBody levelBody : levelBodies) {
			Logger.log(levelBody.toString());

			createBodyFromLevelBody(levelBody);
		}
	}

	private void createBodyFromLevelBody(LevelBody levelBody) {
		createMainFixture(levelBody);
		createLeftSideFixture(levelBody);
		createRightSideFixture(levelBody);
	}
	
	private void createRightSideFixture(LevelBody levelBody) {
		PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox(
				0.5f * GameConstants.UNIT_SCALE,
				(levelBody.height - (5 * GameConstants.UNIT_SCALE)) / 2,
				new Vector2(levelBody.width / 2, 0f),
				0f
				);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.friction = 0f;
		fixtureDef.shape = polygonShape;
		
		Fixture fixture = currentBody.createFixture(fixtureDef);
	}
	
	private void createLeftSideFixture(LevelBody levelBody) {
		PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox(
				0.5f * GameConstants.UNIT_SCALE,
				(levelBody.height - (5 * GameConstants.UNIT_SCALE)) / 2,
				new Vector2(-levelBody.width / 2, 0f),
				0f
				);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.friction = 0f;
		fixtureDef.shape = polygonShape;
		
		Fixture fixture = currentBody.createFixture(fixtureDef);
	}
	
	private void createMainFixture(LevelBody levelBody) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(levelBody.x + levelBody.width / 2, levelBody.y + levelBody.height / 2);
		
		PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox(levelBody.width / 2, levelBody.height / 2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		fixtureDef.friction = 100f;

		Body body = gameWorld.createBody(bodyDef);
		Fixture fixture = body.createFixture(fixtureDef);
		fixture.setUserData("level");
		
		polygonShape.dispose();
		
		currentBody = body;
	}

	private Array<LevelBody> loadBodiesFromFile(File levelXMLFile) {
		Array<LevelBody> levelBodies = new Array<>();

		try {
			DocumentBuilder docBuilder = createDocumentBuilder();

			Document xmlDocument = docBuilder.parse(levelXMLFile);
			xmlDocument.getDocumentElement().normalize();

			NodeList nodeList = xmlDocument.getElementsByTagName("body");

			for (int i = 0; i < nodeList.getLength(); ++i) {
				Node currentNode = nodeList.item(i);

				if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
					Element currentElement = (Element) currentNode;

					float tempX;
					float tempY;
					float tempWidth;
					float tempHeight;

					tempX = Float.parseFloat(currentElement.getElementsByTagName("x").item(0).getTextContent());
					tempY = Float.parseFloat(currentElement.getElementsByTagName("y").item(0).getTextContent());
					tempWidth = Float.parseFloat(currentElement.getElementsByTagName("width").item(0).getTextContent());
					tempHeight = Float.parseFloat(currentElement.getElementsByTagName("height").item(0)
							.getTextContent());

					LevelBody newLevelBody = new LevelBody(tempX, tempY, tempWidth, tempHeight);
					levelBodies.add(newLevelBody);
				}
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		return levelBodies;
	}

	private DocumentBuilder createDocumentBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		return docBuilderFactory.newDocumentBuilder();
	}

	private void destroyAllExistingBodies() {

	}

}
