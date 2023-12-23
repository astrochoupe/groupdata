package fr.walliang.astronomy.groupdata;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Launch {
	public static void main(String[] args) {
		String filename = "filtreRGBHa.csv";
		int binning = 2;
		String filenameOutput = "filtreRGBHa_bin" + binning + ".csv";

		List<Line> binnedData = readFileAndBin(filename, binning);
		writeBinnedData(filenameOutput, binnedData);
	}

	private static List<Line> readFileAndBin(String filename, int binning) {
		// binned data
		List<Line> binnedData = new ArrayList<>();

		// read CSV file
		Path file = Path.of(filename);

		System.out.println("Reading " + file.toAbsolutePath().toString());

		int lineNumber = 0;
		int bin = 1;
		BigDecimal binningBD = BigDecimal.valueOf(binning);

		BigDecimal secondsBinned = BigDecimal.ZERO;
		BigDecimal rBinned = BigDecimal.ZERO;
		BigDecimal gBinned = BigDecimal.ZERO;
		BigDecimal bBinned = BigDecimal.ZERO;
		BigDecimal haBinned = BigDecimal.ZERO;

		try {
			List<String> lines = Files.readAllLines(file);

			for (String line : lines) {
				lineNumber++;

				// skip header line
				if (lineNumber == 1) {
					continue;
				}

				// debug
				System.out.println("Line " + lineNumber + " : " + line);

				String[] fields = line.split(",", -1);
				// debug
				// List<String> fields2 = Arrays.asList(fields);
				// System.out.println(fields2);

				BigDecimal seconds = BigDecimal.valueOf(Double.parseDouble(fields[0]));
				BigDecimal r = stringToBigDecimal(fields[1]);
				BigDecimal g = stringToBigDecimal(fields[2]);
				BigDecimal b = stringToBigDecimal(fields[3]);
				BigDecimal ha = stringToBigDecimal(fields[4]);

				// debug
				/*
				 * System.out.println("s="+seconds); System.out.println("r="+r);
				 * System.out.println("g="+g); System.out.println("b="+b);
				 * System.out.println("ha="+ha);
				 */

				// debug
				// System.out.println("bin="+bin);
				// System.out.println("binning="+binning);

				if (bin < binning) {
					secondsBinned = secondsBinned.add(seconds);
					rBinned = addNullable(rBinned, r);
					gBinned = addNullable(gBinned, g);
					bBinned = addNullable(bBinned, b);
					haBinned = addNullable(haBinned, ha);
				} else {
					secondsBinned = secondsBinned.add(seconds);
					rBinned = addNullable(rBinned, r);
					gBinned = addNullable(gBinned, g);
					bBinned = addNullable(bBinned, b);
					haBinned = addNullable(haBinned, ha);

					secondsBinned = secondsBinned.divide(binningBD);
					rBinned = rBinned.divide(binningBD);
					gBinned = gBinned.divide(binningBD);
					bBinned = bBinned.divide(binningBD);
					haBinned = haBinned.divide(binningBD);

					Line binnedLine = new Line(secondsBinned, rBinned, gBinned, bBinned, haBinned);
					binnedData.add(binnedLine);

					// reset
					secondsBinned = BigDecimal.ZERO;
					rBinned = BigDecimal.ZERO;
					gBinned = BigDecimal.ZERO;
					bBinned = BigDecimal.ZERO;
					haBinned = BigDecimal.ZERO;

					bin = 0;
				}
				bin++;

				/*
				 * System.out.println("secondsBinned="+secondsBinned);
				 * System.out.println("rBinned="+rBinned);
				 * System.out.println("gBinned="+gBinned);
				 * System.out.println("bBinned="+bBinned);
				 * System.out.println("haBinned="+haBinned)
				 */
			}
			System.out.println("Reading finished");
		} catch (IOException e) {
			System.err.println("Error reading file : " + e);
		}

		return binnedData;
	}

	private static void writeBinnedData(String filenameOutput, List<Line> binnedData) {
		// System.out.println(binnedData);

		try (BufferedWriter writer = Files.newBufferedWriter(Path.of(filenameOutput))) {
			// header
			writer.write("Secondes,R,G,B,Ha");
			writer.newLine();

			for (Line line : binnedData) {
				writer.write(line.seconds().toString());
				writer.write(",");

				if (!line.r().equals(BigDecimal.ZERO)) {
					writer.write(line.r().toString());
				}
				writer.write(",");

				if (!line.g().equals(BigDecimal.ZERO)) {
					writer.write(line.g().toString());
				}
				writer.write(",");

				if (!line.b().equals(BigDecimal.ZERO)) {
					writer.write(line.b().toString());
				}
				writer.write(",");

				if (!line.ha().equals(BigDecimal.ZERO)) {
					writer.write(line.ha().toString());
				}

				writer.newLine();
			}
			System.out.println("Writing finished");
		} catch (IOException e) {
			System.err.println("Error writing file : " + e);
		}
	}

	private static BigDecimal addNullable(BigDecimal bd1, BigDecimal bd2) {
		if (bd2 != null) {
			return bd1.add(bd2);
		} else {
			return bd1;
		}
	}

	private static BigDecimal stringToBigDecimal(String s) {
		if (s.isEmpty()) {
			return null;
		} else {
			return BigDecimal.valueOf(Double.parseDouble(s));
		}
	}
}
