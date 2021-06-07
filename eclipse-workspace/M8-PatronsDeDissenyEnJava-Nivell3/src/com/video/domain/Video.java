package com.video.domain;

import java.util.Date;
import java.util.List;

public class Video implements Runnable{
	
	enum VideoStatus{UPLOADING, VERIFYING, PUBLIC};
	enum PlayStatus{STOPPED, PAUSED, REPRODUCING};
	
	private int videoId;
	private String url;
	private String tittle;
	private int videoLength;
	private int actualPosition;
	private User owner;
	private List<Tag> tags;
	private Date uploadDate;
	private VideoStatus vs;
	private PlayStatus ps;
	private boolean isReproducing;
	private static int videosCount = 1;
	
	public Video(String url, String tittle, User owner, List<Tag> tags, Date uploadDate, int videoLength) {
		super();
		this.url = url;
		this.tittle = tittle;
		this.owner = owner;
		this.tags = tags;
		this.uploadDate = uploadDate;
		this.videoLength = videoLength;
		
		vs = VideoStatus.UPLOADING;
		
		videoId = videosCount;
		videosCount++;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTittle() {
		return tittle;
	}

	public void setTittle(String tittle) {
		this.tittle = tittle;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public int getVideoId() {
		return videoId;
	}

	public static int getVideosCount() {
		return videosCount;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	
	
	public VideoStatus getVs() {
		return vs;
	}

	public void setPsToRedproducing() {
		ps = PlayStatus.REPRODUCING;
	}

		
	public PlayStatus getPs() {
		return ps;
	}
	
	public int getActualPosition() {
		return actualPosition;
	}

	public void setActualPosition(int actualPosition) {
		this.actualPosition = actualPosition;
	}

	public int getVideoLength() {
		return videoLength;
	}


	//devuelve, en milisegundos, la diferencia de tiempo desde que se creó el vídeo hasta la fecha pasada por parámetro:
	public long checkVideoStatus(Date actualDate) {

		long difference;
		
		difference = actualDate.getTime() - uploadDate.getTime();
		
		return difference;
	}

	//actualiza el estado del vídeo en función del tiempo que ha pasado desde que se creó:
	public void updateStatus(Date actualDate) {

		if(checkVideoStatus(actualDate)<7500) {
			vs=VideoStatus.UPLOADING;
		}else if(checkVideoStatus(actualDate)<15000) {
			vs=VideoStatus.VERIFYING;
		}else {
			vs=VideoStatus.PUBLIC;
		}
	}
	
	
	//cambia el Video Status a PAUSED:
	public void pause() {
		ps = PlayStatus.PAUSED;
	}
	
	//cambia el Video Status a STOPPED y deja el tiempo a 0:
	public void stop() {
		ps = PlayStatus.STOPPED;
		actualPosition = 0;
	}
	
	//devuelve true si el vídeo está reproduciéndose:
	public boolean isAlreadyReproducing() {
		isReproducing = false;
		if(getPs()==PlayStatus.REPRODUCING) isReproducing = true;
		return isReproducing;
	}
	
	/*método que controla la reproducción.
	 * realiza un bucle FOR desde la posición actual (0 si acaba de iniciarse o si se le ha dado al STOP) hasta la duración final del vídeo
	 * si al comenzar el BUCLE, el vídeo se encuentra en los Play Status PAUSED o STOPPED, realiza un break y no entra en el bucle
	 * la posicion actual se va igualando a la i, que itera sobre el bucle FOR con un Thread.sleep de 1 segundo (1000), lo que 
	 * simula el paso de los segundos
	 * Cuando llega a la longitud final del vídeo, llama al metodo stop(), descrito anteriormente y cambia la variable booleana "isReproducing" a false
	 * */
	public void play() {
		ps = PlayStatus.REPRODUCING;
		for(int i=actualPosition;i<=videoLength;i++) {
			if(getPs()==PlayStatus.PAUSED) {
				break;
			}else if(getPs()==PlayStatus.STOPPED) {
				break;
			}
			try {
				actualPosition = i;
				Thread.sleep(1000);
				System.out.println("Video en el segundo " + i + " / " + videoLength);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(actualPosition==videoLength) {
			System.err.println("Se ha acabado el vídeo");
			stop();
			isReproducing = false;
		}
	}
	
	@Override
	public void run() {
		
		if(getPs()==PlayStatus.REPRODUCING) play();
		
	}

	@Override
	public String toString() {
		return "Video [videoId=" + videoId + ", url=" + url + ", tittle=" + tittle + ", owner=" + owner + ", tags="
				+ tags + ", uploadDate=" + uploadDate + ", vs=" + vs + "]";
	}
}
