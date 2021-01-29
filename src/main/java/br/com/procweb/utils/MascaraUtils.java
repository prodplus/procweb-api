package br.com.procweb.utils;

import java.text.ParseException;

import javax.swing.text.MaskFormatter;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
public class MascaraUtils {

	/**
	 * Aplica máscara à String.
	 * 
	 * @param pattern
	 * @param value
	 * @return
	 */
	public static String format(String pattern, Object value) {
		MaskFormatter mask;
		try {
			mask = new MaskFormatter(pattern);
			mask.setValueContainsLiteralCharacters(false);
			return mask.valueToString(value);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
