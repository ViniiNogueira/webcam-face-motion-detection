package org.example;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import org.example.UI.Tela;
import org.example.motion.DetectorFace;
import org.example.motion.DetectorMovimento;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        nu.pattern.OpenCV.loadLocally();

        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());

        SwingUtilities.invokeLater(() -> {
            Tela tela = new Tela(webcam);

            DetectorMovimento detectorMovimento = new DetectorMovimento(webcam, tela::mostrarMovimento);
            detectorMovimento.iniciar();

            DetectorFace detectorRosto = new DetectorFace(webcam, tela::atualizarRostos);
            detectorRosto.iniciar();
        });

    }
}