package com.evola.edt.utils;

public class LatCyrConverter {

	public enum ConvertType {
		LAT2CYR, CYR2LAT
	}

	private String[] listLat = null;
	private String[] listCyr = null;

	public LatCyrConverter() {
		initList();
	}

	private void initList() {
		String _lat = "A B C Č Ć D Đ E F G H I J K L M N O P R S Š T U V Z Ž";
		String lat = "Dž DŽ dž Lj LJ lj Nj NJ nj " + _lat + " "
				+ _lat.toLowerCase();

		String _cyr = "А Б Ц Ч Ћ Д Ђ Е Ф Г Х И Ј К Л М Н О П Р С Ш Т У В З Ж";
		String cyr = "Џ Џ џ Љ Љ љ Њ Њ њ " + _cyr + " " + _cyr.toLowerCase();

		listLat = lat.split(" ");
		listCyr = cyr.split(" ");
	}

	public String convertText(String line, ConvertType type) {

		if(line == null)
			return null;
		
		int i = 0;

		if (type == ConvertType.LAT2CYR)
			for (String item : listLat) {
				line = line.replaceAll(item, listCyr[i]);
				i++;
			}

		else if (type == ConvertType.CYR2LAT)
			for (String item : listCyr) {
				line = line.replaceAll(item, listLat[i]);
				i++;
			}

		return line;
	}

}
