package com.company;
import java.awt.geom.Rectangle2D;


/**
 * Этот класс предоставляет общий интерфейс и операции для фрактала
 * генератора, которые можно просмотреть в Обозревателе фракталов.
 */
public abstract class FractalGenerator {
    /**
     * Эта статическая вспомогательная функция принимает целочисленную координату и преобразует ее
     * * в значение двойной точности, соответствующее определенному диапазону. Он
     * * используется для преобразования координат пикселей в значения двойной точности для
     * * вычисление фракталов и т. д.
     *
     * @param rangeMin минимальное значение диапазона с плавающей запятой
     * @param rangeMax максмальное значение диапазона с плавающей запятой
     *
     * @param size размер измерения, из которого исходит координата пикселя.
     * * Например, это может быть ширина изображения или высота изображения.
     *
     * @param coord координата для вычисления значения двойной точности.
     * * Координата должна находиться в диапазоне [0, size].
     */
    public static double getCoord(double rangeMin, double rangeMax,
                                  int size, int coord) {

        assert size > 0;
        assert coord >= 0 && coord < size;

        double range = rangeMax - rangeMin;
        return rangeMin + (range * (double) coord / (double) size);
    }


    /**
     * Устанавливает указанный прямоугольник, содержащий начальный диапазон, подходящий для
     * фрактальная генерируется.
     */
    public abstract void getInitialRange(Rectangle2D.Double range);


    /**
     * Обновляет текущий диапазон для центрирования по заданным координатам,
     * а также для увеличения или уменьшения масштаба с заданным коэффициентом масштабирования.
     */
    public void recenterAndZoomRange(Rectangle2D.Double range,
                                     double centerX, double centerY, double scale) {

        double newWidth = range.width * scale;
        double newHeight = range.height * scale;

        range.x = centerX - newWidth / 2;
        range.y = centerY - newHeight / 2;
        range.width = newWidth;
        range.height = newHeight;
    }


    /**
     * Учитывая координату <em>x< / em> + <em>iy</em> в комплексной плоскости,
     * вычисляет и возвращает число итераций перед фракталом
     * функция экранирует ограничивающую область для этой точки. Указывается точка, которая
     * не убегает до достижения предела итерации
     * с результатом -1.
     */
    public abstract int numIterations(double x, double y);
}
