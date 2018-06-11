/******************************************************************************
 *                   Confidential Proprietary                                 *
 *         (c) Copyright Haifeng Li 2011, All Rights Reserved                 *
 ******************************************************************************/

package smile.math.kernel;

import smile.math.Math;

/**
 * The polynomial kernel. k(u, v) = (&gamma; u<sup>T</sup>v - &lambda;)<sup>d</sup>,
 * where &gamma; is the scale of the used inner product, &lambda; the offset of
 * the used inner product, and <i>d</i> the order of the polynomial kernel.
 * The kernel works on sparse binary array as int[], which are the indices of
 * nonzero elements.
 * 
 * @author Haifeng Li
 */
public class BinarySparsePolynomialKernel implements MercerKernel<int[]> {

    private int degree;
    private double scale;
    private double offset;

    /**
     * Constructor with scale 1 and bias 0.
     */
    public BinarySparsePolynomialKernel(int degree) {
        this(degree, 1.0, 0.0);
    }

    /**
     * Constructor.
     */
    public BinarySparsePolynomialKernel(int degree, double scale, double offset) {
        if (degree <= 0) {
            throw new IllegalArgumentException("Non-positive polynomial degree.");
        }

        if (offset < 0.0) {
            throw new IllegalArgumentException("Negative offset: the kernel does not satisfy Mercer's condition.");
        }
        
        this.degree = degree;
        this.scale = scale;
        this.offset = offset;
    }

    @Override
    public String toString() {
        return String.format("Sparse Binary Polynomial Kernel (scale = %.4f, offset = %.4f)", scale, offset);
    }

    @Override
    public double k(int[] x, int[] y) {
        double dot = 0.0;

        for (int p1 = 0, p2 = 0; p1 < x.length && p2 < y.length; ) {
            int i1 = x[p1];
            int i2 = y[p2];
            if (i1 == i2) {
                dot++;
                p1++;
                p2++;
            } else if (i1 > i2) {
                p2++;
            } else {
                p1++;
            }
        }

        return Math.pow(scale * dot + offset, degree);
    }
}
