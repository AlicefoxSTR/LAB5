package com.company;
import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;
import javax.swing.JFileChooser.*;
import javax.swing.filechooser.*;
import javax.imageio.ImageIO.*;
import java.awt.image.*;
import java.io.File;
import java.io.FilenameFilter;


/**
 * Этот класс позволяет исследовать различные области фрактала с помощью
 * создания и отображения графического интерфейса Swing и обработки событий, вызванных
 * взаимодействием приложения с пользователем.
 */
public class FractalExplorer {
    private int displaySize; //Целочисленный размер дисплея

    //Для обновления отображения в разных
    // методах в процессе вычисления фрактала
    private JImageDisplay display;

    //Для использования ссылки на базовый
    //класс для отображения других видов фракталов
    private FractalGenerator fractal;

    //Для  указания диапазона комплексной
    //плоскости, которая выводится на экран
    private Rectangle2D.Double range;

    /*
     * Конструктор, который принимает значение
     * размера отображения в качестве аргумента, затем сохраняет это значение в
     * соответствующем поле, а также инициализирует объекты диапазона и
     * фрактального генератора
     */
    public FractalExplorer(int size) {
        displaySize = size;
        //Инициализация
        fractal = new Mandelbrot();
        range = new Rectangle2D.Double();
        fractal.getInitialRange(range);
        display = new JImageDisplay(displaySize, displaySize);
    }

