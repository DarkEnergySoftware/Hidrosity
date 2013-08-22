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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.des.hidrosity.enemies.Enemy;
import com.des.hidrosity.enemies.EnemyType;
import com.des.hidrosity.enemies.StationaryEnemy;
import com.des.hidrosity.player.Player;

public class LevelManager {

	@SuppressWarnings("unused")
	private final World gameWorld;

	private Texture currentLevelTexture;
	private int currentLevelNumber;

	private LevelImageLoader levelImageLoader;
	private LevelCreator levelCreator;

	public LevelManager(World gameWorld, int startingLevel) {
		this.gameWorld = gameWorld;

		levelImageLoader = new LevelImageLoader();
		levelCreator = new LevelCreator(gameWorld);

		setLevel(startingLevel);
	}

	public void setLevel(int levelNumber) {
		this.currentLevelNumber = levelNumber;

		loadLevelImage();
		loadLevelData();
	}

	private void loadLevelImage() {
		currentLevelTexture = levelImageLoader.loadLevelImage(currentLevelNumber);
	}

	private void loadLevelData() {
		levelCreator.createLevelFromXMLData(currentLevelNumber);
	}
	
	public Vector2 getPlayerSpawnPosition() {
		Vector2 playerSpawnPosition = new Vector2();
		
		try {
			DocumentBuilder docBuilder = createDocumentBuilder();

			Document xmlDocument = docBuilder.parse(new File("res/levels/data/" + "player" + currentLevelNumber + ".xml"));
			xmlDocument.getDocumentElement().normalize();

			NodeList nodeList = xmlDocument.getElementsByTagName("spawn");

			for (int i = 0; i < nodeList.getLength(); ++i) {
				Node currentNode = nodeList.item(i);

				if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
					Element currentElement = (Element) currentNode;

					float tempX;
					float tempY;

					tempX = Float.parseFloat(currentElement.getElementsByTagName("x").item(0).getTextContent());
					tempY = Float.parseFloat(currentElement.getElementsByTagName("y").item(0).getTextContent());
					
					playerSpawnPosition = new Vector2(tempX, tempY);
				}
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		return playerSpawnPosition;
	}
	
	public Array<Enemy> getEnemiesInLevel(Player player) {
		Array<Enemy> enemies = new Array<Enemy>();
		
		try {
			DocumentBuilder docBuilder = createDocumentBuilder();

			Document xmlDocument = docBuilder.parse(new File("res/levels/data/" + "enemies" + currentLevelNumber + ".xml"));
			xmlDocument.getDocumentElement().normalize();

			NodeList nodeList = xmlDocument.getElementsByTagName("enemy");

			for (int i = 0; i < nodeList.getLength(); ++i) {
				Node currentNode = nodeList.item(i);

				if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
					Element currentElement = (Element) currentNode;

					EnemyType enemyType;
					float tempX;
					float tempY;

					tempX = Float.parseFloat(currentElement.getElementsByTagName("x").item(0).getTextContent());
					tempY = Float.parseFloat(currentElement.getElementsByTagName("y").item(0).getTextContent());
					enemyType = EnemyType.valueOf(currentElement.getElementsByTagName("type").item(0).getTextContent());
					
					switch (enemyType) {
					case StationaryEnemy:
						enemies.add(new StationaryEnemy(new Vector2(tempX, tempY), "res/enemies/stationary enemy/left.png", player));
						break;
					default:
						break;
					}
				}
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		return enemies;
	}
	
	private DocumentBuilder createDocumentBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		return docBuilderFactory.newDocumentBuilder();
	}

	public void renderLevel(SpriteBatch spriteBatch) {
		spriteBatch.draw(currentLevelTexture, -Gdx.graphics.getWidth() / 2, -Gdx.graphics.getHeight() / 2,
				currentLevelTexture.getWidth() * 2, currentLevelTexture.getHeight() * 2);
	}
	
	public int getLevelWidth() {
		return currentLevelTexture.getWidth();
	}
	
	public int getLevelHeight() {
		return currentLevelTexture.getHeight();
	}
}
