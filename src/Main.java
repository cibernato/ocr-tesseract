import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import test2.OCR;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

public class Main {
    public JPanel panel1;
    private JButton procesarButton;
    private JButton subirImagenButton;
    private JLabel Imagen;
    private JTextArea ImagenProcesada;
    private JComboBox comboBox1;
    private JButton button1;
    private JButton correcionColor;
    private JButton limpiarButton;
    private JButton outlinedButton;
    private JButton originalButton;
    BufferedImage img = null;
    BufferedImage imgOriginal = null;
    BufferedImage imgOriginalPrincipal = null;


    public Main() {

        ImagenProcesada.setWrapStyleWord(true);
        ImagenProcesada.setLineWrap(true);
        ImagenProcesada.setEditable(false);
        comboBox1.addItem("Entrenado");
        comboBox1.addItem("No entrenado");

        subirImagenButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showOpenDialog(panel1);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                try {
                    img = ImageIO.read(new File(selectedFile.getAbsolutePath()));
                    imgOriginalPrincipal = img;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                Image dimg = img.getScaledInstance(Imagen.getWidth(), Imagen.getHeight(),
                        Image.SCALE_SMOOTH);
                Imagen.setIcon(new ImageIcon(dimg));
            }
        });
        procesarButton.addActionListener(e -> {
            try {
                if (img != null) {
                    imgOriginal = img;
                    Image dimg = img.getScaledInstance(Imagen.getWidth(), Imagen.getHeight(), Image.SCALE_SMOOTH);
                    Tesseract tesseract = new Tesseract();
                    String path = System.getProperty("user.dir").replace('\\', '/');
                    String fullPath = path + "/tessdata";
                    tesseract.setDatapath(fullPath);
                    String languaje;
                    if ("Entrenado".equals(comboBox1.getSelectedItem().toString())) {
                        languaje = "spa";
                    } else {
                        languaje = "tra";
                    }
                    tesseract.setLanguage(languaje);
                    String text = tesseract.doOCR(img);

                    System.out.print(text);
                    procesarImagen();
                    Thread.sleep(800);
                    ImagenProcesada.setText(text);
                    Imagen.setIcon(new ImageIcon(dimg));
                } else {
                    JOptionPane.showMessageDialog(null, "Suba una imagen para procesar",
                            "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
                }

            } catch (TesseractException | InterruptedException ex) {
                ImagenProcesada.setText(ex.getLocalizedMessage() + "    " + ex.getMessage());
                ex.printStackTrace();
            }
        });


        button1.addActionListener(e -> {
            try {
                BufferedImage newImage = OCR.procesar(img, 2);
                Image dimg = newImage.getScaledInstance(Imagen.getWidth(), Imagen.getHeight(),
                        Image.SCALE_SMOOTH);
                img = newImage;
                Imagen.setIcon(new ImageIcon(dimg));

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        correcionColor.addActionListener(e -> procesarImagen());
        limpiarButton.addActionListener(e -> {
            img = null;
            Imagen.setIcon(null);
            ImagenProcesada.setText("");
        });
        outlinedButton.addActionListener(e -> {
            try {
                BufferedImage newImage;
                newImage = OCR.procesar(img, 3);
                Image dimg = newImage.getScaledInstance(Imagen.getWidth(), Imagen.getHeight(),
                        Image.SCALE_SMOOTH);
                img = newImage;
                Imagen.setIcon(new ImageIcon(dimg));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        });
        originalButton.addActionListener(e -> {
            Image dimg = imgOriginalPrincipal.getScaledInstance(Imagen.getWidth(), Imagen.getHeight(),
                    Image.SCALE_SMOOTH);
            Imagen.setIcon(new ImageIcon(dimg));
            ImagenProcesada.setText("");
        });
    }

    public void procesarImagen() {
        double d = img.getRGB(img.getTileWidth() / 2, img.getTileHeight() / 2);

        try {
            if (d >= -1.4211511E7 && d < -7254228) {
                processImg(img, 3f, -10f);
            } else if (d >= -7254228 && d < -2171170) {
                processImg(img, 1.455f, -47f);
            } else if (d >= -2171170 && d < -1907998) {
                processImg(img, 1.35f, -10f);
            } else if (d >= -1907998 && d < -257) {
                processImg(img, 1.19f, 0.5f);
            } else if (d >= -257 && d < -1) {
                processImg(img, 1f, 0.5f);
            } else if (d >= -1 && d < 2) {
                processImg(img, 1f, 0.35f);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void processImg(BufferedImage ipimage, float scaleFactor, float offset) throws IOException {
        BufferedImage opimage = new BufferedImage(1050, 1024, ipimage.getType());
        Graphics2D graphic = opimage.createGraphics();
        graphic.drawImage(ipimage, 0, 0, 1050, 1024, null);
        graphic.dispose();
        RescaleOp rescale = new RescaleOp(scaleFactor, offset, null);
        BufferedImage temp = rescale.filter(opimage, null);
        img = temp;
        Image dimg = temp.getScaledInstance(Imagen.getWidth(), Imagen.getHeight(), Image.SCALE_SMOOTH);
        Imagen.setIcon(new ImageIcon(dimg));
    }


}
