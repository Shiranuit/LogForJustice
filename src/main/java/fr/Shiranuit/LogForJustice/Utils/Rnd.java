package fr.Shiranuit.LogForJustice.Utils;

import java.util.Random;

public class Rnd extends Random {
	public int nextIntMax = Integer.MAX_VALUE;
	public Rnd() {
		super();
	}
	
	public Rnd(int mx) {
		super();
		this.nextIntMax = mx;
	}
	
	public int nextInt(int val) {
		int x = this.nextInt(this.nextIntMax);
		return x;
	}
}
