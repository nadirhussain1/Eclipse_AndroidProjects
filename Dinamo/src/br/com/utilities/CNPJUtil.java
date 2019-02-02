package br.com.utilities;

public class CNPJUtil {

    /**
     * Returns a formatted <code>String</code> of the given CNPJ. A full valid
     * CNPJ which passed the {@link CNPJUtil#validate(String)} test is required.
     * The output matches the format 99.999.999/9999-99.
     * 
     * @param cnpj
     *            A full valid CNPJ.
     * @return Formatted CNPJ <code>String</code>.
     * @throws IllegalArgumentException
     *             if the given CNPJ is not valid.
     * @author Jorge Lee
     */
    public static String format(String cnpj) {
            if (!validate(cnpj)) {
                    throw new IllegalArgumentException("Invalid cnpj " + cnpj);
            }
            StringBuilder builder = new StringBuilder(cnpj.replaceAll("[^\\d]", ""));
            builder.insert(2, '.');
            builder.insert(6, '.');
            builder.insert(10, '/');
            builder.insert(15, '-');
            return builder.toString();
    }

    /**
     * Generates a random full valid CNPJ. The purpose of this method is to help
     * testing procedures by generating valid test data.
     * 
     * @return A full valid CNPJ <code>String</code>.
     * @author Jorge Lee
     */
    public static String generate() {
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < 8; i++) {
                    builder.append(Math.round(Math.random() * 9));
            }
            builder.append("0001");
            builder.append(getValidationDigits(builder.toString()));
            return builder.toString();
    }

    /**
     * A formatted version of {@link CNPJUtil#generate()}. The output is
     * formatted according to {@link CNPJUtil#format(String)}.
     * 
     * @return A formatted and full valid CNPJ <code>String</code>.
     * @author Jorge Lee
     */
    public static String generateFormatted() {
            return format(generate());
    }

    /**
     * Calculate the validation digits based on the government defined method.
     * 
     * @param cnpj
     *            A format valid CNPJ with size between 12 (without validation
     *            digits) and 14 (with validation digits). Existing validation
     *            digits will be ignored.
     * @return A string with two digits.
     * @throws IllegalArgumentException
     *             if input is null or not valid.
     * @author Jorge Lee
     */
    protected static String getValidationDigits(String cnpj) {
            if (cnpj == null || !cnpj.matches("[\\d]{12,14}")) {
                    throw new IllegalArgumentException("CNPJ is not valid: " + cnpj);
            }
            // calculate both digit
            int d1 = 0, d2 = 0;
            for (int i = 0, m1 = 5, m2 = 6; i < 12; i++, m1--, m2--) {
                    m1 = (m1 < 2) ? m1 + 8 : m1; // shift list
                    d1 += Integer.parseInt(cnpj.substring(i, i + 1)) * m1;
                    m2 = (m2 < 2) ? m2 + 8 : m2;
                    d2 += Integer.parseInt(cnpj.substring(i, i + 1)) * m2;
            }
            d1 = 11 - d1 % 11;
            d1 = (d1 > 9) ? 0 : d1;
            // complete using the previous calculated digit
            d2 += d1 * 2;
            d2 = 11 - d2 % 11;
            d2 = (d2 > 9) ? 0 : d2;
            return "" + d1 + d2;
    }

    /**
     * Total validation of the CNPJ ignoring the format. All non-numeric
     * characters will be ignored. Validation digits are tested as well.
     * 
     * @param cnpj
     *            The CNPJ to be tested.
     * @return true if CNPJ is full valid.
     * @author Jorge Lee
     */
    public static boolean validate(String cnpj) {
            if (cnpj == null) {
                    return false;
            }
            String _cnpj = cnpj.replaceAll("[^\\d]", "");
            if (!_cnpj.matches("[\\d]{14}"))
                    return false;
            return _cnpj
                            .equals(_cnpj.substring(0, 12) + getValidationDigits(_cnpj));
    }
}
