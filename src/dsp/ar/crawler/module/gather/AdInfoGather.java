package dsp.ar.crawler.module.gather;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import dsp.ar.crawler.IAR;
import dsp.ar.crawler.domain.ActorEntity;
import dsp.ar.crawler.domain.AdEntity;
import dsp.ar.crawler.domain.DirectorEntity;
import dsp.ar.crawler.domain.TypeEntity;
import dsp.ar.crawler.impl.ARImpl;

public class AdInfoGather {
	private String path;
	private IAR ar;

	private static final String TITLE_TAG = "标题";
	private static final String IMAGE_TAG = "图片";

	public AdInfoGather(String path) {
		this.path = path;
		ar = new ARImpl();
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void process() throws IOException {
		File file = new File(this.getPath());
		if (!file.exists()) {
			System.out.println("Path Error!");
			return;
		}
		System.out.println("File size : " + file.length());
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)));
		String line = null;
		AdEntity ad = new AdEntity();
		while ((line = br.readLine()) != null) {
			if (line.trim().equalsIgnoreCase("")) {
				if (ad.getName() != null && ad.getName().trim().length() > 0) {
					System.out.println("ad : " + ad.getName() + "create!");
					ar.CreateAdEntity(ad);
				}
				ad = null;
				ad = new AdEntity();
			} else if (line.startsWith(TITLE_TAG)) {
				String title = line.substring(line.indexOf(":") + 1);
				System.out.println("名称：" + title);
				ad.setName(title);
			} else if (line.startsWith(IMAGE_TAG)) {
				String imageUrl = line.substring(line.indexOf(":") + 1);
				System.out.println("图片：" + imageUrl);
				ad.setImageUrl(imageUrl);

				// set area
				String area = "中国";
				ad.setArea(area);

				// set publish time
				String publishTime = "2011-8-15 21:00";
				ad.setPublishTime(publishTime);

				// set director(only one)
				List<AdEntity> directorAds = new ArrayList<AdEntity>();
				List<DirectorEntity> directors = ar.getDirectors();
				List<DirectorEntity> directorsChosen = new ArrayList<DirectorEntity>();
				int directorSize = directors.size();
				int randomDirector = (int) (Math.random() * directorSize);
				DirectorEntity director = directors.get(randomDirector);
				directorAds = ar.getDirectorAds(director.getId());
				directorAds.add(ad);
				director.setAds(directorAds);
				directorsChosen.add(director);
				ad.setDirectors(directorsChosen);

				// set actors(0~3)
				List<ActorEntity> actors = ar.getActors();
				List<ActorEntity> actorsChosen = new ArrayList<ActorEntity>();
				int actorSize = actors.size();
				int ranActorNum = (int) (Math.random() * 3);
				for (int i = 0; i < ranActorNum; i++) {
					int randomActor = (int) (Math.random() * actorSize);
					ActorEntity actor = actors.get(randomActor);
					boolean added = false;
					List<AdEntity> actorAds = new ArrayList<AdEntity>();
					for (ActorEntity actorEach : actorsChosen) {
						if (actorEach.getId() == actor.getId()) {
							i--;
							added = true;
							break;
						}
					}
					if (!added) {
						actorAds = ar.getActorAds(actor.getId());
						actorAds.add(ad);
						actor.setAds(actorAds);
						actorsChosen.add(actor);
					}
				}
				ad.setActors(actorsChosen);

				// set type(1~2)
				List<TypeEntity> types = ar.getTypes();
				List<TypeEntity> typesChosen = new ArrayList<TypeEntity>();
				int typeSize = types.size();
				int ranTypeNum = (int) (Math.random() + 1);
				for (int i = 0; i < ranTypeNum; i++) {
					int randomType = (int) (Math.random() * typeSize);
					TypeEntity type = types.get(randomType);
					boolean added = false;
					List<AdEntity> typeAds = new ArrayList<AdEntity>();
					for (TypeEntity typeEach : typesChosen) {
						if (typeEach.getId() == type.getId()) {
							i--;
							added = true;
							break;
						}
					}
					if (!added) {
						typeAds = ar.getTypeAds(type.getId());
						typeAds.add(ad);
						type.setAds(typeAds);
						typesChosen.add(type);
					}
				}
				ad.setTypes(typesChosen);
			}
		}
	}

	public static void main(String[] args) {
		AdInfoGather gather = new AdInfoGather("D:/temp/ad/adRecord0.txt");
		try {
			gather.process();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