    /*
     * Метод, который инициализирует
     * графический интерфейс Swing: JFrame, содержащий объект JimageDisplay, и
     * кнопку для сброса отображения
     */
    public void createAndShowGUI() {
        // Установливает рамку, чтобы использовать java.awt.BorderLayout
        display.setLayout(new BorderLayout());
        JFrame frame = new JFrame("Fractal Explorer");

        //Добавляет объект отображения изображения в позицию в BorderLayout.CENTER
        frame.add(display, BorderLayout.CENTER);

        //Кнопка сброса
        JButton resetBtn = new JButton("Reset Display");

        //Экземпляр обработчика сброса в кнопке сброса.
        //Button reset = new Button();
        resetBtn.addActionListener(new Button());
        //frame.add(resetBtn, BorderLayout.NORTH);

        //Экзмепляр MouseHandler
        MouseHandler click = new MouseHandler();
        display.addMouseListener(click);

        //Обеспечение операции закрытия окна по умолчанию
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Устанавливает Combobox
        JComboBox myComboBox = new JComboBox();

        //Добавление фракталов в список (Combobox)
        FractalGenerator Mandelbrot = new Mandelbrot();
        myComboBox.addItem(Mandelbrot);
        FractalGenerator Tricorn = new Tricorn();
        myComboBox.addItem(Tricorn);
        FractalGenerator BurningShip = new BurningShip();
        myComboBox.addItem(BurningShip);

        // Экземляр кнопки со списком
        Button VariantionFractal = new Button();
        myComboBox.addActionListener(VariantionFractal);

        //Объект JPanel, объект JLabel и JComboBox, панель
        //на позиции south на вашем макете окна
        JPanel myPanel = new JPanel();
        JLabel myLabel = new JLabel("Select a fractal:");
        myPanel.add(myLabel);
        myPanel.add(myComboBox);
        frame.add(myPanel, BorderLayout.SOUTH);

        // Добавление кнопки сохранения
        JButton saveBtn = new JButton("Save Image");
        // Добавление кнопки сохранения и кнопки сброса в JPanel на позицию north
        JPanel PanelButton = new JPanel();
        PanelButton.add(saveBtn);
        PanelButton.add(resetBtn);
        frame.add(PanelButton, BorderLayout.NORTH);


        //Экземпляр обработчика сохранения в кнопке сохранения.
        ActionListener save = new Button();
        saveBtn.addActionListener(save);

        /**
         * Правильно разместит содержимое окна, сделает ее видимой и
         * запретит изменение размера окна.
         */
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    /*
     * Вспомогательный метод с типом доступа private для отображения фрактала. Этот метод циклически проходит
     * через каждый пиксель в отображении и вычисляет количество
     * итераций для соответствующих координат.
     * Если число итераций равно -1, устанавливает цвет пикселя в черный цвет.
     * В противном случае выберает значение, основанное на количестве итераций.
     * Обновляет дисплей цветом для каждого пикселя и перекрашивает его.
     * Отображает изображение, когда все пиксели были закрашены.
     */
    private void drawFractal() {
        // Цикл через каждый пиксель
        for (int x = 0; x < displaySize; x++) {
            for (int y = 0; y < displaySize; y++) {

                // Нахождение соответствующих координат (xCoord и yCoord)
                double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize, x);
                double yCoord = fractal.getCoord(range.y, range.y + range.height, displaySize, y);

                //Вычисление количества итераций в области отображения фрактала
                int iter = fractal.numIterations(xCoord, yCoord);

                //Если число итераций равно -1, устанавливает пиксель в черный цвет
                if (iter == -1) {
                    display.drawPixel(x, y, 0);
                } else {
                    // В ином случае устанавливает значение оттенка на основе числа итераций (из методички)
                    float hue = 0.7f + (float) iter / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);

                    //Обновление дисплея
                    display.drawPixel(x, y, rgbColor);
                }

            }
        }
        //Отображает изображение, когда все пиксели закрашены
        display.repaint();
    }

    /**
     * Внутренний класс для обработки событий java.awt.event.ActionListener
     */
    private class Button implements ActionListener {
        // Проверка выборки (источникка)
        public void actionPerformed(ActionEvent e) {
            String action = e.getActionCommand();

            //Если источник принадлежит (является) Combobox,
            //выберите фрактал и отобразите его
            if (e.getSource() instanceof JComboBox) {
                JComboBox Source = (JComboBox) e.getSource();
                fractal = (FractalGenerator) Source.getSelectedItem(); //возвращает выбранный пункт
                fractal.getInitialRange(range);
                drawFractal();
            }

            //Если источником является кнопка Сохранить, сохраните текущий фрактал
            else if (action.equals("Save Image")) {
                //Разрешение пользователю выбрать файл для сохранения
                JFileChooser myFile = new JFileChooser();
                //Сохранять изображения только в формате PNG
                FileFilter Filter = new FileNameExtensionFilter("PNG Images", "png");
                myFile.setFileFilter(Filter);
                //Гарантирует, что средство выбора не разрешит
                //пользователю использование отличных от png форматов
                myFile.setAcceptAllFileFilterUsed(false);
                //Окно позволяет пользователю выбрать
                //каталог и файл для сохранения
                int choiceuser = myFile.showSaveDialog(display);
                //Если выбор файла в диалоговом окне прошел успешно
                if (choiceuser == JFileChooser.APPROVE_OPTION) {
                    //Файл и его имя
                    java.io.File file = myFile.getSelectedFile();
                    String file_name = file.toString();
                    //Сохранение с обработкой исключений

                    //определяет блок кода, в котором может произойти исключение
                    try {
                        BufferedImage Image = display.getImage();
                        javax.imageio.ImageIO.write(Image, "png", file);
                    }
                    //определяет блок кода, в котором происходит обработка исключения
                    catch (Exception exception) {
                        JOptionPane.showMessageDialog(display, exception.getMessage(), "Can't save image",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

                //Если источником является кнопка сброса, сбросьте дисплей и отобразите фрактал
                else if (action.equals("Reset Display")) {
                    //range = new Rectangle2D.Double();
                    fractal.getInitialRange(range);
                    drawFractal();
                }
                //Если выбор файла в диалоговом окне не прошел успешно
                else return;
            
        }
    }

        /* Внутренний класс для обработки событий
         * java.awt.event.MouseListener с дисплея
         */

        private class MouseHandler extends MouseAdapter {
            // Обработывает события от мыши. При получении события о щелчке мышью,
            // отображает пиксельные кооринаты щелчка в область фрактала, а затем вызывает
            // метод генератора recenterAndZoomRange() с координатами, по которым
            // щелкнули, и масштабом 0.5. Таким образом, нажимая на какое-либо место на
            // фрактальном отображении, вы увеличиваете его

            public void mouseClicked(MouseEvent e) {
                // Получает координату x в области щелчка мыши
                int x = e.getX();
                double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize, x);

                // Получает координату y в области щелчка мыши
                int y = e.getY();
                double yCoord = fractal.getCoord(range.y, range.y + range.height, displaySize, y);

                // Вызовает метод recenterAndZoomRange() с помощью координат и масштаб 0,5
                fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);

                // Перерисовывает фрактал
                drawFractal();
            }
        }

        /**
         * Инициализирует новый экземпляр класса FractalExplorer с
         * размером отображения 800. Вызовает метод createAndShowGUI () класса FractalExplorer.
         * Вызовает метод drawFractal() класса FractalExplorer для отображения начального представления.
         */
        public static void main(String[] args) {
            FractalExplorer displayExplorer = new FractalExplorer(600);
            displayExplorer.createAndShowGUI();
            displayExplorer.drawFractal();
        }
    }
