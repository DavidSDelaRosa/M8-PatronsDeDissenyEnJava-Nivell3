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
				contra = JOptionPane.showInputDialog(null, "Introduce tu contrase�a", "PASSWORD",
						JOptionPane.QUESTION_MESSAGE);

				user = new User(nombre, apellido, contra, new Date());
				JOptionPane.showMessageDialog(null, "Hola " + user.getName() + "!", "Welcome",
						JOptionPane.INFORMATION_MESSAGE);

				if (nombre.equals("") || apellido.equals("") || contra.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Los campos nombre, apellido y contrase�a no pueden estar vac�os", "ERROR_MESSAGE",
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
						"�Qu� acci�n quieres realizar? \n1. Subir v�deo \n2. Ver todos tus v�deos\n3. Buscar un video\n4. Salir",
						"", JOptionPane.QUESTION_MESSAGE));

				switch (opcion) {

				case 1:
					JOptionPane.showMessageDialog(null, "De acuerdo, vamos a subir un v�deo!", "UPLOAD",
							JOptionPane.INFORMATION_MESSAGE);
					JOptionPane.showMessageDialog(null,
							"Para subir un v�deo es necesario: \n - URL del v�deo\n - T�tulo del v�deo\n - Una lista de TAGs\n - Duracion (en segundos) del v�deo",
							"UPLOAD", JOptionPane.INFORMATION_MESSAGE);
					String urlVideo, tituloVideo;
					int numTags, videoLength;
					do {

						try {
							urlVideo = JOptionPane.showInputDialog(null, "Introduce la URL: ", "URL",
									JOptionPane.QUESTION_MESSAGE);
							tituloVideo = JOptionPane.showInputDialog(null, "Introduce el t�tulo: ", "T�TULO",
									JOptionPane.QUESTION_MESSAGE);
							numTags = Integer.parseInt(JOptionPane.showInputDialog(null,
									"�Cu�ntas TAGs quieres que tenga tu v�deo?", "", JOptionPane.QUESTION_MESSAGE));

							videoLength = Integer.parseInt(JOptionPane.showInputDialog(null,
									"�Cu�ntos segundos dura el v�deo?", "", JOptionPane.QUESTION_MESSAGE));

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
									"Has de introducir un n�mero para la cantidad de TAGs o para la duraci�n del v�deo. Los caracteres "
											+ "A-Z o una cadena vac�a son incorrectos",
									"ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
							urlVideo = "";
							tituloVideo = "";
							numTags = -1;
							break;
						} catch (EmptyFieldsException e) {
							JOptionPane.showMessageDialog(null, "Los campos URL o t�tulo no pueden estar vac�os",
									"ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
							urlVideo = "";
							tituloVideo = "";
							numTags = -1;
							break;
						}

					} while (urlVideo.equals("") || tituloVideo.equals("") || numTags < 0);

					break;

				case 2:
					JOptionPane.showMessageDialog(null, "Veamos qu� v�deos tienes subidos...!", "FIND VIDEO",
							JOptionPane.INFORMATION_MESSAGE);

					List<Video> videosForUser = controller.getUserVideoList(user);

					for (Video v : videosForUser) {
						Date actualDate = new Date();
						v.updateStatus(actualDate);
						if (v.checkVideoStatus(actualDate) < 15000) {
							JOptionPane.showMessageDialog(null, "Tu video: " + v.getTittle() + " se est� subiendo",
									"YOUR VIDEOS", JOptionPane.INFORMATION_MESSAGE);

						} else if (v.checkVideoStatus(actualDate) < 30000) {
							System.out.println("El v�deo se est� verificando");
							JOptionPane.showMessageDialog(null, "Tu video: " + v.getTittle() + " se est� verificando",
									"YOUR VIDEOS", JOptionPane.INFORMATION_MESSAGE);
						} else {
							System.out.println("El v�deo est� preparado!");
							JOptionPane.showMessageDialog(null, v.getTittle() + "\n" + v.getTags(), "YOUR VIDEOS",
									JOptionPane.INFORMATION_MESSAGE);
						}
					}

					break;

				case 3:
					String tituloVideoBuscar = JOptionPane.showInputDialog(null,
							"Introduce el t�tulo del v�deo que quieres buscar: ", "BUSCAR V�DEO",
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
						System.out.println("V�deo preparado...");
						Thread videoThread = new Thread(videoFound);
						while (seleccion != 3) {
							seleccion = JOptionPane.showOptionDialog(null, "�Qu� quieres hacer?",
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
									System.err.println("El v�deo ya est� reproduci�ndose!");
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
								"El v�deo est� subi�ndose o siendo verifiado, espere un momento", "Video not ready",
								JOptionPane.INFORMATION_MESSAGE);
					}

					break;

				case 4:
					int resp = JOptionPane.showConfirmDialog(null, "Est� seguro que desea cerrar?", "Confirmaci�n",
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