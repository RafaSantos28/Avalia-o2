package io.sim.reconciliation;

public class Rec {

	public static void main(String[] args) {

		double[] y = new double[] { 14.941, 7.664, 9.640, 37.264, 11.5735, 7.540, 32.4605, 121.4535};

		double[] v = new double[] { 0.5458598514, 0.2091315421, 0.6555795028, 5.930398953, 0.7120948225, 0.5470909893, 2.645504011, 9.215098194};

		double[][] A = new double[][] { { 1, 1, 1, 1, 1, 1 , 1, -1}};

		Reconciliation rec = new Reconciliation(y, v, A);
		System.out.println("Y_hat:");
		rec.printMatrix(rec.getReconciledFlow());
	}

}
