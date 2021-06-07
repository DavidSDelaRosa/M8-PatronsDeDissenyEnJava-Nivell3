package com.video.view;

import java.util.Date;
import java.util.*;

import javax.swing.JOptionPane;

import com.video.application.VideoController;
import com.video.domain.Tag;
import com.video.domain.User;
import com.video.domain.Video;
import com.video.exceptions.EmptyFieldsException;
import com.video.exceptions.NoVideoIncludedException;

public class AppVideo {

	private static VideoController controller = new VideoController();

	public static void main(String[] args) throws EmptyFieldsException {

		String nombre, apellido, contra;
		User user;
		do {
			try {
				JOptionPane.showMessageDialog(null, "Bienvenido!", "Welcome", JOptionPane.INFORMATION_MESSAGE);
				nombre = JOptionPane.showInputDialog(null, "Introduce tu nombre", "NOMBRE",
						JOptionPane.QUESTION_MESSAGE);
				apellido = JOptionPane.showInputDialog(null, "Introduce tu apellido", "APELLIDO",
						JOptionPane.QUESTION_MESSAGE);
				contra = JOptionPane.showInputDialog(null, "Introduce tu contraseña", "PASSWORD",
						JOptionPane.QUESTION_MESSAGE);

				user = new User(nombre, apellido, contra, new Date());
				JOptionPane.showMessageDialog(null, "Hola " + user.getName() + "!", "Welcome",
						JOptionPane.INFORMATION_MESSAGE);

				if (nombre.equals("") || apellido.equals("") || contra.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Los campos nombre, apellido y contraseña no pueden estar vacíos", "ERROR_MESSAGE",
							JOptionPane.ERROR_MESSAGE);
					throw new EmptyFieldsException();

				}
			} catch (EmptyFieldsException e) {
				e.printStackTrace();
				nombre = "";
				apellido = "";
				contra = "";
				user = null;
			}
		} while (nombre.equals("") || apellido.equals("") || contra.equals(""));

		int opcion;

