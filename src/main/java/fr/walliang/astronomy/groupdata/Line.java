package fr.walliang.astronomy.groupdata;

import java.math.BigDecimal;

public class Line {
	public BigDecimal seconds;
	public BigDecimal r;
	public BigDecimal g;
	public BigDecimal b;
	public BigDecimal ha;

	public Line(BigDecimal seconds, BigDecimal r, BigDecimal g, BigDecimal b, BigDecimal ha) {
		this.seconds = seconds;
		this.r = r;
		this.g = g;
		this.b = b;
		this.ha = ha;
	}

	@Override
	public String toString() {
		return "Line [seconds=" + seconds + ", r=" + r + ", g=" + g + ", b=" + b + ", ha=" + ha + "]";
	}
}
