package com.des.hidrosity.animation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.jakehorsfield.libld.Utils;

public class AnimationLoader {

	public static Animation loadAnimation(float frameDuration, String animationFilePath) {
		Array<String> framePaths = loadFramePathsFromFile(animationFilePath);
		Array<TextureRegion> animationFrames = loadFramesFromPaths(framePaths);
		
		return new Animation(frameDuration, animationFrames);
	}
	
	private static Array<String> loadFramePathsFromFile(String filePath) {
		Array<String> filePaths = new Array<String>();
		
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(filePath))));
			String currentLine = null;
			
			while ((currentLine = in.readLine()) != null) {
				filePaths.add(currentLine);
			}
			
			in.close();
		} catch (IOException e) {
			System.err.println("There was an error opening the animation " + filePath);
		}
		
		return filePaths;
	}
	
	private static Array<TextureRegion> loadFramesFromPaths(Array<String> framePaths) {
		Array<TextureRegion> loadedFrames = new Array<TextureRegion>();
		
		for (String filePath : framePaths) {
			loadedFrames.add(new TextureRegion(Utils.loadTexture(filePath)));
		}
		
		return loadedFrames;
	}
}