		do {

			try {
				opcion = Integer.parseInt(JOptionPane.showInputDialog(null,
						"¿Qué acción quieres realizar? \n1. Subir vídeo \n2. Ver todos tus vídeos\n3. Buscar un video\n4. Salir",
						"", JOptionPane.QUESTION_MESSAGE));

				switch (opcion) {

				case 1:
					JOptionPane.showMessageDialog(null, "De acuerdo, vamos a subir un vídeo!", "UPLOAD",
							JOptionPane.INFORMATION_MESSAGE);
					JOptionPane.showMessageDialog(null,
							"Para subir un vídeo es necesario: \n - URL del vídeo\n - Título del vídeo\n - Una lista de TAGs\n - Duracion (en segundos) del vídeo",
							"UPLOAD", JOptionPane.INFORMATION_MESSAGE);
					String urlVideo, tituloVideo;
					int numTags, videoLength;
					do {

						try {
							urlVideo = JOptionPane.showInputDialog(null, "Introduce la URL: ", "URL",
									JOptionPane.QUESTION_MESSAGE);
							tituloVideo = JOptionPane.showInputDialog(null, "Introduce el título: ", "TÍTULO",
									JOptionPane.QUESTION_MESSAGE);
							numTags = Integer.parseInt(JOptionPane.showInputDialog(null,
									"¿Cuántas TAGs quieres que tenga tu vídeo?", "", JOptionPane.QUESTION_MESSAGE));

							videoLength = Integer.parseInt(JOptionPane.showInputDialog(null,
									"¿Cuántos segundos dura el vídeo?", "", JOptionPane.QUESTION_MESSAGE));

							if (urlVideo.equals("") || tituloVideo.equals("") || numTags < 0 || videoLength < 0)
								throw new EmptyFieldsException();
							List<Tag> userTags = controller.addTag(numTags);
							JOptionPane.showMessageDialog(null, "Todo listo!", "", JOptionPane.INFORMATION_MESSAGE);
							try {
								controller.createVideo(urlVideo, tituloVideo, user, userTags, videoLength);
							} catch (NoVideoIncludedException e) {
								JOptionPane.showMessageDialog(null, "NO VIDEO INCLUDED", "ERROR",
										JOptionPane.ERROR_MESSAGE);
								e.printStackTrace();
							}
							if (urlVideo.equals("") || tituloVideo.equals("") || numTags < 0 || videoLength < 0) {
								JOptionPane.showMessageDialog(null, "SOME FIELDS ARE EMPTY", "ERROR",
										JOptionPane.ERROR_MESSAGE);
							}
							break;

						} catch (NumberFormatException e) {
							JOptionPane.showMessageDialog(null,
									"Has de introducir un número para la cantidad de TAGs o para la duración del vídeo. Los caracteres "
											+ "A-Z o una cadena vacía son incorrectos",
									"ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
							urlVideo = "";
							tituloVideo = "";
							numTags = -1;
							break;
						} catch (EmptyFieldsException e) {
							JOptionPane.showMessageDialog(null, "Los campos URL o título no pueden estar vacíos",
									"ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
							urlVideo = "";
							tituloVideo = "";
							numTags = -1;
							break;
						}

					} while (urlVideo.equals("") || tituloVideo.equals("") || numTags < 0);

					break;

				case 2:
					JOptionPane.showMessageDialog(null, "Veamos qué vídeos tienes subidos...!", "FIND VIDEO",
							JOptionPane.INFORMATION_MESSAGE);

					List<Video> videosForUser = controller.getUserVideoList(user);

					for (Video v : videosForUser) {
						Date actualDate = new Date();
						v.updateStatus(actualDate);
						if (v.checkVideoStatus(actualDate) < 15000) {
							JOptionPane.showMessageDialog(null, "Tu video: " + v.getTittle() + " se está subiendo",
									"YOUR VIDEOS", JOptionPane.INFORMATION_MESSAGE);

						} else if (v.checkVideoStatus(actualDate) < 30000) {
							System.out.println("El vídeo se está verificando");
							JOptionPane.showMessageDialog(null, "Tu video: " + v.getTittle() + " se está verificando",
									"YOUR VIDEOS", JOptionPane.INFORMATION_MESSAGE);
						} else {
							System.out.println("El vídeo está preparado!");
							JOptionPane.showMessageDialog(null, v.getTittle() + "\n" + v.getTags(), "YOUR VIDEOS",
									JOptionPane.INFORMATION_MESSAGE);
						}
					}

					break;

				case 3:
					String tituloVideoBuscar = JOptionPane.showInputDialog(null,
							"Introduce el título del vídeo que quieres buscar: ", "BUSCAR VÍDEO",
							JOptionPane.QUESTION_MESSAGE);
					Video videoFound = null; // TODO CONTROLAR ESTO

					List<Video> videosForUser2 = controller.getUserVideoList(user);

					for (Video v : videosForUser2) {
						if (v.getTittle().equalsIgnoreCase(tituloVideoBuscar)) {
							JOptionPane.showMessageDialog(null, v.getTittle(), "VIDEO FOUND!",
									JOptionPane.INFORMATION_MESSAGE);
							videoFound = v;
						}
					}

					int seleccion = -1;

					if (videoFound.checkVideoStatus(new Date()) > 30000) {
						System.out.println("Vídeo preparado...");
						Thread videoThread = new Thread(videoFound);
						while (seleccion != 3) {
							seleccion = JOptionPane.showOptionDialog(null, "¿Qué quieres hacer?",
									"Selector de opciones", JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.QUESTION_MESSAGE, null, // null para icono por
																		// defecto.
									new Object[] { "play", "pause", "stop", "SALIR" }, // null para YES, NO y CANCEL
									" 1");

							if (seleccion == 0) {
								if (!videoFound.isAlreadyReproducing()) {
									videoFound.setPsToRedproducing();
									videoThread = new Thread(videoFound);
									videoThread.start();
								}else {
									System.err.println("El vídeo ya está reproduciéndose!");
								}

							} else if (seleccion == 1) {
								videoFound.pause();
								System.out.println(videoFound.getActualPosition());
								System.out.println(videoFound.getPs());
								// videoThread.start();

							} else if (seleccion == 2) {
								videoFound.stop();
								System.out.println(videoFound.getActualPosition());
								System.out.println(videoFound.getPs());

							} else if (seleccion == 3) {
								System.out.println("seleccionada SALIR");
								System.out.println(videoFound.getActualPosition());
								System.out.println(videoFound.getPs());
								break;

							} else {
								System.err.println("Error");
								break;
							}
						}
					} else {
						JOptionPane.showMessageDialog(null,
								"El vídeo está subiéndose o siendo verifiado, espere un momento", "Video not ready",
								JOptionPane.INFORMATION_MESSAGE);
					}

					break;

				case 4:
					int resp = JOptionPane.showConfirmDialog(null, "Está seguro que desea cerrar?", "Confirmación",
							JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if (resp == JOptionPane.YES_OPTION)
						break;
					opcion = 4;

				default:
					break;
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Has de seleccionar al menos una opcion", "WARNING_MESSAGE",
						JOptionPane.WARNING_MESSAGE);
				opcion = 2;
			}
		} while (opcion != 4);

	}

}