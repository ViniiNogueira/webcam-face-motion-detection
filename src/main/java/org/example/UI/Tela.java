package org.example.UI;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

// Tela
public class Tela {
    private final JLabel status = new JLabel("Sem movimento", SwingConstants.CENTER);
    private final Timer limpaStatus;
    private volatile Rectangle[] rostos = new Rectangle[0];

    public Tela(Webcam webcam) {
        WebcamPanel panel = new WebcamPanel(webcam);

        WebcamPanel.Painter padrao = panel.getDefaultPainter();

        panel.setPainter(new WebcamPanel.Painter() {
            @Override
            public void paintPanel(WebcamPanel p, Graphics2D g2) {
                padrao.paintPanel(p, g2);
            }

            @Override
            public void paintImage(WebcamPanel p, BufferedImage img, Graphics2D g2) {
                padrao.paintImage(p, img, g2);   // desenha o vídeo normal

                g2.setColor(Color.GREEN);
                g2.setStroke(new BasicStroke(3));
                for (Rectangle r : rostos) {
                    int x = img.getWidth() - r.x - r.width;   // inverte o X por causa do espelhamento
                    g2.drawRect(x, r.y, r.width, r.height);
                }
            }
        });

        panel.setFPSDisplayed(true);
        panel.setImageSizeDisplayed(true);
        panel.setMirrored(true);

        status.setOpaque(true);
        status.setFont(status.getFont().deriveFont(Font.BOLD, 18f));

        limpaStatus = new Timer(1000, e -> {
            status.setText("Sem movimento");
            status.setBackground(null);
        });
        limpaStatus.setRepeats(false);

        JFrame window = new JFrame("WebCam Java");
        window.setLayout(new BorderLayout());
        window.add(panel, BorderLayout.CENTER);
        window.add(status, BorderLayout.SOUTH);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);
    }

    public void mostrarMovimento() {
        SwingUtilities.invokeLater(() -> {
            status.setText("MOVIMENTO DETECTADO!");
            status.setBackground(Color.RED);
            limpaStatus.restart();
        });
    }

    //metodo de rosto
    public  void atualizarRostos(Rectangle[] novos) {
        rostos = novos.clone();

    }

}
