package com.company;
import java.awt.geom.Rectangle2D;

//Этот класс является подклассом FractalGenerator и используется для вычисления Mandelbrot fractal
public class Mandelbrot extends FractalGenerator{
    public static final int MAX_ITER = 2000; //Константа

    /*
     * Этот метод позволяет генератору фракталов определить
     * наиболее «интересную» область комплексной плоскости для конкретного фрактала.
     * Метод изменяет поля rectangle, чтобы показать
     * правильный начальный диапазон для фрактала.
     * Исходный диапазон: (-2 - 1.5 i) - (1 + 1.5 i)
     * Т.е. значения x и y будут равны -2 и -1.5 соответственно,
     * а ширина и высота будут равны 3
     */
    public void getInitialRange(Rectangle2D.Double range) //Класс Rectangle2D создает прямоугольник с заданными координатами левого верхнего угла прямоугольника, шириной и высотой или определяется местоположением (minX, minY) и размером (ширина x высота).
    {
        range.x = -2;
        range.y = -1.5;
        range.width = 3;
        range.height = 3;
    }

    /**
     * Этот метод реализует итеративную функцию для фрактала Мандельброта.
     */
    public int numIterations(double x, double y)
    {
        int iter = 0;
        double zreal = 0;
        double zimaginary = 0;

        /**
         * Вычислите Zn = Zn-1^2 + c, где значения представляют собой комплексные числа, где
         * Z0=0 (по реальному и мнимому), а c-это конкретная точка в фрактале (задается x и y).
         * Он повторяется до тех пор, пока Z^2 > 4 (абсолютное значение Z больше 2) или максимума
         * достигнуто количество итераций.
         */
        while (iter < MAX_ITER && zreal * zreal + zimaginary * zimaginary < 4)
        {
            double zreal2= zreal * zreal - zimaginary * zimaginary + x;
            double zimaginary2 = 2 * zreal * zimaginary + y;
            zreal = zreal2;
            zimaginary = zimaginary2;
            iter += 1;
        }

        /**
         * В случае, если алгоритм дошел до значения MAX_ITERATIONS возвращает -1,
         * чтобы показать, что точка не выходит за границы.
         */
        if (iter == MAX_ITER)
        {
            return -1;
        }

        return iter;
    }

    public String toString() {
        return "Mandelbrot";
    }
}
