package org.example.motion;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;

// DetectorMovimento
public class DetectorMovimento implements WebcamMotionListener {

    private final WebcamMotionDetector detector;
    private final Runnable aoDetectar;

    public DetectorMovimento(Webcam webcam, Runnable aoDetectar) {
        this.aoDetectar = aoDetectar;
        detector = new WebcamMotionDetector(webcam);
        detector.setInterval(500);
        detector.addMotionListener(this);   // this, e não this::motionDetected
    }

    public void iniciar() {
        detector.start();
    }

    @Override
    public void motionDetected(WebcamMotionEvent wme) {
        System.out.println("Movimento detectado!");
        aoDetectar.run();
    }

}

