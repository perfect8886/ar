package dsp.ar.crawler.module.gather;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import dsp.ar.Constants;
import dsp.ar.IAR;
import dsp.ar.domain.ActorEntity;
import dsp.ar.domain.AdEntity;
import dsp.ar.domain.AreaEntity;
import dsp.ar.domain.DirectorEntity;
import dsp.ar.domain.StrategyEntity;
import dsp.ar.domain.TypeEntity;
import dsp.ar.impl.ARImpl;

public class AdInfoGather {
	private String path;
	private IAR ar;

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
			} else if (line.startsWith(Constants.Tag.TITLE)) {
				String title = line.substring(line.indexOf(":") + 1);
				// System.out.println("名称：" + title);
				ad.setName(title);
			} else if (line.startsWith(Constants.Tag.IMAGE)) {
				String imageUrl = line.substring(line.indexOf(":") + 1);
				// System.out.println("图片：" + imageUrl);
				ad.setImageUrl(imageUrl);

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

				// set actors(0~2)
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

				// set type(only one)
				List<AdEntity> typeAds = new ArrayList<AdEntity>();
				List<TypeEntity> types = ar.getTypes();
				List<TypeEntity> typesChosen = new ArrayList<TypeEntity>();
				int typeSize = types.size();
				int randomType = (int) (Math.random() * typeSize);
				TypeEntity type = types.get(randomType);
				typeAds = ar.getTypeAds(type.getId());
				typeAds.add(ad);
				type.setAds(typeAds);
				typesChosen.add(type);
				ad.setTypes(typesChosen);

				// set area(only one)
				List<AdEntity> areaAds = new ArrayList<AdEntity>();
				List<AreaEntity> areas = ar.getAreas();
				List<AreaEntity> areasChosen = new ArrayList<AreaEntity>();
				int areaSize = areas.size();
				int randomArea = (int) (Math.random() * areaSize);
				AreaEntity area = areas.get(randomArea);
				areaAds = ar.getAreaAds(area.getId());
				areaAds.add(ad);
				area.setAds(areaAds);
				areasChosen.add(area);
				ad.setAreas(areasChosen);

				// set strategy
				StrategyEntity strategy = new StrategyEntity();
				strategy.setEndDayTime("235959");
				strategy.setStartDayTime("000000");
				strategy.setIpPrefix("192.168;127.0");
				strategy.setPublishEndDate("20120212000000");
				strategy.setPublishStartDate("20100818194959");

				ad.setStrategy(strategy);

				// set publish time
				ad.setPublishTime("20110818194530");
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