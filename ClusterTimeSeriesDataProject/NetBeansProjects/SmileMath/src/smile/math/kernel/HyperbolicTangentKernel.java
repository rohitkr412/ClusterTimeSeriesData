/******************************************************************************
 *                   Confidential Proprietary                                 *
 *         (c) Copyright Haifeng Li 2011, All Rights Reserved                 *
 ******************************************************************************/

package smile.math.kernel;

import smile.math.Math;

/**
 * The hyperbolic tangent kernel.
 * k(u, v) = tanh(&gamma; u<sup>T</sup>v - &lambda;), where &gamma; is the scale
 * of the used inner product and &lambda; is the offset of the used inner
 * product. If the offset is negative the likelihood of obtaining a kernel
 * matrix that is not positive definite is much higher (since then even some
 * diagonal elements may be negative), hence if this kernel has to be used,
 * the offset should always be positive. Note, however, that this is no
 * guarantee that the kernel will be positive.
 * <p>
 * The hyperbolic tangent kernel was quite popular for support vector machines
 * due to its origin from neural networks. However, it should be used carefully
 * since the kernel matrix may not be positive semi-definite. Besides, it was
 * reported the hyperbolic tangent kernel is not better than the Gaussian kernel
 * in general.
 *
 * @author Haifeng Li
 */
public class HyperbolicTangentKernel implements MercerKernel<double[]> {

    private double scale;
    private double offset;

    /**
     * Constructor.
     */
    public HyperbolicTangentKernel() {
        this(1, 0);
    }

    /**
     * Constructor.
     */
    public HyperbolicTangentKernel(double scale, double offset) {
        this.scale = scale;
        this.offset = offset;
    }

    @Override
    public String toString() {
        return String.format("Hyperbolic Tangent Kernel (scale = %.4f, offset = %.4f)", scale, offset);
    }

    @Override
    public double k(double[] x, double[] y) {
        if (x.length != y.length)
            throw new IllegalArgumentException(String.format("Arrays have different length: x[%d], y[%d]", x.length, y.length));

        double dot = Math.dot(x, y);
        return Math.tanh(scale * dot + offset);
    }
}
