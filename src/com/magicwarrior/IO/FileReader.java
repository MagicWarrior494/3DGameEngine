package com.magicwarrior.IO;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.magicwarrior.mesh.Mesh;
import com.magicwarrior.mesh.Triangle;

public class FileReader {

	Mesh mesh = new Mesh();

	public void readFile(Path myObj) {

		List<String> list = new ArrayList<>();
		List<String> triangle = new ArrayList<>();
		List<String> vertex = new ArrayList<>();

		try (Stream<String> lines = Files.lines(myObj)) {

			list = lines.filter(l -> !l.startsWith("#")).filter(l -> !l.startsWith("s")).map(String::toUpperCase)
					.collect(Collectors.toList());
			
		} catch (Exception e) {

		}

		triangle = list.stream().filter(l -> l.startsWith("F")).collect(Collectors.toList());
		vertex = list.stream().filter(g -> g.startsWith("V")).collect(Collectors.toList());
		
		for (String t : triangle) {
			String[] vertNum = t.split(" ");
			
			Triangle temp = new Triangle(vertex.get((Integer.parseInt(vertNum[1]) - 1)/1),
					vertex.get((Integer.parseInt(vertNum[2]) - 1)/1), vertex.get((Integer.parseInt(vertNum[3]) - 1)/1));

			mesh.mesh.add(temp);
		}
		
	}

	public Mesh getMesh() {
		System.out.println(mesh.getSize());
		return mesh;
	}
}
