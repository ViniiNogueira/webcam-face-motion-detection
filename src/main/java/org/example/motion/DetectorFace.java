package org.example.motion;

import com.github.sarxos.webcam.Webcam;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.function.Consumer;

public class DetectorFace {
    private final Webcam webcam;
    private final Consumer<Rectangle[]> aoDetectar;
    private final CascadeClassifier classificador;

    public DetectorFace(Webcam webcam, Consumer<Rectangle[]> aoDetectar) {
        this.webcam = webcam;
        this.aoDetectar = aoDetectar;
        this.classificador = new CascadeClassifier("haarcascade_frontalface_default.xml");
        if (classificador.empty()) {
            System.out.println("Não carregou o arquivo do classificador!");
        }
    }

    public void iniciar() {
        Thread t = new Thread(this::loop);
        t.setDaemon(true);
        t.start();
    }

    private void loop() {
        while (!Thread.currentThread().isInterrupted()) {
            BufferedImage img = webcam.getImage();
            if (img != null) {
                MatOfRect encontrados = new MatOfRect();
                classificador.detectMultiScale(paraCinza(img), encontrados);

                Rect[] rects = encontrados.toArray();
                Rectangle[] resultado = new Rectangle[rects.length];
                for (int i = 0; i < rects.length; i++) {
                    resultado[i] = new Rectangle(rects[i].x, rects[i].y, rects[i].width, rects[i].height);
                }
                aoDetectar.accept(resultado);
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    private Mat paraCinza(BufferedImage original) {
        BufferedImage bgr = new BufferedImage(
                original.getWidth(), original.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = bgr.createGraphics();
        g.drawImage(original, 0, 0, null);
        g.dispose();

        byte[] dados = ((DataBufferByte) bgr.getRaster().getDataBuffer()).getData();
        Mat colorido = new Mat(bgr.getHeight(), bgr.getWidth(), CvType.CV_8UC3);
        colorido.put(0, 0, dados);

        Mat cinza = new Mat();
        Imgproc.cvtColor(colorido, cinza, Imgproc.COLOR_BGR2GRAY);
        return cinza;
    }
}
